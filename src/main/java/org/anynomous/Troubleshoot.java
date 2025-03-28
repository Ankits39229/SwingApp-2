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
import javax.swing.*;
import java.io.File;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.BasicStroke;
import java.awt.event.HierarchyEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import main.java.org.anynomous.utils.IconLoader;
import java.util.List;
import java.awt.geom.Rectangle2D;


public class Troubleshoot {
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    private static final Color PANEL_BACKGROUND = new Color(0, 0, 0);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color SECONDARY_TEXT_COLOR = new Color(170, 170, 170);
    private static final Color HOVER_COLOR = new Color(5, 5, 5);
    private static final Color GRADIENT_START = new Color(0, 0, 0);
    private static final Color GRADIENT_END = new Color(0, 0, 0);
    private static final Color ACCENT_COLOR = new Color(45, 45, 45);
    private static final Color ACCENT_HOVER_COLOR = new Color(55, 55, 55);
    private static final Color CARD_SHADOW_COLOR = new Color(0, 0, 0, 100);


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
    private final AudioTroubleshootingPanel AudioTroubleshootingPanel;
    private final FileSystemRepairPanel FileSystemRepairPanel;
    private final WindowsIssuesPanel WindowsIssuesPanel;
    private final HardwareIssuesPanel HardwareIssuesPanel;
    private final OtherErrorPanel OtherErrorPanel;
    private final BluetoothPanel BluetoothPanel;
    private final BrowserPanel BrowserPanel;
    
    
    private final JPanel mainPanel;

    public Troubleshoot(JPanel... sidePanels) {
        this.SystemPerformancePanel = new SystemPerformancePanel();
        this.NetworkDiagnosticsPanel = new NetworkDiagnosticsPanel();
        this.HardwareIssuesPanel = new HardwareIssuesPanel();
        this.FileSystemRepairPanel = new FileSystemRepairPanel();
        this.OtherErrorPanel = new OtherErrorPanel();
        this.WindowsIssuesPanel = new WindowsIssuesPanel();
        this.AudioTroubleshootingPanel = new AudioTroubleshootingPanel();
        this.BluetoothPanel = new BluetoothPanel();
        this.BrowserPanel = new BrowserPanel();

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
        mainPanel.add(createItemPanel("Audio", "Fix sound and audio problems", 2));
        mainPanel.add(createItemPanel("File System", "Repair system files", 3));
        mainPanel.add(createItemPanel("Windows Update", "Fix update related problems", 4));
        mainPanel.add(createItemPanel("Hardware", "Resolve external device connectivity issues", 5));
        mainPanel.add(createItemPanel("Other", "Fix other common problems", 6));
        mainPanel.add(createItemPanel("Bluetooth", "Resolve device connectivity issues", 7));
        mainPanel.add(createItemPanel("Browser", "Fix common browser problems", 8));

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
                    g2d.setColor(isPressed ? HOVER_COLOR : new Color(5, 5, 5));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 8, 8));
                    
                    // Subtle color accent on hover
                    Color hoverAccent = new Color(
                        categoryColor.getRed(),
                        categoryColor.getGreen(),
                        categoryColor.getBlue(),
                        (int)(20 * hoverAlpha)
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
            case 0 -> "activity.svg";  // System Performance
            case 1 -> "wifi.svg";  // Network Diagnostics
            case 2 -> "speaker.svg";  // Audio Issues
            case 3 -> "folder.svg";  // File System Repair
            case 4 -> "arrow-repeat.svg";  // Windows Update
            case 5 -> "app.svg";  // Hardware Issues
            case 6 -> "question.svg";  // Other Issues
            case 7 -> "bluetooth.svg";  // Bluetooth & Devices
            case 8 -> "browser.svg";  // Browser Issues
            default -> "question.svg";
        };
    }

    private void openFullSizeWindow(int index) {
        JPanel selectedPanel;
        String title;
        
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
                selectedPanel = this.AudioTroubleshootingPanel;
                title = "Audio Troubleshooting";
            }
            case 3 -> {
                selectedPanel = this.FileSystemRepairPanel;
                title = "File System Repair";
            }
            case 4 -> {
                selectedPanel = this.WindowsIssuesPanel;
                title = "Windows Update Issues";
            }
            case 5 -> {
                selectedPanel = this.HardwareIssuesPanel;
                title = "Hardware Issues";
            }
            case 6 -> {
                selectedPanel = this.OtherErrorPanel;
                title = "Other Issues";
            }
            case 7 -> {
                selectedPanel = this.BluetoothPanel;
                title = "Bluetooth and Device Connectivity";
            }
            case 8 -> {
                selectedPanel = this.BrowserPanel;
                title = "Browser Issues";
            }
            default -> {
                selectedPanel = createPlaceholderPanel("This feature is coming soon!");
                title = "Coming Soon";
            }
        }

            // Create a container panel with a back button
            JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(0, 0, 0));
            
            // Create header panel with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JButton backButton = new JButton("←");
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
        private static final Color PANEL_BACKGROUND = new Color(0, 0, 0);
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(70, 130, 180, 150);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final Color OUTPUT_BACKGROUND = new Color(0, 0, 0);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
        private static final int CORNER_RADIUS = 4;

        public SystemPerformancePanel() {
            super("System Performance");
        }

        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.setBackground(new Color(0, 0, 0));
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
            scanPanel.setBackground(new Color(0, 0, 0));
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
                    
                    // Draw permanent border
                    g2d.setColor(new Color(70, 70, 70)); // Added visible border
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw hover effects
                    if (isHovered) {
                        // Glow effect
                        Point2D center = new Point2D.Float(width/2, height/2);
                        float radius = width/2;
                        float[] dist = {0.0f, 1.0f};
                        Color[] colors = {
                            new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 150),
                            new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 0)
                        };
                        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                        g2d.setPaint(gradient);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Accent color
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            isPressed ? 100 : 60
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border with increased opacity
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            (int)(200 * borderAlpha) // Increased opacity
                        ));
                        g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text with shadow for better visibility
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Enhanced text shadow
                    g2d.setColor(new Color(0, 0, 0, 150)); // Darker shadow
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
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            // Create scrollable output panel with improved styling
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setPreferredSize(new Dimension(0, 200));
            scrollPane.setBackground(new Color(0, 0, 0));
            
            // Apply custom scrollbar UI with subtle accent
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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



    private static class NetworkDiagnosticsPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 255); // Increased opacity for better visibility
        private static final Color CARD_BACKGROUND = new Color(0, 0, 0);
        private static final Color CARD_BORDER = new Color(70, 70, 70); // Added visible border color
        private static final Color TEXT_COLOR = new Color(200, 200, 200);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final int CORNER_RADIUS = 15;
        
        private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
        
        private JPanel mainContainer;
        private JTextArea outputArea;
        private JProgressBar progressBar;
        private JLabel statusLabel;

        public NetworkDiagnosticsPanel() {
            super("Network Diagnostics");
        }

        @Override
        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(0, 0, 0));
            
            // Create main container with card layout
            mainContainer = new JPanel(new CardLayout());
            mainContainer.setOpaque(false);
            mainContainer.setBackground(new Color(0, 0, 0));
            
            // Create and add panels
            mainContainer.add(createMenuPanel(), "menu");
            mainContainer.add(createWifiIconPanel(), "wifi_icon");
            mainContainer.add(createNoInternetPanel(), "no_internet");
            
            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(0, 0, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
            
            // Create header with animation
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(new Color(0, 0, 0));
            
            JLabel titleLabel = new JLabel("Network Troubleshooting");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel subtitleLabel = new JLabel("Select a network issue to diagnose");
            subtitleLabel.setFont(SUBTITLE_FONT);
            subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);
            
            // Create cards container
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(new Color(0, 0, 0));
            
            // Add issue cards
            cardsPanel.add(createIssueCard(
                "Wi-Fi Icon Not Visible",
                "Diagnose and fix issues with the Wi-Fi icon in the system tray",
                "wifi.svg",
                "wifi_icon",
                mainContainer
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                "No Internet After Connection",
                "Troubleshoot connectivity issues after establishing a Wi-Fi connection",
                "network.svg",
                "no_internet",
                mainContainer
            ));
            
            // Add components to panel
            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(40));
            panel.add(cardsPanel);
            
            return panel;
        }
        
        private JPanel createIssueCard(String title, String description, String iconName, String type, JPanel mainContainer) {
            JPanel card = new JPanel() {
                private float opacity = 0.0f;
                private float translateY = 50.0f;
                private boolean isHovered = false;
                private Timer animTimer;
                
                {
                    setLayout(new BorderLayout(15, 0));
                    setOpaque(false);
                    setMaximumSize(new Dimension(500, 100));
                    setAlignmentX(Component.CENTER_ALIGNMENT);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            repaint();
                        }
                        
                        @Override
                        public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                            cl.show(mainContainer, type);
                        }
                    });
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    animTimer = new Timer(16, null);
                    animTimer.addActionListener(e -> {
                        opacity = Math.min(1.0f, opacity + 0.05f);
                        translateY = Math.max(0.0f, translateY - 2.0f);
                        
                        if (opacity >= 1.0f && translateY <= 0) {
                            animTimer.stop();
                        }
                        repaint();
                    });
                    animTimer.start();
                }
                
                    @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.translate(0, translateY);
                    
                    // Draw card background
                    g2d.setColor(isHovered ? new Color(5, 5, 5) : new Color(0, 0, 0));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                    g2d.setColor(isHovered ? new Color(10, 10, 10) : new Color(0, 0, 0));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, CORNER_RADIUS, CORNER_RADIUS));
                    
                    if (isHovered) {
                        g2d.setColor(new Color(65, 105, 225, 10));
                        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    super.paintComponent(g2d);
                    g2d.dispose();
                }
            };
            
            JPanel iconPanel = new JPanel() {
                    @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // Draw a dark black background for better contrast
                    g2d.setColor(new Color(0, 0, 0));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Load and draw the icon with increased size and better contrast
                    ImageIcon icon = IconLoader.loadIcon(iconName, 35, 35, new Color(65, 105, 225, 255));
                    icon.paintIcon(this, g2d, (getWidth() - icon.getIconWidth()) / 2,
                                            (getHeight() - icon.getIconHeight()) / 2);
                    
                    g2d.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(50, 50));
            iconPanel.setBackground(new Color(0, 0, 0));
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(0, 0, 0));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(BUTTON_FONT);
            
            JLabel descLabel = new JLabel(description);
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(STATUS_FONT);
            
            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(descLabel);
            
            card.add(iconPanel, BorderLayout.WEST);
            card.add(contentPanel, BorderLayout.CENTER);
            
            return card;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private float glowEffect = 0.0f;
                private Timer pulseTimer;
                private Timer glowTimer;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(200, 50));
                    setToolTipText(tooltip);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            startGlowAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
                            startPulseAnimation(false);
                            startGlowAnimation(false);
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
                
                private void startGlowAnimation(boolean start) {
                    if (glowTimer != null && glowTimer.isRunning()) {
                        glowTimer.stop();
                    }
                    
                    if (start) {
                        glowEffect = 0.0f;
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.min(1.0f, glowEffect + 0.1f);
                            repaint();
                        });
                        glowTimer.start();
                    } else {
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.max(0.0f, glowEffect - 0.1f);
                            if (glowEffect <= 0) {
                                glowTimer.stop();
                            }
                            repaint();
                        });
                        glowTimer.start();
                    }
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    g2d.setColor(isPressed ? BUTTON_HOVER_COLOR : (isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(new Color(70, 70, 70)); // Added visible border
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw hover effects
                    if (isHovered) {
                        // Glow effect
                        Point2D center = new Point2D.Float(width/2, height/2);
                        float radius = width/2;
                        float[] dist = {0.0f, 1.0f};
                        Color[] colors = {
                            new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 150),
                            new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 0)
                        };
                        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                        g2d.setPaint(gradient);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Accent color
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            isPressed ? 100 : 60
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border with increased opacity
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            (int)(200 * borderAlpha) // Increased opacity
                        ));
                        g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text with shadow for better visibility
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Enhanced text shadow
                    g2d.setColor(new Color(0, 0, 0, 150)); // Darker shadow
                    g2d.drawString(text, textX + 1, textY + 1);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                    
                    g2d.dispose();
                }
            };
        }
        
        private JPanel createWifiIconPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });
            
            JLabel titleLabel = new JLabel("Wi-Fi Icon Not Visible");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            
            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));
            
            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>This tool will diagnose and fix issues with the Wi-Fi icon in your system tray. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));
            
            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));
            
            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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
            
            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));
            
            JButton startButton = createStyledButton("Start Diagnosis", "Begin Wi-Fi icon diagnosis", "wifi_icon");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/WifiIcon.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis script not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            return panel;
        }
        
        private JPanel createNoInternetPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });
            
            JLabel titleLabel = new JLabel("No Internet After Connection");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            
            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));
            
            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>This tool will diagnose and fix connectivity issues after establishing a Wi-Fi connection. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));
            
            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));
            
            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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
            
            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));
            
            JButton startButton = createStyledButton("Start Diagnosis", "Begin internet connectivity diagnosis", "no_internet");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/NoInternet.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis script not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            return panel;
        }
    }



    private static class HardwareIssuesPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 255); // Increased opacity for better visibility
        private static final Color CARD_BACKGROUND = new Color(0, 0, 0);
        private static final Color CARD_BORDER = new Color(70, 70, 70); // Added visible border color
        private static final Color TEXT_COLOR = new Color(200, 200, 200);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final int CORNER_RADIUS = 15;

        private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);

        private JPanel mainContainer;
        private JTextArea outputArea;
        private JProgressBar progressBar;
        private JLabel statusLabel;

        public HardwareIssuesPanel() {
            super("Network Diagnostics");
        }

        public HardwareIssuesPanel(String title) {
            super(title);
        }

        @Override
        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(0, 0, 0));

            // Create main container with card layout
            mainContainer = new JPanel(new CardLayout());
            mainContainer.setOpaque(false);
            mainContainer.setBackground(new Color(0, 0, 0));

            // Create and add panels
            mainContainer.add(createMenuPanel(), "menu");
            mainContainer.add(createWifiIconPanel(), "wifi_icon");
            mainContainer.add(createNoInternetPanel(), "no_internet");

            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(0, 0, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            // Create header with animation
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(new Color(0, 0, 0));

            JLabel titleLabel = new JLabel("Network Troubleshooting");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel subtitleLabel = new JLabel("Select a network issue to diagnose");
            subtitleLabel.setFont(SUBTITLE_FONT);
            subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);

            // Create cards container
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(new Color(0, 0, 0));

            // Add issue cards
            cardsPanel.add(createIssueCard(
                    "Wi-Fi Icon Not Visible",
                    "Diagnose and fix issues with the Wi-Fi icon in the system tray",
                    "wifi.svg",
                    "wifi_icon",
                    mainContainer
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                    "No Internet After Connection",
                    "Troubleshoot connectivity issues after establishing a Wi-Fi connection",
                    "network.svg",
                    "no_internet",
                    mainContainer
            ));

            // Add components to panel
            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(40));
            panel.add(cardsPanel);

            return panel;
        }

        private JPanel createIssueCard(String title, String description, String iconName, String type, JPanel mainContainer) {
            JPanel card = new JPanel() {
                private float opacity = 0.0f;
                private float translateY = 50.0f;
                private boolean isHovered = false;
                private Timer animTimer;

                {
                    setLayout(new BorderLayout(15, 0));
                    setOpaque(false);
                    setMaximumSize(new Dimension(500, 100));
                    setAlignmentX(Component.CENTER_ALIGNMENT);

                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            repaint();
                        }

                    @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            repaint();
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            CardLayout cl = (CardLayout) mainContainer.getLayout();
                            cl.show(mainContainer, type);
                        }
                    });
                    setCursor(new Cursor(Cursor.HAND_CURSOR));

                    animTimer = new Timer(16, null);
                    animTimer.addActionListener(e -> {
                        opacity = Math.min(1.0f, opacity + 0.05f);
                        translateY = Math.max(0.0f, translateY - 2.0f);

                        if (opacity >= 1.0f && translateY <= 0) {
                            animTimer.stop();
                        }
                        repaint();
                    });
                    animTimer.start();
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.translate(0, translateY);

                    // Draw card background
                    g2d.setColor(isHovered ? new Color(5, 5, 5) : new Color(0, 0, 0));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));

                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                    g2d.setColor(isHovered ? new Color(10, 10, 10) : new Color(0, 0, 0));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, CORNER_RADIUS, CORNER_RADIUS));

                    if (isHovered) {
                        g2d.setColor(new Color(65, 105, 225, 10));
                        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    }

                    super.paintComponent(g2d);
                    g2d.dispose();
                }
            };

            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    // Draw a dark black background for better contrast
                    g2d.setColor(new Color(0, 0, 0));
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    // Load and draw the icon with increased size and better contrast
                    ImageIcon icon = IconLoader.loadIcon(iconName, 35, 35, new Color(65, 105, 225, 255));
                    icon.paintIcon(this, g2d, (getWidth() - icon.getIconWidth()) / 2,
                            (getHeight() - icon.getIconHeight()) / 2);

                    g2d.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(50, 50));
            iconPanel.setBackground(new Color(0, 0, 0));

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(0, 0, 0));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(BUTTON_FONT);

            JLabel descLabel = new JLabel(description);
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(STATUS_FONT);

            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(descLabel);

            card.add(iconPanel, BorderLayout.WEST);
            card.add(contentPanel, BorderLayout.CENTER);

            return card;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private float glowEffect = 0.0f;
                private Timer pulseTimer;
                private Timer glowTimer;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(200, 50));
                    setToolTipText(tooltip);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            startGlowAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
                            startPulseAnimation(false);
                            startGlowAnimation(false);
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

                private void startGlowAnimation(boolean start) {
                    if (glowTimer != null && glowTimer.isRunning()) {
                        glowTimer.stop();
                    }

                    if (start) {
                        glowEffect = 0.0f;
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.min(1.0f, glowEffect + 0.1f);
                            repaint();
                        });
                        glowTimer.start();
                    } else {
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.max(0.0f, glowEffect - 0.1f);
                            if (glowEffect <= 0) {
                                glowTimer.stop();
                            }
                            repaint();
                        });
                        glowTimer.start();
                    }
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    g2d.setColor(isPressed ? BUTTON_HOVER_COLOR : (isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(new Color(70, 70, 70)); // Added visible border
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));

                    // Draw hover effects
                    if (isHovered) {
                        // Glow effect
                        Point2D center = new Point2D.Float(width/2, height/2);
                        float radius = width/2;
                        float[] dist = {0.0f, 1.0f};
                        Color[] colors = {
                                new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 150),
                                new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 0)
                        };
                        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                        g2d.setPaint(gradient);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));

                        // Accent color
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                                isPressed ? 100 : 60
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border with increased opacity
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                                (int)(200 * borderAlpha) // Increased opacity
                        ));
                        g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text with shadow for better visibility
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Enhanced text shadow
                    g2d.setColor(new Color(0, 0, 0, 150)); // Darker shadow
                    g2d.drawString(text, textX + 1, textY + 1);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                    
                    g2d.dispose();
                }
            };
        }

        private JPanel createWifiIconPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));

            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });

            JLabel titleLabel = new JLabel("Wi-Fi Icon Not Visible");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);

            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>This tool will diagnose and fix issues with the Wi-Fi icon in your system tray. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));

            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));

            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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

            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));

            JButton startButton = createStyledButton("Start Diagnosis", "Begin Wi-Fi icon diagnosis", "wifi_icon");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");

                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/WifiIcon.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis script not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);

            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createNoInternetPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });

            JLabel titleLabel = new JLabel("No Internet After Connection");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>This tool will diagnose and fix connectivity issues after establishing a Wi-Fi connection. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));

            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));

            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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

            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));
            
            JButton startButton = createStyledButton("Start Diagnosis", "Begin internet connectivity diagnosis", "no_internet");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/NoInternet.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis script not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            return panel;
        }
    }





    private static class FileSystemRepairPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 255); // Increased opacity for better visibility
        private static final Color CARD_BACKGROUND = new Color(0, 0, 0);
        private static final Color CARD_BORDER = new Color(70, 70, 70); // Added visible border color
        private static final Color TEXT_COLOR = new Color(200, 200, 200);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final int CORNER_RADIUS = 15;

        private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);

        private JPanel mainContainer;
        private JTextArea outputArea;
        private JProgressBar progressBar;
        private JLabel statusLabel;

        public FileSystemRepairPanel() {
            super("Network Diagnostics");
        }

//        public FileSystemRepairPanel (String title) {
//            super(title);
//        }

        public FileSystemRepairPanel(String title) {
            super(title);
        }

        @Override
        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(0, 0, 0));

            // Create main container with card layout
            mainContainer = new JPanel(new CardLayout());
            mainContainer.setOpaque(false);
            mainContainer.setBackground(new Color(0, 0, 0));

            // Create and add panels
            mainContainer.add(createMenuPanel(), "menu");
            mainContainer.add(createWifiIconPanel(), "wifi_icon");
            mainContainer.add(createNoInternetPanel(), "no_internet");

            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(0, 0, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            // Create header with animation
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(new Color(0, 0, 0));

            JLabel titleLabel = new JLabel("Network Troubleshooting");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel subtitleLabel = new JLabel("Select a network issue to diagnose");
            subtitleLabel.setFont(SUBTITLE_FONT);
            subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);

            // Create cards container
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(new Color(0, 0, 0));

            // Add issue cards
            cardsPanel.add(createIssueCard(
                    "Wi-Fi Icon Not Visible",
                    "Diagnose and fix issues with the Wi-Fi icon in the system tray",
                    "wifi.svg",
                    "wifi_icon",
                    mainContainer
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                    "No Internet After Connection",
                    "Troubleshoot connectivity issues after establishing a Wi-Fi connection",
                    "network.svg",
                    "no_internet",
                    mainContainer
            ));

            // Add components to panel
            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(40));
            panel.add(cardsPanel);

            return panel;
        }

        private JPanel createIssueCard(String title, String description, String iconName, String type, JPanel mainContainer) {
            JPanel card = new JPanel() {
                private float opacity = 0.0f;
                private float translateY = 50.0f;
                private boolean isHovered = false;
                private Timer animTimer;

                {
                    setLayout(new BorderLayout(15, 0));
                    setOpaque(false);
                    setMaximumSize(new Dimension(500, 100));
                    setAlignmentX(Component.CENTER_ALIGNMENT);

                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            repaint();
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            repaint();
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            CardLayout cl = (CardLayout) mainContainer.getLayout();
                            cl.show(mainContainer, type);
                        }
                    });
                    setCursor(new Cursor(Cursor.HAND_CURSOR));

                    animTimer = new Timer(16, null);
                    animTimer.addActionListener(e -> {
                        opacity = Math.min(1.0f, opacity + 0.05f);
                        translateY = Math.max(0.0f, translateY - 2.0f);

                        if (opacity >= 1.0f && translateY <= 0) {
                            animTimer.stop();
                        }
                        repaint();
                    });
                    animTimer.start();
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.translate(0, translateY);

                    // Draw card background
                    g2d.setColor(isHovered ? new Color(5, 5, 5) : new Color(0, 0, 0));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));

                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                    g2d.setColor(isHovered ? new Color(10, 10, 10) : new Color(0, 0, 0));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, CORNER_RADIUS, CORNER_RADIUS));

                    if (isHovered) {
                        g2d.setColor(new Color(65, 105, 225, 10));
                        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    }

                    super.paintComponent(g2d);
                    g2d.dispose();
                }
            };

            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    // Draw a dark black background for better contrast
                    g2d.setColor(new Color(0, 0, 0));
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    // Load and draw the icon with increased size and better contrast
                    ImageIcon icon = IconLoader.loadIcon(iconName, 35, 35, new Color(65, 105, 225, 255));
                    icon.paintIcon(this, g2d, (getWidth() - icon.getIconWidth()) / 2,
                            (getHeight() - icon.getIconHeight()) / 2);

                    g2d.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(50, 50));
            iconPanel.setBackground(new Color(0, 0, 0));

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(0, 0, 0));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(BUTTON_FONT);
            
            JLabel descLabel = new JLabel(description);
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(STATUS_FONT);

            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(descLabel);

            card.add(iconPanel, BorderLayout.WEST);
            card.add(contentPanel, BorderLayout.CENTER);

            return card;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private float glowEffect = 0.0f;
                private Timer pulseTimer;
                private Timer glowTimer;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(200, 50));
                    setToolTipText(tooltip);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            startGlowAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
                            startPulseAnimation(false);
                            startGlowAnimation(false);
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

                private void startGlowAnimation(boolean start) {
                    if (glowTimer != null && glowTimer.isRunning()) {
                        glowTimer.stop();
                    }

                    if (start) {
                        glowEffect = 0.0f;
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.min(1.0f, glowEffect + 0.1f);
                            repaint();
                        });
                        glowTimer.start();
                    } else {
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.max(0.0f, glowEffect - 0.1f);
                            if (glowEffect <= 0) {
                                glowTimer.stop();
                            }
                            repaint();
                        });
                        glowTimer.start();
                    }
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    g2d.setColor(isPressed ? BUTTON_HOVER_COLOR : (isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(new Color(70, 70, 70)); // Added visible border
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));

                    // Draw hover effects
                    if (isHovered) {
                        // Glow effect
                        Point2D center = new Point2D.Float(width/2, height/2);
                        float radius = width/2;
                        float[] dist = {0.0f, 1.0f};
                        Color[] colors = {
                                new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 150),
                                new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 0)
                        };
                        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                        g2d.setPaint(gradient);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));

                        // Accent color
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                                isPressed ? 100 : 60
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border with increased opacity
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                                (int)(200 * borderAlpha) // Increased opacity
                        ));
                        g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text with shadow for better visibility
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Enhanced text shadow
                    g2d.setColor(new Color(0, 0, 0, 150)); // Darker shadow
                    g2d.drawString(text, textX + 1, textY + 1);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                    
                    g2d.dispose();
                }
            };
        }

        private JPanel createWifiIconPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });

            JLabel titleLabel = new JLabel("Wi-Fi Icon Not Visible");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);

            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>This tool will diagnose and fix issues with the Wi-Fi icon in your system tray. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));

            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));

            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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

            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));
            
            JButton startButton = createStyledButton("Start Diagnosis", "Begin Wi-Fi icon diagnosis", "wifi_icon");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/WifiIcon.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis script not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);

            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createNoInternetPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));

            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });

            JLabel titleLabel = new JLabel("No Internet After Connection");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);

            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>This tool will diagnose and fix connectivity issues after establishing a Wi-Fi connection. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));

            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));

            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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

            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));

            JButton startButton = createStyledButton("Start Diagnosis", "Begin internet connectivity diagnosis", "no_internet");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");

                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/NoInternet.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis script not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            return panel;
        }
    }



    private static class OtherErrorPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 255);
        private static final Color CARD_BACKGROUND = new Color(0, 0, 0);
        private static final Color CARD_BORDER = new Color(70, 70, 70);
        private static final Color TEXT_COLOR = new Color(200, 200, 200);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final int CORNER_RADIUS = 15;

        private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);

        private JPanel mainContainer;
        private JTextArea outputArea;

        public OtherErrorPanel() {
            super("Core Diagnostics");
        }

        public OtherErrorPanel(String title) {
            super(title);
        }

        @Override
        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(CARD_BACKGROUND);

            mainContainer = new JPanel(new CardLayout());
            mainContainer.setOpaque(false);

            mainContainer.add(createMenuPanel(), "menu");
            mainContainer.add(createWifiIconPanel(), "wifi_icon");
            mainContainer.add(createNoInternetPanel(), "no_internet");
            mainContainer.add(createSlowSpeedPanel(), "slow_speed");
            mainContainer.add(createDNSConfigPanel(), "dns_config");
            
            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(CARD_BACKGROUND);
            panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(CARD_BACKGROUND);

            JLabel titleLabel = new JLabel("System Core Troubleshooting");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel subtitleLabel = new JLabel("Select a network issue to diagnose");
            subtitleLabel.setFont(SUBTITLE_FONT);
            subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);

            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(CARD_BACKGROUND);

            cardsPanel.add(createIssueCard(
                "Wi-Fi Icon Not Visible",
                "Diagnose and fix issues with the Wi-Fi icon in the system tray",
                "wifi.svg",
                "wifi_icon"
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                "No Internet After Connection",
                "Troubleshoot connectivity issues after establishing a Wi-Fi connection",
                "network.svg",
                "no_internet"
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                "Slow Internet Speed",
                "Optimize network performance and troubleshoot bandwidth issues",
                "speed.svg",
                "slow_speed"
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                "DNS Configuration Issues",
                "Resolve domain resolution problems and configure DNS settings",
                "dns.svg",
                "dns_config"
            ));

            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(40));
            panel.add(cardsPanel);

            return panel;
        }

        private JPanel createIssueCard(String title, String description, String iconName, String panelName) {
            JPanel card = new JPanel(new BorderLayout(15, 0)) {
                private float opacity = 0.0f;
                private float translateY = 50.0f;
                private boolean isHovered = false;
                private Timer animTimer;
                
                {
                    setOpaque(false);
                    setMaximumSize(new Dimension(500, 100));
                    setAlignmentX(Component.CENTER_ALIGNMENT);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            repaint();
                        }
                        
                        @Override
                        public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                            cl.show(mainContainer, panelName);
                        }
                    });
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    animTimer = new Timer(16, null);
                    animTimer.addActionListener(e -> {
                        opacity = Math.min(1.0f, opacity + 0.05f);
                        translateY = Math.max(0.0f, translateY - 2.0f);
                        
                        if (opacity >= 1.0f && translateY <= 0) {
                            animTimer.stop();
                        }
                        repaint();
                    });
                    animTimer.start();
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.translate(0, translateY);
                    
                    // Draw card background
                    g2d.setColor(isHovered ? new Color(5, 5, 5) : CARD_BACKGROUND);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, CORNER_RADIUS, CORNER_RADIUS));
                    
                    if (isHovered) {
                        g2d.setColor(new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 10));
                        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    super.paintComponent(g2d);
                    g2d.dispose();
                }
            };

            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // Draw a dark black background for better contrast
                    g2d.setColor(CARD_BACKGROUND);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Load and draw the icon with increased size and better contrast
                    ImageIcon icon = IconLoader.loadIcon(iconName, 35, 35, BUTTON_ACCENT);
                    icon.paintIcon(this, g2d, (getWidth() - icon.getIconWidth()) / 2,
                                            (getHeight() - icon.getIconHeight()) / 2);
                    
                    g2d.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(50, 50));
            iconPanel.setBackground(CARD_BACKGROUND);
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(CARD_BACKGROUND);
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(BUTTON_FONT);
            
            JLabel descLabel = new JLabel(description);
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(STATUS_FONT);
            
            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(descLabel);
            
            card.add(iconPanel, BorderLayout.WEST);
            card.add(contentPanel, BorderLayout.CENTER);
            
            return card;
        }

        private JPanel createCommonHeader(String title) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(CARD_BACKGROUND);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            return headerPanel;
        }

        private JPanel createCommonContent(String description, String scriptName) {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            contentPanel.setBackground(CARD_BACKGROUND);
            
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(CARD_BACKGROUND);
            
            JLabel descriptionLabel = new JLabel(description);
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));
            
            outputArea = createOutputArea();
            JScrollPane scrollPane = createScrollPane(outputArea);
            JPanel controlPanel = createControlPanel(scriptName);
            
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            return contentPanel;
        }

        private JTextArea createOutputArea() {
            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setBackground(CARD_BACKGROUND);
            area.setForeground(TEXT_COLOR);
            area.setFont(STATUS_FONT);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            return area;
        }

        private JScrollPane createScrollPane(JTextArea area) {
            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(CARD_BACKGROUND);
            
            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = CARD_BACKGROUND;
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
            
            return scrollPane;
        }

        private JPanel createControlPanel(String scriptName) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panel.setBackground(CARD_BACKGROUND);
            
            JButton startButton = createStyledButton("Start Diagnosis", "Begin diagnosis", scriptName);
            startButton.addActionListener(e -> executeDiagnosisScript(startButton, outputArea, scriptName));
            
            panel.add(startButton);
            return panel;
        }

        private void executeDiagnosisScript(JButton button, JTextArea output, String scriptName) {
            button.setText("Diagnosing...");
            button.setEnabled(false);
            output.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                        File batFile = new File("Bat_Scripts/" + scriptName + ".bat");
                            if (!batFile.exists()) {
                            publish("Error: Diagnosis script not found.");
                                return null;
                            }

                        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                        pb.redirectErrorStream(true);
                        Process process = pb.start();
                        
                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                        );
                        
                            String line;
                            while ((line = reader.readLine()) != null) {
                                publish(line);
                            }
                            process.waitFor();
                        } catch (Exception ex) {
                        publish("Error: " + ex.getMessage());
                        }
                        return null;
                    }

                    @Override
                protected void process(List<String> chunks) {
                    chunks.forEach(line -> output.append(line + "\n"));
                    output.setCaretPosition(output.getDocument().getLength());
                    }

                    @Override
                    protected void done() {
                    button.setText("Start Diagnosis");
                    button.setEnabled(true);
                    }
                };
                worker.execute();
        }

        // Panel-specific creation methods
        private JPanel createWifiIconPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("Wi-Fi Icon Not Visible"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "This tool will diagnose and fix issues with the Wi-Fi icon in your system tray." +
                "</div></html>", 
                "WifiIcon"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createNoInternetPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("No Internet After Connection"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "Troubleshoot connectivity issues after establishing a Wi-Fi connection." +
                "</div></html>", 
                "NoInternet"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createSlowSpeedPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("Slow Internet Speed"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "Optimize network performance and troubleshoot bandwidth issues." +
                "</div></html>", 
                "SlowSpeed"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createDNSConfigPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("DNS Configuration Issues"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "Resolve domain resolution problems and configure DNS settings." +
                "</div></html>", 
                "DNSConfig"
            ), BorderLayout.CENTER);
            return panel;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private float glowEffect = 0.0f;
                private Timer pulseTimer;
                private Timer glowTimer;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(200, 50));
                    setToolTipText(tooltip);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            startGlowAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
                            startPulseAnimation(false);
                            startGlowAnimation(false);
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
                
                private void startGlowAnimation(boolean start) {
                    if (glowTimer != null && glowTimer.isRunning()) {
                        glowTimer.stop();
                    }
                    
                    if (start) {
                        glowEffect = 0.0f;
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.min(1.0f, glowEffect + 0.1f);
                            repaint();
                        });
                        glowTimer.start();
                    } else {
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.max(0.0f, glowEffect - 0.1f);
                            if (glowEffect <= 0) {
                                glowTimer.stop();
                            }
                            repaint();
                        });
                        glowTimer.start();
                    }
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    g2d.setColor(isPressed ? BUTTON_HOVER_COLOR : (isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw hover effects
                    if (isHovered) {
                        // Glow effect
                        Point2D center = new Point2D.Float(width/2, height/2);
                        float radius = width/2;
                        float[] dist = {0.0f, 1.0f};
                        Color[] colors = {
                            new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 150),
                            new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 0)
                        };
                        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                        g2d.setPaint(gradient);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Accent color
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            isPressed ? 100 : 60
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            (int)(200 * borderAlpha)
                        ));
                        g2d.setStroke(new BasicStroke(2.0f));
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text with shadow
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Enhanced text shadow
                    g2d.setColor(new Color(0, 0, 0, 150));
                    g2d.drawString(text, textX + 1, textY + 1);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                    
                    g2d.dispose();
                }
            };
        }
    }






    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------





    private static class WindowsIssuesPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 255); // Increased opacity for better visibility
        private static final Color CARD_BACKGROUND = new Color(0, 0, 0);
        private static final Color CARD_BORDER = new Color(70, 70, 70); // Added visible border color
        private static final Color TEXT_COLOR = new Color(200, 200, 200);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final int CORNER_RADIUS = 15;

        private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);

        private JPanel mainContainer;
        private JTextArea outputArea;
        private JProgressBar progressBar;
        private JLabel statusLabel;

        public WindowsIssuesPanel() {
            super("Windows Diagnostics");
        }

        public WindowsIssuesPanel(String title) {
            super(title);
        }


        @Override
        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(0, 0, 0));

            // Create main container with card layout
            mainContainer = new JPanel(new CardLayout());
            mainContainer.setOpaque(false);
            mainContainer.setBackground(new Color(0, 0, 0));

            // Create and add panels
            mainContainer.add(createMenuPanel(), "menu");
            mainContainer.add(createWifiIconPanel(), "wifi_icon");
            mainContainer.add(createNoInternetPanel(), "no_internet");

            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(0, 0, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            // Create header with animation
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(new Color(0, 0, 0));

            JLabel titleLabel = new JLabel("Network Troubleshooting");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel subtitleLabel = new JLabel("Select a network issue to diagnose");
            subtitleLabel.setFont(SUBTITLE_FONT);
            subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);

            // Create cards container
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(new Color(0, 0, 0));

            // Add issue cards
            cardsPanel.add(createIssueCard(
                    "Wi-Fi Icon Not Visible",
                    "Diagnose and fix issues with the Wi-Fi icon in the system tray",
                    "wifi.svg",
                    "wifi_icon",
                    mainContainer
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                    "No Internet After Connection",
                    "Troubleshoot connectivity issues after establishing a Wi-Fi connection",
                    "network.svg",
                    "no_internet",
                    mainContainer
            ));

            // Add components to panel
            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(40));
            panel.add(cardsPanel);

            return panel;
        }

        private JPanel createIssueCard(String title, String description, String iconName, String type, JPanel mainContainer) {
            JPanel card = new JPanel() {
                private float opacity = 0.0f;
                private float translateY = 50.0f;
                private boolean isHovered = false;
                private Timer animTimer;

                {
                    setLayout(new BorderLayout(15, 0));
                    setOpaque(false);
                    setMaximumSize(new Dimension(500, 100));
                    setAlignmentX(Component.CENTER_ALIGNMENT);

                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            repaint();
                        }

                    @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            repaint();
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            CardLayout cl = (CardLayout) mainContainer.getLayout();
                            cl.show(mainContainer, type);
                        }
                    });
                    setCursor(new Cursor(Cursor.HAND_CURSOR));

                    animTimer = new Timer(16, null);
                    animTimer.addActionListener(e -> {
                        opacity = Math.min(1.0f, opacity + 0.05f);
                        translateY = Math.max(0.0f, translateY - 2.0f);

                        if (opacity >= 1.0f && translateY <= 0) {
                            animTimer.stop();
                        }
                        repaint();
                    });
                    animTimer.start();
                    }

                    @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.translate(0, translateY);

                    // Draw card background
                    g2d.setColor(isHovered ? new Color(5, 5, 5) : new Color(0, 0, 0));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));

                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                    g2d.setColor(isHovered ? new Color(10, 10, 10) : new Color(0, 0, 0));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, CORNER_RADIUS, CORNER_RADIUS));

                    if (isHovered) {
                        g2d.setColor(new Color(65, 105, 225, 10));
                        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    }

                    super.paintComponent(g2d);
                    g2d.dispose();
                }
            };

            JPanel iconPanel = new JPanel() {
                    @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    // Draw a dark black background for better contrast
                    g2d.setColor(new Color(0, 0, 0));
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    // Load and draw the icon with increased size and better contrast
                    ImageIcon icon = IconLoader.loadIcon(iconName, 35, 35, new Color(65, 105, 225, 255));
                    icon.paintIcon(this, g2d, (getWidth() - icon.getIconWidth()) / 2,
                            (getHeight() - icon.getIconHeight()) / 2);

                    g2d.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(50, 50));
            iconPanel.setBackground(new Color(0, 0, 0));

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(0, 0, 0));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(BUTTON_FONT);

            JLabel descLabel = new JLabel(description);
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(STATUS_FONT);

            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(descLabel);

            card.add(iconPanel, BorderLayout.WEST);
            card.add(contentPanel, BorderLayout.CENTER);

            return card;
        }
        
        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private float glowEffect = 0.0f;
                private Timer pulseTimer;
                private Timer glowTimer;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(200, 50));
                    setToolTipText(tooltip);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            startGlowAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
                            startPulseAnimation(false);
                            startGlowAnimation(false);
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

                private void startGlowAnimation(boolean start) {
                    if (glowTimer != null && glowTimer.isRunning()) {
                        glowTimer.stop();
                    }

                    if (start) {
                        glowEffect = 0.0f;
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.min(1.0f, glowEffect + 0.1f);
                            repaint();
                        });
                        glowTimer.start();
                    } else {
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.max(0.0f, glowEffect - 0.1f);
                            if (glowEffect <= 0) {
                                glowTimer.stop();
                            }
                            repaint();
                        });
                        glowTimer.start();
                    }
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    g2d.setColor(isPressed ? BUTTON_HOVER_COLOR : (isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(new Color(70, 70, 70)); // Added visible border
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));

                    // Draw hover effects
                    if (isHovered) {
                        // Glow effect
                        Point2D center = new Point2D.Float(width/2, height/2);
                        float radius = width/2;
                        float[] dist = {0.0f, 1.0f};
                        Color[] colors = {
                                new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 150),
                                new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 0)
                        };
                        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                        g2d.setPaint(gradient);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));

                        // Accent color
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                                isPressed ? 100 : 60
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border with increased opacity
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                                (int)(200 * borderAlpha) // Increased opacity
                        ));
                        g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text with shadow for better visibility
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Enhanced text shadow
                    g2d.setColor(new Color(0, 0, 0, 150)); // Darker shadow
                    g2d.drawString(text, textX + 1, textY + 1);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                    
                    g2d.dispose();
                }
            };
        }

        private JPanel createWifiIconPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));

            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JButton backButton = createStyledButton(" ← ", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });

            JLabel titleLabel = new JLabel("Windows update issues");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);

            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>This tool will diagnose and fix issues with windows. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));

            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));

            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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

            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));

            JButton startButton = createStyledButton("Start Fix", "Begin update diagnosis", "wifi_icon");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");

                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/FixUpdateAndNetworkIssues.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis tool not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);

            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createNoInternetPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));
            
            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JButton backButton = createStyledButton(" ← ", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });

            JLabel titleLabel = new JLabel("BSOD (Blue Screen of Death)");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>This tool will diagnose and fix the continuous hanging and restarting your pc. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));

            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));

            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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

            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));
            
            JButton startButton = createStyledButton("Start Diagnosis", "Begin BSOD diagnosis", "no_internet");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/FixBSOD.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis tool not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);
            
            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            return panel;
        }
    }




//-----------------------------------------------------------------------------------------------------------------------------------------------------------------



    private static class AudioTroubleshootingPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 255); // Increased opacity for better visibility
        private static final Color CARD_BACKGROUND = new Color(0, 0, 0);
        private static final Color CARD_BORDER = new Color(70, 70, 70); // Added visible border color
        private static final Color TEXT_COLOR = new Color(200, 200, 200);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final int CORNER_RADIUS = 15;

        private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);

        private JPanel mainContainer;
        private JTextArea outputArea;
        private JProgressBar progressBar;
        private JLabel statusLabel;

        public AudioTroubleshootingPanel() {
            super("Audio & Microphone Diagnostics");
        }

        public AudioTroubleshootingPanel(String title) {
            super(title);
        }

        @Override
        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(0, 0, 0));

            // Create main container with card layout
            mainContainer = new JPanel(new CardLayout());
            mainContainer.setOpaque(false);
            mainContainer.setBackground(new Color(0, 0, 0));

            // Create and add panels
            mainContainer.add(createMenuPanel(), "menu");
            mainContainer.add(createWifiIconPanel(), "wifi_icon");
            mainContainer.add(createNoInternetPanel(), "no_internet");

            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(0, 0, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            // Create header with animation
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(new Color(0, 0, 0));

            JLabel titleLabel = new JLabel("Audio & Microphone Troubleshooting");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel subtitleLabel = new JLabel("Select an issue to diagnose");
            subtitleLabel.setFont(SUBTITLE_FONT);
            subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);

            // Create cards container
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(new Color(0, 0, 0));

            // Add issue cards
            cardsPanel.add(createIssueCard(
                    "Audio issue",
                    "Diagnose and fix issues with the audio output",
                    "wifi.svg",
                    "wifi_icon",
                    mainContainer
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                    "Microphone issue",
                    "Troubleshoot microphone privacy setting error",
                    "network.svg",
                    "no_internet",
                    mainContainer
            ));

            // Add components to panel
            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(40));
            panel.add(cardsPanel);

            return panel;
        }

        private JPanel createIssueCard(String title, String description, String iconName, String type, JPanel mainContainer) {
            JPanel card = new JPanel() {
                private float opacity = 0.0f;
                private float translateY = 50.0f;
                private boolean isHovered = false;
                private Timer animTimer;

                {
                    setLayout(new BorderLayout(15, 0));
                    setOpaque(false);
                    setMaximumSize(new Dimension(500, 100));
                    setAlignmentX(Component.CENTER_ALIGNMENT);

                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            repaint();
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            repaint();
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            CardLayout cl = (CardLayout) mainContainer.getLayout();
                            cl.show(mainContainer, type);
                        }
                    });
                    setCursor(new Cursor(Cursor.HAND_CURSOR));

                    animTimer = new Timer(16, null);
                    animTimer.addActionListener(e -> {
                        opacity = Math.min(1.0f, opacity + 0.05f);
                        translateY = Math.max(0.0f, translateY - 2.0f);

                        if (opacity >= 1.0f && translateY <= 0) {
                            animTimer.stop();
                        }
                        repaint();
                    });
                    animTimer.start();
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.translate(0, translateY);

                    // Draw card background
                    g2d.setColor(isHovered ? new Color(5, 5, 5) : new Color(0, 0, 0));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));

                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                    g2d.setColor(isHovered ? new Color(10, 10, 10) : new Color(0, 0, 0));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, CORNER_RADIUS, CORNER_RADIUS));

                    if (isHovered) {
                        g2d.setColor(new Color(65, 105, 225, 10));
                        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    }

                    super.paintComponent(g2d);
                    g2d.dispose();
                }
            };

            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    // Draw a dark black background for better contrast
                    g2d.setColor(new Color(0, 0, 0));
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    // Load and draw the icon with increased size and better contrast
                    ImageIcon icon = IconLoader.loadIcon(iconName, 35, 35, new Color(65, 105, 225, 255));
                    icon.paintIcon(this, g2d, (getWidth() - icon.getIconWidth()) / 2,
                            (getHeight() - icon.getIconHeight()) / 2);

                    g2d.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(50, 50));
            iconPanel.setBackground(new Color(0, 0, 0));

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(0, 0, 0));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(BUTTON_FONT);

            JLabel descLabel = new JLabel(description);
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(STATUS_FONT);

            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(descLabel);

            card.add(iconPanel, BorderLayout.WEST);
            card.add(contentPanel, BorderLayout.CENTER);

            return card;
        }

        private JButton createStyledButton(String text, String tooltip) {
            return new JButton(text) {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private float glowEffect = 0.0f;
                private Timer pulseTimer;
                private Timer glowTimer;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(200, 50));
                    setToolTipText(tooltip);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            startGlowAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
                            startPulseAnimation(false);
                            startGlowAnimation(false);
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

                private void startGlowAnimation(boolean start) {
                    if (glowTimer != null && glowTimer.isRunning()) {
                        glowTimer.stop();
                    }

                    if (start) {
                        glowEffect = 0.0f;
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.min(1.0f, glowEffect + 0.1f);
                            repaint();
                        });
                        glowTimer.start();
                    } else {
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.max(0.0f, glowEffect - 0.1f);
                            if (glowEffect <= 0) {
                                glowTimer.stop();
                            }
                            repaint();
                        });
                        glowTimer.start();
                    }
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    g2d.setColor(isPressed ? BUTTON_HOVER_COLOR : (isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(new Color(70, 70, 70)); // Added visible border
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));

                    // Draw hover effects
                    if (isHovered) {
                        // Glow effect
                        Point2D center = new Point2D.Float(width/2, height/2);
                        float radius = width/2;
                        float[] dist = {0.0f, 1.0f};
                        Color[] colors = {
                                new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 150),
                                new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 0)
                        };
                        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                        g2d.setPaint(gradient);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));

                        // Accent color
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                                isPressed ? 100 : 60
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border with increased opacity
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                                (int)(200 * borderAlpha) // Increased opacity
                        ));
                        g2d.setStroke(new BasicStroke(2.0f)); // Increased border width
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text with shadow for better visibility
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Enhanced text shadow
                    g2d.setColor(new Color(0, 0, 0, 150)); // Darker shadow
                    g2d.drawString(text, textX + 1, textY + 1);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                    
                    g2d.dispose();
                }
            };
        }

        private JPanel createWifiIconPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));

            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JButton backButton = createStyledButton(" ← ", "Return to menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });

            JLabel titleLabel = new JLabel("Audio issues");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);

            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>Diagnose and fix issues with the audio output. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));

            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));

            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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

            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));

            JButton startButton = createStyledButton("Start Fix", "Begin Audio diagnosis");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");

                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/audio.bat");
                            if (!batFile.exists()) {
                                publish("Error: tool not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Fix");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);

            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createNoInternetPanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(new Color(0, 0, 0));

            // Create header with back button
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JButton backButton = createStyledButton(" ← ", "Return to menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });

            JLabel titleLabel = new JLabel("Microphone related issues");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);

            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // Create content panel with improved styling
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setBackground(new Color(0, 0, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            // Create description panel
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(new Color(0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>Troubleshoot microphone privacy setting error. The process may take a few minutes to complete.</div></html>");
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));

            // Create output area with improved styling
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setBackground(new Color(0, 0, 0));
            outputArea.setForeground(TEXT_COLOR);
            outputArea.setFont(STATUS_FONT);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(0, 0, 0));

            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = new Color(0, 0, 0);
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

            // Create control panel with improved styling
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            controlPanel.setBackground(new Color(0, 0, 0));

            JButton startButton = createStyledButton("Start Fix", "Begin Troubleshoot microphone privacy setting error");
            startButton.addActionListener(e -> {
                startButton.setText("Diagnosing...");
                startButton.setEnabled(false);
                outputArea.setText("");

                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/NetworkFix.bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis script not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
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
                        startButton.setText("Start Diagnosis");
                        startButton.setEnabled(true);
                    }
                };
                worker.execute();
            });

            controlPanel.add(startButton);

            // Add components to panel with proper layout
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.CENTER);
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);

            return panel;
        }
    }


    abstract static class BaseSidebarPanel extends JPanel {
        protected static final Color PANEL_BACKGROUND = new Color(0, 0, 0);
        protected static final Color TEXT_COLOR = new Color(255, 255, 255);
        protected static final Color SECONDARY_TEXT_COLOR = new Color(170, 170, 170);
        protected static final Color BUTTON_COLOR = new Color(0, 0, 0);
        protected static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        
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
            setBackground(new Color(0, 0, 0));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            add(createHeader(title), BorderLayout.NORTH);
            
            // Create a container for content that will be animated
            contentContainer = new JPanel(new BorderLayout());
            contentContainer.setOpaque(false);
            contentContainer.setBackground(new Color(0, 0, 0));
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
                    g2d.setColor(new Color(0, 0, 0));
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
            headerPanel.setBackground(new Color(0, 0, 0));
            headerPanel.setOpaque(true);
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

    private static class BluetoothPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 255);
        private static final Color CARD_BACKGROUND = new Color(0, 0, 0);
        private static final Color CARD_BORDER = new Color(70, 70, 70);
        private static final Color TEXT_COLOR = new Color(200, 200, 200);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final int CORNER_RADIUS = 15;

        private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);

        private JPanel mainContainer;
        private JTextArea outputArea;

        public BluetoothPanel() {
            super("Bluetooth Troubleshooting");
        }

        public BluetoothPanel(String title) {
            super(title);
        }

        @Override
        protected JPanel createContentPanel() {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(CARD_BACKGROUND);

            mainContainer = new JPanel(new CardLayout());
            mainContainer.setOpaque(false);

            mainContainer.add(createMenuPanel(), "menu");
            mainContainer.add(createBluetoothNotWorkingPanel(), "bluetooth_not_working");
            mainContainer.add(createDeviceNotShowingPanel(), "device_not_showing");
            mainContainer.add(createConnectionIssuesPanel(), "connection_issues");

            contentPanel.add(mainContainer, BorderLayout.CENTER);
            return contentPanel;
        }

        private JPanel createMenuPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(CARD_BACKGROUND);
            panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(CARD_BACKGROUND);

            JLabel titleLabel = new JLabel("Bluetooth Troubleshooting");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel subtitleLabel = new JLabel("Select a Bluetooth issue to diagnose");
            subtitleLabel.setFont(SUBTITLE_FONT);
            subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);

            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(CARD_BACKGROUND);

            cardsPanel.add(createIssueCard(
                "Bluetooth Not Working",
                "Diagnose and fix issues with Bluetooth functionality",
                "bluetooth.svg",
                "bluetooth_not_working"
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                "Device Not Showing",
                "Troubleshoot issues with Bluetooth device discovery",
                "device.svg",
                "device_not_showing"
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                "Connection Issues",
                "Resolve problems with Bluetooth device connections",
                "connection.svg",
                "connection_issues"
            ));

            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(40));
            panel.add(cardsPanel);

            return panel;
        }

        private JPanel createIssueCard(String title, String description, String iconName, String panelName) {
            JPanel card = new JPanel(new BorderLayout(15, 0)) {
                private float opacity = 0.0f;
                private float translateY = 50.0f;
                private boolean isHovered = false;
                private Timer animTimer;
                
                {
                    setOpaque(false);
                    setMaximumSize(new Dimension(500, 100));
                    setAlignmentX(Component.CENTER_ALIGNMENT);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            repaint();
                        }
                        
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            CardLayout cl = (CardLayout) mainContainer.getLayout();
                            cl.show(mainContainer, panelName);
                        }
                    });
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    animTimer = new Timer(16, null);
                    animTimer.addActionListener(e -> {
                        opacity = Math.min(1.0f, opacity + 0.05f);
                        translateY = Math.max(0.0f, translateY - 2.0f);
                        
                        if (opacity >= 1.0f && translateY <= 0) {
                            animTimer.stop();
                        }
                        repaint();
                    });
                    animTimer.start();
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.translate(0, translateY);
                    
                    // Draw card background
                    g2d.setColor(isHovered ? new Color(5, 5, 5) : CARD_BACKGROUND);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, CORNER_RADIUS, CORNER_RADIUS));
                    
                    if (isHovered) {
                        g2d.setColor(new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 10));
                        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    super.paintComponent(g2d);
                    g2d.dispose();
                }
            };

            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // Draw a dark black background for better contrast
                    g2d.setColor(CARD_BACKGROUND);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Load and draw the icon with increased size and better contrast
                    ImageIcon icon = IconLoader.loadIcon(iconName, 35, 35, BUTTON_ACCENT);
                    icon.paintIcon(this, g2d, (getWidth() - icon.getIconWidth()) / 2,
                                            (getHeight() - icon.getIconHeight()) / 2);
                    
                    g2d.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(50, 50));
            iconPanel.setBackground(CARD_BACKGROUND);
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(CARD_BACKGROUND);
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(BUTTON_FONT);
            
            JLabel descLabel = new JLabel(description);
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(STATUS_FONT);
            
            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(descLabel);
            
            card.add(iconPanel, BorderLayout.WEST);
            card.add(contentPanel, BorderLayout.CENTER);
            
            return card;
        }

        private JPanel createCommonHeader(String title) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(CARD_BACKGROUND);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            return headerPanel;
        }

        private JPanel createCommonContent(String description, String scriptName) {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            contentPanel.setBackground(CARD_BACKGROUND);
            
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(CARD_BACKGROUND);
            
            JLabel descriptionLabel = new JLabel(description);
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));
            
            outputArea = createOutputArea();
            JScrollPane scrollPane = createScrollPane(outputArea);
            JPanel controlPanel = createControlPanel(scriptName);
            
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            return contentPanel;
        }

        private JTextArea createOutputArea() {
            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setBackground(CARD_BACKGROUND);
            area.setForeground(TEXT_COLOR);
            area.setFont(STATUS_FONT);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            return area;
        }

        private JScrollPane createScrollPane(JTextArea area) {
            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(CARD_BACKGROUND);
            
            // Apply custom scrollbar UI
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 80);
                    this.trackColor = CARD_BACKGROUND;
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
            
            return scrollPane;
        }

        private JPanel createControlPanel(String scriptName) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panel.setBackground(CARD_BACKGROUND);
            
            JButton startButton = createStyledButton("Start Diagnosis", "Begin diagnosis", scriptName);
            startButton.addActionListener(e -> executeDiagnosisScript(startButton, outputArea, scriptName));
            
            panel.add(startButton);
            return panel;
        }

        private void executeDiagnosisScript(JButton button, JTextArea output, String scriptName) {
            button.setText("Diagnosing...");
            button.setEnabled(false);
            output.setText("");
            
            SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        File batFile = new File("Bat_Scripts/" + scriptName + ".bat");
                        if (!batFile.exists()) {
                            publish("Error: Diagnosis script not found.");
                            return null;
                        }
                        
                        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                        pb.redirectErrorStream(true);
                        Process process = pb.start();
                        
                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                        );
                        
                        String line;
                        while ((line = reader.readLine()) != null) {
                            publish(line);
                        }
                        process.waitFor();
                    } catch (Exception ex) {
                        publish("Error: " + ex.getMessage());
                    }
                    return null;
                }
                
                @Override
                protected void process(List<String> chunks) {
                    chunks.forEach(line -> output.append(line + "\n"));
                    output.setCaretPosition(output.getDocument().getLength());
                }
                
                @Override
                protected void done() {
                    button.setText("Start Diagnosis");
                    button.setEnabled(true);
                }
            };
            worker.execute();
        }

        // Panel-specific creation methods
        private JPanel createBluetoothNotWorkingPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("Bluetooth Not Working"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "This tool will diagnose and fix issues with Bluetooth functionality." +
                "</div></html>", 
                "BluetoothNotWorking"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createDeviceNotShowingPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("Device Not Showing"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "Troubleshoot issues with Bluetooth device discovery." +
                "</div></html>", 
                "DeviceNotShowing"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createConnectionIssuesPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("Connection Issues"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "Resolve problems with Bluetooth device connections." +
                "</div></html>", 
                "ConnectionIssues"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private float glowEffect = 0.0f;
                private Timer pulseTimer;
                private Timer glowTimer;
                
                {
                    setContentAreaFilled(false);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setPreferredSize(new Dimension(200, 50));
                    setToolTipText(tooltip);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            startGlowAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            isPressed = false;
                            startPulseAnimation(false);
                            startGlowAnimation(false);
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
                
                private void startGlowAnimation(boolean start) {
                    if (glowTimer != null && glowTimer.isRunning()) {
                        glowTimer.stop();
                    }
                    
                    if (start) {
                        glowEffect = 0.0f;
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.min(1.0f, glowEffect + 0.1f);
                            repaint();
                        });
                        glowTimer.start();
                    } else {
                        glowTimer = new Timer(16, e -> {
                            glowEffect = Math.max(0.0f, glowEffect - 0.1f);
                            if (glowEffect <= 0) {
                                glowTimer.stop();
                            }
                            repaint();
                        });
                        glowTimer.start();
                    }
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    g2d.setColor(isPressed ? BUTTON_HOVER_COLOR : (isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw permanent border
                    g2d.setColor(CARD_BORDER);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    
                    // Draw hover effects
                    if (isHovered) {
                        // Glow effect
                        Point2D center = new Point2D.Float(width/2, height/2);
                        float radius = width/2;
                        float[] dist = {0.0f, 1.0f};
                        Color[] colors = {
                            new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 150),
                            new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), BUTTON_ACCENT.getBlue(), 0)
                        };
                        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                        g2d.setPaint(gradient);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Accent color
                        Color accentColor = new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            isPressed ? 100 : 60
                        );
                        g2d.setColor(accentColor);
                        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
                        
                        // Animated border
                        float borderAlpha = 0.6f + 0.4f * (float)Math.sin(pulseEffect);
                        g2d.setColor(new Color(
                            BUTTON_ACCENT.getRed(),
                            BUTTON_ACCENT.getGreen(),
                            BUTTON_ACCENT.getBlue(),
                            (int)(200 * borderAlpha)
                        ));
                        g2d.setStroke(new BasicStroke(2.0f));
                        g2d.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, CORNER_RADIUS, CORNER_RADIUS));
                    }
                    
                    // Apply press effect
                    if (isPressed) {
                        g2d.translate(1, 1);
                    }
                    
                    // Draw text with shadow
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height - textHeight) / 2 + fm.getAscent();
                    
                    // Enhanced text shadow
                    g2d.setColor(new Color(0, 0, 0, 150));
                    g2d.drawString(text, textX + 1, textY + 1);
                    
                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                    
                    g2d.dispose();
                }
            };
        }
    }

    private static class BrowserPanel extends BaseSidebarPanel {
        private static final Color BUTTON_COLOR = new Color(0, 0, 0);
        private static final Color BUTTON_HOVER_COLOR = new Color(5, 5, 5);
        private static final Color BUTTON_ACCENT = new Color(65, 105, 225, 255);
        private static final Color CARD_BACKGROUND = new Color(0, 0, 0);
        private static final Color CARD_BORDER = new Color(70, 70, 70);
        private static final Color TEXT_COLOR = new Color(200, 200, 200);
        private static final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
        private static final int CORNER_RADIUS = 15;

        private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
        private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
        private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);

        private JPanel mainContainer;
        private JTextArea outputArea;

        public BrowserPanel() {
            super("Browser Diagnostics");
        }

        public BrowserPanel(String title) {
            super(title);
        }

        @Override
        protected JPanel createContentPanel() {
            mainContainer = new JPanel(new CardLayout());
            mainContainer.setBackground(CARD_BACKGROUND);
            mainContainer.add(createMenuPanel(), "menu");
            mainContainer.add(createSlowBrowserPanel(), "slow_browser");
            mainContainer.add(createCrashesPanel(), "crashes");
            mainContainer.add(createExtensionsPanel(), "extensions");
            return mainContainer;
        }

        private JPanel createMenuPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(CARD_BACKGROUND);
            panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(CARD_BACKGROUND);

            JLabel titleLabel = new JLabel("Browser Troubleshooting");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel subtitleLabel = new JLabel("Select a browser issue to diagnose");
            subtitleLabel.setFont(SUBTITLE_FONT);
            subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);

            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(CARD_BACKGROUND);

            cardsPanel.add(createIssueCard(
                "Slow Browser Performance",
                "Diagnose and optimize browser speed issues",
                "browser.svg",
                "slow_browser"
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                "Frequent Crashes",
                "Troubleshoot browser stability problems",
                "browser.svg",
                "crashes"
            ));
            cardsPanel.add(Box.createVerticalStrut(20));
            cardsPanel.add(createIssueCard(
                "Extension Problems",
                "Fix issues with browser extensions",
                "browser.svg",
                "extensions"
            ));

            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(30));
            panel.add(cardsPanel);
            
            return panel;
        }

        private JPanel createIssueCard(String title, String description, String iconName, String panelName) {
            JPanel card = new JPanel(new BorderLayout(15, 0));
            card.setBackground(CARD_BACKGROUND);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    g2d.setColor(new Color(0, 0, 0));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    ImageIcon icon = IconLoader.loadIcon(iconName, 35, 35, BUTTON_ACCENT);
                    icon.paintIcon(this, g2d, (getWidth() - icon.getIconWidth()) / 2,
                                            (getHeight() - icon.getIconHeight()) / 2);
                    
                    g2d.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(50, 50));
            iconPanel.setBackground(new Color(0, 0, 0));
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(0, 0, 0));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(BUTTON_FONT);
            
            JLabel descLabel = new JLabel(description);
            descLabel.setForeground(SECONDARY_TEXT_COLOR);
            descLabel.setFont(STATUS_FONT);
            
            contentPanel.add(titleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(descLabel);
            
            card.add(iconPanel, BorderLayout.WEST);
            card.add(contentPanel, BorderLayout.CENTER);
            
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BUTTON_ACCENT, 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    ));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CARD_BORDER, 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    ));
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    CardLayout cl = (CardLayout) mainContainer.getLayout();
                    cl.show(mainContainer, panelName);
                }
            });
            
            return card;
        }

        private JPanel createCommonHeader(String title) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(CARD_BACKGROUND);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JButton backButton = createStyledButton("← Back", "Return to menu", "menu");
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContainer.getLayout();
                cl.show(mainContainer, "menu");
            });
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(TITLE_FONT);
            
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            return headerPanel;
        }

        private JPanel createCommonContent(String description, String scriptName) {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            contentPanel.setBackground(CARD_BACKGROUND);
            
            JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
            descriptionPanel.setBackground(CARD_BACKGROUND);
            
            JLabel descriptionLabel = new JLabel(description);
            descriptionLabel.setForeground(SECONDARY_TEXT_COLOR);
            descriptionLabel.setFont(SUBTITLE_FONT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(Box.createVerticalStrut(20));
            
            outputArea = createOutputArea();
            JScrollPane scrollPane = createScrollPane(outputArea);
            JPanel controlPanel = createControlPanel(scriptName);
            
            contentPanel.add(descriptionPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(controlPanel, BorderLayout.SOUTH);
            
            return contentPanel;
        }

        private JTextArea createOutputArea() {
            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setBackground(CARD_BACKGROUND);
            area.setForeground(TEXT_COLOR);
            area.setFont(STATUS_FONT);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            return area;
        }

        private JScrollPane createScrollPane(JTextArea area) {
            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(CARD_BACKGROUND);
            scrollPane.getViewport().setBackground(CARD_BACKGROUND);
            
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(70, 70, 70);
                    this.trackColor = new Color(0, 0, 0);
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
                    button.setMaximumSize(new Dimension(0, 0));
                    return button;
                }
                
                @Override
                protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                    if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                        return;
                    }
                    
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(thumbColor);
                    g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 5, 5);
                    
                    g2d.dispose();
                }
            });
            
            return scrollPane;
        }

        private JPanel createControlPanel(String scriptName) {
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
            controlPanel.setBackground(CARD_BACKGROUND);
            
            JButton startButton = createStyledButton("Start Diagnosis", "Begin the diagnosis process", "diagnose");
            executeDiagnosisScript(startButton, outputArea, scriptName);
            
            controlPanel.add(Box.createHorizontalGlue());
            controlPanel.add(startButton);
            controlPanel.add(Box.createHorizontalGlue());
            
            return controlPanel;
        }

        private void executeDiagnosisScript(JButton button, JTextArea output, String scriptName) {
            button.addActionListener(e -> {
                button.setText("Diagnosing...");
                button.setEnabled(false);
                output.setText("");

                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            File batFile = new File("Bat_Scripts/" + scriptName + ".bat");
                            if (!batFile.exists()) {
                                publish("Error: Diagnosis tool not found.");
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
                            publish("Error: Couldn't complete diagnosis.");
                        }
                        return null;
                    }

                    @Override
                    protected void process(List<String> chunks) {
                        for (String chunk : chunks) {
                            output.append(chunk + "\n");
                            output.setCaretPosition(output.getDocument().getLength());
                        }
                    }

                    @Override
                    protected void done() {
                        button.setText("Start Diagnosis");
                        button.setEnabled(true);
                    }
                };
                worker.execute();
            });
        }

        private JPanel createSlowBrowserPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("Slow Browser Performance"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "This tool will diagnose and optimize your browser's performance." +
                "</div></html>", 
                "SlowBrowser"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createCrashesPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("Frequent Crashes"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "Troubleshoot and fix browser stability issues." +
                "</div></html>", 
                "BrowserCrashes"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createExtensionsPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(createCommonHeader("Extension Problems"), BorderLayout.NORTH);
            panel.add(createCommonContent(
                "<html><div style='text-align: center; width: 400px;'>" +
                "Diagnose and resolve issues with browser extensions." +
                "</div></html>", 
                "BrowserExtensions"
            ), BorderLayout.CENTER);
            return panel;
        }

        private JButton createStyledButton(String text, String tooltip, String type) {
            return new JButton(text) {
                private boolean isHovered = false;
                private boolean isPressed = false;
                private float pulseEffect = 0.0f;
                private float glowEffect = 0.0f;
                private Timer pulseTimer;
                private Timer glowTimer;
                
                {
                    setForeground(TEXT_COLOR);
                    setBackground(BUTTON_COLOR);
                    setBorderPainted(false);
                    setFocusPainted(false);
                    setFont(BUTTON_FONT);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setToolTipText(tooltip);
                    
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            isHovered = true;
                            startPulseAnimation(true);
                            startGlowAnimation(true);
                            repaint();
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            isHovered = false;
                            startPulseAnimation(false);
                            startGlowAnimation(false);
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
                    if (pulseTimer != null) {
                        pulseTimer.stop();
                    }
                    
                    if (start) {
                        pulseTimer = new Timer(50, e -> {
                            pulseEffect = (float) (0.5 + Math.sin(System.currentTimeMillis() / 200.0) * 0.2);
                            repaint();
                        });
                        pulseTimer.start();
                    }
                }
                
                private void startGlowAnimation(boolean start) {
                    if (glowTimer != null) {
                        glowTimer.stop();
                    }
                    
                    if (start) {
                        glowTimer = new Timer(50, e -> {
                            glowEffect = (float) (0.5 + Math.sin(System.currentTimeMillis() / 300.0) * 0.2);
                            repaint();
                        });
                        glowTimer.start();
                    }
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int width = getWidth();
                    int height = getHeight();
                    
                    // Draw button background
                    if (isPressed) {
                        g2d.setColor(BUTTON_HOVER_COLOR);
                    } else if (isHovered) {
                        g2d.setColor(BUTTON_COLOR);
                    } else {
                        g2d.setColor(BUTTON_COLOR);
                    }
                    
                    g2d.fillRoundRect(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS);
                    
                    // Draw glow effect
                    if (isHovered) {
                        float glowAlpha = 0.3f * glowEffect;
                        g2d.setColor(new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), 
                                             BUTTON_ACCENT.getBlue(), (int)(glowAlpha * 255)));
                        g2d.fillRoundRect(-5, -5, width + 10, height + 10, CORNER_RADIUS + 5, CORNER_RADIUS + 5);
                    }
                    
                    // Draw button border
                    g2d.setColor(new Color(BUTTON_ACCENT.getRed(), BUTTON_ACCENT.getGreen(), 
                                         BUTTON_ACCENT.getBlue(), (int)(100 * pulseEffect)));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, width - 2, height - 2, CORNER_RADIUS - 1, CORNER_RADIUS - 1);
                    
                    // Draw text
                    FontMetrics fm = g2d.getFontMetrics(getFont());
                    Rectangle2D r = fm.getStringBounds(getText(), g2d);
                    int x = (width - (int) r.getWidth()) / 2;
                    int y = (height - (int) r.getHeight()) / 2 + fm.getAscent();
                    
                    g2d.setColor(getForeground());
                    g2d.drawString(getText(), x, y);
                    
                    g2d.dispose();
                }
            };
        }
    }
}
