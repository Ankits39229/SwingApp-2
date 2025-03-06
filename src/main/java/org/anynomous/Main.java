package main.java.org.anynomous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingWorker;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
    // Refined corporate color scheme
    private static final Color BACKGROUND_COLOR = new Color(21, 25, 30);      // Dark background
    private static final Color MENU_BACKGROUND = new Color(28, 30, 36);       // Subtle menu background
    private static final Color MENU_SELECTED = new Color(35, 38, 45);         // Professional selection
    private static final Color TEXT_COLOR = new Color(220, 222, 226);         // Soft white text
    private static final Color ACCENT_COLOR = new Color(65, 105, 170);        // Business blue
    private static final Color HOVER_COLOR = new Color(40, 45, 55);           // Refined hover
    private static final Color CARD_BACKGROUND = new Color(25, 28, 35);       // Subtle card background
    private static final Color BORDER_COLOR = new Color(45, 48, 55);          // Refined borders
    
    // Professional icon colors
    private static final Color HOME_COLOR = new Color(65, 105, 170);          // Corporate blue
    private static final Color CLEAN_COLOR = new Color(70, 140, 110);         // Forest green
    private static final Color TROUBLESHOOT_COLOR = new Color(180, 120, 65);  // Caramel
    private static final Color FIREWALL_COLOR = new Color(160, 70, 70);       // Burgundy
    private static final Color AUDIT_COLOR = new Color(100, 80, 140);         // Deep purple
    private static final Color REMEDIATE_COLOR = new Color(55, 125, 145);     // Steel blue
    private static final Color MALWARE_COLOR = new Color(160, 80, 100);       // Muted rose
    private static final Color ASSISTANT_COLOR = new Color(180, 140, 60);     // Gold

    private final JPanel mainContentPanel;
    private final CardLayout cardLayout;
    private final Map<String, JPanel> menuItems = new HashMap<>();
    private String currentCard = "Home";

    public Main() {
        setTitle("Windows Security Suite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);  // Larger default size
        setMinimumSize(new Dimension(1000, 700)); // Set minimum size
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize card layout for main content
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BACKGROUND_COLOR);
        
        // Create main UI components
        JPanel headerPanel = createHeaderPanel();
        JPanel sideMenuPanel = createSideMenu();
        JPanel contentWrapper = createContentWrapper(mainContentPanel);
        
        // Create split pane for resizable menu
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideMenuPanel, contentWrapper);
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        
        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // Add panels to card layout
        mainContentPanel.add(new Home(), "Home");
        mainContentPanel.add(new Clean(), "Clean");
        mainContentPanel.add(new Troubleshoot().getSplitPane(), "Troubleshoot");
        mainContentPanel.add(new Firewall(), "FIREWALL");
        mainContentPanel.add(new Audit(), "Audit");
        mainContentPanel.add(new Remediate(), "Remediate");
        mainContentPanel.add(new Malware(), "Malware");
        mainContentPanel.add(new Assistant(), "Assistant");

        // Setup global UI defaults
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            setupUIDefaults();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Add resize listener for responsive design
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Adjust UI elements based on size if needed
                if (getWidth() < 1100) {
                    splitPane.setDividerLocation(240);
                } else {
                    splitPane.setDividerLocation(280);
                }
            }
        });
        
        // Set initial selected menu item
        selectMenuItem("Home");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create subtle gradient for header
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(30, 33, 38),
                    0, getHeight(), new Color(25, 28, 33)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw refined bottom border
                g2d.setColor(BORDER_COLOR);
                g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 54));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Create app title with logo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setOpaque(false);
        
        // Professional logo icon
        JLabel logoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw professional shield logo
                int width = getWidth();
                int height = getHeight();
                int padding = 3;
                
                // Shield outline
                g2d.setColor(ACCENT_COLOR);
                int[] xPoints = {width/2, width - padding, width - padding - width/5, width/2, padding + width/5, padding};
                int[] yPoints = {padding, height/3, height - padding, height - padding, height - padding, height/3};
                g2d.fillPolygon(xPoints, yPoints, 6);
                
                // Inner details - more subtle and corporate
                g2d.setColor(new Color(240, 240, 240, 80));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawLine(width/2, height/2 - 2, width/2, height/2 + 5);
                g2d.drawLine(width/2 - 4, height/2 + 2, width/2 + 4, height/2 + 2);
            }
        };
        logoLabel.setPreferredSize(new Dimension(28, 28));
        
        // App title with refined font
        JLabel titleLabel = new JLabel("Enterprise Security Suite");
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        
        titlePanel.add(logoLabel);
        titlePanel.add(titleLabel);
        
        // Create right-side controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setOpaque(false);
        
        // Refined search box
        JTextField searchField = new JTextField(15);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(55, 58, 65), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.setBackground(new Color(40, 43, 48));
        searchField.setForeground(TEXT_COLOR);
        searchField.setCaretColor(TEXT_COLOR);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Professional profile button
        JButton profileButton = createIconButton("Profile", new Color(85, 115, 155));
        
        // Professional settings button
        JButton settingsButton = createIconButton("Settings", new Color(170, 140, 80));
        
        controlsPanel.add(searchField);
        controlsPanel.add(profileButton);
        controlsPanel.add(settingsButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(controlsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JButton createIconButton(String tooltip, Color iconColor) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Professional hover effect
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(45, 48, 55));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                }
                
                // Draw refined icon
                g2d.setColor(iconColor);
                int size = Math.min(getWidth(), getHeight()) - 16;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                if (tooltip.equals("Settings")) {
                    // Draw refined gear icon
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.drawOval(x + 4, y + 4, size - 8, size - 8);
                    int spokeLength = 3;
                    for (int i = 0; i < 8; i++) {
                        double angle = i * Math.PI / 4;
                        int centerX = x + size/2;
                        int centerY = y + size/2;
                        int radius = size/2;
                        int x1 = (int)(centerX + (radius - 4) * Math.cos(angle));
                        int y1 = (int)(centerY + (radius - 4) * Math.sin(angle));
                        int x2 = (int)(centerX + (radius + spokeLength) * Math.cos(angle));
                        int y2 = (int)(centerY + (radius + spokeLength) * Math.sin(angle));
                        g2d.drawLine(x1, y1, x2, y2);
                    }
                } else {
                    // Draw professional profile icon
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.drawOval(x + 3, y + 3, size - 6, size - 6);
                    g2d.drawOval(x + size/3, y + size/4, size/3, size/3);
                    g2d.drawArc(x + size/4, y + size/2, size/2, size/3, 0, 180);
                }
            }
        };
        button.setPreferredSize(new Dimension(30, 30));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private JPanel createContentWrapper(JPanel content) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND_COLOR);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add content with refined shadow effect
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create background with subtle rounded corners
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                
                // Draw refined border
                g2d.setColor(BORDER_COLOR);
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                
                // Create subtle shadow effect
                int shadowSize = 4;
                for (int i = 0; i < shadowSize; i++) {
                    float alpha = 0.07f * (shadowSize - i) / shadowSize;
                    g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                    g2d.draw(new RoundRectangle2D.Float(i, i, getWidth() - i * 2 - 1, getHeight() - i * 2 - 1, 8, 8));
                }
            }
        };
        shadowPanel.setLayout(new BorderLayout());
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        shadowPanel.add(content, BorderLayout.CENTER);
        
        wrapper.add(shadowPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private void setupUIDefaults() {
        // Set global UI properties
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", TEXT_COLOR);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        UIManager.put("Button.select", ACCENT_COLOR.darker());
        
        // Additional UI customizations
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.background", new Color(40, 43, 48));
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.caretForeground", TEXT_COLOR);
        UIManager.put("ScrollBar.thumb", new Color(90, 90, 95));
        UIManager.put("ScrollBar.track", new Color(50, 50, 55));
    }

    private JPanel createSideMenu() {
        JPanel sideMenu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create subtle gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, MENU_BACKGROUND,
                    getWidth(), 0, new Color(25, 27, 32)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw refined right border
                g2d.setColor(BORDER_COLOR);
                g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
            }
        };
        sideMenu.setPreferredSize(new Dimension(280, getHeight()));
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        // Menu items with icons and categories
        createMenuCategory(sideMenu, "MAIN");
        addMenuItem(sideMenu, "Home", "Home", HOME_COLOR);
        
        createMenuCategory(sideMenu, "SECURITY");
        addMenuItem(sideMenu, "Clean", "Clean", CLEAN_COLOR);
        addMenuItem(sideMenu, "Troubleshoot", "Troubleshoot", TROUBLESHOOT_COLOR);
        addMenuItem(sideMenu, "Firewall", "FIREWALL", FIREWALL_COLOR);
        
        createMenuCategory(sideMenu, "ADVANCED");
        addMenuItem(sideMenu, "Audit", "Audit", AUDIT_COLOR);
        addMenuItem(sideMenu, "Remediate", "Remediate", REMEDIATE_COLOR);
        addMenuItem(sideMenu, "Malware", "Malware", MALWARE_COLOR);
        addMenuItem(sideMenu, "Assistant", "Assistant", ASSISTANT_COLOR);

        // Add status panel at bottom
        sideMenu.add(Box.createVerticalGlue());
        sideMenu.add(createStatusPanel());
        
        return sideMenu;
    }
    
    private void createMenuCategory(JPanel menu, String categoryName) {
        JLabel category = new JLabel(categoryName);
        category.setForeground(new Color(140, 145, 160));
        category.setFont(new Font("Segoe UI", Font.BOLD, 11));
        category.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 10));
        category.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(category);
    }
    
    private void addMenuItem(JPanel menu, String text, String cardName, Color iconColor) {
        JPanel item = new JPanel();
        item.setName(cardName);
        item.setLayout(new BoxLayout(item, BoxLayout.X_AXIS));
        item.setBackground(MENU_BACKGROUND);
        item.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42)); // Slightly smaller for refinement
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create more elegant icon for menu item
        JPanel iconPanel = new JPanel() {
            private final Timer pulseTimer = createPulseTimer(this);
            private float pulseScale = 1.0f;
            private boolean hovering = false;
            
            {
                setOpaque(false);
                setPreferredSize(new Dimension(28, 28));
                setMaximumSize(new Dimension(28, 28));
                
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        if (!pulseTimer.isRunning()) {
                            pulseTimer.start();
                        }
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        if (cardName.equals(currentCard)) {
                            // Keep subtle animation for selected item
                        } else {
                            pulseTimer.stop();
                            pulseScale = 1.0f;
                            repaint();
                        }
                    }
                });
            }
            
            private Timer createPulseTimer(JPanel panel) {
                // More subtle, professional animation
                return new Timer(80, e -> {
                    pulseScale = 1.0f + 0.06f * (float)Math.sin(System.currentTimeMillis() / 400.0);
                    panel.repaint();
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isSelected = cardName.equals(currentCard);
                int size = 16; // Slightly smaller icons
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Draw subtle icon background
                if (isSelected || hovering) {
                    int bgSize = (int)(size * pulseScale);
                    int bgX = (getWidth() - bgSize) / 2;
                    int bgY = (getHeight() - bgSize) / 2;
                    
                    if (isSelected && !pulseTimer.isRunning()) {
                        pulseTimer.start();
                    }
                    
                    // Draw subtle background
                    g2d.setColor(new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), 20));
                    g2d.fillOval(bgX, bgY, bgSize, bgSize);
                }
                
                // Draw more professional icons
                g2d.setColor(iconColor);
                g2d.setStroke(new BasicStroke(1.5f));
                
                // ... rest of icon drawing logic, but using strokes instead of fills for a more professional look
                // For example, for Home icon:
                if (text.equals("Home")) {
                    // Home icon - more elegant
                    g2d.drawRect(x + 2, y + size/2, size - 4, size/2);
                    g2d.drawLine(x, y + size/2, x + size/2, y);
                    g2d.drawLine(x + size/2, y, x + size, y + size/2);
                }
                
                // ... (similar changes for other icons)
            }
        };
        
        // Menu text with refined font
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        item.add(iconPanel);
        item.add(Box.createRigidArea(new Dimension(15, 0)));
        item.add(label);
        item.add(Box.createHorizontalGlue());

        // Add hover and selection effects
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(cardName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!cardName.equals(currentCard)) {
                    item.setBackground(HOVER_COLOR);
                }
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!cardName.equals(currentCard)) {
                    item.setBackground(MENU_BACKGROUND);
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Store menu item in map for later reference
        menuItems.put(cardName, item);
        menu.add(item);
        menu.add(Box.createRigidArea(new Dimension(0, 2)));
    }
    
    private void selectMenuItem(String cardName) {
        // Deselect previous item
        if (menuItems.containsKey(currentCard)) {
            menuItems.get(currentCard).setBackground(MENU_BACKGROUND);
        }
        
        // Select new item
        currentCard = cardName;
        if (menuItems.containsKey(cardName)) {
            menuItems.get(cardName).setBackground(MENU_SELECTED);
        }
        
        // Show the corresponding card
        cardLayout.show(mainContentPanel, cardName);
    }
    
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw refined panel background
                g2d.setColor(new Color(25, 27, 31));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                
                // Draw subtle border
                g2d.setColor(new Color(40, 43, 48));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
            }
        };
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        statusPanel.setMaximumSize(new Dimension(280, 80));
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // System status indicator
        JPanel statusInfo = new JPanel();
        statusInfo.setLayout(new BoxLayout(statusInfo, BoxLayout.Y_AXIS));
        statusInfo.setOpaque(false);
        
        JLabel statusTitle = new JLabel("System Status");
        statusTitle.setForeground(TEXT_COLOR);
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel statusIndicator = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statusIndicator.setOpaque(false);
        statusIndicator.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Status icon
        JPanel statusIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw subtle status indicator
                g2d.setColor(new Color(70, 140, 110));
                int pulse = (int)(6 + 2 * Math.sin(System.currentTimeMillis() / 500.0));
                g2d.fillOval(2, 2, pulse, pulse);
            }
        };
        statusIcon.setOpaque(false);
        statusIcon.setPreferredSize(new Dimension(12, 12));
        
        JLabel statusText = new JLabel("Protected");
        statusText.setForeground(new Color(70, 140, 110));
        statusText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        statusIndicator.add(statusIcon);
        statusIndicator.add(statusText);
        
        // Create animated status bar
        JProgressBar statusBar = new JProgressBar(0, 100);
        statusBar.setValue(100);
        statusBar.setStringPainted(false);
        statusBar.setForeground(new Color(70, 140, 110));
        statusBar.setBackground(new Color(40, 43, 48));
        statusBar.setBorder(null);
        statusBar.setPreferredSize(new Dimension(200, 4));
        statusBar.setMaximumSize(new Dimension(200, 4));
        statusBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        statusInfo.add(statusTitle);
        statusInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        statusInfo.add(statusIndicator);
        statusInfo.add(Box.createRigidArea(new Dimension(0, 8)));
        statusInfo.add(statusBar);
        
        // Add status timer to simulate activity
        new Timer(2000, e -> {
            int newValue = 85 + (int)(Math.random() * 15);
            statusBar.setValue(newValue);
        }).start();
        
        statusPanel.add(statusInfo, BorderLayout.CENTER);
        
        return statusPanel;
    }

    public static void main(String[] args) {
        // Create and show loading screen with enhanced visuals
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.setVisible(true);

        // Use SwingWorker to perform initialization in background
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate loading tasks
                for (int i = 0; i <= 100; i += 5) {
                    Thread.sleep(100); // Simulate work being done
                    final int progress = i;
                    SwingUtilities.invokeLater(() -> {
                        loadingScreen.updateProgress(progress, "Initializing security components... " + progress + "%");
                    });
                    publish(i);
                }
                return null;
            }

            @Override
            protected void done() {
                loadingScreen.dispose(); // Close loading screen
                // Initialize your main application window here
                SwingUtilities.invokeLater(() -> {
                    Main dashboard = new Main();
                    
                    // Create custom glass pane class to avoid casting issues
                    class FadeGlassPane extends JPanel {
                        private int alpha = 255;
                        
                        public FadeGlassPane() {
                            setOpaque(false);
                        }
                        
                        public int getAlpha() {
                            return alpha;
                        }
                        
                        public void setAlpha(int value) {
                            // Ensure alpha stays within valid range (0-255)
                            this.alpha = Math.max(0, Math.min(255, value));
                            repaint();
                        }
                        
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2d = (Graphics2D) g;
                            g2d.setColor(new Color(0, 0, 0, alpha));
                            g2d.fillRect(0, 0, getWidth(), getHeight());
                        }
                    }
                    
                    // Use the proper typed glass pane
                    FadeGlassPane glassPane = new FadeGlassPane();
                    dashboard.setGlassPane(glassPane);
                    glassPane.setVisible(true);
                    
                    dashboard.setVisible(true);
                    
                    // Professional fade-in animation using glass pane with proper type
                    Timer fadeInTimer = new Timer(20, null);
                    fadeInTimer.addActionListener(e -> {
                        if (glassPane.getAlpha() > 0) {
                            glassPane.setAlpha(glassPane.getAlpha() - 10);
                        } else {
                            fadeInTimer.stop();
                            glassPane.setVisible(false);
                        }
                    });
                    fadeInTimer.start();
                });
            }
        };

        worker.execute();
    }
}