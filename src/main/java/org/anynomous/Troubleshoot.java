package main.java.org.anynomous;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.geom.Point2D;
import java.awt.BasicStroke;
import java.awt.event.HierarchyEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class Troubleshoot {
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    private static final Color PANEL_BACKGROUND = new Color(10, 10, 10);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color SECONDARY_TEXT_COLOR = new Color(170, 170, 170);
    private static final Color HOVER_COLOR = new Color(20, 20, 20);
    private static final Color GRADIENT_START = new Color(8, 8, 8);
    private static final Color GRADIENT_END = new Color(12, 12, 12);
    private static final Color ACCENT_COLOR = new Color(45, 45, 45);
    private static final Color ACCENT_HOVER_COLOR = new Color(55, 55, 55);
    private static final Color CARD_SHADOW_COLOR = new Color(0, 0, 0, 100);
    private static final Color SIDEBAR_GRADIENT_START = new Color(5, 5, 5);
    private static final Color SIDEBAR_GRADIENT_END = new Color(10, 10, 10);
    private static final Color SIDEBAR_ACCENT = new Color(50, 50, 50);
    private static final Color SIDEBAR_HIGHLIGHT = new Color(255, 255, 255, 15);
    private static final Color[] CATEGORY_COLORS = {
        new Color(70, 130, 180, 150),  // System Performance - Steel Blue
        new Color(65, 105, 225, 150),  // Network Diagnostics - Royal Blue
        new Color(46, 139, 87, 150),   // Driver Issues - Sea Green
        new Color(210, 105, 30, 150),  // File System Repair - Chocolate
        new Color(106, 90, 205, 150),  // Blue Screen Errors - Slate Blue
        new Color(220, 20, 60, 150),   // App Compatibility - Crimson
        new Color(72, 61, 139, 150),   // Audio Troubleshooting - Dark Slate Blue
        new Color(0, 139, 139, 150),   // Bluetooth & Devices - Dark Cyan
        new Color(184, 134, 11, 150)   // Browser Issues - Dark Goldenrod
    };
//    private static final Color BUTTON_COLOR = new Color(238, 10, 10);

    private final SystemPerformancePanel SystemPerformancePanel;
    private final NetworkDiagnosticsPanel NetworkDiagnosticsPanel;
    private final DriversIssuesPanel DriversIssuesPanel;
    private final FileSystemRepairPanel FileSystemRepairPanel;
    private final BlueScreenErrorPanel BlueScreenErrorPanel;
    private final ApplicationCompatibilityIssuesPanel ApplicationCompatibilityIssuesPanel;
    private final AudioTroubleshootingPanel AudioTroubleshootingPanel;
    private final BluetoothAndDeviceConnectivityPanel BluetoothAndDeviceConnectivityPanel;
    private final BrowserIssuesPanel BrowserIssuesPanel;
    private final JSplitPane splitPane;
//    private final JPanel[] sidePanels;

    public Troubleshoot(JPanel... sidePanels) {
//        this.sidePanels = sidePanels;
        this.SystemPerformancePanel = new SystemPerformancePanel();
        this.NetworkDiagnosticsPanel = new NetworkDiagnosticsPanel();
        this.DriversIssuesPanel = new DriversIssuesPanel();
        this.FileSystemRepairPanel = new FileSystemRepairPanel();
        this.BlueScreenErrorPanel = new BlueScreenErrorPanel();
        this.ApplicationCompatibilityIssuesPanel = new ApplicationCompatibilityIssuesPanel();
        this.AudioTroubleshootingPanel = new AudioTroubleshootingPanel();
        this.BluetoothAndDeviceConnectivityPanel = new BluetoothAndDeviceConnectivityPanel();
        this.BrowserIssuesPanel = new BrowserIssuesPanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel headerPanel = this.createHeaderPanel();
        mainPanel.add(headerPanel, "North");
        JPanel gridPanel = this.createMainGridPanel();
        mainPanel.add(gridPanel, "Center");
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, new JPanel());
        this.splitPane.setDividerLocation(800);
        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setBackground(BACKGROUND_COLOR);
        this.splitPane.setRightComponent(new JPanel());
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Troubleshoot");
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segue UI", Font.BOLD, 22));
        JLabel subtitleLabel = new JLabel("<html>Select a category to diagnose and fix issues</html>");
        subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
        subtitleLabel.setFont(new Font("Segue UI", Font.PLAIN, 13));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);
        return headerPanel;
    }

    private JPanel createMainGridPanel() {
        // Use a more responsive layout with better spacing
        JPanel mainPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Add the item panels
        mainPanel.add(createItemPanel("System Performance", "Analyze and optimize system resources", 0));
        mainPanel.add(createItemPanel("Network Diagnostics", "Troubleshoot network connectivity issues", 1));
        mainPanel.add(createItemPanel("Driver Issues", "Identify and fix driver problems", 2));
        mainPanel.add(createItemPanel("File System Repair", "Scan and repair file system errors", 3));
        mainPanel.add(createItemPanel("Blue Screen Errors", "Analyze and fix system crashes", 4));
        mainPanel.add(createItemPanel("App Compatibility", "Resolve application compatibility issues", 5));
        mainPanel.add(createItemPanel("Audio Troubleshooting", "Fix sound and audio problems", 6));
        mainPanel.add(createItemPanel("Bluetooth & Devices", "Resolve device connectivity issues", 7));
        mainPanel.add(createItemPanel("Browser Issues", "Fix common browser problems", 8));

        return mainPanel;
    }

    public JPanel createItemPanel(String title, String description, int index) {
        // Get the category color for this panel
        final Color categoryColor = CATEGORY_COLORS[index % CATEGORY_COLORS.length];
        
        JPanel panel = new JPanel() {
            private float hoverAlpha = 0.0f;
            private Timer fadeTimer;
            private boolean isHovered = false;
            private boolean isPressed = false;
            private float pulseEffect = 0.0f;
            private Timer pulseTimer;
            
            {
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                setBackground(PANEL_BACKGROUND);
                setBorder(BorderFactory.createEmptyBorder(18, 15, 15, 15));
                
                // Enhanced mouse interaction
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        startFadeAnimation(true);
                        startPulseAnimation(true);
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        isPressed = false;
                        startFadeAnimation(false);
                        startPulseAnimation(false);
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        isPressed = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        isPressed = false;
                        repaint();
                        if (isHovered) {
                            openSideBar(index);
                        }
                    }
                });
            }
            
            private void startFadeAnimation(boolean fadeIn) {
                if (fadeTimer != null && fadeTimer.isRunning()) {
                    fadeTimer.stop();
                }
                
                fadeTimer = new Timer(10, e -> {
                    if (fadeIn) {
                        hoverAlpha = Math.min(1.0f, hoverAlpha + 0.08f);
                    } else {
                        hoverAlpha = Math.max(0.0f, hoverAlpha - 0.08f);
                    }
                    
                    if ((fadeIn && hoverAlpha >= 1.0f) || (!fadeIn && hoverAlpha <= 0.0f)) {
                        fadeTimer.stop();
                    }
                    repaint();
                });
                fadeTimer.start();
            }
            
            private void startPulseAnimation(boolean start) {
                if (pulseTimer != null && pulseTimer.isRunning()) {
                    pulseTimer.stop();
                }
                
                if (start) {
                    pulseTimer = new Timer(30, e -> {
                        pulseEffect += 0.1f;
                        repaint();
                    });
                    pulseTimer.start();
                } else {
                    pulseEffect = 0.0f;
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw black background
                g2d.setColor(PANEL_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 8, 8));
                
                // Draw hover effect with subtle color
                if (isHovered || hoverAlpha > 0) {
                    // Base hover effect
                    g2d.setColor(isPressed ? HOVER_COLOR : new Color(15, 15, 15));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 8, 8));
                    
                    // Subtle color accent on hover
                    Color hoverAccent = new Color(
                        categoryColor.getRed(),
                        categoryColor.getGreen(),
                        categoryColor.getBlue(),
                        (int)(40 * hoverAlpha)
                    );
                    g2d.setColor(hoverAccent);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 8, 8));
                    
                    // Animated border on hover
                    float borderAlpha = hoverAlpha * (0.6f + 0.4f * (float)Math.sin(pulseEffect));
                    g2d.setColor(new Color(
                        categoryColor.getRed(),
                        categoryColor.getGreen(),
                        categoryColor.getBlue(),
                        (int)(100 * borderAlpha)
                    ));
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, 8, 8));
                    
                    // Top accent line
                    g2d.setColor(new Color(
                        categoryColor.getRed(),
                        categoryColor.getGreen(),
                        categoryColor.getBlue(),
                        (int)(150 * hoverAlpha)
                    ));
                    g2d.fillRect(0, 0, width, 2);
                }
                
                // Press effect
                if (isPressed) {
                    g2d.translate(1, 1);
                }
                
                g2d.dispose();
            }
        };

        // Create icon panel with improved styling
        JPanel iconPanel = new JPanel() {
            private float pulseScale = 1.0f;
            private Timer pulseTimer;
            private boolean pulsing = false;
            
            {
                setOpaque(false);
                // Increase the panel size to accommodate larger icons
                setPreferredSize(new Dimension(60, 60));
                
                // Add subtle pulse animation on hover
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        startPulseAnimation();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        stopPulseAnimation();
                    }
                });
            }
            
            private void startPulseAnimation() {
                if (pulseTimer != null && pulseTimer.isRunning()) {
                    return;
                }
                
                pulsing = true;
                pulseTimer = new Timer(50, e -> {
                    pulseScale = 1.0f + 0.05f * (float)Math.sin(System.currentTimeMillis() / 200.0);
                    repaint();
                });
                pulseTimer.start();
            }
            
            private void stopPulseAnimation() {
                if (pulseTimer != null) {
                    pulseTimer.stop();
                    pulsing = false;
                    pulseScale = 1.0f;
                    repaint();
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Center the icon
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                
                // Increase circle size
                int circleSize = pulsing ? (int)(48 * pulseScale) : 48;
                g2d.setColor(new Color(15, 15, 15));
                g2d.fillOval(centerX - circleSize/2, centerY - circleSize/2, circleSize, circleSize);
                
                // Draw subtle glow
                if (pulsing) {
                    int glowSize = circleSize + 12;
                    RadialGradientPaint glow = new RadialGradientPaint(
                        centerX, centerY, glowSize/2,
                        new float[] {0.0f, 1.0f},
                        new Color[] {
                            new Color(categoryColor.getRed(), categoryColor.getGreen(), categoryColor.getBlue(), 40),
                            new Color(categoryColor.getRed(), categoryColor.getGreen(), categoryColor.getBlue(), 0)
                        }
                    );
                    g2d.setPaint(glow);
                    g2d.fillOval(centerX - glowSize/2, centerY - glowSize/2, glowSize, glowSize);
                }
                
                // Draw the emoji with increased size
                String emoji = getEmojiForIndex(index);
                // Increase font size from 20 to 24
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, pulsing ? (int)(24 * pulseScale) : 24));
                FontMetrics fm = g2d.getFontMetrics();
                int emojiWidth = fm.stringWidth(emoji);
                int emojiHeight = fm.getHeight();
                g2d.setColor(categoryColor);
                g2d.drawString(emoji, centerX - emojiWidth/2, centerY + emojiHeight/4);
                
                g2d.dispose();
            }
        };

        // Create title label with subtle color
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create description label
        JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 140px;'>" + description + "</div></html>");
        descLabel.setForeground(SECONDARY_TEXT_COLOR);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with proper spacing
        panel.add(Box.createVerticalStrut(5));
        panel.add(iconPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(descLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private String getEmojiForIndex(int index) {
        return switch (index) {
            case 0 -> "⚙️";
            case 1 -> "🌐";
            case 2 -> "🔧";
            case 3 -> "🗂️";
            case 4 -> "💻";
            case 5 -> "🛠️";
            case 6 -> "🔊";
            case 7 -> "📶";
            case 8 -> "🌐";
            default -> "❓";
        };
    }

    private void openSideBar(int index) {
        JPanel selectedPanel = null;
        switch (index) {
            case 0 -> selectedPanel = this.SystemPerformancePanel;
            case 1 -> selectedPanel = this.NetworkDiagnosticsPanel;
            case 2 -> selectedPanel = this.DriversIssuesPanel;
            case 3 -> selectedPanel = this.FileSystemRepairPanel;
            case 4 -> selectedPanel = this.BlueScreenErrorPanel;
            case 5 -> selectedPanel = this.ApplicationCompatibilityIssuesPanel;
            case 6 -> selectedPanel = this.AudioTroubleshootingPanel;
            case 7 -> selectedPanel = this.BluetoothAndDeviceConnectivityPanel;
            case 8 -> selectedPanel = this.BrowserIssuesPanel;
        }

        if (selectedPanel != null) {
            this.splitPane.setRightComponent(selectedPanel);
        } else {
            JPanel placeholder = new JPanel(new BorderLayout());
            placeholder.setBackground(PANEL_BACKGROUND);
            JLabel noInfoLabel = new JLabel("No Information Available");
            noInfoLabel.setForeground(TEXT_COLOR);
            noInfoLabel.setHorizontalAlignment(0);
            placeholder.add(noInfoLabel, "Center");
            this.splitPane.setRightComponent(placeholder);
        }

        this.splitPane.setDividerLocation(this.splitPane.getWidth() - 300);
        this.splitPane.revalidate();
        this.splitPane.repaint();
    }

    public JSplitPane getSplitPane() {
        return this.splitPane;
    }

    private JPanel createPlaceholderPanel(String message) {
        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setBackground(PANEL_BACKGROUND);
        JLabel noInfoLabel = new JLabel(message);
        noInfoLabel.setForeground(TEXT_COLOR);
        noInfoLabel.setHorizontalAlignment(0);
        placeholder.add(noInfoLabel, "Center");
        return placeholder;
    }

    // Add these helper methods for color manipulation
    private Color darkenColor(Color color, float factor) {
        return new Color(
            Math.max(0, Math.round(color.getRed() * factor)),
            Math.max(0, Math.round(color.getGreen() * factor)),
            Math.max(0, Math.round(color.getBlue() * factor)),
            color.getAlpha()
        );
    }

    private Color lightenColor(Color color, float factor) {
        return new Color(
            Math.min(255, Math.round(color.getRed() + (255 - color.getRed()) * factor)),
            Math.min(255, Math.round(color.getGreen() + (255 - color.getGreen()) * factor)),
            Math.min(255, Math.round(color.getBlue() + (255 - color.getBlue()) * factor)),
            color.getAlpha()
        );
    }

    private static Color blendColors(Color c1, Color c2, float ratio) {
        float iRatio = 1.0f - ratio;
        
        int r = Math.round(c1.getRed() * iRatio + c2.getRed() * ratio);
        int g = Math.round(c1.getGreen() * iRatio + c2.getGreen() * ratio);
        int b = Math.round(c1.getBlue() * iRatio + c2.getBlue() * ratio);
        
        return new Color(
            Math.min(255, Math.max(0, r)),
            Math.min(255, Math.max(0, g)),
            Math.min(255, Math.max(0, b))
        );
    }

    public static class SystemPerformancePanel extends BaseSidebarPanel {
        // Minimal black color scheme with subtle accent
        private static final Color PANEL_BACKGROUND = new Color(10, 10, 10);
        private static final Color BUTTON_COLOR = new Color(25, 25, 25);
        private static final Color BUTTON_HOVER_COLOR = new Color(35, 35, 35);
        private static final Color BUTTON_ACCENT = new Color(70, 130, 180, 150); // Steel Blue
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final Color OUTPUT_BACKGROUND = new Color(5, 5, 5);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
        private static final int CORNER_RADIUS = 4;

        public SystemPerformancePanel() {
            super("System Performance");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            contentPanel.add(createScanSection(), BorderLayout.CENTER);
            
            // Add slide-in animation for content elements
            animateContentElements(contentPanel);
            
            return contentPanel;
        }
        
        private void animateContentElements(JPanel panel) {
            // Get all components to animate
            Component[] components = panel.getComponents();
            
            // Initial state - move components to the right
            for (Component comp : components) {
                if (comp instanceof JComponent) {
                    JComponent jcomp = (JComponent) comp;
                    jcomp.setLocation(jcomp.getX() + 50, jcomp.getY());
                    jcomp.setOpaque(false);
                    jcomp.setAlignmentX(0.0f);
                }
            }
            
            // Create animation timer
            Timer animTimer = new Timer(30, null);
            final int[] step = {0};
            final int totalSteps = 15;
            
            animTimer.addActionListener(e -> {
                step[0]++;
                
                // Animate each component with slight delay between them
                for (int i = 0; i < components.length; i++) {
                    Component comp = components[i];
                    if (comp instanceof JComponent) {
                        JComponent jcomp = (JComponent) comp;
                        
                        // Only start animating this component if it's time
                        if (step[0] > i * 2) {
                            int targetX = jcomp.getX() - (50 / totalSteps);
                            jcomp.setLocation(targetX, jcomp.getY());
                            
                            // Fade in
                            float alpha = Math.min(1.0f, (float)(step[0] - i * 2) / 10);
                            jcomp.putClientProperty("alpha", alpha);
                            jcomp.repaint();
                        }
                    }
                }
                
                if (step[0] >= totalSteps + components.length * 2) {
                    animTimer.stop();
                    // Reset to normal state
                    for (Component comp : components) {
                        if (comp instanceof JComponent) {
                            ((JComponent) comp).putClientProperty("alpha", null);
                        }
                    }
                    panel.revalidate();
                }
            });
            
            // Start animation after a short delay
            Timer startTimer = new Timer(200, e -> animTimer.start());
            startTimer.setRepeats(false);
            startTimer.start();
        }

        private JPanel createScanSection() {
            JPanel scanPanel = new JPanel(new BorderLayout(0, 15));
            scanPanel.setBackground(PANEL_BACKGROUND);
            scanPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Create the scan button with improved styling
            JButton scanButton = new JButton("Quick Scan") {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private Timer pulseTimer;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(0, 40));
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
                            startPulseAnimation(false);
                            repaint();
                        }
                        
                        @Override
                        public void mousePressed(MouseEvent e) {
                            isPressed = true;
                            repaint();
                        }
                        
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            isPressed = false;
                            repaint();
                        }
                    });
                }
                
                private void startPulseAnimation(boolean start) {
                    if (pulseTimer != null && pulseTimer.isRunning()) {
                        pulseTimer.stop();
                    }
                    
                    if (start) {
                        pulseTimer = new Timer(30, e -> {
                            pulseEffect += 0.1f;
                            repaint();
                        });
                        pulseTimer.start();
                    } else {
                        pulseEffect = 0.0f;
                    }
                }
                
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Get button dimensions
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    g2d.setColor(isPressed ? BUTTON_HOVER_COLOR : (isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw subtle accent on hover
                    if (isHovered) {
                        // Subtle color accent
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            isPressed ? 60 : 40
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            (int)(100 * borderAlpha)
                        ));
                        g2d.setStroke(new BasicStroke(1.5f));
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Draw text shadow
                    g2d.setColor(new Color(0, 0, 0, 100));
                    g2d.drawString(text, textX + 1, textY + 1);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                    
                    g2d.dispose();
                }
            };

            // Create output area with minimal styling
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setFont(STATUS_FONT);
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setBackground(OUTPUT_BACKGROUND);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(20, 20, 20), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            // Create scrollable output panel with improved styling
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setPreferredSize(new Dimension(0, 200));
            
            // Apply custom scrollbar UI with subtle accent
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(15, 15, 15);
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
                    button.setMinimumSize(new Dimension(0, 0));
                    button.setMaximumSize(new Dimension(0, 0));
                    return button;
                }
                
                @Override
                protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                    if (thumbBounds.isEmpty()) return;
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(thumbColor);
                    g2.fill(new RoundRectangle2D.Float(thumbBounds.x, thumbBounds.y,
                            thumbBounds.width, thumbBounds.height, 6, 6));
                    g2.dispose();
                }
            });

            scanPanel.add(scanButton, BorderLayout.NORTH);
            scanPanel.add(scrollPane, BorderLayout.CENTER);

            return scanPanel;
        }
    }



    public static class NetworkDiagnosticsPanel extends BaseSidebarPanel {

        public NetworkDiagnosticsPanel() {
            super("Network Diagnostics");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.add(this.createScanSection(), BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createScanSection() {
            JPanel scanPanel = new JPanel(new BorderLayout(0, 10));
            scanPanel.setBackground(PANEL_BACKGROUND);

            JButton scanButton = new JButton("Check");
            scanButton.setPreferredSize(new Dimension(200, 40));
            scanButton.setBackground(BUTTON_COLOR);
            scanButton.setForeground(Color.WHITE);
            scanButton.setFocusPainted(false);
            scanButton.setBorderPainted(false);
            scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Create a scrollable output area
            JTextArea outputArea = new JTextArea(10, 30); // Adjust rows and columns as needed
            outputArea.setEditable(false);
            outputArea.setBackground(PANEL_BACKGROUND);
            outputArea.setForeground(SECONDARY_TEXT_COLOR);
            outputArea.setWrapStyleWord(true);
            outputArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JLabel statusLabel = new JLabel("Ready to scan");
            statusLabel.setForeground(SECONDARY_TEXT_COLOR);
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

            scanButton.addActionListener((ActionEvent e) -> {
                scanButton.setText("Scanning...");
                statusLabel.setText("Scan in progress...");
                outputArea.setText(""); // Clear the output area before starting a new scan

                // Create a SwingWorker to run the batch file in a background thread
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            // Using a relative path to the .bat file
                            String batFilePath = "Bat_Scripts/Network.bat"; // Relative path to the .bat file

                            // Create the full path to the .bat file based on the current directory
                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            // Run the .bat file using cmd.exe
                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);  // Combine output and error streams
                            Process process = processBuilder.start();

                            // Get the output of the process and send it to the text area
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line); // Publish the real-time output to the process
                            }

                            process.waitFor(); // Wait for the batch file to finish
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete scan.");
                        }
                        return null;
                    }

                    // This method is called whenever a message is published in the background thread
                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n"); // Update the text area with real-time output
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Update status and button text when the scan is complete
                            scanButton.setText("Check");
                            statusLabel.setText("Scan Complete.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            statusLabel.setText("Error: Couldn't complete scan.");
                        }
                    }
                };

                // Start the worker thread
                worker.execute();
            });

            scanPanel.add(scanButton, BorderLayout.NORTH);
            scanPanel.add(scrollPane, BorderLayout.CENTER);
            scanPanel.add(statusLabel, BorderLayout.SOUTH);

            return scanPanel;
        }
    }


    class DriversIssuesPanel extends BaseSidebarPanel {
        public DriversIssuesPanel() {
            super("Driver Issues");
        }

        protected JPanel createContentPanel() {
            return Troubleshoot.this.createPlaceholderPanel("Manage your user accounts and security settings.");
        }
    }




    public static class FileSystemRepairPanel extends BaseSidebarPanel {

        public FileSystemRepairPanel (){
            super("File System Repair");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.add(this.createScanSection(), BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createScanSection() {
            JPanel scanPanel = new JPanel(new BorderLayout(0, 10));
            scanPanel.setBackground(PANEL_BACKGROUND);

            JButton scanButton = new JButton("Check");
            scanButton.setPreferredSize(new Dimension(200, 40));
            scanButton.setBackground(BUTTON_COLOR);
            scanButton.setForeground(Color.WHITE);
            scanButton.setFocusPainted(false);
            scanButton.setBorderPainted(false);
            scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Create a scrollable output area
            JTextArea outputArea = new JTextArea(10, 30); // Adjust rows and columns as needed
            outputArea.setEditable(false);
            outputArea.setBackground(PANEL_BACKGROUND);
            outputArea.setForeground(SECONDARY_TEXT_COLOR);
            outputArea.setWrapStyleWord(true);
            outputArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JLabel statusLabel = new JLabel("Ready to scan");
            statusLabel.setForeground(SECONDARY_TEXT_COLOR);
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

            scanButton.addActionListener((ActionEvent e) -> {
                scanButton.setText("Scanning...");
                statusLabel.setText("Scan in progress...");
                outputArea.setText(""); // Clear the output area before starting a new scan

                // Create a SwingWorker to run the batch file in a background thread
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            // Using a relative path to the .bat file
                            String batFilePath = "Bat_Scripts/FileSystem.bat"; // Relative path to the .bat file

                            // Create the full path to the .bat file based on the current directory
                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            // Run the .bat file using cmd.exe
                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);  // Combine output and error streams
                            Process process = processBuilder.start();

                            // Get the output of the process and send it to the text area
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line); // Publish the real-time output to the process
                            }

                            process.waitFor(); // Wait for the batch file to finish
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete scan.");
                        }
                        return null;
                    }

                    // This method is called whenever a message is published in the background thread
                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n"); // Update the text area with real-time output
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Update status and button text when the scan is complete
                            scanButton.setText("Check");
                            statusLabel.setText("Scan Complete.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            statusLabel.setText("Error: Couldn't complete scan.");
                        }
                    }
                };

                // Start the worker thread
                worker.execute();
            });

            scanPanel.add(scanButton, BorderLayout.NORTH);
            scanPanel.add(scrollPane, BorderLayout.CENTER);
            scanPanel.add(statusLabel, BorderLayout.SOUTH);

            return scanPanel;
        }
    }




    public static class BlueScreenErrorPanel extends BaseSidebarPanel {

        public BlueScreenErrorPanel (){
            super("Blue Screen Error");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.add(this.createScanSection(), BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createScanSection() {
            JPanel scanPanel = new JPanel(new BorderLayout(0, 10));
            scanPanel.setBackground(PANEL_BACKGROUND);

            JButton scanButton = new JButton("Check");
            scanButton.setPreferredSize(new Dimension(200, 40));
            scanButton.setBackground(BUTTON_COLOR);
            scanButton.setForeground(Color.WHITE);
            scanButton.setFocusPainted(false);
            scanButton.setBorderPainted(false);
            scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Create a scrollable output area
            JTextArea outputArea = new JTextArea(10, 30); // Adjust rows and columns as needed
            outputArea.setEditable(false);
            outputArea.setBackground(PANEL_BACKGROUND);
            outputArea.setForeground(SECONDARY_TEXT_COLOR);
            outputArea.setWrapStyleWord(true);
            outputArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JLabel statusLabel = new JLabel("Ready to scan");
            statusLabel.setForeground(SECONDARY_TEXT_COLOR);
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

            scanButton.addActionListener((ActionEvent e) -> {
                scanButton.setText("Scanning...");
                statusLabel.setText("Scan in progress...");
                outputArea.setText(""); // Clear the output area before starting a new scan

                // Create a SwingWorker to run the batch file in a background thread
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            // Using a relative path to the .bat file
                            String batFilePath = "Bat_Scripts/BSOD.bat"; // Relative path to the .bat file

                            // Create the full path to the .bat file based on the current directory
                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            // Run the .bat file using cmd.exe
                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);  // Combine output and error streams
                            Process process = processBuilder.start();

                            // Get the output of the process and send it to the text area
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line); // Publish the real-time output to the process
                            }

                            process.waitFor(); // Wait for the batch file to finish
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete scan.");
                        }
                        return null;
                    }

                    // This method is called whenever a message is published in the background thread
                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n"); // Update the text area with real-time output
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Update status and button text when the scan is complete
                            scanButton.setText("Check");
                            statusLabel.setText("Scan Complete.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            statusLabel.setText("Error: Couldn't complete scan.");
                        }
                    }
                };

                // Start the worker thread
                worker.execute();
            });

            scanPanel.add(scanButton, BorderLayout.NORTH);
            scanPanel.add(scrollPane, BorderLayout.CENTER);
            scanPanel.add(statusLabel, BorderLayout.SOUTH);

            return scanPanel;
        }
    }


    class ApplicationCompatibilityIssuesPanel extends BaseSidebarPanel {
        public ApplicationCompatibilityIssuesPanel() {
            super("Application Compatibility Issues");
        }

        protected JPanel createContentPanel() {
            return Troubleshoot.this.createPlaceholderPanel("Monitor the performance and health of your device.");
        }
    }




    public static class AudioTroubleshootingPanel extends BaseSidebarPanel {

        public AudioTroubleshootingPanel (){
            super("Audio & Sound");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.add(this.createScanSection(), BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createScanSection() {
            JPanel scanPanel = new JPanel(new BorderLayout(0, 10));
            scanPanel.setBackground(PANEL_BACKGROUND);

            JButton scanButton = new JButton("Check");
            scanButton.setPreferredSize(new Dimension(200, 40));
            scanButton.setBackground(BUTTON_COLOR);
            scanButton.setForeground(Color.WHITE);
            scanButton.setFocusPainted(false);
            scanButton.setBorderPainted(false);
            scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Create a scrollable output area
            JTextArea outputArea = new JTextArea(10, 30); // Adjust rows and columns as needed
            outputArea.setEditable(false);
            outputArea.setBackground(PANEL_BACKGROUND);
            outputArea.setForeground(SECONDARY_TEXT_COLOR);
            outputArea.setWrapStyleWord(true);
            outputArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JLabel statusLabel = new JLabel("Ready to scan");
            statusLabel.setForeground(SECONDARY_TEXT_COLOR);
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

            scanButton.addActionListener((ActionEvent e) -> {
                scanButton.setText("Scanning...");
                statusLabel.setText("Scan in progress...");
                outputArea.setText(""); // Clear the output area before starting a new scan

                // Create a SwingWorker to run the batch file in a background thread
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            // Using a relative path to the .bat file
                            String batFilePath = "Bat_Scripts/Audio.bat"; // Relative path to the .bat file

                            // Create the full path to the .bat file based on the current directory
                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            // Run the .bat file using cmd.exe
                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);  // Combine output and error streams
                            Process process = processBuilder.start();

                            // Get the output of the process and send it to the text area
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line); // Publish the real-time output to the process
                            }

                            process.waitFor(); // Wait for the batch file to finish
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete scan.");
                        }
                        return null;
                    }

                    // This method is called whenever a message is published in the background thread
                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n"); // Update the text area with real-time output
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Update status and button text when the scan is complete
                            scanButton.setText("Check");
                            statusLabel.setText("Scan Complete.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            statusLabel.setText("Error: Couldn't complete scan.");
                        }
                    }
                };

                // Start the worker thread
                worker.execute();
            });

            scanPanel.add(scanButton, BorderLayout.NORTH);
            scanPanel.add(scrollPane, BorderLayout.CENTER);
            scanPanel.add(statusLabel, BorderLayout.SOUTH);

            return scanPanel;
        }
    }




    public static class BluetoothAndDeviceConnectivityPanel extends BaseSidebarPanel {

        public BluetoothAndDeviceConnectivityPanel (){
            super("Bluetooth and Device Connectivity");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.add(this.createScanSection(), BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createScanSection() {
            JPanel scanPanel = new JPanel(new BorderLayout(0, 10));
            scanPanel.setBackground(PANEL_BACKGROUND);

            JButton scanButton = new JButton("Check");
            scanButton.setPreferredSize(new Dimension(200, 40));
            scanButton.setBackground(BUTTON_COLOR);
            scanButton.setForeground(Color.WHITE);
            scanButton.setFocusPainted(false);
            scanButton.setBorderPainted(false);
            scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Create a scrollable output area
            JTextArea outputArea = new JTextArea(10, 30); // Adjust rows and columns as needed
            outputArea.setEditable(false);
            outputArea.setBackground(PANEL_BACKGROUND);
            outputArea.setForeground(SECONDARY_TEXT_COLOR);
            outputArea.setWrapStyleWord(true);
            outputArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JLabel statusLabel = new JLabel("Ready to scan");
            statusLabel.setForeground(SECONDARY_TEXT_COLOR);
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

            scanButton.addActionListener((ActionEvent e) -> {
                scanButton.setText("Scanning...");
                statusLabel.setText("Scan in progress...");
                outputArea.setText(""); // Clear the output area before starting a new scan

                // Create a SwingWorker to run the batch file in a background thread
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            // Using a relative path to the .bat file
                            String batFilePath = "Bat_Scripts/Bluetooth.bat"; // Relative path to the .bat file

                            // Create the full path to the .bat file based on the current directory
                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            // Run the .bat file using cmd.exe
                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);  // Combine output and error streams
                            Process process = processBuilder.start();

                            // Get the output of the process and send it to the text area
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line); // Publish the real-time output to the process
                            }

                            process.waitFor(); // Wait for the batch file to finish
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete scan.");
                        }
                        return null;
                    }

                    // This method is called whenever a message is published in the background thread
                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n"); // Update the text area with real-time output
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Update status and button text when the scan is complete
                            scanButton.setText("Check");
                            statusLabel.setText("Scan Complete.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            statusLabel.setText("Error: Couldn't complete scan.");
                        }
                    }
                };

                // Start the worker thread
                worker.execute();
            });

            scanPanel.add(scanButton, BorderLayout.NORTH);
            scanPanel.add(scrollPane, BorderLayout.CENTER);
            scanPanel.add(statusLabel, BorderLayout.SOUTH);

            return scanPanel;
        }
    }




    public static class BrowserIssuesPanel extends BaseSidebarPanel {

        public BrowserIssuesPanel (){
            super("Browser");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.add(this.createScanSection(), BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createScanSection() {
            JPanel scanPanel = new JPanel(new BorderLayout(0, 10));
            scanPanel.setBackground(PANEL_BACKGROUND);

            JButton scanButton = new JButton("Check");
            scanButton.setPreferredSize(new Dimension(200, 40));
            scanButton.setBackground(BUTTON_COLOR);
            scanButton.setForeground(Color.WHITE);
            scanButton.setFocusPainted(false);
            scanButton.setBorderPainted(false);
            scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Create a scrollable output area
            JTextArea outputArea = new JTextArea(10, 30); // Adjust rows and columns as needed
            outputArea.setEditable(false);
            outputArea.setBackground(PANEL_BACKGROUND);
            outputArea.setForeground(SECONDARY_TEXT_COLOR);
            outputArea.setWrapStyleWord(true);
            outputArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JLabel statusLabel = new JLabel("Ready to scan");
            statusLabel.setForeground(SECONDARY_TEXT_COLOR);
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

            scanButton.addActionListener((ActionEvent e) -> {
                scanButton.setText("Scanning...");
                statusLabel.setText("Scan in progress...");
                outputArea.setText(""); // Clear the output area before starting a new scan

                // Create a SwingWorker to run the batch file in a background thread
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            // Using a relative path to the .bat file
                            String batFilePath = "Bat_Scripts/Browser.bat"; // Relative path to the .bat file

                            // Create the full path to the .bat file based on the current directory
                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            // Run the .bat file using cmd.exe
                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);  // Combine output and error streams
                            Process process = processBuilder.start();

                            // Get the output of the process and send it to the text area
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line); // Publish the real-time output to the process
                            }

                            process.waitFor(); // Wait for the batch file to finish
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete scan.");
                        }
                        return null;
                    }

                    // This method is called whenever a message is published in the background thread
                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n"); // Update the text area with real-time output
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Update status and button text when the scan is complete
                            scanButton.setText("Check");
                            statusLabel.setText("Scan Complete.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            statusLabel.setText("Error: Couldn't complete scan.");
                        }
                    }
                };

                // Start the worker thread
                worker.execute();
            });

            scanPanel.add(scanButton, BorderLayout.NORTH);
            scanPanel.add(scrollPane, BorderLayout.CENTER);
            scanPanel.add(statusLabel, BorderLayout.SOUTH);

            return scanPanel;
        }
    }




    abstract static class BaseSidebarPanel extends JPanel {
        protected static final Color PANEL_BACKGROUND = new Color(10, 10, 10);
        protected static final Color TEXT_COLOR = new Color(255, 255, 255);
        protected static final Color SECONDARY_TEXT_COLOR = new Color(170, 170, 170);
        protected static final Color BUTTON_COLOR = new Color(30, 30, 30);
        protected static final Color BUTTON_HOVER_COLOR = new Color(40, 40, 40);
        
        // Animation properties
        private float fadeInAlpha = 0.0f;
        private Timer fadeInTimer;
        private boolean animationComplete = false;
        private JPanel contentContainer;
        private final String title;
        private final Color accentColor;
        private float slideOffset = 50.0f;

        public BaseSidebarPanel(String title) {
            this.title = title;
            
            // Determine which panel this is to get the right accent color
            int panelIndex = 0;
            if (title.contains("Network")) panelIndex = 1;
            else if (title.contains("Driver")) panelIndex = 2;
            else if (title.contains("File System")) panelIndex = 3;
            else if (title.contains("Blue Screen")) panelIndex = 4;
            else if (title.contains("App Compatibility")) panelIndex = 5;
            else if (title.contains("Audio")) panelIndex = 6;
            else if (title.contains("Bluetooth")) panelIndex = 7;
            else if (title.contains("Browser")) panelIndex = 8;
            
            this.accentColor = CATEGORY_COLORS[panelIndex];
            
            setLayout(new BorderLayout(0, 20));
            setBackground(PANEL_BACKGROUND);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            add(createHeader(title), BorderLayout.NORTH);
            
            // Create a container for content that will be animated
            contentContainer = new JPanel(new BorderLayout());
            contentContainer.setOpaque(false);
            add(contentContainer, BorderLayout.CENTER);
            
            // Add fade-in animation when panel is shown
            addHierarchyListener(e -> {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (isShowing() && !animationComplete) {
                        startFadeInAnimation();
                    }
                }
            });
        }
        
        private void startFadeInAnimation() {
            if (fadeInTimer != null && fadeInTimer.isRunning()) {
                fadeInTimer.stop();
            }
            
            // Set initial opacity
            setOpaque(false);
            fadeInAlpha = 0.0f;
            
            // Create panel content with initial transparency
            JPanel content = createContentPanel();
            content.setOpaque(false);
            
            // Add content to the container instead of directly to this panel
            contentContainer.removeAll();
            contentContainer.add(content, BorderLayout.CENTER);
            
            // Start fade-in animation
            fadeInTimer = new Timer(16, e -> {
                fadeInAlpha = Math.min(1.0f, fadeInAlpha + 0.05f);
                slideOffset = Math.max(0.0f, slideOffset - 2.5f);
                
                if (fadeInAlpha >= 1.0f && slideOffset <= 0) {
                    fadeInTimer.stop();
                    animationComplete = true;
                    setOpaque(true);
                    content.setOpaque(true);
                    revalidate();
                }
                repaint();
            });
            fadeInTimer.start();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Simple black background
            g2d.setColor(PANEL_BACKGROUND);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Apply fade-in effect if animation is in progress
            if (!animationComplete) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeInAlpha));
                g2d.translate(slideOffset, 0);
            }
            
            g2d.dispose();
        }

        protected JPanel createHeader(String title) {
            JPanel headerPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw header background
                    g2d.setColor(new Color(5, 5, 5));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Draw subtle accent line at bottom
                    g2d.setColor(new Color(
                        accentColor.getRed(),
                        accentColor.getGreen(),
                        accentColor.getBlue(),
                        100
                    ));
                    g2d.fillRect(0, getHeight() - 1, getWidth(), 1);
                    
                    g2d.dispose();
                }
            };
            headerPanel.setLayout(new BorderLayout());
            headerPanel.setOpaque(false);
            headerPanel.setPreferredSize(new Dimension(0, 45));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            // Create title with subtle color
            JLabel titleLabel = new JLabel(title) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    
                    // Draw text shadow
                    g2d.setFont(getFont());
                    g2d.setColor(new Color(0, 0, 0, 100));
                    g2d.drawString(getText(), 1, getHeight() - 3);
                    
                    // Draw text with subtle gradient
                    GradientPaint textGradient = new GradientPaint(
                        0, 0, TEXT_COLOR,
                        getWidth(), 0, new Color(
                            accentColor.getRed(),
                            accentColor.getGreen(),
                            accentColor.getBlue(),
                            200
                        )
                    );
                    g2d.setPaint(textGradient);
                    g2d.drawString(getText(), 0, getHeight() - 4);
                    
                    g2d.dispose();
                }
            };
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            titleLabel.setForeground(TEXT_COLOR);
            
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            return headerPanel;
        }

        protected abstract JPanel createContentPanel();
    }
}
