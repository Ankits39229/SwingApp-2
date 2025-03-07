package main.java.org.anynomous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingWorker;
import java.util.HashMap;
import java.util.Map;
//import com.github.weis.jide.flatlaf.FlatDarkLaf;
// Change the import from
//import com.github.weis.jide.flatlaf.*;flatlaf

// To the correct one:
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

public class Main extends JFrame {
    // Enhanced modern color scheme with higher contrast
    private static final Color BACKGROUND_COLOR = new Color(5, 5, 7);         // Deeper black background
    private static final Color MENU_BACKGROUND = new Color(12, 12, 15);       // Darker menu background
    private static final Color MENU_SELECTED = new Color(30, 32, 40);         // More visible selection
    private static final Color TEXT_COLOR = new Color(255, 255, 255);         // Pure white text
    private static final Color ACCENT_COLOR = new Color(64, 125, 255);        // Slightly darker blue
    private static final Color HOVER_COLOR = new Color(35, 37, 45);           // More visible hover
    private static final Color CARD_BACKGROUND = new Color(10, 10, 12);       // Darker card background
    private static final Color BORDER_COLOR = new Color(45, 48, 55);          // More visible borders
    
    // Menu icons colors with increased contrast
    private static final Color HOME_COLOR = new Color(82, 140, 255);         // Brighter blue
    private static final Color CLEAN_COLOR = new Color(65, 230, 130);        // More vibrant green
    private static final Color TROUBLESHOOT_COLOR = new Color(255, 145, 45); // Brighter orange
    private static final Color FIREWALL_COLOR = new Color(255, 80, 80);      // More vibrant red
    private static final Color AUDIT_COLOR = new Color(180, 85, 255);        // Brighter purple
    private static final Color REMEDIATE_COLOR = new Color(0, 220, 240);     // Brighter cyan
    private static final Color MALWARE_COLOR = new Color(255, 70, 130);      // More vibrant pink
    private static final Color ASSISTANT_COLOR = new Color(255, 210, 25);    // Brighter amber

    private final JPanel mainContentPanel;
    private final CardLayout cardLayout;
    private final Map<String, JPanel> menuItems = new HashMap<>();
    private String currentCard = "Home";

    public Main() {
        // Set dark title bar with FlatLaf
        try {
            // Use FlatDarkLaf instead of system look and feel
            FlatDarkLaf.setup();
            
            // For custom colors that match your theme exactly
            UIManager.put("TitlePane.background", MENU_BACKGROUND);
            UIManager.put("TitlePane.foreground", TEXT_COLOR);
            UIManager.put("TitlePane.inactiveBackground", BACKGROUND_COLOR);
            UIManager.put("TitlePane.inactiveForeground", TEXT_COLOR.darker());
            
            // Button colors in title bar
            UIManager.put("TitlePane.buttonHoverBackground", HOVER_COLOR);
            UIManager.put("TitlePane.buttonPressedBackground", ACCENT_COLOR);
            
            // Apply additional UI defaults
            setupUIDefaults();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
                
                // Enhanced gradient for header with more contrast
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 27, 32),
                    0, getHeight(), new Color(12, 12, 15)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // More visible bottom border
                g2d.setColor(new Color(BORDER_COLOR.getRed(), BORDER_COLOR.getGreen(), BORDER_COLOR.getBlue(), 80));
                g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Create app title with logo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setOpaque(false);
        
        // Logo icon
        JLabel logoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shield logo
                int width = getWidth();
                int height = getHeight();
                int padding = 3;
                
                // Shield background
                g2d.setColor(ACCENT_COLOR);
                int[] xPoints = {padding, width/2, width - padding, width - padding, width/2, padding};
                int[] yPoints = {padding + height/4, padding, padding + height/4, height - padding, height - padding, height - padding};
                g2d.fillPolygon(xPoints, yPoints, 6);
                
                // Inner details
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRect(width/2 - 3, height/2 - 2, 6, 10);
                g2d.fillOval(width/2 - 5, height/2 + 8, 10, 5);
            }
        };
        logoLabel.setPreferredSize(new Dimension(30, 30));
        
        // App title
        JLabel titleLabel = new JLabel("Windows Security Suite");
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        titlePanel.add(logoLabel);
        titlePanel.add(titleLabel);
        
        // Create right-side controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setOpaque(false);
        
        // Search box with animation
        JTextField searchField = new JTextField(15);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.setBackground(new Color(45, 47, 50));
        searchField.setForeground(TEXT_COLOR);
        searchField.setCaretColor(TEXT_COLOR);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Profile button
        JButton profileButton = createIconButton("Profile", new Color(100, 110, 240));
        
        // Settings button
        JButton settingsButton = createIconButton("Settings", new Color(240, 180, 70));
        
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
                
                // Only paint background if mouse is over
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(50, 53, 58));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                
                // Draw icon
                g2d.setColor(iconColor);
                int size = Math.min(getWidth(), getHeight()) - 16;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                if (tooltip.equals("Settings")) {
                    // Draw gear icon
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(x + 4, y + 4, size - 8, size - 8);
                    g2d.drawLine(x + size/2, y, x + size/2, y + 3);
                    g2d.drawLine(x + size/2, y + size - 3, x + size/2, y + size);
                    g2d.drawLine(x, y + size/2, x + 3, y + size/2);
                    g2d.drawLine(x + size - 3, y + size/2, x + size, y + size/2);
                } else {
                    // Draw profile icon
                    g2d.fillOval(x + 3, y + 3, size - 6, size - 6);
                    g2d.setColor(new Color(45, 47, 50));
                    g2d.fillOval(x + size/3, y + size/4, size/3, size/3);
                    g2d.fillOval(x + size/4, y + size/2, size/2, size/3);
                }
            }
        };
        button.setPreferredSize(new Dimension(32, 32));
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
        
        // Add content with shadow effect
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create background with rounded corners
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                
                // Draw subtle border
                g2d.setColor(BORDER_COLOR);
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
                
                // Create shadow effect
                int shadowSize = 5;
                for (int i = 0; i < shadowSize; i++) {
                    float alpha = 0.1f * (shadowSize - i) / shadowSize;
                    g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                    g2d.draw(new RoundRectangle2D.Float(i, i, getWidth() - i * 2 - 1, getHeight() - i * 2 - 1, 15, 15));
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
        // Enhanced UI properties with higher contrast
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", TEXT_COLOR);
        UIManager.put("Button.font", new Font("Segoe UI Semibold", Font.PLAIN, 13));
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        UIManager.put("Button.select", ACCENT_COLOR.brighter());
        
        // Higher contrast text field
        UIManager.put("TextField.background", new Color(15, 15, 18));
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.caretForeground", TEXT_COLOR);
        
        // Enhanced scrollbar contrast
        UIManager.put("ScrollBar.thumb", new Color(50, 53, 60));
        UIManager.put("ScrollBar.track", new Color(12, 12, 15));
        UIManager.put("ScrollBar.thumbDarkShadow", BACKGROUND_COLOR);
        UIManager.put("ScrollBar.thumbHighlight", HOVER_COLOR.brighter());
        UIManager.put("ScrollBar.thumbShadow", MENU_BACKGROUND);
        
        // Additional UI improvements
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Panel.foreground", TEXT_COLOR);
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
    }

    private JPanel createSideMenu() {
        JPanel sideMenu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, MENU_BACKGROUND,
                    getWidth(), 0, new Color(26, 27, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw subtle right border
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
        category.setForeground(new Color(150, 150, 160));
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
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create animated icon for menu item
        JPanel iconPanel = new JPanel() {
            private final Timer pulseTimer = createPulseTimer(this);
            private float pulseScale = 1.0f;
            private boolean hovering = false;
            
            {
                setOpaque(false);
                setPreferredSize(new Dimension(30, 30));
                setMaximumSize(new Dimension(30, 30));
                
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
                            // Keep animation for selected item
                        } else {
                            pulseTimer.stop();
                            pulseScale = 1.0f;
                            repaint();
                        }
                    }
                });
            }
            
            private Timer createPulseTimer(JPanel panel) {
                return new Timer(50, e -> {
                    pulseScale = 1.0f + 0.1f * (float)Math.sin(System.currentTimeMillis() / 300.0);
                    panel.repaint();
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isSelected = cardName.equals(currentCard);
                int size = 18;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Draw icon background with pulsing effect if selected or hovered
                if (isSelected || hovering) {
                    int bgSize = (int)(size * pulseScale);
                    int bgX = (getWidth() - bgSize) / 2;
                    int bgY = (getHeight() - bgSize) / 2;
                    
                    // Start pulse animation if selected
                    if (isSelected && !pulseTimer.isRunning()) {
                        pulseTimer.start();
                    }
                    
                    // Draw glowing background
                    g2d.setColor(new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), 30));
                    g2d.fillOval(bgX, bgY, bgSize, bgSize);
                }
                
                // Draw the appropriate icon based on menu item
                g2d.setColor(iconColor);
                
                if (text.equals("Home")) {
                    // Home icon
                    int[] xPoints = {x + size/2, x + size, x + size, x + 3*size/4, x + 3*size/4, x + size/4, x + size/4, x, x};
                    int[] yPoints = {y, y + size/2, y + size, y + size, y + size/2, y + size/2, y + size, y + size, y + size/2};
                    g2d.fillPolygon(xPoints, yPoints, 9);
                } else if (text.equals("Clean")) {
                    // Clean icon
                    g2d.fillOval(x, y, size, size);
                    g2d.setColor(MENU_BACKGROUND);
                    g2d.fillOval(x + size/4, y + size/4, size/2, size/2);
                } else if (text.equals("Troubleshoot")) {
                    // Troubleshoot icon
                    g2d.fillRect(x, y + 3*size/4, size/3, size/4);
                    g2d.fillRect(x + size/3, y + size/2, size/3, size/2);
                    g2d.fillRect(x + 2*size/3, y, size/3, size);
                } else if (text.equals("Firewall")) {
                    // Firewall icon
                    g2d.fillRect(x, y, size, size/5);
                    g2d.fillRect(x, y + 2*size/5, size, size/5);
                    g2d.fillRect(x, y + 4*size/5, size, size/5);
                } else if (text.equals("Audit")) {
                    // Audit icon
                    g2d.fillOval(x + size/4, y, size/2, size/2);
                    g2d.fillRect(x + 2*size/5, y + size/2, size/5, size/2);
                    g2d.fillOval(x, y + 3*size/4, size, size/4);
                } else if (text.equals("Remediate")) {
                    // Remediate icon
                    g2d.fillRect(x, y + size/3, size, size/3);
                    g2d.fillRect(x + size/3, y, size/3, size);
                } else if (text.equals("Malware")) {
                    // Malware icon
                    g2d.fillOval(x, y, size, size);
                    g2d.setColor(MENU_BACKGROUND);
                    for (int i = 0; i < 3; i++) {
                        int dotSize = size/5;
                        g2d.fillOval(x + size/2 - dotSize/2, y + i*size/3 + size/6 - dotSize/2, dotSize, dotSize);
                    }
                } else { // Assistant
                    // Assistant icon
                    g2d.fillRoundRect(x, y, size, size, size/3, size/3);
                    g2d.setColor(MENU_BACKGROUND);
                    g2d.fillOval(x + size/4, y + size/3, size/5, size/5);
                    g2d.fillOval(x + 3*size/5, y + size/3, size/5, size/5);
                    g2d.fillRect(x + size/4, y + 2*size/3, size/2, size/6);
                }
            }
        };

        // Menu text
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

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
                
                // Higher contrast status panel background
                g2d.setColor(new Color(18, 20, 24));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // More visible border
                g2d.setColor(new Color(50, 53, 60));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
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
                
                // Draw pulsing green status indicator
                g2d.setColor(new Color(80, 200, 120));
                int pulse = (int)(6 + 2 * Math.sin(System.currentTimeMillis() / 500.0));
                g2d.fillOval(2, 2, pulse, pulse);
            }
        };
        statusIcon.setOpaque(false);
        statusIcon.setPreferredSize(new Dimension(12, 12));
        
        JLabel statusText = new JLabel("Protected");
        statusText.setForeground(new Color(80, 200, 120));
        statusText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        statusIndicator.add(statusIcon);
        statusIndicator.add(statusText);
        
        // Create animated status bar
        JProgressBar statusBar = new JProgressBar(0, 100);
        statusBar.setValue(100);
        statusBar.setStringPainted(false);
        statusBar.setForeground(new Color(80, 200, 120));
        statusBar.setBackground(new Color(40, 42, 45));
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
                
                SwingUtilities.invokeLater(() -> {
                    Main dashboard = new Main();
                    
                    // Create a custom glass pane class
                    class FadeGlassPane extends JPanel {
                        private float alpha = 1.0f;
                        
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setColor(BACKGROUND_COLOR);
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                            g2d.fillRect(0, 0, getWidth(), getHeight());
                            g2d.dispose();
                        }
                        
                        public void setAlpha(float value) {
                            this.alpha = value;
                            repaint();
                        }
                        
                        public float getAlpha() {
                            return alpha;
                        }
                    }
                    
                    // Create and set up the glass pane
                    FadeGlassPane glassPane = new FadeGlassPane();
                    glassPane.setOpaque(false);
                    dashboard.setGlassPane(glassPane);
                    glassPane.setVisible(true);
                    
                    // Make the window visible
                    dashboard.setVisible(true);
                    
                    // Create fade-in animation using the glass pane
                    Timer fadeInTimer = new Timer(20, null);
                    fadeInTimer.addActionListener(e -> {
                        float newAlpha = glassPane.getAlpha() - 0.05f;
                        if (newAlpha <= 0.0f) {
                            newAlpha = 0.0f;
                            fadeInTimer.stop();
                            glassPane.setVisible(false);
                        }
                        glassPane.setAlpha(newAlpha);
                    });
                    fadeInTimer.start();
                });
            }
        };

        worker.execute();
    }
}