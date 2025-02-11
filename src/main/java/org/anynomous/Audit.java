package main.java.org.anynomous;

import javax.swing.*;
import javax.swing.border.*;
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
//    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private static final Color TEXT_SECONDARY = new Color(156, 163, 175);

    private static final Font HEADER_FONT = new Font("Inter", Font.BOLD, 32);
    private static final Font SUBTITLE_FONT = new Font("Inter", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 14);
    private static final Font LOG_FONT = new Font("JetBrains Mono", Font.PLAIN, 12);

    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_WIDTH = 200;
    private static final int PANEL_PADDING = 24;
    private static final int COMPONENT_SPACING = 16;

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

        JLabel iconLabel = new JLabel("\uD83D\uDEE1");
        iconLabel.setFont(new Font("Segue UI Emoji", Font.PLAIN, 48));
        iconLabel.setForeground(ACCENT_COLOR);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel header = new JLabel("Audit");
        header.setForeground(TEXT_PRIMARY);
        header.setFont(HEADER_FONT);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Real-time Network Traffic Protection");
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, COMPONENT_SPACING)));
        headerPanel.add(header);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(subtitle);

        return headerPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, COMPONENT_SPACING, COMPONENT_SPACING));
        controlPanel.setBackground(SECONDARY_DARK);
        controlPanel.setBorder(createRoundedBorder());

        JButton startButton = createButton("Config. Audit", //function of this button is below the second button
                e -> checkScripts(),
                ACCENT_COLOR, ACCENT_HOVER);


        JButton stopButton = createButton("Network Audit",
                e -> executePythonScript("PythonScripts/check/sc.py"),
                DANGER_COLOR, new Color(239, 68, 68));

        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        return controlPanel;
    }
  // audit button script
    private void checkScripts() {
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
//                statusLabel.setText("In Progress...");
//                progressBar.setIndeterminate(true);
                logArea.setText("");
                logArea.append("hello");

                String[] scripts = {"2.1.py", "2.2.py", "2.3.py", "2.py", "3.1.py", "3.2.py", "9.1.py", "17.1.py",
                        "17.2.py", "18.1.py", "18.2.py", "18.3.py", "18.4.py", "18.5.py", "18.6.py", "18.7.py",
                        "18.8.py", "18.9.py", "19.1.py", "19.2.py", "19.3.py", "19.4"};
                for (int i = 0; i < scripts.length; i++) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("python", "PythonScripts/audit/" + scripts[i]);
                        Process process = pb.start();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            publish(line);
                        }
                        process.waitFor();
//                        progressBar.setValue((i + 1) * 100 / scripts.length);
                    } catch (Exception e) {
                        publish("Error running " + scripts[i] + ": " + e.getMessage());
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

    private JPanel createLogPanel() {
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.setBackground(SECONDARY_DARK);
        logPanel.setBorder(createRoundedBorder());

        JLabel logHeader = new JLabel("Activity Log");
        logHeader.setFont(BUTTON_FONT);
        logHeader.setForeground(TEXT_PRIMARY);
        logHeader.setBorder(new EmptyBorder(12, 16, 12, 16));

        logArea = new JTextArea();
        logArea.setBackground(PRIMARY_DARK);
        logArea.setForeground(TEXT_PRIMARY);
        logArea.setFont(LOG_FONT);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PRIMARY_DARK);
        scrollPane.getViewport().setBackground(PRIMARY_DARK);

        logPanel.add(logHeader, BorderLayout.NORTH);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        return logPanel;
    }

    private JButton createButton(String label, ActionListener action, Color backgroundColor, Color hoverColor) {
        JButton button = new JButton(label);
        button.setFont(BUTTON_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
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

    private Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                BorderFactory.createLineBorder(SECONDARY_DARK, 1, true)
        );
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
