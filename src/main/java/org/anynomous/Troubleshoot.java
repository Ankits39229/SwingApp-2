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
    private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
    private static final Color PANEL_BACKGROUND = new Color(30, 30, 30);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color SECONDARY_TEXT_COLOR = new Color(170, 170, 170);
    private static final Color HOVER_COLOR = new Color(45, 45, 45);
    private static final Color GRADIENT_START = new Color(25, 25, 25);
    private static final Color GRADIENT_END = new Color(35, 35, 35);
    private static final Color ACCENT_COLOR = new Color(56, 132, 255);
    private static final Color ACCENT_HOVER_COLOR = new Color(80, 160, 255);
    private static final Color CARD_SHADOW_COLOR = new Color(10, 10, 10, 100);
    private static final Color SIDEBAR_GRADIENT_START = new Color(28, 28, 32);
    private static final Color SIDEBAR_GRADIENT_END = new Color(35, 35, 40);
    private static final Color SIDEBAR_ACCENT = new Color(70, 130, 230);
    private static final Color SIDEBAR_HIGHLIGHT = new Color(255, 255, 255, 30);
    private static final Color[] CATEGORY_COLORS = {
        new Color(56, 132, 255),    // System Performance - Blue
        new Color(92, 184, 92),     // Network Diagnostics - Green
        new Color(240, 173, 78),    // Driver Issues - Orange
        new Color(217, 83, 79),     // File System Repair - Red
        new Color(156, 39, 176),    // Blue Screen Errors - Purple
        new Color(0, 188, 212),     // App Compatibility - Cyan
        new Color(255, 64, 129),    // Audio Troubleshooting - Pink
        new Color(63, 81, 181),     // Bluetooth & Devices - Indigo
        new Color(255, 152, 0)      // Browser Issues - Amber
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
        titleLabel.setFont(new Font("Segue UI", Font.BOLD, 24));
        JLabel subtitleLabel = new JLabel("<html></html>");
        subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
        subtitleLabel.setFont(new Font("Segue UI", Font.PLAIN, 14));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);
        return headerPanel;
    }

    private JPanel createMainGridPanel() {
        // Use a more responsive layout with better spacing
        JPanel mainPanel = new JPanel(new GridLayout(3, 3, 18, 18));
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
        final Color categoryColorDarker = darkenColor(categoryColor, 0.8f);
        final Color categoryColorLighter = lightenColor(categoryColor, 0.2f);
        
        JPanel panel = new JPanel() {
            private float alpha = 0.0f;
            private Timer fadeTimer;
            private boolean isHovered = false;
            private boolean isPressed = false;
            private final int shadowSize = 8;
            
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
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        isPressed = false;
                        startFadeAnimation(false);
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
                        alpha = Math.min(1.0f, alpha + 0.08f);
            } else {
                        alpha = Math.max(0.0f, alpha - 0.08f);
                    }
                    
                    if ((fadeIn && alpha >= 1.0f) || (!fadeIn && alpha <= 0.0f)) {
                        fadeTimer.stop();
                    }
                    repaint();
                });
                fadeTimer.start();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw shadow (when not pressed)
                if (!isPressed) {
                    for (int i = 0; i < shadowSize; i++) {
                        float shadowAlpha = 0.2f * (1 - (float)i / shadowSize);
                        if (isHovered) {
                            shadowAlpha *= 1.5f;
                        }
                        g2d.setColor(new Color(0, 0, 0, (int)(shadowAlpha * 255)));
                        g2d.fill(new RoundRectangle2D.Float(
                            shadowSize - i, shadowSize - i, 
                            width - (shadowSize - i) * 2, 
                            height - (shadowSize - i) * 2, 
                            12, 12
                        ));
                    }
                }
                
                // Create gradient background with category color influence
                Point2D start = new Point2D.Float(0, 0);
                Point2D end = new Point2D.Float(0, height);
                
                // Base gradient colors
                Color gradStart = GRADIENT_START;
                Color gradEnd = GRADIENT_END;
                
                // Add category color influence
                if (isHovered || isPressed) {
                    // More color when hovered/pressed
                    gradStart = blendColors(gradStart, categoryColor, isPressed ? 0.15f : 0.1f);
                    gradEnd = blendColors(gradEnd, categoryColor, isPressed ? 0.2f : 0.15f);
                } else {
                    // Subtle color influence when not hovered
                    gradStart = blendColors(gradStart, categoryColor, 0.05f);
                    gradEnd = blendColors(gradEnd, categoryColor, 0.08f);
                }
                
                // Apply press effect
                if (isPressed) {
                    gradStart = darkenColor(gradStart, 0.9f);
                    gradEnd = darkenColor(gradEnd, 0.9f);
                    g2d.translate(1, 1);
                }
                
                LinearGradientPaint gradientPaint = new LinearGradientPaint(
                    start, end,
                    new float[]{0.0f, 1.0f},
                    new Color[]{gradStart, gradEnd}
                );
                
                // Draw panel background
                g2d.setPaint(gradientPaint);
                g2d.fill(new RoundRectangle2D.Float(0, 0, width - (isPressed ? 2 : 0), height - (isPressed ? 2 : 0), 12, 12));
                
                // Draw category color accent on top
                g2d.setColor(categoryColor);
                g2d.fillRect(0, 0, width, 3);
                
                // Draw hover/active effects
                if (alpha > 0.0f) {
                    // Accent border with category color
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.8f));
                    g2d.setColor(categoryColor);
                    g2d.setStroke(new BasicStroke(2f));
                    g2d.draw(new RoundRectangle2D.Float(1, 1, width - 3, height - 3, 12, 12));
                    
                    // Subtle inner glow with category color
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.15f));
                    g2d.setColor(categoryColorLighter);
                    g2d.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, 10, 10));
                    
                    // Top highlight
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.4f));
                    Point2D highlightStart = new Point2D.Float(width/2, 0);
                    Point2D highlightEnd = new Point2D.Float(width/2, height/3);
                    LinearGradientPaint highlightPaint = new LinearGradientPaint(
                        highlightStart, highlightEnd,
                        new float[]{0.0f, 1.0f},
                        new Color[]{new Color(255, 255, 255, 60), new Color(255, 255, 255, 0)}
                    );
                    g2d.setPaint(highlightPaint);
                    g2d.fill(new RoundRectangle2D.Float(2, 2, width - 4, height/3, 10, 10));
                }
                
                g2d.dispose();
            }
        };

        // Create icon panel with category color background
        JPanel iconPanel = new JPanel() {
            private float pulseScale = 1.0f;
            private Timer pulseTimer;
            private boolean pulsing = false;
            
            {
                setOpaque(false);
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
                
                // Draw circle background with category color
                int circleSize = 48;
                int adjustedSize = pulsing ? (int)(circleSize * pulseScale) : circleSize;
                
                // Draw gradient circle background
                RadialGradientPaint circlePaint = new RadialGradientPaint(
                    centerX, centerY, adjustedSize/2,
                    new float[]{0.0f, 0.7f, 1.0f},
                    new Color[]{
                        categoryColor,
                        categoryColorDarker,
                        new Color(categoryColorDarker.getRed(), categoryColorDarker.getGreen(), categoryColorDarker.getBlue(), 100)
                    }
                );
                g2d.setPaint(circlePaint);
                g2d.fillOval(centerX - adjustedSize/2, centerY - adjustedSize/2, adjustedSize, adjustedSize);
                
                // Draw the emoji with slight scaling if pulsing
                String emoji = getEmojiForIndex(index);
                float fontSize = pulsing ? 32 * pulseScale : 32;
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, (int)fontSize));
                FontMetrics fm = g2d.getFontMetrics();
                int emojiWidth = fm.stringWidth(emoji);
                int emojiHeight = fm.getHeight();
                g2d.setColor(Color.WHITE);
                g2d.drawString(emoji, centerX - emojiWidth/2, centerY + emojiHeight/4);
                
                g2d.dispose();
            }
        };

        // Create title label with category color
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(categoryColor);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
        // Modern color scheme
        private static final Color PANEL_BACKGROUND = new Color(30, 30, 30);
        private static final Color BUTTON_COLOR = new Color(56, 132, 255);
        private static final Color BUTTON_HOVER_COLOR = new Color(25, 113, 255);
        private static final Color SECONDARY_TEXT_COLOR = new Color(107, 119, 140);
        private static final Color OUTPUT_BACKGROUND = new Color(30, 30, 30);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 13);
        private static final int CORNER_RADIUS = 8;

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

            // Create the scan button with enhanced styling
            JButton scanButton = new JButton("Quick Scan") {
                private boolean isHovered = false;
                private boolean isPressed = false;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(0, 45)); // Taller button
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
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
                
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Get button dimensions
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Create gradient based on state
                    Color startColor = BUTTON_COLOR;
                    Color endColor = BUTTON_HOVER_COLOR;
                    
                    if (isPressed) {
                        startColor = BUTTON_HOVER_COLOR;
                        endColor = BUTTON_COLOR;
                    } else if (isHovered) {
                        startColor = new Color(
                            Math.min(255, startColor.getRed() + 20),
                            Math.min(255, startColor.getGreen() + 20),
                            Math.min(255, startColor.getBlue() + 20)
                        );
                    }
                    
                    // Apply transform for press effect
                    if (isPressed) {
                        g2d.translate(0, 1);
                    }
                    
                    // Draw button background
                    Point2D start = new Point2D.Float(0, 0);
                    Point2D end = new Point2D.Float(0, height);
                    LinearGradientPaint gradientPaint = new LinearGradientPaint(
                        start, end,
                        new float[]{0.0f, 1.0f},
                        new Color[]{startColor, endColor}
                    );
                    
                    g2d.setPaint(gradientPaint);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Add highlight on top edge
                    if (!isPressed) {
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(CORNER_RADIUS/2, 1, width - CORNER_RADIUS, 2);
                    }
                    
                    // Draw text with shadow
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

            // Create output area with improved styling
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setFont(STATUS_FONT);
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setBackground(OUTPUT_BACKGROUND);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            // Create scrollable output panel
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setPreferredSize(new Dimension(0, 200));
            
            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(80, 80, 80);
                    this.trackColor = new Color(45, 45, 45);
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
            });

            // Add action to scan button
            scanButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    outputArea.setText("Starting system performance scan...\n");
                    scanButton.setEnabled(false);
                    
                    // Create animated progress indicator
                    JPanel progressPanel = new JPanel(new BorderLayout());
                    progressPanel.setOpaque(false);
                    
                    JProgressBar progressBar = new JProgressBar(0, 100);
                    progressBar.setIndeterminate(true);
                    progressBar.setStringPainted(true);
                    progressBar.setString("Scanning...");
                    progressBar.setForeground(BUTTON_COLOR);
                    progressBar.setBackground(new Color(45, 45, 45));
                    progressBar.setBorder(BorderFactory.createEmptyBorder());
                    
                    progressPanel.add(progressBar, BorderLayout.CENTER);
                    scanPanel.add(progressPanel, BorderLayout.SOUTH);
                    scanPanel.revalidate();
                    
                    // Simulate scan with SwingWorker
                    new SwingWorker<Void, String>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            // Simulate scanning steps
                            publish("Checking CPU usage...");
                            Thread.sleep(800);
                            publish("CPU usage: 23%");
                            Thread.sleep(500);
                            
                            publish("Checking memory usage...");
                            Thread.sleep(800);
                            publish("Memory usage: 4.2GB / 16GB (26%)");
                            Thread.sleep(500);
                            
                            publish("Checking disk performance...");
                            Thread.sleep(1000);
                            publish("Disk read speed: 520MB/s");
                            publish("Disk write speed: 480MB/s");
                            Thread.sleep(500);
                            
                            publish("Checking for high resource processes...");
                            Thread.sleep(1200);
                            publish("Found 2 processes with high resource usage:");
                            publish("- chrome.exe (CPU: 15%, Memory: 1.2GB)");
                            publish("- explorer.exe (CPU: 5%, Memory: 300MB)");
                            Thread.sleep(500);
                            
                            publish("Checking startup impact...");
                            Thread.sleep(1000);
                            publish("3 high-impact startup items found");
                            Thread.sleep(500);
                            
                            publish("Checking system temperature...");
                            Thread.sleep(800);
                            publish("CPU temperature: 65°C (Normal)");
                            Thread.sleep(500);
                            
                            publish("Scan complete!");
                            return null;
                        }
                        
                        @Override
                        protected void process(java.util.List<String> chunks) {
                            for (String chunk : chunks) {
                                outputArea.append(chunk + "\n");
                                outputArea.setCaretPosition(outputArea.getDocument().getLength());
                            }
                        }
                        
                        @Override
                        protected void done() {
                                scanButton.setEnabled(true);
                            progressBar.setIndeterminate(false);
                            progressBar.setValue(100);
                            progressBar.setString("Complete");
                            
                            // Add summary panel with recommendations
                            JPanel summaryPanel = new JPanel();
                            summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
                            summaryPanel.setBackground(new Color(40, 40, 45));
                            summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(BUTTON_COLOR, 1),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)
                            ));
                            
                            JLabel summaryLabel = new JLabel("System Performance Summary");
                            summaryLabel.setForeground(Color.WHITE);
                            summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                            summaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                            
                            JLabel recommendationsLabel = new JLabel("<html>Recommendations:<br>" +
                                "• Consider closing Chrome to free up memory<br>" +
                                "• Optimize startup items to improve boot time<br>" +
                                "• System performance is generally good</html>");
                            recommendationsLabel.setForeground(SECONDARY_TEXT_COLOR);
                            recommendationsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                            recommendationsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                            
                            summaryPanel.add(summaryLabel);
                            summaryPanel.add(Box.createVerticalStrut(10));
                            summaryPanel.add(recommendationsLabel);
                            
                            // Animate the summary panel appearance
                            summaryPanel.setVisible(false);
                            scanPanel.add(summaryPanel, BorderLayout.NORTH);
                            
                            Timer summaryTimer = new Timer(500, evt -> {
                                summaryPanel.setVisible(true);
                                scanPanel.revalidate();
                                
                                // Slide-in animation
                                int targetHeight = summaryPanel.getPreferredSize().height;
                                summaryPanel.setSize(summaryPanel.getWidth(), 0);
                                
                                Timer slideTimer = new Timer(20, e -> {
                                    int currentHeight = summaryPanel.getHeight();
                                    int newHeight = Math.min(targetHeight, currentHeight + 5);
                                    summaryPanel.setSize(summaryPanel.getWidth(), newHeight);
                                    
                                    if (newHeight >= targetHeight) {
                                        ((Timer)e.getSource()).stop();
                                    }
                                });
                                slideTimer.start();
                            });
                            summaryTimer.setRepeats(false);
                            summaryTimer.start();
                        }
                    }.execute();
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
        protected static final Color PANEL_BACKGROUND = new Color(30, 30, 30);
        protected static final Color TEXT_COLOR = new Color(255, 255, 255);
        protected static final Color SECONDARY_TEXT_COLOR = new Color(170, 170, 170);
        protected static final Color BUTTON_COLOR = new Color(238, 10, 10);
        
        // Animation properties
        private float fadeInAlpha = 0.0f;
        private Timer fadeInTimer;
        private boolean animationComplete = false;
        private JPanel contentContainer;
        private final String title;
        private final Color accentColor;

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
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            
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
            fadeInTimer = new Timer(20, e -> {
                fadeInAlpha = Math.min(1.0f, fadeInAlpha + 0.05f);
                
                if (fadeInAlpha >= 1.0f) {
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
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Create gradient background with accent color influence
            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(0, getHeight());
            
            // Blend the sidebar gradient with the accent color
            Color gradStart = blendColors(SIDEBAR_GRADIENT_START, accentColor, 0.05f);
            Color gradEnd = blendColors(SIDEBAR_GRADIENT_END, accentColor, 0.08f);
            
            LinearGradientPaint gradientPaint = new LinearGradientPaint(
                start, end,
                new float[]{0.0f, 1.0f},
                new Color[]{gradStart, gradEnd}
            );
            
            // Apply fade-in effect if animation is in progress
            if (!animationComplete) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeInAlpha));
            }
            
            g2d.setPaint(gradientPaint);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
            
            // Add accent color line at top
            g2d.setColor(accentColor);
            g2d.fillRect(0, 0, getWidth(), 3);
            
            // Add subtle top highlight
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
            Point2D highlightStart = new Point2D.Float(getWidth()/2, 0);
            Point2D highlightEnd = new Point2D.Float(getWidth()/2, getHeight()/4);
            LinearGradientPaint highlightPaint = new LinearGradientPaint(
                highlightStart, highlightEnd,
                new float[]{0.0f, 1.0f},
                new Color[]{SIDEBAR_HIGHLIGHT, new Color(255, 255, 255, 0)}
            );
            g2d.setPaint(highlightPaint);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight()/4, 12, 12));
            
            g2d.dispose();
        }

        protected JPanel createHeader(String title) {
            JPanel headerPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Create header background with accent color influence
                    Point2D start = new Point2D.Float(0, 0);
                    Point2D end = new Point2D.Float(0, getHeight());
                    
                    // Blend header colors with accent color
                    Color headerStart = blendColors(new Color(40, 40, 45), accentColor, 0.1f);
                    Color headerEnd = blendColors(new Color(35, 35, 40), accentColor, 0.15f);
                    
                    LinearGradientPaint gradientPaint = new LinearGradientPaint(
                        start, end,
                        new float[]{0.0f, 1.0f},
                        new Color[]{headerStart, headerEnd}
                    );
                    
                    g2d.setPaint(gradientPaint);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                    
                    // Add accent line at bottom
                    g2d.setColor(accentColor);
                    g2d.fillRect(0, getHeight() - 2, getWidth(), 2);
                    
                    g2d.dispose();
                }
            };
            
            headerPanel.setLayout(new BorderLayout());
            headerPanel.setOpaque(false);
            headerPanel.setPreferredSize(new Dimension(0, 50));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            // Create title with enhanced styling
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(accentColor);
            
            // Add subtle shadow to text
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            titleLabel.putClientProperty(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            return headerPanel;
        }

        protected abstract JPanel createContentPanel();
    }
}
