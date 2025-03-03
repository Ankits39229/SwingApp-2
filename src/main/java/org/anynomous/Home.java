package main.java.org.anynomous;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Home extends JPanel {
    // Modern dark theme colors
    private static final Color ACCENT_COLOR = new Color(82, 145, 255);
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color CARD_BACKGROUND = new Color(45, 45, 45);
    private static final Color SUCCESS_COLOR = new Color(46, 160, 67);
    private static final Color WARNING_COLOR = new Color(210, 153, 34);
    private static final Color ERROR_COLOR = new Color(248, 81, 73);
    private static final Color TEXT_COLOR = new Color(230, 230, 230);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    private final ExecutorService executorService;
    private JLabel lastScanLabel;
    private  JTabbedPane tabbedPane;
    private  JTextArea outputArea;
    private  Timer updateTimer;
    private static final Logger logger = LoggerFactory.getLogger(Home.class);

    private static final String[] WINDOWS_COMMAND = {"tasklist", "/FO", "CSV"};
    private static final String[] UNIX_COMMAND = {"/bin/ps", "aux"};

    public Home() {
        executorService = Executors.newFixedThreadPool(4);
        setupMainPanel();
        initializeComponents();
        initializeTabs();
        setupGlobalStyle();
    }

    private void setupMainPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        // Initialize tabbed pane with modern styling
        tabbedPane = createModernTabbedPane();

        // Create modern output console
        outputArea = createOutputConsole();
        JScrollPane outputScrollPane = createModernScrollPane(outputArea);
        outputScrollPane.setPreferredSize(new Dimension(0, 200));

        // Setup main panel with components
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(outputScrollPane, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // Initialize and start update timer
        updateTimer = new Timer(1000, e -> updateLastScanTime());
        updateTimer.start();
    }

    private JTabbedPane createModernTabbedPane() {
        JTabbedPane pane = new JTabbedPane();
        pane.putClientProperty("JTabbedPane.tabType", "card");
        pane.putClientProperty("JTabbedPane.showTabSeparators", true);
        pane.putClientProperty("JTabbedPane.tabAreaAlignment", "leading");
        pane.putClientProperty("JTabbedPane.minimumTabWidth", 100);
        pane.putClientProperty("JTabbedPane.tabHeight", 35);
        return pane;
    }

    private void setupGlobalStyle() {
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("TextField.background", CARD_BACKGROUND);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.caretForeground", TEXT_COLOR);
        UIManager.put("ComboBox.background", CARD_BACKGROUND);
        UIManager.put("ComboBox.foreground", TEXT_COLOR);
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 12);
        UIManager.put("ScrollBar.thumb", new Color(60, 60, 60));
        UIManager.put("ScrollBar.track", CARD_BACKGROUND);
        UIManager.put("Table.selectionBackground", ACCENT_COLOR);
        UIManager.put("Table.selectionForeground", TEXT_COLOR);
    }

    private JScrollPane createModernScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);
        return scrollPane;
    }

    private JTextArea createOutputConsole() {
        JTextArea console = new JTextArea();
        console.setEditable(false);
        console.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        console.setBackground(CARD_BACKGROUND);
        console.setForeground(TEXT_COLOR);
        console.setBorder(new EmptyBorder(10, 10, 10, 10));
        return console;
    }

    private void initializeTabs() {
        createDashboardTab();
        createProcessTab();
        createNetworkTab();
        createFilesystemTab();
        createSettingsTab();
    }

    private void createDashboardTab() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(15, 15));
        dashboardPanel.setBackground(BACKGROUND_COLOR);
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Status Cards
        JPanel statusPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statusPanel.setBackground(BACKGROUND_COLOR);
        addStatusCard(statusPanel, "System Status", "Running", SUCCESS_COLOR);
        addStatusCard(statusPanel, "Active Monitors", "3/3", ACCENT_COLOR);
        lastScanLabel = new JLabel("Just now");
        addStatusCard(statusPanel, "Last Scan", lastScanLabel, Color.GRAY);

        // Action Buttons
        JPanel actionsPanel = createActionsPanel();

        // Charts
        JPanel chartsPanel = createChartsPanel();

        // Combine all panels
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(actionsPanel, BorderLayout.NORTH);
        centerPanel.add(chartsPanel, BorderLayout.CENTER);

        dashboardPanel.add(statusPanel, BorderLayout.NORTH);
        dashboardPanel.add(centerPanel, BorderLayout.CENTER);

        tabbedPane.addTab("Dashboard", dashboardPanel);
    }

    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        actionsPanel.setBackground(BACKGROUND_COLOR);

        JButton scanButton = createStyledButton("Run System Scan", "scan");
        JButton exportButton = createStyledButton("Export Results", "export");
        JButton settingsButton = createStyledButton("Quick Settings", "settings");
        JButton helpButton = createStyledButton("Help", "help");

        actionsPanel.add(scanButton);
        actionsPanel.add(exportButton);
        actionsPanel.add(settingsButton);
        actionsPanel.add(helpButton);

        return actionsPanel;
    }

    private JPanel createChartsPanel() {
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        chartsPanel.setBackground(BACKGROUND_COLOR);
        addChartPanel(chartsPanel, "System Resources");
        addChartPanel(chartsPanel, "Network Activity");
        return chartsPanel;
    }

    private void addChartPanel(JPanel container, String title) {
        JPanel chartPanel = new JPanel(new BorderLayout(10, 10));
        chartPanel.setBackground(CARD_BACKGROUND);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel chartPlaceholder = new JPanel();
        chartPlaceholder.setBackground(CARD_BACKGROUND);
        chartPlaceholder.setPreferredSize(new Dimension(0, 200));

        chartPanel.add(titleLabel, BorderLayout.NORTH);
        chartPanel.add(chartPlaceholder, BorderLayout.CENTER);
        container.add(chartPanel);
    }

    private void createProcessTab() {
        JPanel processPanel = new JPanel(new BorderLayout(15, 15));
        processPanel.setBackground(BACKGROUND_COLOR);
        processPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Control buttons
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlsPanel.setBackground(BACKGROUND_COLOR);
        JButton refreshButton = createStyledButton("Refresh Processes", "refresh");
        JButton killButton = createStyledButton("End Process", "kill");
        killButton.setBackground(ERROR_COLOR);
        controlsPanel.add(refreshButton);
        controlsPanel.add(killButton);

        // Process table
        String[] columnNames = {"PID", "Process Name", "CPU %", "Memory", "Status"};
        Object[][] data = {
                {"1234", "System", "0.1%", "50MB", "Running"},
                {"5678", "User Process", "2.3%", "150MB", "Running"}
        };
        JTable processTable = createStyledTable(data, columnNames);
        JScrollPane tableScroll = createModernScrollPane(processTable);

        processPanel.add(controlsPanel, BorderLayout.NORTH);
        processPanel.add(tableScroll, BorderLayout.CENTER);

        tabbedPane.addTab("Processes", processPanel);
    }

    private void createNetworkTab() {
        JPanel networkPanel = new JPanel(new BorderLayout(15, 15));
        networkPanel.setBackground(BACKGROUND_COLOR);
        networkPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Control buttons
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlsPanel.setBackground(BACKGROUND_COLOR);
        JButton scanButton = createStyledButton("Scan Network", "network_scan");
        JButton blockButton = createStyledButton("Block Connection", "block");
        blockButton.setBackground(WARNING_COLOR);
        controlsPanel.add(scanButton);
        controlsPanel.add(blockButton);

        // Network table
        String[] columnNames = {"Source", "Destination", "Protocol", "Status", "Bandwidth"};
        Object[][] data = {
                {"192.168.1.1:80", "10.0.0.1:443", "TCP", "Active", "1.2 MB/s"},
                {"192.168.1.1:443", "8.8.8.8:53", "UDP", "Active", "0.1 MB/s"}
        };
        JTable networkTable = createStyledTable(data, columnNames);
        JScrollPane tableScroll = createModernScrollPane(networkTable);

        networkPanel.add(controlsPanel, BorderLayout.NORTH);
        networkPanel.add(tableScroll, BorderLayout.CENTER);

        tabbedPane.addTab("Network", networkPanel);
    }

    private void createFilesystemTab() {
        JPanel filesystemPanel = new JPanel(new BorderLayout(15, 15));
        filesystemPanel.setBackground(BACKGROUND_COLOR);
        filesystemPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Control buttons
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlsPanel.setBackground(BACKGROUND_COLOR);
        JButton scanButton = createStyledButton("Scan Files", "file_scan");
        JButton analyzeButton = createStyledButton("Analyze Changes", "analyze");
        controlsPanel.add(scanButton);
        controlsPanel.add(analyzeButton);

        // Filesystem table
        String[] columnNames = {"Path", "Size", "Modified", "Permissions", "Status"};
        Object[][] data = {
                {"/usr/bin", "1.2GB", "2024-02-17", "rwxr-xr-x", "Secure"},
                {"/etc", "85MB", "2024-02-17", "rwxr-xr--", "Warning"}
        };
        JTable filesystemTable = createStyledTable(data, columnNames);
        JScrollPane tableScroll = createModernScrollPane(filesystemTable);

        filesystemPanel.add(controlsPanel, BorderLayout.NORTH);
        filesystemPanel.add(tableScroll, BorderLayout.CENTER);

        tabbedPane.addTab("File System", filesystemPanel);
    }

    private void createSettingsTab() {
        JPanel settingsPanel = new JPanel(new BorderLayout(15, 15));
        settingsPanel.setBackground(BACKGROUND_COLOR);
        settingsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Settings options
        JPanel optionsPanel = createSettingsOptionsPanel();

        // Buttons panel
        JPanel buttonPanel = createSettingsButtonPanel();

        settingsPanel.add(optionsPanel, BorderLayout.CENTER);
        settingsPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Settings", settingsPanel);
    }

    private JPanel createSettingsOptionsPanel() {
        JPanel optionsPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        optionsPanel.setBackground(CARD_BACKGROUND);
        optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JCheckBox processMonitor = createStyledCheckBox("Enable Process Monitoring", true);
        JCheckBox networkMonitor = createStyledCheckBox("Enable Network Monitoring", true);
        JCheckBox filesystemMonitor = createStyledCheckBox("Enable File System Monitoring", true);
        JPanel processInterval = createIntervalPanel("Process Scan Interval (seconds):", "60");
        JPanel networkInterval = createIntervalPanel("Network Scan Interval (seconds):", "30");
        JPanel filesystemInterval = createIntervalPanel("File System Scan Interval (seconds):", "300");
        JCheckBox notifyOnWarning = createStyledCheckBox("Notify on Warnings", true);
        JCheckBox notifyOnError = createStyledCheckBox("Notify on Errors", true);

        optionsPanel.add(processMonitor);
        optionsPanel.add(networkMonitor);
        optionsPanel.add(filesystemMonitor);
        optionsPanel.add(processInterval);
        optionsPanel.add(networkInterval);
        optionsPanel.add(filesystemInterval);
        optionsPanel.add(notifyOnWarning);
        optionsPanel.add(notifyOnError);

        return optionsPanel;
    }

    private JPanel createSettingsButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton saveButton = createStyledButton("Save Settings", "save");
        saveButton.setBackground(SUCCESS_COLOR);
        JButton resetButton = createStyledButton("Reset to Default", "reset");
        resetButton.setBackground(WARNING_COLOR);

        buttonPanel.add(resetButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text, selected);
        checkBox.setForeground(TEXT_COLOR);
        checkBox.setBackground(CARD_BACKGROUND);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return checkBox;
    }

    private JPanel createIntervalPanel(String labelText, String defaultValue) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(CARD_BACKGROUND);

        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JTextField textField = new JTextField(defaultValue, 5);
        textField.setBackground(BACKGROUND_COLOR);
        textField.setForeground(TEXT_COLOR);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(5, 5, 5, 5)
        ));

        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private JTable createStyledTable(Object[][] data, String[] columnNames) {
        JTable table = new JTable(data, columnNames);
        table.setBackground(CARD_BACKGROUND);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setBackground(BACKGROUND_COLOR);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setRowHeight(30);
        table.setShowGrid(true);
        return table;
    }

    private void addStatusCard(JPanel container, String title, Object value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel valueLabel;
        if (value instanceof JLabel) {
            valueLabel = (JLabel) value;
        } else {
            valueLabel = new JLabel(value.toString());
        }
        valueLabel.setForeground(accentColor);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        container.add(card);
    }

    private JButton createStyledButton(String text, String action) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.addActionListener(e -> handleButtonAction(action));
        return button;
    }

    private void handleButtonAction(String action) {
        switch (action) {
            case "scan":
                executeScan();
                break;
            case "export":
                exportResults();
                break;
            case "settings":
                tabbedPane.setSelectedIndex(4); // Switch to settings tab
                break;
            case "help":
                showHelp();
                break;
            // Add other action handlers as needed
        }
    }

    private void executeScan() {
        executorService.submit(() -> {
            appendToOutput("Starting system scan...");
            try {
                String[] command = System.getProperty("os.name").toLowerCase().contains("windows") 
                    ? WINDOWS_COMMAND 
                    : UNIX_COMMAND;
                
                ProcessBuilder pb = new ProcessBuilder(command);
                
                // Basic security checks
                String userHome = System.getProperty("user.home");
                if (userHome == null || !new File(userHome).isDirectory()) {
                    throw new SecurityException("Invalid working directory");
                }
                pb.directory(new File(userHome));
                
                // Clear sensitive env vars
                pb.environment().clear();
                pb.redirectErrorStream(true);
                
                Process process = pb.start();
                
                if (!process.waitFor(30, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                    appendToOutput("Process timed out");
                    return;
                }
                
                // Read output with strict bounds
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    int lineCount = 0;
                    final int MAX_LINES = 1000; // Prevent DoS
                    
                    while ((line = reader.readLine()) != null && lineCount < MAX_LINES) {
                        final String sanitizedLine = sanitizeOutput(line);
                        if (!sanitizedLine.isEmpty()) {
                            SwingUtilities.invokeLater(() -> appendToOutput(sanitizedLine));
                        }
                        lineCount++;
                    }
                    
                    if (lineCount >= MAX_LINES) {
                        appendToOutput("Output truncated due to length");
                    }
                }
                
                int exitCode = process.exitValue();
                if (exitCode != 0) {
                    logger.warn("Process completed with non-zero exit code: {}", exitCode);
                    appendToOutput("Process completed with warnings");
                }
                
            } catch (Exception e) {
                logger.error("Scan error", e);
                appendToOutput("An error occurred during the scan");
            }
        });
    }

    private String sanitizeOutput(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        
        // Strict whitelist of allowed characters
        return input.replaceAll("[^a-zA-Z0-9\\s\\-_.,:]", "")
                   .trim();
    }

    private void exportResults() {
        appendToOutput("Exporting results...");
        // Add export functionality
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this,
                "System Monitor Help\n\n" +
                        "This application monitors system processes, network activity, and filesystem changes.\n" +
                        "Use the tabs to navigate between different monitoring functions.\n" +
                        "Click 'Run System Scan' to perform a complete system analysis.",
                "Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void appendToOutput(String message) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(String.format("[%s] %s%n",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    message));
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    private void updateLastScanTime() {
        if (lastScanLabel != null) {
            LocalDateTime now = LocalDateTime.now();
            lastScanLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }

//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(new FlatDarkLaf());
//        } catch (Exception e) {
//            System.err.println("Failed to initialize dark theme");
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("System Monitor");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setContentPane(new Malware());
//            frame.setSize(1000, 700);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
}