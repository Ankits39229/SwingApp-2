package main.java.org.anynomous;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Assistant extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
    private static final Color SECONDARY_COLOR = new Color(30, 30, 30);
    private static final Color ACCENT_COLOR = new Color(79, 147, 255);
    private static final Color TEXT_COLOR = new Color(245, 245, 245);
//    private static final Color SUCCESS_COLOR = new Color(75, 181, 67);
//    private static final Color ERROR_COLOR = new Color(255, 85, 85);
    private static final Color USER_MESSAGE_COLOR = new Color(187, 187, 187);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final String SCRIPTS_PATH = "AssistantScripts/";

    private JTextPane chatArea;
    private JTextField userInput;
    private HashMap<String, Runnable> actions;
//    private JProgressBar progressBar;
    private StyledDocument doc;
    private Style userStyle;
    private Style assistantStyle;
    private Style systemStyle;

    public Assistant() {
        setupUI();
        initializeActions();
        initializeStyles();
    }

    private void setupUI() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createChatPanel(), BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel header = new JLabel("System Assistant");
        header.setForeground(ACCENT_COLOR);
        header.setFont(HEADER_FONT);

        JLabel subtitle = new JLabel("Type 'help' to see available commands");
        subtitle.setForeground(new Color(128, 128, 128));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel textPanel = new JPanel(new BorderLayout(5, 5));
        textPanel.setBackground(BACKGROUND_COLOR);
        textPanel.add(header, BorderLayout.NORTH);
        textPanel.add(subtitle, BorderLayout.CENTER);

        headerPanel.add(textPanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createChatPanel() {
        JPanel chatPanel = new JPanel(new BorderLayout(0, 10));
        chatPanel.setBackground(BACKGROUND_COLOR);

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setBackground(SECONDARY_COLOR);
        chatArea.setForeground(TEXT_COLOR);
        chatArea.setFont(MAIN_FONT);
        chatArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        doc = chatArea.getStyledDocument();

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 45), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.setBackground(SECONDARY_COLOR);

        chatPanel.add(scrollPane, BorderLayout.CENTER);

        return chatPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        userInput = new JTextField();
        userInput.setBackground(SECONDARY_COLOR);
        userInput.setForeground(TEXT_COLOR);
        userInput.setFont(MAIN_FONT);
        userInput.setCaretColor(TEXT_COLOR);
        userInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 45), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JButton sendButton = createStyledButton("Send");

        inputPanel.add(userInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        userInput.addActionListener(e -> processUserInput());

        return inputPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(80, 36));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 160, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });

        button.addActionListener(e -> processUserInput());
        return button;
    }

    private void initializeStyles() {
        userStyle = chatArea.addStyle("User", null);
        StyleConstants.setForeground(userStyle, USER_MESSAGE_COLOR);
        StyleConstants.setFontFamily(userStyle, "Segoe UI");
        StyleConstants.setFontSize(userStyle, 14);

        assistantStyle = chatArea.addStyle("Assistant", null);
        StyleConstants.setForeground(assistantStyle, TEXT_COLOR);
        StyleConstants.setFontFamily(assistantStyle, "Segoe UI");
        StyleConstants.setFontSize(assistantStyle, 14);

        systemStyle = chatArea.addStyle("System", null);
        StyleConstants.setForeground(systemStyle, ACCENT_COLOR);
        StyleConstants.setFontFamily(systemStyle, "Segoe UI");
        StyleConstants.setFontSize(systemStyle, 14);
        StyleConstants.setItalic(systemStyle, true);
    }

    private void initializeActions() {
        actions = new HashMap<>();
        actions.put("clear cache", () -> executePythonScript("temp.py"));
        actions.put("network", () -> executeBatchScript("Network.bat"));
        actions.put("bsod", () -> executeBatchScript("BSOD.bat")); // Updated to use batch script
        actions.put("audio", () -> executeBatchScript("Audio.bat"));
        actions.put("bluetooth", () -> executeBatchScript("Bluetooth.bat"));
        actions.put("browser", () -> executeBatchScript("Browser.bat")); // Updated to use batch script

        actions.put("help", this::showHelp);
        actions.put("clear chat", this::clearChat);
        actions.put("network info", () -> executePythonScript("2.1.py"));
    }

    private void processUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) return;

        appendMessage("You", input, userStyle);
        userInput.setText("");

        boolean commandFound = false;
        String inputLower = input.toLowerCase();
        for (String key : actions.keySet()) {
            if (inputLower.contains(key)) {
                actions.get(key).run();
                commandFound = true;
                break;
            }
        }

        if (!commandFound) {
            appendMessage("Assistant", "Command not recognized. Type 'help' to see available commands.", assistantStyle);
        }
    }

    private void appendMessage(String sender, String message, Style style) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String formattedMessage = String.format("[%s] %s: %s\n", timestamp, sender, message);
            doc.insertString(doc.getLength(), formattedMessage, style);
            chatArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void showProgress(String task) {
//        progressBar.setVisible(true);
//        progressBar.setIndeterminate(true);
        appendMessage("System", "Running " + task + "...", systemStyle);
    }

    private void hideProgress(String completionMessage) {
//        progressBar.setVisible(false);
        appendMessage("System", completionMessage, systemStyle);
    }

    private void executePythonScript(String scriptName) {
        showProgress("executing " + scriptName);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    String scriptPath = Paths.get(SCRIPTS_PATH, scriptName).toString();
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
                    if (exitCode == 0) {
                        appendMessage("Assistant", output.toString(), assistantStyle);
                    } else {
                        appendMessage("System", "Error executing script: " + scriptName, systemStyle);
                    }
                } catch (Exception e) {
                    appendMessage("System", "Error: " + e.getMessage(), systemStyle);
                }
                return null;
            }

            @Override
            protected void done() {
                hideProgress("✓ " + scriptName + " executed successfully!");
            }
        };
        worker.execute();
    }

    private void executeBatchScript(String scriptName) {
        showProgress("Executing " + scriptName);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    String scriptPath = Paths.get(SCRIPTS_PATH, scriptName).toString();
                    ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", scriptPath);

                    pb.redirectErrorStream(true);
                    Process process = pb.start();

                    // Capture output to avoid deadlocks
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            appendMessage("Assistant", line, assistantStyle);
                        }
                    }

                    // Ensure the process exits properly
                    boolean finished = process.waitFor(10, TimeUnit.SECONDS);
                    if (!finished) {
                        process.destroyForcibly();  // Kill if it doesn't stop
                        appendMessage("System", "Script timeout, forcefully terminated: " + scriptName, systemStyle);
                    } else {
                        int exitCode = process.exitValue();
                        appendMessage("System", "Script " + scriptName + " exited with code: " + exitCode, systemStyle);
                    }

                } catch (Exception e) {
                    appendMessage("System", "Error: " + e.getMessage(), systemStyle);
                }
                return null;
            }

            @Override
            protected void done() {
                hideProgress("✓ " + scriptName + " execution completed.");
            }
        };
        worker.execute();
    }



    private void clearChat() {
        chatArea.setText("");
        appendMessage("System", "Chat cleared. Type 'help' to see available commands.", systemStyle);
    }

    private void showHelp() {
        String helpText = """
            🔍 Available commands :
            
            ├─ clear cache : Removes temporary files (run this application as Administrator for better result)
            
            ├─ network : To troubleshoot the network (if your wifi or network device is not working properly) 
            
            ├─ bsod : To troubleshoot system files (if you are facing blue screen error continuously) 
            
            ├─ audio : To troubleshoot audio (if you have problem in audio output) 
            
            ├─ bluetooth : To troubleshoot bluetooth (if you have problem in bluetooth device connection) 
            
            ├─ browser : To clear browser cache
            
            └─ help : Shows this help message
            """;
        appendMessage("Assistant", helpText, assistantStyle);
    }
}

class ModernScrollBarUI extends BasicScrollBarUI {

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(79, 147, 255, 100);
        this.trackColor = new Color(30, 30, 30);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y,
                thumbBounds.width, thumbBounds.height,
                8, 8);

        g2.dispose();
    }
}