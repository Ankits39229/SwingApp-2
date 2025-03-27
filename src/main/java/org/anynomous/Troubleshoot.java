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
import main.java.org.anynomous.utils.IconLoader;


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
    private final JPanel mainPanel;

    public Troubleshoot(JPanel... sidePanels) {
        this.SystemPerformancePanel = new SystemPerformancePanel();
        this.NetworkDiagnosticsPanel = new NetworkDiagnosticsPanel();
        this.DriversIssuesPanel = new DriversIssuesPanel();
        this.FileSystemRepairPanel = new FileSystemRepairPanel();
        this.BlueScreenErrorPanel = new BlueScreenErrorPanel();
        this.ApplicationCompatibilityIssuesPanel = new ApplicationCompatibilityIssuesPanel();
        this.AudioTroubleshootingPanel = new AudioTroubleshootingPanel();
        this.BluetoothAndDeviceConnectivityPanel = new BluetoothAndDeviceConnectivityPanel();
        this.BrowserIssuesPanel = new BrowserIssuesPanel();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel headerPanel = this.createHeaderPanel();
        mainPanel.add(headerPanel, "North");
        JPanel gridPanel = this.createMainGridPanel();
        mainPanel.add(gridPanel, "Center");
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
        final float[] pulseEffect = {0.0f};
        
        JPanel panel = new JPanel() {
            private float hoverAlpha = 0.0f;
            private Timer fadeTimer;
            private boolean isHovered = false;
            private boolean isPressed = false;
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
                        startFadeAnimation();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        startFadeAnimation();
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
                        openFullSizeWindow(index);
                    }
                });
                
                // Add hover effect animation
                fadeTimer = new Timer(16, e -> {
                    float targetAlpha = isHovered ? 1.0f : 0.0f;
                    float alphaStep = 0.1f;
                    
                    if (Math.abs(hoverAlpha - targetAlpha) < alphaStep) {
                        hoverAlpha = targetAlpha;
                        fadeTimer.stop();
                    } else {
                        hoverAlpha += (targetAlpha - hoverAlpha) * alphaStep;
                    }
                    
                    repaint();
                });
                
                // Add pulse animation timer
                pulseTimer = new Timer(50, e -> {
                    pulseEffect[0] += 0.1f;
                    if (pulseEffect[0] > 2 * Math.PI) {
                        pulseEffect[0] = 0;
                    }
                    repaint();
                });
            }
            
            private void startFadeAnimation() {
                if (!fadeTimer.isRunning()) {
                    fadeTimer.start();
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
                    float borderAlpha = hoverAlpha * (0.6f + 0.4f * (float)Math.sin(pulseEffect[0]));
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
                // Decrease the panel size for smaller icons
                setPreferredSize(new Dimension(35, 35));
                
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
                if (!pulsing) {
                    pulsing = true;
                    pulseTimer = new Timer(50, e -> {
                        pulseScale = 1.0f + 0.1f * (float)Math.sin(pulseEffect[0]);
                        repaint();
                    });
                    pulseTimer.start();
                }
            }
            
            private void stopPulseAnimation() {
                if (pulsing) {
                    pulsing = false;
                    if (pulseTimer != null) {
                        pulseTimer.stop();
                    }
                    pulseScale = 1.0f;
                    repaint();
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int width = getWidth();
                int height = getHeight();
                
                // Calculate center position
                int centerX = width / 2;
                int centerY = height / 2;
                
                // Load and draw the icon with smaller size
                String iconName = getIconNameForIndex(index);
                ImageIcon icon = IconLoader.loadIcon(iconName, 25, 25, categoryColor);
                
                // Apply pulse effect
                if (pulsing) {
                    g2d.translate(centerX, centerY);
                    g2d.scale(pulseScale, pulseScale);
                    g2d.translate(-centerX, -centerY);
                }
                
                // Draw the icon
                icon.paintIcon(this, g2d, centerX - icon.getIconWidth()/2, centerY - icon.getIconHeight()/2);
                
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

    private String getIconNameForIndex(int index) {
        return switch (index) {
            case 0 -> "cpu.svg";  // System Performance
            case 1 -> "wifi.svg";  // Network Diagnostics
            case 2 -> "driver.svg";  // Driver Issues
            case 3 -> "folder.svg";  // File System Repair
            case 4 -> "error.svg";  // Blue Screen Errors
            case 5 -> "app.svg";  // App Compatibility
            case 6 -> "speaker.svg";  // Audio Troubleshooting
            case 7 -> "bluetooth.svg";  // Bluetooth & Devices
            case 8 -> "browser.svg";  // Browser Issues
            default -> "question.svg";
        };
    }

    private void openFullSizeWindow(int index) {
        JPanel selectedPanel = null;
        String title = "";
        
        switch (index) {
            case 0 -> {
                selectedPanel = this.SystemPerformancePanel;
                title = "System Performance";
            }
            case 1 -> {
                selectedPanel = this.NetworkDiagnosticsPanel;
                title = "Network Diagnostics";
            }
            case 2 -> {
                selectedPanel = this.DriversIssuesPanel;
                title = "Driver Issues";
            }
            case 3 -> {
                selectedPanel = this.FileSystemRepairPanel;
                title = "File System Repair";
            }
            case 4 -> {
                selectedPanel = this.BlueScreenErrorPanel;
                title = "Blue Screen Error";
            }
            case 5 -> {
                selectedPanel = this.ApplicationCompatibilityIssuesPanel;
                title = "Application Compatibility Issues";
            }
            case 6 -> {
                selectedPanel = this.AudioTroubleshootingPanel;
                title = "Audio & Sound";
            }
            case 7 -> {
                selectedPanel = this.BluetoothAndDeviceConnectivityPanel;
                title = "Bluetooth and Device Connectivity";
            }
            case 8 -> {
                selectedPanel = this.BrowserIssuesPanel;
                title = "Browser Issues";
            }
        }

        if (selectedPanel != null) {
            // Create a container panel with a back button
            JPanel containerPanel = new JPanel(new BorderLayout());
            containerPanel.setBackground(PANEL_BACKGROUND);
            
            // Create header panel with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(5, 5, 5));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JButton backButton = new JButton("← Back");
            backButton.setForeground(TEXT_COLOR);
            backButton.setBackground(new Color(30, 30, 30));
            backButton.setBorderPainted(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backButton.addActionListener(e -> {
                // Restore the original content
                mainPanel.removeAll();
                mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
                mainPanel.add(createMainGridPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            });
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            containerPanel.add(headerPanel, BorderLayout.NORTH);
            containerPanel.add(selectedPanel, BorderLayout.CENTER);
            
            // Replace the current content with the new panel
            mainPanel.removeAll();
            mainPanel.add(containerPanel, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    private JPanel createPlaceholderPanel(String message) {
        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setBackground(PANEL_BACKGROUND);
        placeholder.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel noInfoLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        noInfoLabel.setForeground(TEXT_COLOR);
        noInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        placeholder.add(noInfoLabel, BorderLayout.CENTER);
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
        private static final Color BUTTON_COLOR = new Color(25, 25, 25);
        private static final Color BUTTON_HOVER_COLOR = new Color(35, 35, 35);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 150); // Royal Blue
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
        private static final int CORNER_RADIUS = 4;

        public NetworkDiagnosticsPanel() {
            super("Network Diagnostics");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create main container with card layout
            JPanel mainContainer = new JPanel(new CardLayout(0, 0));
            mainContainer.setBackground(PANEL_BACKGROUND);
            
            // Create the main menu panel
            JPanel menuPanel = createMenuPanel(mainContainer);
            
            // Create result panels
            JPanel pingResultPanel = createResultPanel("Ping Test Results", "ping");
            JPanel speedResultPanel = createResultPanel("Speed Test Results", "speed");
            
            // Add all panels to the card layout
            mainContainer.add(menuPanel, "menu");
            mainContainer.add(pingResultPanel, "ping");
            mainContainer.add(speedResultPanel, "speed");
            
            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel(JPanel mainContainer) {
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
            menuPanel.setBackground(PANEL_BACKGROUND);
            
            // Add title
            JLabel titleLabel = new JLabel("Network Diagnostics Tools");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add description
            JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 300px;'>Select a diagnostic tool to check your network connectivity and performance</div></html>");
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            menuPanel.add(Box.createVerticalStrut(20));
            menuPanel.add(titleLabel);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(descLabel);
            menuPanel.add(Box.createVerticalStrut(30));
            
            // Create and add the first button
            JButton pingButton = createStyledButton("Ping Test", "Test network connectivity", "ping");
            pingButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "ping");
            });
            
            menuPanel.add(pingButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the second button
            JButton speedTestButton = createStyledButton("Speed Test", "Check internet speed", "speed");
            speedTestButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "speed");
            });
            
            menuPanel.add(speedTestButton);
            menuPanel.add(Box.createVerticalGlue());
            
            return menuPanel;
        }
        
        private JPanel createResultPanel(String title, String type) {
            JPanel resultPanel = new JPanel(new BorderLayout(0, 10));
            resultPanel.setBackground(PANEL_BACKGROUND);
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(5, 5, 5));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JButton backButton = new JButton("← Back");
            backButton.setForeground(TEXT_COLOR);
            backButton.setBackground(new Color(30, 30, 30));
            backButton.setBorderPainted(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel
            JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create output area
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(5, 5, 5));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(PANEL_BACKGROUND);
            
            // Create control panel
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(PANEL_BACKGROUND);
            
            JButton startButton = createStyledButton("Start Test", "Start the network test", type);
            startButton.addActionListener(e -> {
                startButton.setText("Running...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            String batFilePath = type.equals("ping") ? 
                                "Bat_Scripts/Network.bat" : 
                                "Bat_Scripts/SpeedTest.bat";

                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);
                            Process process = processBuilder.start();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line);
                            }

                            process.waitFor();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete test.");
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n");
                            // Auto-scroll to bottom
                            outputArea.setCaretPosition(outputArea.getDocument().getLength());
                        }
                    }

                    @Override
                    protected void done() {
                        startButton.setText("Start Test");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to the result panel
            resultPanel.add(headerPanel, BorderLayout.NORTH);
            resultPanel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            // Add back button action
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) resultPanel.getParent().getLayout();
                cl.show(resultPanel.getParent(), "menu");
            });
            
            return resultPanel;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
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
                    setPreferredSize(new Dimension(200, 45));
                    setToolTipText(tooltip);
                    
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
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
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
        }
    }



    class DriversIssuesPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(25, 25, 25);
        private static final Color BUTTON_HOVER_COLOR = new Color(35, 35, 35);
        private static final Color BUTTON_ACCENT = new Color(46, 139, 87, 150); // Sea Green
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
        private static final int CORNER_RADIUS = 4;

        public DriversIssuesPanel() {
            super("Driver Issues");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create main container with card layout
            JPanel mainContainer = new JPanel(new CardLayout(0, 0));
            mainContainer.setBackground(PANEL_BACKGROUND);
            
            // Create the main menu panel
            JPanel menuPanel = createMenuPanel(mainContainer);
            
            // Create result panels
            JPanel scanResultPanel = createResultPanel("Driver Scan Results", "scan");
            JPanel updateResultPanel = createResultPanel("Driver Update Results", "update");
            JPanel rollbackResultPanel = createResultPanel("Driver Rollback Results", "rollback");
            
            // Add all panels to the card layout
            mainContainer.add(menuPanel, "menu");
            mainContainer.add(scanResultPanel, "scan");
            mainContainer.add(updateResultPanel, "update");
            mainContainer.add(rollbackResultPanel, "rollback");
            
            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }
        
        private JPanel createMenuPanel(JPanel mainContainer) {
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
            menuPanel.setBackground(PANEL_BACKGROUND);
            
            // Add title
            JLabel titleLabel = new JLabel("Driver Management Tools");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add description
            JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 300px;'>Select a tool to scan, update, or rollback your system drivers</div></html>");
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            menuPanel.add(Box.createVerticalStrut(20));
            menuPanel.add(titleLabel);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(descLabel);
            menuPanel.add(Box.createVerticalStrut(30));
            
            // Create and add the scan button
            JButton scanButton = createStyledButton("Scan Drivers", "Scan for driver issues", "scan");
            scanButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "scan");
            });
            
            menuPanel.add(scanButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the update button
            JButton updateButton = createStyledButton("Update Drivers", "Update system drivers", "update");
            updateButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "update");
            });
            
            menuPanel.add(updateButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the rollback button
            JButton rollbackButton = createStyledButton("Rollback Drivers", "Rollback to previous driver versions", "rollback");
            rollbackButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "rollback");
            });
            
            menuPanel.add(rollbackButton);
            menuPanel.add(Box.createVerticalGlue());
            
            return menuPanel;
        }
        
        private JPanel createResultPanel(String title, String type) {
            JPanel resultPanel = new JPanel(new BorderLayout(0, 10));
            resultPanel.setBackground(PANEL_BACKGROUND);
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(5, 5, 5));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JButton backButton = new JButton("← Back");
            backButton.setForeground(TEXT_COLOR);
            backButton.setBackground(new Color(30, 30, 30));
            backButton.setBorderPainted(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel
            JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create output area
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(5, 5, 5));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(PANEL_BACKGROUND);
            
            // Create control panel
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(PANEL_BACKGROUND);
            
            JButton startButton = createStyledButton("Start Operation", "Start the driver operation", type);
            startButton.addActionListener(e -> {
                startButton.setText("Running...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            String batFilePath = switch (type) {
                                case "scan" -> "Bat_Scripts/DriverScan.bat";
                                case "update" -> "Bat_Scripts/DriverUpdate.bat";
                                case "rollback" -> "Bat_Scripts/DriverRollback.bat";
                                default -> throw new IllegalArgumentException("Invalid operation type");
                            };

                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);
                            Process process = processBuilder.start();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line);
                            }

                            process.waitFor();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete operation.");
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n");
                            // Auto-scroll to bottom
                            outputArea.setCaretPosition(outputArea.getDocument().getLength());
                        }
                    }

                    @Override
                    protected void done() {
                        startButton.setText("Start Operation");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to the result panel
            resultPanel.add(headerPanel, BorderLayout.NORTH);
            resultPanel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            // Add back button action
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) resultPanel.getParent().getLayout();
                cl.show(resultPanel.getParent(), "menu");
            });
            
            return resultPanel;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
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
                    setPreferredSize(new Dimension(200, 45));
                    setToolTipText(tooltip);
                    
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
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
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
            return Troubleshoot.this.createPlaceholderPanel("Application compatibility troubleshooting features will be available in a future update.");
        }
    }




    public static class AudioTroubleshootingPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(25, 25, 25);
        private static final Color BUTTON_HOVER_COLOR = new Color(35, 35, 35);
        private static final Color BUTTON_ACCENT = new Color(72, 61, 139, 150); // Dark Slate Blue
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
        private static final int CORNER_RADIUS = 4;

        public AudioTroubleshootingPanel() {
            super("Audio & Sound");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create main container with card layout
            JPanel mainContainer = new JPanel(new CardLayout(0, 0));
            mainContainer.setBackground(PANEL_BACKGROUND);
            
            // Create the main menu panel
            JPanel menuPanel = createMenuPanel(mainContainer);
            
            // Create result panels
            JPanel audioScanResultPanel = createResultPanel("Audio Device Scan Results", "scan");
            JPanel audioFixResultPanel = createResultPanel("Audio Fix Results", "fix");
            
            // Add all panels to the card layout
            mainContainer.add(menuPanel, "menu");
            mainContainer.add(audioScanResultPanel, "scan");
            mainContainer.add(audioFixResultPanel, "fix");
            
            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel(JPanel mainContainer) {
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
            menuPanel.setBackground(PANEL_BACKGROUND);
            
            // Add title
            JLabel titleLabel = new JLabel("Audio Troubleshooting Tools");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add description
            JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 300px;'>Select a tool to scan audio devices or fix common audio issues</div></html>");
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            menuPanel.add(Box.createVerticalStrut(20));
            menuPanel.add(titleLabel);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(descLabel);
            menuPanel.add(Box.createVerticalStrut(30));
            
            // Create and add the scan button
            JButton scanButton = createStyledButton("Scan Audio Devices", "Scan for audio device issues", "scan");
            scanButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "scan");
            });
            
            menuPanel.add(scanButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the fix button
            JButton fixButton = createStyledButton("Fix Audio Issues", "Fix common audio problems", "fix");
            fixButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "fix");
            });
            
            menuPanel.add(fixButton);
            menuPanel.add(Box.createVerticalGlue());
            
            return menuPanel;
        }
        
        private JPanel createResultPanel(String title, String type) {
            JPanel resultPanel = new JPanel(new BorderLayout(0, 10));
            resultPanel.setBackground(PANEL_BACKGROUND);
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(5, 5, 5));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JButton backButton = new JButton("← Back");
            backButton.setForeground(TEXT_COLOR);
            backButton.setBackground(new Color(30, 30, 30));
            backButton.setBorderPainted(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel
            JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create output area
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(5, 5, 5));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(PANEL_BACKGROUND);
            
            // Create control panel
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(PANEL_BACKGROUND);
            
            JButton startButton = createStyledButton("Start Operation", "Start the audio operation", type);
            startButton.addActionListener(e -> {
                startButton.setText("Running...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            String batFilePath = switch (type) {
                                case "scan" -> "Bat_Scripts/AudioScan.bat";
                                case "fix" -> "Bat_Scripts/AudioFix.bat";
                                default -> throw new IllegalArgumentException("Invalid operation type");
                            };

                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);
                            Process process = processBuilder.start();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line);
                            }

                            process.waitFor();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete operation.");
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n");
                            // Auto-scroll to bottom
                            outputArea.setCaretPosition(outputArea.getDocument().getLength());
                        }
                    }

                    @Override
                    protected void done() {
                        startButton.setText("Start Operation");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to the result panel
            resultPanel.add(headerPanel, BorderLayout.NORTH);
            resultPanel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            // Add back button action
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) resultPanel.getParent().getLayout();
                cl.show(resultPanel.getParent(), "menu");
            });
            
            return resultPanel;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
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
                    setPreferredSize(new Dimension(200, 45));
                    setToolTipText(tooltip);
                    
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
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
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
        }
    }




    public static class BluetoothAndDeviceConnectivityPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(25, 25, 25);
        private static final Color BUTTON_HOVER_COLOR = new Color(35, 35, 35);
        private static final Color BUTTON_ACCENT = new Color(0, 139, 139, 150); // Dark Cyan
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
        private static final int CORNER_RADIUS = 4;

        public BluetoothAndDeviceConnectivityPanel() {
            super("Bluetooth and Device Connectivity");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create main container with card layout
            JPanel mainContainer = new JPanel(new CardLayout(0, 0));
            mainContainer.setBackground(PANEL_BACKGROUND);
            
            // Create the main menu panel
            JPanel menuPanel = createMenuPanel(mainContainer);
            
            // Create result panels
            JPanel scanResultPanel = createResultPanel("Device Scan Results", "scan");
            JPanel bluetoothResultPanel = createResultPanel("Bluetooth Troubleshooting Results", "bluetooth");
            JPanel deviceResultPanel = createResultPanel("Device Management Results", "device");
            
            // Add all panels to the card layout
            mainContainer.add(menuPanel, "menu");
            mainContainer.add(scanResultPanel, "scan");
            mainContainer.add(bluetoothResultPanel, "bluetooth");
            mainContainer.add(deviceResultPanel, "device");
            
            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel(JPanel mainContainer) {
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
            menuPanel.setBackground(PANEL_BACKGROUND);
            
            // Add title
            JLabel titleLabel = new JLabel("Device Management Tools");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add description
            JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 300px;'>Select a tool to scan devices, troubleshoot Bluetooth, or manage device connections</div></html>");
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            menuPanel.add(Box.createVerticalStrut(20));
            menuPanel.add(titleLabel);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(descLabel);
            menuPanel.add(Box.createVerticalStrut(30));
            
            // Create and add the scan button
            JButton scanButton = createStyledButton("Scan Devices", "Scan for connected devices", "scan");
            scanButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "scan");
            });
            
            menuPanel.add(scanButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the Bluetooth button
            JButton bluetoothButton = createStyledButton("Bluetooth Troubleshooting", "Fix Bluetooth connectivity issues", "bluetooth");
            bluetoothButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "bluetooth");
            });
            
            menuPanel.add(bluetoothButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the device management button
            JButton deviceButton = createStyledButton("Device Management", "Manage device connections and settings", "device");
            deviceButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "device");
            });
            
            menuPanel.add(deviceButton);
            menuPanel.add(Box.createVerticalGlue());
            
            return menuPanel;
        }
        
        private JPanel createResultPanel(String title, String type) {
            JPanel resultPanel = new JPanel(new BorderLayout(0, 10));
            resultPanel.setBackground(PANEL_BACKGROUND);
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(5, 5, 5));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JButton backButton = new JButton("← Back");
            backButton.setForeground(TEXT_COLOR);
            backButton.setBackground(new Color(30, 30, 30));
            backButton.setBorderPainted(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel
            JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create output area
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(5, 5, 5));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(PANEL_BACKGROUND);
            
            // Create control panel
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(PANEL_BACKGROUND);
            
            JButton startButton = createStyledButton("Start Operation", "Start the device operation", type);
            startButton.addActionListener(e -> {
                startButton.setText("Running...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            String batFilePath = switch (type) {
                                case "scan" -> "Bat_Scripts/DeviceScan.bat";
                                case "bluetooth" -> "Bat_Scripts/Bluetooth.bat";
                                case "device" -> "Bat_Scripts/DeviceManage.bat";
                                default -> throw new IllegalArgumentException("Invalid operation type");
                            };

                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);
                            Process process = processBuilder.start();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line);
                            }

                            process.waitFor();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete operation.");
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n");
                            // Auto-scroll to bottom
                            outputArea.setCaretPosition(outputArea.getDocument().getLength());
                        }
                    }

                    @Override
                    protected void done() {
                        startButton.setText("Start Operation");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to the result panel
            resultPanel.add(headerPanel, BorderLayout.NORTH);
            resultPanel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            // Add back button action
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) resultPanel.getParent().getLayout();
                cl.show(resultPanel.getParent(), "menu");
            });
            
            return resultPanel;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
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
                    setPreferredSize(new Dimension(200, 45));
                    setToolTipText(tooltip);
                    
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
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
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
        }
    }




    public static class BrowserIssuesPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(25, 25, 25);
        private static final Color BUTTON_HOVER_COLOR = new Color(35, 35, 35);
        private static final Color BUTTON_ACCENT = new Color(184, 134, 11, 150); // Dark Goldenrod
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
        private static final int CORNER_RADIUS = 4;

        public BrowserIssuesPanel() {
            super("Browser Issues");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create main container with card layout
            JPanel mainContainer = new JPanel(new CardLayout(0, 0));
            mainContainer.setBackground(PANEL_BACKGROUND);
            
            // Create the main menu panel
            JPanel menuPanel = createMenuPanel(mainContainer);
            
            // Create result panels
            JPanel scanResultPanel = createResultPanel("Browser Scan Results", "scan");
            JPanel cacheResultPanel = createResultPanel("Cache Cleanup Results", "cache");
            JPanel extensionResultPanel = createResultPanel("Extension Management Results", "extension");
            JPanel settingsResultPanel = createResultPanel("Browser Settings Results", "settings");
            
            // Add all panels to the card layout
            mainContainer.add(menuPanel, "menu");
            mainContainer.add(scanResultPanel, "scan");
            mainContainer.add(cacheResultPanel, "cache");
            mainContainer.add(extensionResultPanel, "extension");
            mainContainer.add(settingsResultPanel, "settings");
            
            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel(JPanel mainContainer) {
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
            menuPanel.setBackground(PANEL_BACKGROUND);
            
            // Add title
            JLabel titleLabel = new JLabel("Browser Troubleshooting Tools");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add description
            JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 300px;'>Select a tool to scan browser issues, clean cache, manage extensions, or reset settings</div></html>");
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            menuPanel.add(Box.createVerticalStrut(20));
            menuPanel.add(titleLabel);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(descLabel);
            menuPanel.add(Box.createVerticalStrut(30));
            
            // Create and add the scan button
            JButton scanButton = createStyledButton("Scan Browser Issues", "Scan for browser problems", "scan");
            scanButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "scan");
            });
            
            menuPanel.add(scanButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the cache cleanup button
            JButton cacheButton = createStyledButton("Clean Browser Cache", "Clear browser cache and temporary files", "cache");
            cacheButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "cache");
            });
            
            menuPanel.add(cacheButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the extension management button
            JButton extensionButton = createStyledButton("Manage Extensions", "Check and manage browser extensions", "extension");
            extensionButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "extension");
            });
            
            menuPanel.add(extensionButton);
            menuPanel.add(Box.createVerticalStrut(15));
            
            // Create and add the settings button
            JButton settingsButton = createStyledButton("Reset Browser Settings", "Reset browser to default settings", "settings");
            settingsButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "settings");
            });
            
            menuPanel.add(settingsButton);
            menuPanel.add(Box.createVerticalGlue());
            
            return menuPanel;
        }
        
        private JPanel createResultPanel(String title, String type) {
            JPanel resultPanel = new JPanel(new BorderLayout(0, 10));
            resultPanel.setBackground(PANEL_BACKGROUND);
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(5, 5, 5));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JButton backButton = new JButton("← Back");
            backButton.setForeground(TEXT_COLOR);
            backButton.setBackground(new Color(30, 30, 30));
            backButton.setBorderPainted(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel
            JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
            contentPanel.setBackground(PANEL_BACKGROUND);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create output area
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(5, 5, 5));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(PANEL_BACKGROUND);
            
            // Create control panel
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(PANEL_BACKGROUND);
            
            JButton startButton = createStyledButton("Start Operation", "Start the browser operation", type);
            startButton.addActionListener(e -> {
                startButton.setText("Running...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            String batFilePath = switch (type) {
                                case "scan" -> "Bat_Scripts/BrowserScan.bat";
                                case "cache" -> "Bat_Scripts/BrowserCache.bat";
                                case "extension" -> "Bat_Scripts/BrowserExtension.bat";
                                case "settings" -> "Bat_Scripts/BrowserSettings.bat";
                                default -> throw new IllegalArgumentException("Invalid operation type");
                            };

                            File batFile = new File(batFilePath);
                            if (!batFile.exists()) {
                                publish("Error: Batch file not found.");
                                return null;
                            }

                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                            processBuilder.redirectErrorStream(true);
                            Process process = processBuilder.start();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line);
                            }

                            process.waitFor();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish("Error: Couldn't complete operation.");
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String chunk : chunks) {
                            outputArea.append(chunk + "\n");
                            // Auto-scroll to bottom
                            outputArea.setCaretPosition(outputArea.getDocument().getLength());
                        }
                    }

                    @Override
                    protected void done() {
                        startButton.setText("Start Operation");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to the result panel
            resultPanel.add(headerPanel, BorderLayout.NORTH);
            resultPanel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            // Add back button action
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) resultPanel.getParent().getLayout();
                cl.show(resultPanel.getParent(), "menu");
            });
            
            return resultPanel;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
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
                    setPreferredSize(new Dimension(200, 45));
                    setToolTipText(tooltip);
                    
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
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
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
