package main.java.org.anynomous;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Audit extends JPanel {

    private static final Color PRIMARY_DARK = new Color(17, 24, 39);
    private static final Color SECONDARY_DARK = new Color(31, 41, 55);
    private static final Color ACCENT_COLOR = new Color(59, 130, 246);
    private static final Color ACCENT_HOVER = new Color(96, 165, 250);
    private static final Color DANGER_COLOR = new Color(220, 38, 38);
    private static final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private static final Color TEXT_SECONDARY = new Color(156, 163, 175);
    private static final Color BORDER_COLOR = new Color(55, 65, 81);

    private static final Font HEADER_FONT = new Font("Inter", Font.BOLD, 36);
    private static final Font SUBTITLE_FONT = new Font("Inter", Font.BOLD, 18);
    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 14);
    private static final Font LOG_FONT = new Font("JetBrains Mono", Font.PLAIN, 13);

    private static final int BUTTON_HEIGHT = 44;
    private static final int BUTTON_WIDTH = 220;
    private static final int PANEL_PADDING = 32;
    private static final int COMPONENT_SPACING = 24;
    private static final int CORNER_RADIUS = 12;

    private JTextArea logArea;

    public Audit() {
        setupMainPanel();
        createLayout();
    }

    private void setupMainPanel() {
        setBackground(PRIMARY_DARK);
        setLayout(new BorderLayout(PANEL_PADDING, PANEL_PADDING));
        setBorder(new EmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
    }

    private void createLayout() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(PRIMARY_DARK);

        mainContent.add(createHeaderPanel());
        mainContent.add(Box.createRigidArea(new Dimension(0, COMPONENT_SPACING)));
        mainContent.add(createControlPanel());
        mainContent.add(Box.createRigidArea(new Dimension(0, COMPONENT_SPACING)));
        mainContent.add(createLogPanel());

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(PRIMARY_DARK);
        headerPanel.setBorder(new EmptyBorder(0, 0, COMPONENT_SPACING, 0));

        // Create a panel for the icon with a subtle background
        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SECONDARY_DARK);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS * 2, CORNER_RADIUS * 2);
            }
        };
        iconContainer.setOpaque(false);
        iconContainer.setPreferredSize(new Dimension(80, 80));
        iconContainer.setMaximumSize(new Dimension(80, 80));

        JLabel iconLabel = new JLabel("\uD83D\uDEE1");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setForeground(ACCENT_COLOR);
        iconContainer.add(iconLabel);
        iconContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel header = new JLabel("Audit");
        header.setForeground(TEXT_PRIMARY);
        header.setFont(HEADER_FONT);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Real-time Network Traffic Protection");
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconContainer);
        headerPanel.add(Box.createRigidArea(new Dimension(0, COMPONENT_SPACING)));
        headerPanel.add(header);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        headerPanel.add(subtitle);

        return headerPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SECONDARY_DARK);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
            }
        };
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, COMPONENT_SPACING, COMPONENT_SPACING));
        controlPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JButton configButton = createButton("Configuration Audit",
                e -> checkScripts(),
                ACCENT_COLOR, ACCENT_HOVER);

        JButton networkButton = createButton("Network Audit",
                e -> executePythonScript("PythonScripts/check/sc.py"),
                DANGER_COLOR, new Color(239, 68, 68));

        controlPanel.add(configButton);
        controlPanel.add(networkButton);

        return controlPanel;
    }

    private JPanel createLogPanel() {
        JPanel logPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SECONDARY_DARK);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
            }
        };
        logPanel.setOpaque(false);
        logPanel.setLayout(new BorderLayout());

        // Create header with icon
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel terminalIcon = new JLabel("⌨");
        terminalIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        terminalIcon.setForeground(TEXT_PRIMARY);

        JLabel logHeader = new JLabel("Activity Log");
        logHeader.setFont(BUTTON_FONT);
        logHeader.setForeground(TEXT_PRIMARY);

        headerPanel.add(terminalIcon);
        headerPanel.add(logHeader);

        // Customize log area
        logArea = new JTextArea();
        logArea.setBackground(PRIMARY_DARK);
        logArea.setForeground(TEXT_PRIMARY);
        logArea.setFont(LOG_FONT);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(new EmptyBorder(12, 16, 12, 16));
        logArea.setCaretColor(TEXT_PRIMARY);

        // Custom scroll pane
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PRIMARY_DARK);
        scrollPane.getViewport().setBackground(PRIMARY_DARK);

        // Customize scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                super.configureScrollBarColors();
                this.trackColor = PRIMARY_DARK;
                this.thumbColor = BORDER_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });

        logPanel.add(headerPanel, BorderLayout.NORTH);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        return logPanel;
    }

    private JButton createButton(String label, ActionListener action, Color backgroundColor, Color hoverColor) {
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                super.paintComponent(g);
            }
        };

        button.setFont(BUTTON_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        button.addActionListener(action);
        return button;
    }

    // Rest of the methods (checkScripts, executePythonScript, getCurrentTimestamp) remain unchanged
    private void checkScripts() {
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                logArea.setText("");
                logArea.append("Starting configuration audit...\n");

                String[] scripts = {"2.1.py", "2.2.py", "2.3.py", "2.py", "3.1.py", "3.2.py", "9.1.py", "17.1.py",
                        "17.2.py", "18.1.py", "18.2.py", "18.3.py", "18.4.py", "18.5.py", "18.6.py", "18.7.py",
                        "18.8.py", "18.9.py", "19.1.py", "19.2.py", "19.3.py", "19.4"};

                for (String script : scripts) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("python", "PythonScripts/audit/" + script);
                        Process process = pb.start();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            publish(line);
                        }
                        process.waitFor();
                    } catch (Exception e) {
                        publish("Error running " + script + ": " + e.getMessage());
                    }
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks) {
                    logArea.append(line + "\n");
                }
            }
        };
        worker.execute();
    }

    private void executePythonScript(String scriptPath) {
        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
                pb.redirectErrorStream(true);

                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                int exitCode = process.waitFor();
                SwingUtilities.invokeLater(() -> logArea.append(String.format(
                        "[%s] Script '%s' executed with exit code %d\nOutput:\n%s\n",
                        getCurrentTimestamp(), scriptPath, exitCode, output.toString())));

            } catch (IOException | InterruptedException e) {
                SwingUtilities.invokeLater(() -> logArea.append(String.format(
                        "[%s] Error running script '%s': %s\n",
                        getCurrentTimestamp(), scriptPath, e.getMessage())));
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private String getCurrentTimestamp() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}