package main.java.org.anynomous;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.Timer;
import java.awt.geom.*;
import java.awt.MultipleGradientPaint;
import java.awt.LinearGradientPaint;

public class Firewall extends JPanel {

    private static final Color PRIMARY_DARK = new Color(0, 0, 0);
    private static final Color ACCENT_COLOR = new Color(147, 51, 234);
    private static final Color SECONDARY_ACCENT = new Color(99, 102, 241);
    private static final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private static final Color BUTTON_COLOR = new Color(38, 38, 38);
    private static final Color BUTTON_HOVER = new Color(48, 48, 48);
    private static final Color LOG_BACKGROUND = new Color(24, 24, 27);
    private static final Color LOG_BORDER = new Color(63, 63, 70);
    private static final Color OUTPUT_BACKGROUND = new Color(15, 15, 15);
    private static final Color OUTPUT_TEXT = new Color(0, 255, 0);
    private static final Color OUTPUT_BORDER = new Color(76, 29, 149);
    private static final Color HEADER_BACKGROUND = new Color(24, 24, 27);

    private static final Font BUTTON_FONT = new Font("Inter", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("Inter", Font.BOLD, 24);
    private static final Font LOG_FONT = new Font("Consolas", Font.PLAIN, 12);
    
    private static final int BUTTON_HEIGHT = 35;
    private static final int BUTTON_WIDTH = 140;
    private static final int BUTTON_RADIUS = 8;

    private JTextArea outputArea;
    private int loadingAngle = 0;
    private JLabel statusLabel;
    private boolean isScanning = false;
    private JProgressBar progressBar;
    private JPanel metricsPanel;
    private Timer animationTimer;

    private static final Color GLOW_COLOR_1 = new Color(147, 51, 234);  // Purple
    private static final Color GLOW_COLOR_2 = new Color(79, 70, 229);   // Indigo
    private static final Color GLOW_COLOR_3 = new Color(236, 72, 153);  // Pink
    private static final Color HOVER_GRADIENT_START = new Color(55, 48, 163);
    private static final Color HOVER_GRADIENT_END = new Color(76, 29, 149);

    public Firewall() {
        setupMainPanel();
        createLayout();
        startLoadingAnimation();
    }

    private void setupMainPanel() {
        setBackground(PRIMARY_DARK);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void createLayout() {
        // Header panel with title and metrics
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Network Security Dashboard");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_PRIMARY);
        
        // Create metrics panel for real-time data
        metricsPanel = createMetricsPanel();
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(metricsPanel, BorderLayout.EAST);
        
        // Create two-column main panel for better responsiveness
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(PRIMARY_DARK);

        // Left column - animation and status
        JPanel leftColumn = new JPanel(new BorderLayout(0, 15));
        leftColumn.setBackground(PRIMARY_DARK);
        
        // Animation panel (made smaller)
        JPanel animationWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        animationWrapper.setBackground(PRIMARY_DARK);
        FirewallAnimationPanel animationPanel = new FirewallAnimationPanel();
        animationWrapper.add(animationPanel);
        
        // Status panel with progress bar
        JPanel statusContainer = new JPanel(new BorderLayout(5, 0));
        statusContainer.setBackground(PRIMARY_DARK);
        statusContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statusPanel.setBackground(PRIMARY_DARK);
        statusLabel = new JLabel("Ready to scan");
        statusLabel.setForeground(TEXT_PRIMARY);
        statusLabel.setFont(new Font("Inter", Font.BOLD, 14));
        statusPanel.add(statusLabel);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setBackground(new Color(38, 38, 38));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(150, 8));
        progressBar.setValue(0);
        progressBar.setVisible(false);
        
        statusContainer.add(statusPanel, BorderLayout.WEST);
        statusContainer.add(progressBar, BorderLayout.EAST);
        
        leftColumn.add(animationWrapper, BorderLayout.CENTER);
        leftColumn.add(statusContainer, BorderLayout.SOUTH);
        
        // Right column - button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Output panel (full width)
        JPanel outputPanel = createOutputPanel();
        
        // Add components to main content with GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Left column (animation and status)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 10);
        mainContent.add(leftColumn, gbc);
        
        // Right column (buttons)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 10, 15, 0);
        mainContent.add(buttonPanel, gbc);
        
        // Output panel (full width)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContent.add(outputPanel, gbc);
        
        // Add components to main panel with scroll capability
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PRIMARY_DARK);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add window resize listener
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustLayout();
            }
        });
    }
    
    private void adjustLayout() {
        int width = getWidth();
        
        // Switch to vertical layout if width is too small
        if (width < 700) {
            // Find the main content panel and adjust its layout
            for (Component comp : getComponents()) {
                if (comp instanceof JScrollPane) {
                    Component viewport = ((JScrollPane)comp).getViewport().getView();
                    if (viewport instanceof JPanel) {
                        JPanel mainContent = (JPanel)viewport;
                        
                        // Get the left and right components
                        Component leftColumn = null;
                        Component buttonPanel = null;
                        Component outputPanel = null;
                        
                        for (Component c : mainContent.getComponents()) {
                            GridBagConstraints gbc = ((GridBagLayout)mainContent.getLayout()).getConstraints(c);
                            if (gbc.gridx == 0 && gbc.gridy == 0) leftColumn = c;
                            if (gbc.gridx == 1 && gbc.gridy == 0) buttonPanel = c;
                            if (gbc.gridy == 1) outputPanel = c;
                        }
                        
                        if (leftColumn != null && buttonPanel != null && outputPanel != null) {
                            // Remove all components
                            mainContent.removeAll();
                            
                            // Add components in vertical layout
                            GridBagConstraints gbc = new GridBagConstraints();
                            
                            // Animation on top
                            gbc.gridx = 0;
                            gbc.gridy = 0;
                            gbc.gridwidth = 1;
                            gbc.weightx = 1.0;
                            gbc.weighty = 0;
                            gbc.fill = GridBagConstraints.HORIZONTAL;
                            gbc.insets = new Insets(0, 0, 15, 0);
                            mainContent.add(leftColumn, gbc);
                            
                            // Buttons below
                            gbc.gridy = 1;
                            gbc.insets = new Insets(0, 0, 15, 0);
                            mainContent.add(buttonPanel, gbc);
                            
                            // Output at bottom
                            gbc.gridy = 2;
                            gbc.weighty = 1.0;
                            gbc.fill = GridBagConstraints.BOTH;
                            gbc.insets = new Insets(0, 0, 0, 0);
                            mainContent.add(outputPanel, gbc);
                            
                            mainContent.revalidate();
                            mainContent.repaint();
                        }
                    }
                }
            }
        }
        
        // Remove the button layout adjustment since we're using a fixed grid
        JPanel buttonPanel = findButtonPanel();
        if (buttonPanel != null) {
            // Always maintain 2x2 grid, but adjust gaps based on width
            int gap = width < 800 ? 10 : 15;
            ((GridLayout)buttonPanel.getLayout()).setHgap(gap);
            ((GridLayout)buttonPanel.getLayout()).setVgap(gap);
            buttonPanel.revalidate();
        }
    }
    
    private JPanel findButtonPanel() {
        // Search for the button panel by name
        return findComponentByName("buttonPanel");
    }
    
    private JPanel findComponentByName(String name) {
        return findComponentByName(this, name);
    }
    
    private JPanel findComponentByName(Container container, String name) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel && name.equals(((JPanel)comp).getName())) {
                return (JPanel)comp;
            } else if (comp instanceof Container) {
                JPanel found = findComponentByName((Container)comp, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setBackground(HEADER_BACKGROUND);
        
        // CPU Usage
        JPanel cpuPanel = createMetricItem("CPU", "0%");
        
        // Memory Usage
        JPanel memPanel = createMetricItem("Memory", "0MB");
        
        // Network Traffic
        JPanel netPanel = createMetricItem("Network", "0 KB/s");
        
        panel.add(cpuPanel);
        panel.add(memPanel);
        panel.add(netPanel);
        
        // Start a timer to update metrics
        Timer metricsTimer = new Timer(2000, e -> updateMetrics());
        metricsTimer.start();
        
        return panel;
    }
    
    private JPanel createMetricItem(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(5, 2));
        panel.setBackground(HEADER_BACKGROUND);
        
        JLabel titleLabel = new JLabel(label);
        titleLabel.setForeground(new Color(156, 163, 175));
        titleLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 14));
        valueLabel.setName(label.toLowerCase() + "Value"); // For updating later
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateMetrics() {
        // Update CPU usage (simulated)
        JLabel cpuLabel = findLabelByName(metricsPanel, "cpuValue");
        if (cpuLabel != null) {
            int cpuUsage = isScanning ? 30 + new Random().nextInt(40) : 5 + new Random().nextInt(15);
            cpuLabel.setText(cpuUsage + "%");
            if (cpuUsage > 70) {
                cpuLabel.setForeground(new Color(239, 68, 68)); // Red for high usage
            } else if (cpuUsage > 40) {
                cpuLabel.setForeground(new Color(245, 158, 11)); // Orange for medium
            } else {
                cpuLabel.setForeground(TEXT_PRIMARY);
            }
        }
        
        // Update memory usage (simulated)
        JLabel memLabel = findLabelByName(metricsPanel, "memoryValue");
        if (memLabel != null) {
            int memUsage = 200 + new Random().nextInt(300);
            memLabel.setText(memUsage + "MB");
        }
        
        // Update network traffic (simulated)
        JLabel netLabel = findLabelByName(metricsPanel, "networkValue");
        if (netLabel != null) {
            int netTraffic = isScanning ? 50 + new Random().nextInt(200) : 1 + new Random().nextInt(10);
            netLabel.setText(netTraffic + " KB/s");
        }
    }
    
    private JLabel findLabelByName(Container container, String name) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel && name.equals(comp.getName())) {
                return (JLabel) comp;
            } else if (comp instanceof Container) {
                JLabel found = findLabelByName((Container) comp, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    private JPanel createButtonPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(PRIMARY_DARK);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15)); // 2x2 grid with 15px gaps
        panel.setName("buttonPanel");
        panel.setBackground(PRIMARY_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton checkButton = createButton("Check Ports", e -> {
            updateStatus("Scanning open ports...", true);
            simulateProgress("PythonScripts/check/open_port.py");
        });
        
        JButton blockButton = createButton("Block Ports", e -> {
            updateStatus("Blocking vulnerable ports...", true);
            simulateProgress("PythonScripts/check/block_port.py");
        });
        
        JButton scanNetworkButton = createButton("Scan Network", e -> {
            updateStatus("Scanning network for devices...", true);
            simulateProgress("PythonScripts/check/scan_network.py");
        });
        
        JButton analyzeButton = createAnalyzeTrafficButton();
        
        // Add buttons in grid order
        panel.add(checkButton);
        panel.add(blockButton);
        panel.add(scanNetworkButton);
        panel.add(analyzeButton);
        
        outerPanel.add(panel, BorderLayout.CENTER);
        
        return outerPanel;
    }
    
    private JPanel createOutputPanel() {
        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(OUTPUT_BACKGROUND);
        outputArea.setForeground(OUTPUT_TEXT);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setBorder(BorderFactory.createLineBorder(OUTPUT_BORDER));
        outputScrollPane.setMinimumSize(new Dimension(200, 150));
        
        // Create panel with border
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        outputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(OUTPUT_BORDER),
            "Command Output",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Inter", Font.BOLD, 12),
            TEXT_PRIMARY
        ));
        
        return outputPanel;
    }

    private void updateStatus(String message, boolean isScanning) {
        this.isScanning = isScanning;
        statusLabel.setText(message);
        statusLabel.setForeground(isScanning ? SECONDARY_ACCENT : TEXT_PRIMARY);
        progressBar.setVisible(isScanning);
        if (!isScanning) {
            progressBar.setValue(0);
        }
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text) {
            private float glowOpacity = 0f;
            private float hoverScale = 1f;
            private float rippleSize = 0f;
            private float shimmerPosition = 0f;
            private Point ripplePoint = null;
            private Timer hoverTimer;
            private Timer shimmerTimer;
            
            {
                // Initialize hover animation timer with enhanced effects
                hoverTimer = new Timer(16, e -> {
                    if (getBackground() == BUTTON_HOVER) {
                        glowOpacity = Math.min(1f, glowOpacity + 0.08f);
                        hoverScale = Math.min(1.03f, hoverScale + 0.005f);
                    } else {
                        glowOpacity = Math.max(0f, glowOpacity - 0.08f);
                        hoverScale = Math.max(1f, hoverScale - 0.005f);
                    }
                    shimmerPosition = (shimmerPosition + 0.02f) % 2.0f;
                    repaint();
                });
                hoverTimer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int w = getWidth();
                int h = getHeight();
                
                // Apply scale transform for hover effect
                int cx = w / 2;
                int cy = h / 2;
                g2.translate(cx, cy);
                g2.scale(hoverScale, hoverScale);
                g2.translate(-cx, -cy);
                
                // Enhanced button background with multi-color gradient
                if (getBackground() == BUTTON_HOVER) {
                    // Create animated gradient background
                    LinearGradientPaint gradient = new LinearGradientPaint(
                        0, 0,
                        w, h,
                        new float[]{0f, 0.5f, 1f},
                        new Color[]{
                            HOVER_GRADIENT_START,
                            HOVER_GRADIENT_END,
                            HOVER_GRADIENT_START
                        }
                    );
                    g2.setPaint(gradient);
                } else {
                    // Normal state gradient
                    GradientPaint gradient = new GradientPaint(
                        0, 0, BUTTON_COLOR,
                        w, h, new Color(BUTTON_COLOR.getRed() + 10, 
                                      BUTTON_COLOR.getGreen() + 10, 
                                      BUTTON_COLOR.getBlue() + 10)
                    );
                    g2.setPaint(gradient);
                }
                g2.fillRoundRect(0, 0, w, h, BUTTON_RADIUS, BUTTON_RADIUS);
                
                // Shimmer effect
                if (glowOpacity > 0) {
                    float shimmerWidth = w * 0.7f;
                    float shimmerX = -shimmerWidth + (w + shimmerWidth * 2) * shimmerPosition;
                    GradientPaint shimmerGradient = new GradientPaint(
                        shimmerX, 0,
                        new Color(255, 255, 255, 0),
                        shimmerX + shimmerWidth, h,
                        new Color(255, 255, 255, (int)(40 * glowOpacity))
                    );
                    g2.setPaint(shimmerGradient);
                    g2.fillRoundRect(0, 0, w, h, BUTTON_RADIUS, BUTTON_RADIUS);
                }
                
                // Multi-color glow effect
                if (glowOpacity > 0) {
                    // Outer glow with multiple colors
                    int glowSize = 5;
                    Color[] glowColors = {GLOW_COLOR_1, GLOW_COLOR_2, GLOW_COLOR_3};
                    for (int i = 0; i < glowSize; i++) {
                        float alpha = (glowOpacity * (glowSize - i) / glowSize) * 0.3f;
                        Color glowColor = glowColors[i % glowColors.length];
                        g2.setColor(new Color(glowColor.getRed()/255f, 
                                            glowColor.getGreen()/255f, 
                                            glowColor.getBlue()/255f, 
                                            alpha));
                        g2.setStroke(new BasicStroke(i + 1.5f));
                        g2.drawRoundRect(i, i, w - 2*i - 1, h - 2*i - 1, BUTTON_RADIUS, BUTTON_RADIUS);
                    }
                    
                    // Animated border gradient
                    float gradientPosition = (float)((System.currentTimeMillis() % 2000) / 2000.0);
                    float[] fractions = {0.0f, 0.5f, 1.0f};
                    Color[] colors = {GLOW_COLOR_1, GLOW_COLOR_2, GLOW_COLOR_1};
                    LinearGradientPaint borderGradient = new LinearGradientPaint(
                        0, 0, w, h,
                        fractions, colors,
                        MultipleGradientPaint.CycleMethod.REPEAT
                    );
                    g2.setPaint(borderGradient);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(1, 1, w-3, h-3, BUTTON_RADIUS, BUTTON_RADIUS);
                }
                
                // Draw ripple effect with color transition
                if (ripplePoint != null && rippleSize > 0) {
                    float rippleOpacity = Math.max(0, 1 - rippleSize / Math.max(w, h));
                    RadialGradientPaint rippleGradient = new RadialGradientPaint(
                        ripplePoint,
                        rippleSize * Math.max(w, h),
                        new float[]{0f, 1f},
                        new Color[]{
                            new Color(GLOW_COLOR_2.getRed(), 
                                    GLOW_COLOR_2.getGreen(), 
                                    GLOW_COLOR_2.getBlue(), 
                                    (int)(100 * rippleOpacity)),
                            new Color(GLOW_COLOR_1.getRed(), 
                                    GLOW_COLOR_1.getGreen(), 
                                    GLOW_COLOR_1.getBlue(), 
                                    0)
                        }
                    );
                    g2.setPaint(rippleGradient);
                    g2.fillRoundRect(0, 0, w, h, BUTTON_RADIUS, BUTTON_RADIUS);
                }
                
                // Enhanced text rendering
                FontMetrics fm = g2.getFontMetrics(getFont());
                String text = getText();
                int textX = (w - fm.stringWidth(text)) / 2;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2;
                
                if (glowOpacity > 0) {
                    // Text glow effect
                    g2.setColor(new Color(GLOW_COLOR_2.getRed(), 
                                        GLOW_COLOR_2.getGreen(), 
                                        GLOW_COLOR_2.getBlue(), 
                                        (int)(50 * glowOpacity)));
                    g2.drawString(text, textX, textY + 1);
                    
                    // Text gradient when hovered
                    LinearGradientPaint textGradient = new LinearGradientPaint(
                        textX, textY - fm.getAscent(),
                        textX, textY,
                        new float[]{0f, 1f},
                        new Color[]{
                            new Color(255, 255, 255),
                            new Color(220, 220, 255)
                        }
                    );
                    g2.setPaint(textGradient);
                } else {
                    g2.setColor(getForeground());
                }
                g2.drawString(text, textX, textY);
                
                g2.dispose();
            }
        };

        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

                    @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Start ripple effect from click point
                JButton btn = (JButton)e.getSource();
                Point ripplePoint = e.getPoint();
                btn.putClientProperty("ripplePoint", ripplePoint);
                
                // Create and start ripple animation
                Timer rippleTimer = new Timer(16, ae -> {
                    float size = (float)btn.getClientProperty("rippleSize");
                    size += 0.1f;
                    btn.putClientProperty("rippleSize", size);
                    if (size >= 2.0f) {
                        ((Timer)ae.getSource()).stop();
                        btn.putClientProperty("rippleSize", 0f);
                        btn.putClientProperty("ripplePoint", null);
                    }
                    btn.repaint();
                });
                btn.putClientProperty("rippleSize", 0f);
                rippleTimer.start();
            }
        });

        button.addActionListener(action);
        return button;
    }

    private void simulateProgress(String scriptPath) {
        progressBar.setValue(0);
        progressBar.setVisible(true);
        
        Timer progressTimer = new Timer(100, null);
        final int[] progress = {0};
        
        progressTimer.addActionListener(e -> {
            progress[0] += 1 + new Random().nextInt(3);
            if (progress[0] >= 100) {
                progress[0] = 100;
                progressTimer.stop();
                executePythonScript(scriptPath);
            }
            progressBar.setValue(progress[0]);
        });
        
        progressTimer.start();
    }

    private void simulateEnterpriseAnalysis(String scriptPath, String[] stages) {
        progressBar.setValue(0);
        progressBar.setVisible(true);
        
        Timer progressTimer = new Timer(300, null);
        final int[] progress = {0};
        final int[] currentStage = {0};
        
        progressTimer.addActionListener(e -> {
            // Calculate stage progress
            int stageProgress = progress[0] % (100 / stages.length);
            int currentStageIndex = progress[0] / (100 / stages.length);
            
            if (currentStageIndex != currentStage[0] && currentStageIndex < stages.length) {
                currentStage[0] = currentStageIndex;
                SwingUtilities.invokeLater(() -> {
                    outputArea.append("\n[" + getCurrentTimestamp() + "] " + stages[currentStageIndex] + "\n");
                    outputArea.append("------------------------------------------------\n");
                    outputArea.setCaretPosition(outputArea.getDocument().getLength());
                });
            }
            
            progress[0] += 1 + new Random().nextInt(2);
            if (progress[0] >= 100) {
                progress[0] = 100;
                progressTimer.stop();
                executePythonScript(scriptPath);
            }
            progressBar.setValue(progress[0]);
        });
        
        progressTimer.start();
    }

    private JButton createAnalyzeTrafficButton() {
        JButton analyzeButton = createButton("Analyze Traffic", e -> {
            updateStatus("Initializing network traffic analysis...", true);
            outputArea.setText("");
            outputArea.append("Starting Enterprise Network Traffic Analysis\n");
            outputArea.append("==========================================\n\n");
            
            String[] analysisStages = {
                "Initializing traffic capture modules",
                "Analyzing protocol distribution",
                "Performing security threat analysis",
                "Measuring network performance metrics",
                "Analyzing application layer traffic",
                "Processing endpoint connections",
                "Generating comprehensive report"
            };
            
            simulateEnterpriseAnalysis("PythonScripts/check/analyze_traffic.py", analysisStages);
        });
        return analyzeButton;
    }

    private class FirewallAnimationPanel extends JPanel {
        private final int PARTICLE_COUNT = 40;
        private final Random random = new Random();
        private final ArrayList<Particle> particles = new ArrayList<>();
        private final ArrayList<Pulse> pulses = new ArrayList<>();
        
        private FirewallAnimationPanel() {
            // Smaller animation area
            setPreferredSize(new Dimension(160, 160));
            setBackground(PRIMARY_DARK);
            
            // Initialize particles
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                particles.add(new Particle());
            }
            
            // Initialize pulses
            for (int i = 0; i < 3; i++) {
                pulses.add(new Pulse(random.nextInt(100)));
            }
        }

                    @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(getWidth(), getHeight()) / 3;
            
            // Draw pulses (expanding circles)
            for (Pulse pulse : pulses) {
                pulse.update();
                pulse.draw(g2, centerX, centerY);
            }
            
            // Draw outer circle
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 100));
            g2.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
            
            // Draw rotating shield segments
            g2.rotate(Math.toRadians(loadingAngle / 2.0), centerX, centerY);
            
            // Shield segments with improved visual
            for (int i = 0; i < 4; i++) {
                int arcWidth = 60;
                Color segmentColor = new Color(
                    ACCENT_COLOR.getRed(), 
                    ACCENT_COLOR.getGreen(), 
                    ACCENT_COLOR.getBlue(), 
                    isScanning ? 180 : 120
                );
                
                // Alternate segment colors for more visual interest
                if (i % 2 == 0) {
                    segmentColor = new Color(
                        SECONDARY_ACCENT.getRed(), 
                        SECONDARY_ACCENT.getGreen(), 
                        SECONDARY_ACCENT.getBlue(), 
                        isScanning ? 180 : 120
                    );
                }
                
                g2.setColor(segmentColor);
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                          i * 90 - arcWidth/2, arcWidth);
            }
            
            // Draw particles with improved visuals
            for (Particle p : particles) {
                p.update();
                p.draw(g2, centerX, centerY, radius);
            }
            
            // Draw inner glow
            RadialGradientPaint gradient = new RadialGradientPaint(
                centerX, centerY, radius/2,
                new float[] {0.0f, 0.7f, 1.0f},
                new Color[] {
                    new Color(SECONDARY_ACCENT.getRed(), SECONDARY_ACCENT.getGreen(), SECONDARY_ACCENT.getBlue(), 70),
                    new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 30),
                    new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 0)
                }
            );
            g2.setPaint(gradient);
            g2.fillOval(centerX - radius/2, centerY - radius/2, radius, radius);
            
            // Draw scanning effect when active
            if (isScanning) {
                // Add scanning wave effect
                float[] dash = {3.0f, 6.0f};
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dash, loadingAngle / 10f));
                g2.setColor(new Color(SECONDARY_ACCENT.getRed(), SECONDARY_ACCENT.getGreen(), SECONDARY_ACCENT.getBlue(), 150));
                g2.drawOval(centerX - radius * 3/4, centerY - radius * 3/4, radius * 3/2, radius * 3/2);
            }
            
            // Draw center icon - shield or lock
            int iconSize = radius / 3;
            g2.setColor(TEXT_PRIMARY);
            g2.setStroke(new BasicStroke(2));
            
            // Draw shield or lock based on state
            if (isScanning) {
                // Draw a shield shape when scanning
                int[] xPoints = {centerX, centerX + iconSize/2, centerX, centerX - iconSize/2};
                int[] yPoints = {centerY - iconSize/2, centerY, centerY + iconSize/2, centerY};
                g2.fillPolygon(xPoints, yPoints, 4);
            } else {
                // Draw simplified lock when idle
                g2.drawRoundRect(centerX - iconSize/3, centerY - iconSize/4, iconSize*2/3, iconSize/2, 5, 5);
                g2.fillRoundRect(centerX - iconSize/6, centerY - iconSize/8, iconSize/3, iconSize/4, 3, 3);
            }
            
                        g2.dispose();
                    }

        private class Pulse {
            private float size;
            private float maxSize;
            private float opacity;
            
            public Pulse(int delay) {
                size = 0;
                maxSize = 40 + random.nextInt(30);
                opacity = 0.7f;
                
                // Start at different phases
                size = delay;
            }
            
            public void update() {
                size += 0.8f;
                if (size > maxSize) {
                    size = 0;
                    maxSize = 40 + random.nextInt(30);
                }
                
                opacity = Math.max(0, 0.7f - (size / maxSize) * 0.7f);
            }
            
            public void draw(Graphics2D g2, int centerX, int centerY) {
                if (size <= 0) return;
                
                g2.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 
                                     (int)(opacity * 255)));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(centerX - (int)size/2, centerY - (int)size/2, (int)size, (int)size);
            }
        }

        private class Particle {
            private double angle;
            private double distance;
            private double speed;
            private int alpha;
            private int size;
            private boolean isMovingOut;
            private Color color;
            
            public Particle() {
                reset(true);
            }
            
            public void reset(boolean randomizeDistance) {
                angle = random.nextDouble() * 360;
                distance = randomizeDistance ? random.nextDouble() * 0.5 : 0.1;
                speed = 0.005 + random.nextDouble() * 0.01;
                alpha = 50 + random.nextInt(150);
                size = 1 + random.nextInt(3);
                isMovingOut = true;
                
                // Random color between accent and secondary accent
                float mix = random.nextFloat();
                color = new Color(
                    (int)(ACCENT_COLOR.getRed() * mix + SECONDARY_ACCENT.getRed() * (1-mix)),
                    (int)(ACCENT_COLOR.getGreen() * mix + SECONDARY_ACCENT.getGreen() * (1-mix)),
                    (int)(ACCENT_COLOR.getBlue() * mix + SECONDARY_ACCENT.getBlue() * (1-mix))
                );
            }
            
            public void update() {
                if (isMovingOut) {
                    distance += speed;
                    if (distance > 1.0) {
                        if (random.nextDouble() < 0.3) {
                            reset(false);
                        } else {
                            isMovingOut = false;
                        }
                    }
                } else {
                    distance -= speed * 0.7; // Slower return for visual interest
                    if (distance < 0.1) {
                        reset(false);
                        isMovingOut = true;
                    }
                }
                
                // Update angle slightly for curved trajectories
                angle += (random.nextDouble() - 0.5) * 2;
            }
            
            public void draw(Graphics2D g2, int centerX, int centerY, int radius) {
                double rads = Math.toRadians(angle);
                int x = (int)(centerX + Math.cos(rads) * radius * distance);
                int y = (int)(centerY + Math.sin(rads) * radius * distance);
                
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                                     isScanning ? alpha : alpha/2));
                
                // Draw particles with blur effect for more visual appeal
                if (size > 1) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                    g2.fillOval(x - size, y - size, size*2, size*2);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
                
                g2.fillOval(x - size/2, y - size/2, size, size);
            }
        }
    }

    private void startLoadingAnimation() {
        animationTimer = new Timer(40, e -> {
            loadingAngle = (loadingAngle + 2) % 360;
            repaint();
        });
        animationTimer.start();
    }

    private void executePythonScript(String scriptPath) {
        // Clear output area before starting new script execution
        SwingUtilities.invokeLater(() -> {
            outputArea.setText("");
            outputArea.append("Executing " + scriptPath + "...\n\n");
        });
        
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
                    
                    // Update output area in real-time
                    final String currentLine = line;
                    SwingUtilities.invokeLater(() -> {
                        outputArea.append(currentLine + "\n");
                        outputArea.setCaretPosition(outputArea.getDocument().getLength());
                    });
                    
                    // Simulate some processing time
                    try {
                        Thread.sleep(50 + new Random().nextInt(150));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }

                int exitCode = process.waitFor();

                SwingUtilities.invokeLater(() -> {
                    // Add separator in output area
                    outputArea.append("\n----- Process completed with exit code " + exitCode + " -----\n\n");
                    outputArea.setCaretPosition(outputArea.getDocument().getLength());
                    updateStatus("Scan complete", false);
                });

            } catch (IOException | InterruptedException e) {
                SwingUtilities.invokeLater(() -> {
                    outputArea.append("ERROR: " + e.getMessage() + "\n");
                    updateStatus("Error during scan", false);
                });
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private String getCurrentTimestamp() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}
