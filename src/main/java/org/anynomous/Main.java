package main.java.org.anynomous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
// import javax.swing.border.EmptyBorder;
import javax.swing.SwingWorker;
import java.util.HashMap;
import java.util.Map;
// import java.util.Random;
// import java.awt.geom.AffineTransform;
// import java.awt.Composite;
//import com.github.weis.jide.flatlaf.FlatDarkLaf;
// Change the import from
//import com.github.weis.jide.flatlaf.*;flatlaf

// To the correct one:
import com.formdev.flatlaf.FlatDarkLaf;
// import com.formdev.flatlaf.FlatLaf;
// import main.java.org.anynomous.utils.IconLoader;

public class Main extends JFrame {
    // Enhanced modern color scheme with even darker contrast
    private static final Color BACKGROUND_COLOR = new Color(2, 2, 4);         // Deeper black background
    private static final Color MENU_BACKGROUND = new Color(8, 8, 10);         // Darker menu background
    private static final Color MENU_SELECTED = new Color(20, 22, 28);         // More visible selection
    private static final Color TEXT_COLOR = new Color(255, 255, 255);         // Pure white text
    private static final Color ACCENT_COLOR = new Color(50, 100, 220);        // Darker blue
    private static final Color HOVER_COLOR = new Color(25, 27, 32);           // More visible hover
    private static final Color CARD_BACKGROUND = new Color(5, 5, 7);          // Darker card background
    private static final Color BORDER_COLOR = new Color(35, 38, 45);          // More visible borders
    
    // Menu icons colors with increased contrast
    private static final Color HOME_COLOR = new Color(70, 120, 255);         // Brighter blue
    private static final Color CLEAN_COLOR = new Color(50, 210, 110);        // More vibrant green
    private static final Color TROUBLESHOOT_COLOR = new Color(255, 130, 40); // Brighter orange
    private static final Color FIREWALL_COLOR = new Color(255, 70, 70);      // More vibrant red
    private static final Color AUDIT_COLOR = new Color(160, 70, 240);        // Brighter purple
    private static final Color REMEDIATE_COLOR = new Color(0, 200, 220);     // Brighter cyan
    private static final Color MALWARE_COLOR = new Color(240, 60, 120);      // More vibrant pink
    private static final Color ASSISTANT_COLOR = new Color(240, 200, 20);    // Brighter amber

    private final JPanel mainContentPanel;
    private final CardLayout cardLayout;
    private final Map<String, JPanel> menuItems = new HashMap<>();
    private String currentCard = "Home";

    // Add this at the top of your class to store icon URLs
    private static final Map<String, String> ICON_URLS = new HashMap<>();
    static {
        // Free SVG icons from various sources
        ICON_URLS.put("Home", "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/house.svg");
        ICON_URLS.put("Clean", "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/trash.svg");
        ICON_URLS.put("Troubleshoot", "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/tools.svg");
        ICON_URLS.put("FIREWALL", "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/shield.svg");
        ICON_URLS.put("Audit", "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/clipboard-check.svg");
        ICON_URLS.put("Remediate", "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/bandaid.svg");
        ICON_URLS.put("Malware", "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/bug.svg");
        ICON_URLS.put("Assistant", "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/headset.svg");
    }

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
        setSize(1300, 800);  // Larger default size
        setMinimumSize(new Dimension(1280, 800)); // Set minimum size
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
        mainContentPanel.add(new Firewall(), "Firewall");
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

        // Add chat bubble
        ChatBubble chatBubble = new ChatBubble();
        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.add(chatBubble, JLayeredPane.POPUP_LAYER);
        
        // Position the chat bubble in the bottom-right corner
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = getWidth() - chatBubble.getPreferredSize().width - 20;
                int y = getHeight() - chatBubble.getPreferredSize().height - 20;
                chatBubble.setBounds(x, y, 
                    chatBubble.getPreferredSize().width, 
                    chatBubble.getPreferredSize().height);
            }
        });
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
        // Create the sidebar menu with your desired width
        SidebarMenu sideMenu = new SidebarMenu(280);
        
        // Set colors to match your application theme
        sideMenu.setMenuBackgroundColor(MENU_BACKGROUND);
        sideMenu.setSelectedColor(MENU_SELECTED);
        sideMenu.setHoverColor(HOVER_COLOR);
        sideMenu.setTextColor(TEXT_COLOR);
        
        // Add categories and menu items
        sideMenu.addCategory("MAIN");
        sideMenu.addMenuItem("Home", "Home", HOME_COLOR);
        
        sideMenu.addCategory("SECURITY");
        sideMenu.addMenuItem("Clean", "Clean", CLEAN_COLOR);
        sideMenu.addMenuItem("Troubleshoot", "Troubleshoot", TROUBLESHOOT_COLOR);
        sideMenu.addMenuItem("Firewall", "Firewall", FIREWALL_COLOR);
        
        sideMenu.addCategory("ADVANCED");
        sideMenu.addMenuItem("Audit", "Audit", AUDIT_COLOR);
        sideMenu.addMenuItem("Remediate", "Remediate", REMEDIATE_COLOR);
        sideMenu.addMenuItem("Malware", "Malware", MALWARE_COLOR);
        sideMenu.addMenuItem("Assistant", "Assistant", ASSISTANT_COLOR);
        
        // Add flexible space to push the status panel to the bottom
        sideMenu.addFlexibleSpace();
        
        // Add a status panel at the bottom
        sideMenu.addComponent(createStatusPanel());
        
        // Set up the selection change handler
        sideMenu.setOnSelectionChanged(cardName -> {
            // Show the corresponding card
            cardLayout.show(mainContentPanel, cardName);
            currentCard = cardName;
        });
        
        // Set initial selection
        sideMenu.selectMenuItem("Home");
        
        return sideMenu;
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

    private void selectMenuItem(String cardName) {
        // Reset background of all menu items
        menuItems.forEach((key, item) -> item.setBackground(MENU_BACKGROUND));
        
        // Set selected item background
        JPanel selectedItem = menuItems.get(cardName);
        if (selectedItem != null) {
            selectedItem.setBackground(MENU_SELECTED);
            currentCard = cardName;
            cardLayout.show(mainContentPanel, cardName);
        }
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