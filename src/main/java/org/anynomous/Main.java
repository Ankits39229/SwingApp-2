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
import main.java.org.anynomous.utils.PremiumAccessManager;
import main.java.org.anynomous.utils.PremiumAccessManager.PremiumAccessListener;
import main.java.org.anynomous.utils.BlockchainService;
import main.java.org.anynomous.AuthenticationEndpoint.AuthenticationStatus;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.DefaultBlockParameterName;
import java.math.BigInteger;

public class Main extends JFrame implements PremiumAccessListener {
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

    private PremiumAccessManager premiumAccessManager;
    private JPanel cardPanel;
    private SidebarMenu sidebarMenu;
    private JPanel glassPane;

    // Add a static cache for the authenticated address to improve persistence
    private static String cachedUserAddress = null;
    
    private static long lastAuthCheckTime = 0;
    private static final long AUTH_CHECK_INTERVAL = 10000; // 10 seconds between full log outputs
    
    /**
     * Static method to update the cached user address from other classes
     * @param address The Ethereum address to cache
     */
    public static void setCachedUserAddress(String address) {
        if (address != null && !address.isEmpty()) {
            System.out.println("ℹ️ Main.setCachedUserAddress called with: " + address);
            cachedUserAddress = address;
            
            // Also update the AuthenticationEndpoint if available
            try {
                if (AuthenticationEndpoint.getInstance() != null) {
                    AuthenticationEndpoint.AuthenticationStatus status = 
                        AuthenticationEndpoint.getInstance().getStatus("default");
                    
                    if (status == null) {
                        status = AuthenticationEndpoint.getInstance().createNewStatus("default");
                    }
                    
                    if (status != null) {
                        status.setAuthenticated(true);
                        status.setAccount(address);
                        System.out.println("✅ Updated AuthenticationEndpoint status with address: " + address);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error updating AuthenticationEndpoint: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Attempted to set null/empty cachedUserAddress");
        }
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

        // Register shutdown hook
        registerShutdownHook();
        
        // Try to initialize the cached address from existing authentication state
        if (cachedUserAddress == null || cachedUserAddress.isEmpty()) {
            try {
                if (AuthenticationEndpoint.getInstance() != null) {
                    AuthenticationEndpoint.AuthenticationStatus status = 
                        AuthenticationEndpoint.getInstance().getStatus("default");
                    
                    if (status != null && status.isAuthenticated() && 
                        status.getAccount() != null && !status.getAccount().isEmpty()) {
                        
                        String account = status.getAccount();
                        cachedUserAddress = account;
                        System.out.println("✅ Initialized cachedUserAddress from AuthenticationEndpoint: " + account);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error initializing cachedUserAddress: " + e.getMessage());
            }
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

        // Initialize premium access manager
        premiumAccessManager = PremiumAccessManager.getInstance();
        premiumAccessManager.addPremiumAccessListener(this);

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
        mainContentPanel.add(new Troubleshoot().getMainPanel(), "Troubleshoot");
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
//        ChatBubble chatBubble = new ChatBubble();
//        JLayeredPane layeredPane = getLayeredPane();
//        layeredPane.add(chatBubble, JLayeredPane.POPUP_LAYER);
//
//        // Position the chat bubble in the bottom-right corner
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                int x = getWidth() - chatBubble.getPreferredSize().width - 20;
//                int y = getHeight() - chatBubble.getPreferredSize().height - 20;
//                chatBubble.setBounds(x, y,
//                        chatBubble.getPreferredSize().width,
//                        chatBubble.getPreferredSize().height);
//            }
//        });
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

        // Create wallet connection button that shows connection status
        JButton walletButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                String userAddress = getCurrentUserAddress();
                boolean isConnected = userAddress != null && !userAddress.isEmpty();
                
                // Background with hover effect
                if (getModel().isPressed()) {
                    g2d.setColor(isConnected ? new Color(60, 170, 100) : new Color(200, 100, 20));
                } else if (getModel().isRollover()) {
                    g2d.setColor(isConnected ? new Color(70, 190, 120) : new Color(250, 140, 50));
                } else {
                    g2d.setColor(isConnected ? new Color(65, 180, 110) : new Color(240, 140, 50));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Draw icon
                int iconSize = 18;
                int textWidth = g2d.getFontMetrics().stringWidth(getText());
                int totalWidth = iconSize + 8 + textWidth; // icon + gap + text
                int startX = (getWidth() - totalWidth) / 2;
                
                if (isConnected) {
                    // Connected - draw checkmark in circle
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(startX, (getHeight() - iconSize) / 2, iconSize, iconSize);
                    g2d.drawLine(startX + 5, (getHeight() - iconSize) / 2 + 9, 
                                 startX + 8, (getHeight() - iconSize) / 2 + 12);
                    g2d.drawLine(startX + 8, (getHeight() - iconSize) / 2 + 12, 
                                 startX + 14, (getHeight() - iconSize) / 2 + 6);
                    
                    // Draw first few characters of address for quick identification
                    if (userAddress.length() > 6) {
                        String shortAddress = userAddress.substring(0, 6) + "...";
                        Font originalFont = g2d.getFont();
                        g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
                        g2d.drawString(shortAddress, startX + iconSize + textWidth + 5, 
                                       (getHeight() + g2d.getFontMetrics().getAscent()) / 2 - 2);
                        g2d.setFont(originalFont);
                    }
                } else {
                    // Not connected - draw MetaMask icon
                    // Fox face
                    g2d.setColor(new Color(250, 180, 80));
                    g2d.fillOval(startX + 4, (getHeight() - iconSize) / 2 + 4, iconSize - 8, iconSize - 8);
                    
                    // Fox ears
                    int centerY = (getHeight() - iconSize) / 2;
                    int[] leftEarX = {startX + 4, startX, startX + 6};
                    int[] leftEarY = {centerY + 4, centerY, centerY + 3};
                    g2d.setColor(new Color(220, 120, 30));
                    g2d.fillPolygon(leftEarX, leftEarY, 3);
                    
                    int[] rightEarX = {startX + iconSize - 4, startX + iconSize, startX + iconSize - 6};
                    int[] rightEarY = {centerY + 4, centerY, centerY + 3};
                    g2d.fillPolygon(rightEarX, rightEarY, 3);
                    
                    // Add a subtle pulsing effect to attract attention
                    float phase = (float)(System.currentTimeMillis() % 2000) / 2000f;
                    float alpha = 0.7f + 0.3f * (float)Math.sin(phase * 2 * Math.PI);
                    g2d.setColor(new Color(1f, 1f, 1f, alpha));
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.drawRoundRect(3, 3, getWidth()-6, getHeight()-6, 8, 8);
                }
                
                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                g2d.drawString(getText(), startX + iconSize + 8, 
                              (getHeight() + g2d.getFontMetrics().getAscent()) / 2 - 2);
            }
            
            // Update text based on connection status
            @Override
            public String getText() {
                String userAddress = getCurrentUserAddress();
                if (userAddress != null && !userAddress.isEmpty()) {
                    return "Connected";
                } else {
                    return "Connect Wallet";
                }
            }
        };
        walletButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        walletButton.setBorderPainted(false);
        walletButton.setContentAreaFilled(false);
        walletButton.setFocusPainted(false);
        walletButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        walletButton.setPreferredSize(new Dimension(150, 36));
        walletButton.setToolTipText("Connect your MetaMask wallet to access premium features");

        // Add a timer for animation
        Timer walletButtonTimer = new Timer(40, e -> walletButton.repaint());
        walletButtonTimer.start();

        walletButton.addActionListener(e -> {
            String userAddress = getCurrentUserAddress();
            if (userAddress != null && !userAddress.isEmpty()) {
                // Already connected - show profile info
                showProfileInfo();
            } else {
                // Not connected - show auth dialog
                MetaMaskAuth authDialog = new MetaMaskAuth();
                authDialog.setAuthenticationListener(new MetaMaskAuth.AuthenticationListener() {
                    @Override
                    public void onAuthenticationSuccess() {
                        // Refresh UI after successful connection
                        refreshAllUI();
                        walletButton.repaint(); // Update button display
                    }
                    
                    @Override
                    public void onAuthenticationFailure(String reason) {
                        JOptionPane.showMessageDialog(
                            Main.this,
                            "Authentication failed: " + reason,
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
                authDialog.setLocationRelativeTo(Main.this);
                authDialog.setVisible(true);
            }
        });
        
        // Add contract info button
        JButton contractInfoBtn = new JButton("Contract Info");
        contractInfoBtn.setBackground(new Color(80, 90, 150));
        contractInfoBtn.setForeground(TEXT_COLOR);
        contractInfoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contractInfoBtn.addActionListener(e -> showContractInfo());
        
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
        profileButton.addActionListener(e -> showProfileInfo());

        // Settings button
        JButton settingsButton = createIconButton("Settings", new Color(240, 180, 70));

        // Add all controls
        controlsPanel.add(walletButton);
        controlsPanel.add(contractInfoBtn);
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
        sidebarMenu = new SidebarMenu(250, true);
        sidebarMenu.setMenuBackgroundColor(MENU_BACKGROUND);
        sidebarMenu.setSelectedColor(MENU_SELECTED);
        sidebarMenu.setHoverColor(HOVER_COLOR);
        sidebarMenu.setTextColor(TEXT_COLOR);

        sidebarMenu.addCategory("DASHBOARD");
        sidebarMenu.addMenuItem("Home", "Home", HOME_COLOR, false);
        
        sidebarMenu.addCategory("SECURITY");
        sidebarMenu.addMenuItem("Clean", "Clean System", CLEAN_COLOR, false);
        sidebarMenu.addMenuItem("Troubleshoot", "Troubleshoot", TROUBLESHOOT_COLOR, false);
        sidebarMenu.addMenuItem("Firewall", "Firewall", FIREWALL_COLOR, false);
        
        sidebarMenu.addCategory("ADVANCED");
        // Mark these as premium features
        sidebarMenu.addMenuItem("Audit", "Security Audit", AUDIT_COLOR, true);
        sidebarMenu.addMenuItem("Malware", "Malware Scanner", MALWARE_COLOR, true);
        
        sidebarMenu.addCategory("HELP");
        sidebarMenu.addMenuItem("Remediate", "Remediate", REMEDIATE_COLOR, false);
        sidebarMenu.addMenuItem("Assistant", "AI Assistant", ASSISTANT_COLOR, false);

        // Add flexible space before status panel
        sidebarMenu.addFlexibleSpace();

        // Add connection status
        JPanel statusPanel = createStatusPanel();
        sidebarMenu.addComponent(statusPanel);

        // Store menu item references for selection handling
        Component[] components = sidebarMenu.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                String name = panel.getName();
                if (name != null && !name.isEmpty()) {
                    menuItems.put(name, panel);
                }
            }
        }

        // Set selection handler
        sidebarMenu.setOnSelectionChanged(this::selectMenuItem);
        
        // Set premium feature click handler
        sidebarMenu.setOnPremiumFeatureClicked(this::handlePremiumFeatureClicked);
        
        // Initialize premium features status
        updatePremiumAccess();

        return sidebarMenu;
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
        // Check if premium access is required for this feature
        if (isPremiumFeature(cardName)) {
            // Get authenticated address from MetaMask
            String userAddress = getCurrentUserAddress();
            
            // Check if the user has premium access
            if (userAddress != null && !premiumAccessManager.hasPremiumAccess(userAddress)) {
                // Show premium access dialog
                premiumAccessManager.showPremiumFeatureDialog(this, () -> {
                    // This will be called if the user wants to purchase premium access
                    premiumAccessManager.openPurchasePage();
                });
                
                // Revert to previously selected menu item
                return;
            }
        }
        
        // Continue with normal menu selection
        if (menuItems.containsKey(currentCard)) {
            menuItems.get(currentCard).setBackground(MENU_BACKGROUND);
        }
        
        if (menuItems.containsKey(cardName)) {
            menuItems.get(cardName).setBackground(MENU_SELECTED);
            currentCard = cardName;
            cardLayout.show(mainContentPanel, cardName);
        }
    }

    /**
     * Check if a feature requires premium access
     * @param featureName Name of the feature
     * @return true if premium access is required
     */
    private boolean isPremiumFeature(String featureName) {
        // Currently, only Audit and Malware are premium features
        return "Audit".equals(featureName) || "Malware".equals(featureName);
    }
    
    // Flag to prevent multiple concurrent refresh operations
    private volatile boolean isPremiumRefreshInProgress = false;
    
    /**
     * Register a clean shutdown hook for the application
     */
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cleanShutdown();
        }));
    }
    
    /**
     * Clean up resources before application shutdown
     */
    private void cleanShutdown() {
        System.out.println("🔄 Application shutting down, cleaning up resources...");
        
        // Clean up premium access manager
        if (premiumAccessManager != null) {
            try {
                premiumAccessManager.shutdown();
            } catch (Exception e) {
                System.err.println("Error shutting down premium access manager: " + e.getMessage());
            }
        }
        
        System.out.println("✅ Cleanup completed. Goodbye!");
    }
    
    /**
     * Get the current user's Ethereum address from authentication
     * @return The user's Ethereum address or null if not authenticated
     */
    private String getCurrentUserAddress() {
        try {
            long currentTime = System.currentTimeMillis();
            boolean verboseLogging = (currentTime - lastAuthCheckTime) >= AUTH_CHECK_INTERVAL;
            
            if (verboseLogging) {
            System.out.println("🔍 Getting current user address...");
                lastAuthCheckTime = currentTime;
            }
            
            // First check if we have a cached address that was previously authenticated
            if (cachedUserAddress != null && !cachedUserAddress.isEmpty()) {
                if (verboseLogging) {
                    System.out.println("✅ Using cached user address: " + cachedUserAddress);
                }
                
                // If we have a cached address, ensure the authentication endpoint knows about it too
                if (AuthenticationEndpoint.getInstance() != null) {
                    AuthenticationStatus status = AuthenticationEndpoint.getInstance().getStatus("default");
                    
                    // If status exists but doesn't have this address, update it
                    if (status != null && (!status.isAuthenticated() || !cachedUserAddress.equals(status.getAccount()))) {
                        if (verboseLogging) {
                            System.out.println("🔄 Updating authentication endpoint with cached address");
                        }
                        status.setAuthenticated(true);
                        status.setAccount(cachedUserAddress);
                    } else if (status == null) {
                        // Create a new status with the cached address if needed
                        if (verboseLogging) {
                            System.out.println("🔄 Creating new authentication status with cached address");
                        }
                        AuthenticationStatus newStatus = AuthenticationEndpoint.getInstance().createNewStatus("default");
                        if (newStatus != null) {
                            newStatus.setAuthenticated(true);
                            newStatus.setAccount(cachedUserAddress);
                        }
                    }
                }
                
                return cachedUserAddress;
            }
            
            if (AuthenticationEndpoint.getInstance() == null) {
                if (verboseLogging) {
                System.out.println("❌ AuthenticationEndpoint instance is null");
                }
                return null;
            }
            
            AuthenticationStatus status = AuthenticationEndpoint.getInstance().getStatus("default");
            if (status == null) {
                if (verboseLogging) {
                System.out.println("❌ Authentication status is null");
                }
                return null;
            }
            
            boolean isAuthenticated = status.isAuthenticated();
            String account = status.getAccount();
            
            if (verboseLogging) {
            System.out.println("🔑 Authentication check:");
            System.out.println("  - Is authenticated: " + isAuthenticated);
                System.out.println("  - Account: " + (account != null ? account : ""));
            System.out.println("  - Error reason: " + status.getErrorReason());
            }
            
            // Check if we have an account even if isAuthenticated is false
            // Sometimes the authentication state doesn't get properly set but account is available
            if (account != null && !account.isEmpty()) {
                // We have an account, so consider the user authenticated even if flag is not set
                if (!isAuthenticated) {
                    if (verboseLogging) {
                        System.out.println("⚠️ Found account but authenticated flag not set, fixing...");
                    }
                    status.setAuthenticated(true);
                    isAuthenticated = true;
                }
                
                if (verboseLogging) {
                System.out.println("✅ User is authenticated with account: " + account);
                }
                // Store in cache for future use
                cachedUserAddress = account;
                return account;
            } else if (isAuthenticated) {
                // This is odd - authenticated but no account
                if (verboseLogging) {
                    System.out.println("⚠️ Authentication flag is set but no account found, resetting status");
                }
                status.setAuthenticated(false);
                return null;
            } else {
                if (verboseLogging) {
                System.out.println("❌ User is not authenticated");
                }
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Error retrieving user address: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
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
                        // Update loading screen with success message
                loadingScreen.updateProgress(100, "Initialization complete! Starting application...");
                        
                        // Small delay to show success message
                        Timer delayTimer = new Timer(1000, e -> {
                            loadingScreen.dispose(); // Close loading screen
                            
                    // Launch main application without requiring authentication
                            SwingUtilities.invokeLater(() -> {
                                showMainApplication();
                            });
                        });
                        delayTimer.setRepeats(false);
                        delayTimer.start();
            }
        };

        worker.execute();
    }
    
    private static void showMainApplication() {
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
                
                // Check if user is not authenticated and show a welcome notification
                String userAddress = dashboard.getCurrentUserAddress();
                if (userAddress == null || userAddress.isEmpty()) {
                    // Show welcome tooltip notification after a short delay
                    Timer notificationTimer = new Timer(1000, event -> {
                        showWalletConnectNotification(dashboard);
                    });
                    notificationTimer.setRepeats(false);
                    notificationTimer.start();
                }
            }
            glassPane.setAlpha(newAlpha);
        });
        fadeInTimer.start();
    }

    /**
     * Show a notification to connect a wallet when the application starts
     */
    private static void showWalletConnectNotification(Main dashboard) {
        // Create notification panel
        JPanel notificationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw panel background with gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 140, 50),
                    0, getHeight(), new Color(200, 100, 20)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Add subtle border
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        notificationPanel.setLayout(new BorderLayout(10, 10));
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Create MetaMask icon
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw fox face
                int size = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Fox face
                g2d.setColor(new Color(250, 200, 70));
                g2d.fillOval(x + size/4, y + size/4, size/2, size/2);
                
                // Left ear
                int[] xPointsLeft = {x + size/3, x + size/5, x + size/3 - 3};
                int[] yPointsLeft = {y + size/3, y + size/8, y + size/4};
                g2d.setColor(new Color(240, 140, 50));
                g2d.fillPolygon(xPointsLeft, yPointsLeft, 3);
                
                // Right ear
                int[] xPointsRight = {x + 2*size/3, x + 4*size/5, x + 2*size/3 + 3};
                int[] yPointsRight = {y + size/3, y + size/8, y + size/4};
                g2d.fillPolygon(xPointsRight, yPointsRight, 3);
                
                // Eyes
                g2d.setColor(new Color(40, 40, 40));
                g2d.fillOval(x + size/3, y + size/2 - size/15, size/12, size/18);
                g2d.fillOval(x + 2*size/3 - size/12, y + size/2 - size/15, size/12, size/18);
            }
        };
        iconPanel.setPreferredSize(new Dimension(60, 60));
        iconPanel.setOpaque(false);
        
        // Create text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Connect Your Wallet");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("<html>Connect your MetaMask wallet to<br>access premium features and secure your account.</html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(new Color(255, 255, 255, 220));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(messageLabel);
        
        // Create connect button
        JButton connectButton = new JButton("Connect Now") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(20, 20, 25));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(40, 40, 45));
                } else {
                    g2d.setColor(new Color(30, 30, 35));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Draw text
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), textX, textY);
            }
        };
        connectButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        connectButton.setBorderPainted(false);
        connectButton.setContentAreaFilled(false);
        connectButton.setFocusPainted(false);
        connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        connectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        connectButton.addActionListener(e -> {
            // Close notification (find and dispose parent window)
            Container c = connectButton.getParent();
            while (c != null && !(c instanceof Window)) {
                c = c.getParent();
            }
            if (c instanceof Window) {
                ((Window) c).dispose();
            }
            
            // Show MetaMask auth dialog
            MetaMaskAuth authDialog = new MetaMaskAuth();
            authDialog.setAuthenticationListener(new MetaMaskAuth.AuthenticationListener() {
                @Override
                public void onAuthenticationSuccess() {
                    dashboard.refreshAllUI();
                }
                
                @Override
                public void onAuthenticationFailure(String reason) {
                    JOptionPane.showMessageDialog(
                        dashboard,
                        "Authentication failed: " + reason,
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            });
            authDialog.setLocationRelativeTo(dashboard);
            authDialog.setVisible(true);
        });
        
        // Close button 
        JButton closeButton = new JButton("×") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 60));
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString("×", (getWidth() - fm.stringWidth("×")) / 2, 
                              (getHeight() + fm.getAscent()) / 2 - 2);
            }
        };
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(20, 20));
        closeButton.addActionListener(e -> {
            // Close notification (find and dispose parent window)
            Container c = closeButton.getParent();
            while (c != null && !(c instanceof Window)) {
                c = c.getParent();
            }
            if (c instanceof Window) {
                ((Window) c).dispose();
            }
        });
        
        // Add buttons panel
        JPanel buttonsPanel = new JPanel(new BorderLayout(5, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(connectButton, BorderLayout.CENTER);
        buttonsPanel.add(closeButton, BorderLayout.EAST);
        
        // Add all components
        notificationPanel.add(iconPanel, BorderLayout.WEST);
        notificationPanel.add(textPanel, BorderLayout.CENTER);
        notificationPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Create and configure the notification dialog
        JDialog notificationDialog = new JDialog(dashboard, "", false);
        notificationDialog.setUndecorated(true);
        notificationDialog.setContentPane(notificationPanel);
        notificationDialog.pack();
        
        // Set location in the top-right corner with slight offset from edges
        int x = dashboard.getX() + dashboard.getWidth() - notificationDialog.getWidth() - 20;
        int y = dashboard.getY() + 80; // Below the header
        notificationDialog.setLocation(x, y);
        
        // Add a fade-in effect
        notificationPanel.setOpaque(false);
        notificationDialog.setBackground(new Color(0, 0, 0, 0));
        
        // Show the notification with animation
        notificationDialog.setOpacity(0.0f);
        notificationDialog.setVisible(true);
        
        // Animate fade in
        Timer fadeTimer = new Timer(30, null);
        final float[] opacity = {0.0f};
        fadeTimer.addActionListener(e -> {
            opacity[0] += 0.05f;
            if (opacity[0] >= 1.0f) {
                opacity[0] = 1.0f;
                fadeTimer.stop();
                
                // Auto-close after 15 seconds if not interacted with
                Timer closeTimer = new Timer(15000, closeEvent -> {
                    notificationDialog.dispose();
                });
                closeTimer.setRepeats(false);
                closeTimer.start();
            }
            notificationDialog.setOpacity(opacity[0]);
        });
        fadeTimer.start();
    }

    /**
     * Handle click on premium feature that is not accessible
     */
    private void handlePremiumFeatureClicked(String featureId) {
        premiumAccessManager.showPremiumFeatureDialog(this, () -> {
            premiumAccessManager.openPurchasePage();
        });
    }
    
    /**
     * Update premium access status
     */
    private void updatePremiumAccess() {
        // Get the current user address from Authentication
        String userAddress = null;
        try {
            if (AuthenticationEndpoint.getInstance() != null && 
                AuthenticationEndpoint.getInstance().getStatus("default") != null && 
                AuthenticationEndpoint.getInstance().getStatus("default").isAuthenticated()) {
                
                userAddress = AuthenticationEndpoint.getInstance().getStatus("default").getAccount();
            }
        } catch (Exception e) {
            System.err.println("Error getting authenticated user: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Check premium access
        boolean hasPremiumAccess = false;
        if (userAddress != null && !userAddress.isEmpty()) {
            System.out.println("Checking premium access for: " + userAddress);
            hasPremiumAccess = premiumAccessManager.hasPremiumAccess(userAddress);
            System.out.println("Premium access for " + userAddress + ": " + hasPremiumAccess);
        } else {
            System.out.println("No authenticated user address found");
        }
        
        // Update the sidebar
        if (sidebarMenu != null) {
            System.out.println("Updating sidebar menu with premium access: " + hasPremiumAccess);
            sidebarMenu.updatePremiumAccess(hasPremiumAccess);
            
            // Force a repaint to ensure the UI updates
            sidebarMenu.revalidate();
            sidebarMenu.repaint();
        } else {
            System.err.println("sidebarMenu is null, cannot update premium access");
        }
    }
    
    @Override
    public void onPremiumAccessUpdated(boolean hasPremiumAccess) {
        // Prevent recursive updates
        if (isPremiumRefreshInProgress) {
            System.out.println("⚠️ Premium refresh already in progress in onPremiumAccessUpdated, skipping");
            return;
        }
        
        isPremiumRefreshInProgress = true;
        try {
            System.out.println("Premium access updated in Main: " + hasPremiumAccess);
            
            // Use SwingUtilities.invokeLater to avoid threading issues
            SwingUtilities.invokeLater(() -> {
                try {
            refreshAllUI();
                } catch (Exception e) {
                    System.err.println("Error in UI refresh: " + e.getMessage());
                    isPremiumRefreshInProgress = false;
                }
            });
        } finally {
            // Only reset the flag here if we're not delegating to refreshAllUI
            // refreshAllUI will reset it when it's done
        }
    }

    /**
     * Manually refresh premium access status
     * This can be called from a menu item or button to force a refresh
     */
    public void refreshPremiumAccess() {
        String userAddress = getCurrentUserAddress();
        
        // Create a debug panel to display detailed information
        StringBuilder debugInfo = new StringBuilder();
        debugInfo.append("==== Premium Access Debug Info ====\n\n");
        
        if (userAddress != null && !userAddress.isEmpty()) {
            debugInfo.append("Authenticated user: ").append(userAddress).append("\n\n");
            
            try {
                // First check cached status
                boolean cachedStatus = premiumAccessManager.hasPremiumAccess(userAddress);
                debugInfo.append("Cached premium status: ").append(cachedStatus).append("\n");
                
                // Try direct contract check first
                boolean directStatus = premiumAccessManager.getBlockchainService().directContractCheck(userAddress);
                debugInfo.append("Direct blockchain call: ").append(directStatus).append("\n");
                
                // Force a blockchain refresh as backup
                boolean hasAccess = premiumAccessManager.refreshPremiumAccess(userAddress);
                debugInfo.append("Premium refresh result: ").append(hasAccess).append("\n");
                
                // Use the most positive result (any true value)
                boolean finalStatus = cachedStatus || directStatus || hasAccess;
                debugInfo.append("Final status used: ").append(finalStatus).append("\n");
                
                debugInfo.append("Contract address: ").append(premiumAccessManager.getContractAddress()).append("\n\n");
                
                // Do a complete UI refresh
                refreshAllUI();
                
                debugInfo.append("UI refresh completed with status: ").append(finalStatus ? "PREMIUM ACCESS ENABLED" : "NO PREMIUM ACCESS").append("\n");
                
                // Show a message to the user
                if (finalStatus) {
                    JOptionPane.showMessageDialog(
                        this,
                        debugInfo.toString(),
                        "Premium Access Verified",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    // Create a custom dialog with detailed information and purchase option
                    JPanel panel = new JPanel(new BorderLayout(10, 10));
                    JTextArea textArea = new JTextArea(debugInfo.toString());
                    textArea.setEditable(false);
                    panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
                    
                    Object[] options = {"Purchase Premium", "OK"};
                    int result = JOptionPane.showOptionDialog(this, panel, 
                                  "Premium Access Required", 
                                  JOptionPane.YES_NO_OPTION,
                                  JOptionPane.QUESTION_MESSAGE,
                                  null, options, options[0]);
                    
                    if (result == 0) {
                        // User clicked "Purchase Premium"
                        premiumAccessManager.openPurchasePage();
                    }
                }
            } catch (Exception e) {
                debugInfo.append("ERROR checking premium access: ").append(e.getMessage()).append("\n");
                e.printStackTrace();
                
                JTextArea textArea = new JTextArea(debugInfo.toString() + "\n\n" + e.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                
                JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Premium Access Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            debugInfo.append("No authenticated user found. Please connect your MetaMask wallet first.\n");
            
            JOptionPane.showMessageDialog(
                this,
                debugInfo.toString(),
                "Authentication Required",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Super aggressive force reset for premium access
     * This will clear all caches, recreate UI components, and force a direct blockchain check
     */
    public void forceResetPremiumAccess() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("🚨 EMERGENCY FORCE RESET INITIATED 🚨");
            String userAddress = getCurrentUserAddress();
            
            // Create a debug panel to display detailed information
            StringBuilder debugInfo = new StringBuilder();
            debugInfo.append("==== FORCE RESET: Premium Access Debug Info ====\n\n");
            
            if (userAddress != null && !userAddress.isEmpty()) {
                debugInfo.append("Authenticated user: ").append(userAddress).append("\n\n");
                
                try {
                    System.out.println("🔥 FORCE RESET: Requesting complete premium status reset");
                    
                    // Force direct contract check multiple times with delays
                    System.out.println("🔄 Attempt 1: Direct contract check");
                    boolean directCheck = premiumAccessManager.getBlockchainService().directContractCheck(userAddress);
                    debugInfo.append("Direct blockchain check #1: ").append(directCheck).append("\n");
                    
                    // Small delay
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                    
                    // Second attempt
                    System.out.println("🔄 Attempt 2: Direct contract check");
                    boolean directCheck2 = premiumAccessManager.getBlockchainService().directContractCheck(userAddress);
                    debugInfo.append("Direct blockchain check #2: ").append(directCheck2).append("\n");
                    
                    // Another small delay
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                    
                    // Force refresh premium access
                    System.out.println("🔄 Refreshing premium access");
                    boolean refreshResult = premiumAccessManager.refreshPremiumAccess(userAddress);
                    debugInfo.append("Premium refresh result: ").append(refreshResult).append("\n");
                    
                    // Clear everything from old session and update UI
                    boolean anyPositiveResult = directCheck || directCheck2 || refreshResult;
                    
                    // One more delay before final check
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                    
                    // Make one final direct contract check to confirm
                    System.out.println("🔄 Final confirmation check");
                    boolean confirmCheck = premiumAccessManager.getBlockchainService().directContractCheck(userAddress);
                    debugInfo.append("Final confirmation check: ").append(confirmCheck).append("\n");
                    
                    // Use most positive result
                    boolean finalStatus = anyPositiveResult || confirmCheck;
                    
                    debugInfo.append("\nContract address: ").append(premiumAccessManager.getContractAddress())
                             .append("\nAny positive result: ").append(anyPositiveResult)
                             .append("\nFinal status: ").append(finalStatus).append("\n\n");
                    
                    // Perform a complete UI refresh
                    System.out.println("🔄 Performing complete UI refresh");
                    refreshAllUI();
                    
                    // Final output
                    debugInfo.append("Force reset completed at: ").append(new java.util.Date()).append("\n")
                             .append("Final premium status: ").append(finalStatus ? "ENABLED" : "DISABLED")
                             .append("\n\nIf premium access is still not working, please restart the application.");
                    
                    // Show confirmation
                    JOptionPane.showMessageDialog(
                        this,
                        new JScrollPane(new JTextArea(debugInfo.toString())),
                        finalStatus ? "Premium Access Enabled" : "Premium Access Issue",
                        finalStatus ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
                    );
                    
                } catch (Exception e) {
                    debugInfo.append("ERROR during force reset: ").append(e.getMessage()).append("\n");
                    e.printStackTrace();
                    
                    JOptionPane.showMessageDialog(
                        this,
                        new JScrollPane(new JTextArea(debugInfo.toString() + "\n\n" + e.toString())),
                        "Error During Force Reset",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No authenticated user found. Please connect your MetaMask wallet first.",
                    "Authentication Required",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }

    /**
     * Force a complete refresh of all UI components related to premium access
     * This ensures that all components have the latest premium access status
     */
    private void refreshAllUI() {
        System.out.println("⚡ REFRESHING ALL UI COMPONENTS");
        
        // Prevent recursive updates
        if (isPremiumRefreshInProgress) {
            System.out.println("⚠️ Premium refresh already in progress, skipping");
            return;
        }
        
        // Get current user address
            String userAddress = getCurrentUserAddress();
        if (userAddress == null || userAddress.isEmpty()) {
            System.out.println("❌ Cannot refresh UI - no user address available");
            return;
        }
            
        try {
            isPremiumRefreshInProgress = true;
            
            // Check premium access for UI updates
                System.out.println("⚡ Checking premium access for UI refresh: " + userAddress);
            boolean hasPremium = false;
            
            try {
                // Try direct contract check first
                hasPremium = premiumAccessManager.getBlockchainService().directContractCheck(userAddress);
                System.out.println("⚡ Direct contract check for UI refresh: " + hasPremium);
            } catch (Exception e) {
                System.out.println("⚠️ Error during premium check - using cached value");
                hasPremium = premiumAccessManager.hasPremiumAccess(userAddress); 
            }
            
            // Update the sidebar menu
            System.out.println("⚡ Updating sidebar with premium access: " + hasPremium);
            if (sidebarMenu != null) {
                sidebarMenu.updatePremiumAccess(hasPremium);
            }
            
            // Notify components that need premium access updates - ONE AT A TIME
            try {
                System.out.println("⚡ Notifying component: Audit");
                for (Component comp : mainContentPanel.getComponents()) {
                    if (comp instanceof Audit) {
                        ((Audit) comp).onPremiumAccessUpdated(hasPremium);
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error updating Audit component: " + e.getMessage());
            }
            
            try {
                System.out.println("⚡ Notifying component: Malware");
                for (Component comp : mainContentPanel.getComponents()) {
                    if (comp instanceof Malware) {
                        ((Malware) comp).onPremiumAccessUpdated(hasPremium);
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error updating Malware component: " + e.getMessage());
            }
        } finally {
            isPremiumRefreshInProgress = false;
        }
    }

    /**
     * Show contract info dialog
     */
    private void showContractInfo() {
        String userAddress = getCurrentUserAddress();
        StringBuilder info = new StringBuilder();
        
        info.append("Premium Features Contract Information\n\n");
        info.append("Contract Address: ").append(premiumAccessManager.getContractAddress()).append("\n\n");
        
        // Check if the user is authenticated
        if (userAddress == null || userAddress.isEmpty()) {
            info.append("No authenticated user. Please connect your MetaMask wallet first.");
        } else {
            info.append("Authenticated User: ").append(userAddress).append("\n\n");
            
            // Direct access checks
            try {
                BlockchainService service = premiumAccessManager.getBlockchainService();
                
                // Try to check direct premium access
                info.append("Premium Access Status:\n");
                try {
                    // Check cached status
                    boolean cachedStatus = premiumAccessManager.hasPremiumAccess(userAddress);
                    info.append("Cached premium status: ").append(cachedStatus).append("\n");
                    
                    // Direct contract check
                    boolean directStatus = service.directContractCheck(userAddress);
                    info.append("Direct contract check: ").append(directStatus).append("\n");
                    
                    // Force refresh check
                    boolean refreshedStatus = premiumAccessManager.refreshPremiumAccess(userAddress);
                    info.append("Force refreshed status: ").append(refreshedStatus).append("\n");
                    
                    // Overall status
                    boolean overallStatus = cachedStatus || directStatus || refreshedStatus;
                    info.append("\nOverall premium status: ").append(overallStatus ? "ENABLED" : "DISABLED").append("\n");
                } catch (Exception e) {
                    info.append("Error in premium checks: ").append(e.getMessage()).append("\n");
                }
                
                // Function signature info
                info.append("\nContract Function Info:\n");
                info.append("Function: hasPremiumAccess(address) => bool\n");
                
            } catch (Exception e) {
                info.append("\nError getting blockchain information: ").append(e.getMessage());
            }
        }
        
        // Create text area with info
        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        // Show dialog
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Premium Contract Info",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Show profile info dialog with improved UI
     */
    private void showProfileInfo() {
        String userAddress = getCurrentUserAddress();
        
        // Create main panel with border layout and modern design
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(CARD_BACKGROUND);
        
        // Create header panel with avatar and basic info
        JPanel headerPanel = new JPanel(new BorderLayout(15, 15));
        headerPanel.setOpaque(false);
        
        // Create avatar panel with animated glow effect
        JPanel avatarPanel = new JPanel() {
            private float animationPhase = 0;
            private final Timer animationTimer;
            
            {
                // Initialize animation timer for pulsing effect
                animationTimer = new Timer(30, e -> {
                    animationPhase += 0.05f;
                    if (animationPhase > 2 * Math.PI) {
                        animationPhase = 0;
                    }
                    repaint();
                });
                animationTimer.start();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Calculate glow size for animation
                float glowSize = 4 + (float)(2 * Math.sin(animationPhase));
                
                // Draw outer glow for authenticated users
                if (userAddress != null && !userAddress.isEmpty()) {
                    // Create outer glow with gradient
                    Color glowColor = new Color(240, 160, 50, 120);
                    RadialGradientPaint gradient = new RadialGradientPaint(
                        getWidth()/2, getHeight()/2, getWidth()/2 + glowSize,
                        new float[]{0.0f, 1.0f},
                        new Color[]{
                            new Color(240, 180, 50, 150),
                            new Color(240, 160, 50, 0)
                        }
                    );
                    
                    g2d.setPaint(gradient);
                    g2d.fillOval(-10, -10, getWidth() + 20, getHeight() + 20);
                }
                
                // Draw avatar background with gradient
                if (userAddress != null && !userAddress.isEmpty()) {
                    // Connected user has colorful gradient
                    GradientPaint bgGradient = new GradientPaint(
                        0, 0, new Color(240, 140, 50),
                        getWidth(), getHeight(), new Color(240, 100, 30)
                    );
                    g2d.setPaint(bgGradient);
                } else {
                    // Not connected user has gray gradient
                    g2d.setColor(new Color(60, 63, 66));
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                // Draw profile image
                if (userAddress != null && !userAddress.isEmpty()) {
                    // Draw MetaMask fox for connected users
                    g2d.setColor(new Color(250, 200, 70));
                    g2d.fillOval(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2);
                    
                    // Fox ears
                    int[] leftEarX = {getWidth()/3, getWidth()/5, getWidth()/3-3};
                    int[] leftEarY = {getHeight()/3, getHeight()/8, getHeight()/4};
                    g2d.setColor(new Color(240, 140, 30));
                    g2d.fillPolygon(leftEarX, leftEarY, 3);
                    
                    int[] rightEarX = {2*getWidth()/3, 4*getWidth()/5, 2*getWidth()/3+3};
                    int[] rightEarY = {getHeight()/3, getHeight()/8, getHeight()/4};
                    g2d.fillPolygon(rightEarX, rightEarY, 3);
                    
                    // Eyes
                    g2d.setColor(new Color(40, 40, 40));
                    g2d.fillOval(getWidth()/3, getHeight()/2 - getHeight()/12, getWidth()/12, getHeight()/18);
                    g2d.fillOval(2*getWidth()/3 - getWidth()/12, getHeight()/2 - getHeight()/12, getWidth()/12, getHeight()/18);
                    
                    // Smile
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawArc(getWidth()/3, getHeight()/2, getWidth()/3, getHeight()/6, 0, 180);
                } else {
                    // Draw silhouette for not connected users
                    g2d.setColor(new Color(45, 47, 50));
                    g2d.fillOval(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2);
                    
                    // Draw question mark
                    g2d.setColor(new Color(80, 83, 88));
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, getWidth()/3));
                    FontMetrics fm = g2d.getFontMetrics();
                    g2d.drawString("?", (getWidth() - fm.stringWidth("?"))/2, 
                                   getHeight()/2 + fm.getAscent()/2);
                }
            }
            
            @Override
            public void removeNotify() {
                super.removeNotify();
                if (animationTimer != null && animationTimer.isRunning()) {
                    animationTimer.stop();
                }
            }
        };
        avatarPanel.setPreferredSize(new Dimension(100, 100));
        
        // Create info panel with improved styling
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        // Add title with animation for connected status
        JLabel titleLabel = new JLabel(userAddress != null ? "MetaMask Connected" : "MetaMask Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(userAddress != null ? new Color(80, 200, 120) : TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add address with truncation and copy-on-click functionality
        String displayAddress = (userAddress != null && !userAddress.isEmpty()) 
            ? userAddress.substring(0, 8) + "..." + userAddress.substring(userAddress.length() - 6)
            : "Not Connected";
            
        // Create custom address label with hover effect and click to copy
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)) {
            private boolean hover = false;
            {
                setOpaque(false);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (userAddress != null && !userAddress.isEmpty()) {
                            hover = true;
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                            repaint();
                        }
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        repaint();
                    }
                    
                    @Override
                    public void mouseClicked(MouseEvent e) {
        if (userAddress != null && !userAddress.isEmpty()) {
                            // Copy address to clipboard
                            try {
                                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                                    new java.awt.datatransfer.StringSelection(userAddress), null);
                                
                                // Show visual feedback
                                setBackground(new Color(80, 200, 120, 100));
                                repaint();
                                
                                // Reset background after delay
                                Timer resetTimer = new Timer(1500, resetEvent -> {
                                    setBackground(null);
                                    repaint();
                                });
                                resetTimer.setRepeats(false);
                                resetTimer.start();
                                
                                // Create a small hint by changing label text temporarily
                                for (Component c : getComponents()) {
                                    if (c instanceof JLabel && ((JLabel)c).getText().equals("(click to copy)")) {
                                        JLabel label = (JLabel)c;
                                        String originalText = label.getText();
                                        label.setText("Copied!");
                                        label.setForeground(new Color(80, 200, 120));
                                        
                                        // Reset label after delay
                                        Timer resetLabelTimer = new Timer(1500, resetLabelEvent -> {
                                            label.setText(originalText);
                                            label.setForeground(new Color(120, 120, 160));
                                        });
                                        resetLabelTimer.setRepeats(false);
                                        resetLabelTimer.start();
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                System.err.println("Failed to copy address: " + ex.getMessage());
                            }
                        }
                    }
                });
            }
            
                @Override
                protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (hover && userAddress != null && !userAddress.isEmpty()) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(60, 63, 68));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                }
            }
        };
        addressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel addressLabel = new JLabel(displayAddress);
        addressLabel.setFont(new Font("Segoe UI Mono", Font.PLAIN, 16));
        addressLabel.setForeground(new Color(200, 200, 200));
        
        // Add copy icon if address is available
        if (userAddress != null && !userAddress.isEmpty()) {
            JLabel copyIcon = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(120, 120, 160));
                    
                    // Draw copy icon
                    int padding = 2;
                    int size = Math.min(getWidth(), getHeight()) - (padding * 2);
                    g2d.drawRect(padding, padding, size * 2/3, size * 2/3);
                    g2d.drawRect(padding + size/3, padding + size/3, size * 2/3, size * 2/3);
                    g2d.setColor(new Color(60, 63, 68));
                    g2d.fillRect(padding + size/3 + 1, padding + 1, size * 2/3 - 1, size/3);
                }
            };
            copyIcon.setPreferredSize(new Dimension(16, 16));
            addressPanel.add(addressLabel);
            addressPanel.add(copyIcon);
            
            // Add click to copy hint
            JLabel copyHintLabel = new JLabel("(click to copy)");
            copyHintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            copyHintLabel.setForeground(new Color(120, 120, 160));
            addressPanel.add(copyHintLabel);
        } else {
            addressPanel.add(addressLabel);
        }
        
        // Add connection status
        JLabel statusLabel = new JLabel(userAddress != null && !userAddress.isEmpty() 
            ? "Wallet is securely connected" : "Wallet not connected");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(userAddress != null && !userAddress.isEmpty() 
            ? new Color(80, 200, 120) : new Color(220, 80, 80));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(addressPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(statusLabel);
        
        headerPanel.add(avatarPanel, BorderLayout.WEST);
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Create button container with modern styling
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Create MetaMask connect button with animated effects
        JButton connectButton = new JButton(userAddress != null ? "Disconnect Wallet" : "Connect MetaMask Wallet") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                if (userAddress != null) {
                    // Disconnect button (red)
                    if (getModel().isPressed()) {
                        g2d.setColor(new Color(180, 40, 40));
                    } else if (getModel().isRollover()) {
                        g2d.setColor(new Color(220, 45, 45));
                    } else {
                        g2d.setColor(new Color(200, 45, 45));
                    }
                } else {
                    // Connect button (MetaMask orange)
                    if (getModel().isPressed()) {
                        g2d.setColor(new Color(200, 100, 20));
                    } else if (getModel().isRollover()) {
                        g2d.setColor(new Color(250, 140, 50));
                    } else {
                        g2d.setColor(new Color(240, 140, 50));
                    }
                    }
                    
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Add MetaMask icon for connect button or disconnect icon for disconnect button
                int iconSize = getHeight() - 10;
                int iconX = 10;
                int iconY = 5;
                
                if (userAddress == null) {
                    // Draw fox face for connect button
                    g2d.setColor(new Color(250, 180, 80));
                    g2d.fillOval(iconX + iconSize/4, iconY + iconSize/4, iconSize/2, iconSize/2);
                    
                    // Draw fox ears
                    int[] leftEarX = {iconX + iconSize/4, iconX, iconX + iconSize/3};
                    int[] leftEarY = {iconY + iconSize/4, iconY, iconY + iconSize/5};
                    g2d.setColor(new Color(220, 120, 30));
                    g2d.fillPolygon(leftEarX, leftEarY, 3);
                    
                    int[] rightEarX = {iconX + 3*iconSize/4, iconX + iconSize, iconX + 2*iconSize/3};
                    int[] rightEarY = {iconY + iconSize/4, iconY, iconY + iconSize/5};
                    g2d.fillPolygon(rightEarX, rightEarY, 3);
                } else {
                    // Draw disconnect icon
                    g2d.setColor(new Color(255, 200, 200));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(iconX, iconY, iconSize, iconSize);
                    g2d.drawLine(iconX + 4, iconY + 4, iconX + iconSize - 4, iconY + iconSize - 4);
                    g2d.drawLine(iconX + iconSize - 4, iconY + 4, iconX + 4, iconY + iconSize - 4);
                }
                
                // Position text to account for icon
                String text = getText();
                FontMetrics fm = g2d.getFontMetrics();
                int textX = iconX + iconSize + 8;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, textX, textY);
            }
        };
        connectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        connectButton.setBorderPainted(false);
        connectButton.setContentAreaFilled(false);
        connectButton.setFocusPainted(false);
        connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        connectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        connectButton.setBorder(BorderFactory.createEmptyBorder(12, 45, 12, 20));
        
        // Handle connect/disconnect action
        connectButton.addActionListener(e -> {
            if (userAddress != null && !userAddress.isEmpty()) {
                // Disconnect action
                int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to disconnect your wallet?",
                    "Disconnect Wallet",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    // Clear authentication info
                    clearAuthInfo();
                    
                    // Close dialog and reopen with updated state
                    Window window = SwingUtilities.getWindowAncestor(connectButton);
                        if (window instanceof JDialog) {
                            ((JDialog) window).dispose();
                        }
                        
                    // Refresh UI components that depend on authentication
                    refreshAllUI();
                    
                    // Show profile again after short delay
                    Timer timer = new Timer(300, event -> showProfileInfo());
                            timer.setRepeats(false);
                            timer.start();
                }
        } else {
                // Connect action
                // Close the current dialog
                Window window = SwingUtilities.getWindowAncestor(connectButton);
                if (window instanceof JDialog) {
                    ((JDialog) window).dispose();
                }
                
                // Show MetaMask auth dialog
                MetaMaskAuth authDialog = new MetaMaskAuth();
                authDialog.setAuthenticationListener(new MetaMaskAuth.AuthenticationListener() {
                    @Override
                    public void onAuthenticationSuccess() {
                        // Refresh UI and show profile dialog again with updated info
                        refreshAllUI();
                        // Use SwingUtilities.invokeLater to ensure the auth dialog is fully closed
                        SwingUtilities.invokeLater(() -> {
                            // Short delay before showing profile again
                            Timer timer = new Timer(500, event -> showProfileInfo());
                            timer.setRepeats(false);
                            timer.start();
                        });
                    }
                    
                    @Override
                    public void onAuthenticationFailure(String reason) {
                        // Show error message
                        JOptionPane.showMessageDialog(
                            Main.this,
                            "Authentication failed: " + reason,
                            "MetaMask Connection Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
                authDialog.setLocationRelativeTo(Main.this);
                authDialog.setVisible(true);
            }
        });
        
        // Add button to panel
        buttonPanel.add(connectButton);
        
        // Add additional buttons if needed
        if (userAddress != null && !userAddress.isEmpty()) {
            // Add refresh button
            JButton refreshButton = new JButton("Refresh Premium Status") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isPressed()) {
                        g2d.setColor(new Color(30, 80, 190));
                    } else if (getModel().isRollover()) {
                        g2d.setColor(new Color(60, 110, 230));
                    } else {
                        g2d.setColor(new Color(50, 100, 220));
                    }
                    
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    
                    // Add refresh icon
                    int iconSize = getHeight() - 10;
                    int iconX = 10;
                    int iconY = 5;
                    
                    // Draw refresh icon
                    g2d.setColor(new Color(200, 230, 255));
                    g2d.setStroke(new BasicStroke(2));
                    int arcSize = iconSize - 8;
                    g2d.drawArc(iconX + 4, iconY + 4, arcSize, arcSize, 45, 270);
                    
                    // Draw arrow head
                    int[] arrowX = {iconX + arcSize - 2, iconX + arcSize + 4, iconX + arcSize + 7};
                    int[] arrowY = {iconY + 8, iconY + 8, iconY + 14};
                    g2d.fillPolygon(arrowX, arrowY, 3);
                    
                    // Position text to account for icon
                    String text = getText();
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = iconX + iconSize + 8;
                    int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, textX, textY);
                }
            };
            refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            refreshButton.setBorderPainted(false);
            refreshButton.setContentAreaFilled(false);
            refreshButton.setFocusPainted(false);
            refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            refreshButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            refreshButton.setBorder(BorderFactory.createEmptyBorder(12, 45, 12, 20));
            refreshButton.addActionListener(e -> {
                // Get the dialog reference
                Window window = SwingUtilities.getWindowAncestor(refreshButton);
                if (window instanceof JDialog) {
                    handleProfileRefresh((JDialog) window);
                }
            });
            
            // Add spacing between buttons
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            buttonPanel.add(refreshButton);
        }
        
        // Add header and button panel to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Create and show dialog
        JDialog dialog = new JDialog(this, "Profile", true);
        dialog.setContentPane(mainPanel);
        dialog.pack();
        dialog.setSize(new Dimension(450, userAddress != null ? 280 : 240));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    /**
     * Handle profile refresh action
     */
    private void handleProfileRefresh(JDialog dialog) {
        // Close the dialog
        if (dialog != null) {
            dialog.dispose();
        }
        
        // Refresh premium access
        refreshPremiumAccess();
        
        // Show profile again after short delay
        Timer timer = new Timer(300, event -> showProfileInfo());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Clear authentication information
     */
    private void clearAuthInfo() {
        System.out.println("🔄 Clearing authentication information...");
        
        // Clear cached address
        cachedUserAddress = null;
        
        // Clear authentication endpoint status
        try {
            if (AuthenticationEndpoint.getInstance() != null) {
                AuthenticationStatus status = AuthenticationEndpoint.getInstance().getStatus("default");
                if (status != null) {
                    status.setAuthenticated(false);
                    status.setAccount(null);
                    System.out.println("✅ Cleared authentication status");
                }
            }
        } catch (Exception e) {
            System.err.println("Error clearing authentication status: " + e.getMessage());
        }
        
        System.out.println("✅ Authentication information cleared");
    }
}