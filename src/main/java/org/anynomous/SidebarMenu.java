package main.java.org.anynomous;

import main.java.org.anynomous.utils.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A customizable sidebar menu component for navigation
 */
public class SidebarMenu extends JPanel {
    // Color scheme - can be customized through constructor or setters
    private Color backgroundColor = new Color(8, 8, 10);
    private Color textColor = new Color(255, 255, 255);
    private Color selectedColor = new Color(20, 22, 28);
    private Color hoverColor = new Color(25, 27, 32);
    private Color borderColor = new Color(35, 38, 45);
    private static final Color DISABLED_TEXT_COLOR = new Color(120, 120, 120);
    
    // Sidebar configuration
    private final int menuWidth;
    private final boolean showIcons;
    private final int iconSize = 20;
    private final int spacing = 5;
    
    // State tracking
    private final Map<String, JPanel> menuItems = new HashMap<>();
    private final Map<String, String> menuDisplayNames = new HashMap<>();
    private final Map<String, Color> menuItemColors = new HashMap<>();
    private final Map<String, Boolean> premiumFeatures = new HashMap<>();
    private String currentSelection = null;
    
    // Icon URLs for menu items
    private static final Map<String, String> ICON_URLS = new HashMap<>();
    static {
        // Free SVG icons from Bootstrap Icons
        ICON_URLS.put("Home", "house.svg");
        ICON_URLS.put("Clean", "trash.svg");
        ICON_URLS.put("Troubleshoot", "tools.svg");
        ICON_URLS.put("Firewall", "shield.svg");
        ICON_URLS.put("Audit", "clipboard-check.svg");
        ICON_URLS.put("Remediate", "bandaid.svg");
        ICON_URLS.put("Malware", "bug.svg");
        ICON_URLS.put("Assistant", "headset.svg");
    }
    
    // Callback for when a menu item is selected
    private Consumer<String> onSelectionChanged;
    
    // Add a flag to track if premium features are accessible
    private Map<String, Boolean> premiumAccessible = new HashMap<>();
    
    // Add a callback for premium feature clicks
    private Consumer<String> onPremiumFeatureClicked;
    
    /**
     * Create a sidebar menu with default settings
     * 
     * @param width Width of the sidebar
     */
    public SidebarMenu(int width) {
        this(width, true);
    }
    
    /**
     * Create a sidebar menu
     * 
     * @param width Width of the sidebar
     * @param showIcons Whether to show icons next to menu items
     */
    public SidebarMenu(int width, boolean showIcons) {
        this.menuWidth = width;
        this.showIcons = showIcons;
        
        setPreferredSize(new Dimension(width, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        
        // Custom painting for the background
        setOpaque(false);
        
        // Initialize collections
        menuItems.clear();
        menuDisplayNames.clear();
        menuItemColors.clear();
        premiumFeatures.clear();
        premiumAccessible.clear();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, backgroundColor,
            getWidth(), 0, new Color(
                Math.max(0, backgroundColor.getRed() - 10),
                Math.max(0, backgroundColor.getGreen() - 10),
                Math.max(0, backgroundColor.getBlue() - 10)
            )
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw subtle right border
        g2d.setColor(borderColor);
        g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
    }
    
    /**
     * Create a category label for the sidebar
     * 
     * @param categoryName The name of the category
     */
    public void addCategory(String categoryName) {
        JLabel category = new JLabel(categoryName);
        category.setForeground(new Color(150, 150, 160));
        category.setFont(new Font("Segoe UI", Font.BOLD, 11));
        category.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 10));
        category.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(category);
    }
    
    /**
     * Add a menu item to the sidebar
     * 
     * @param id Unique identifier for the menu item
     * @param displayText Text to display
     * @param iconColor Color for the icon
     */
    public void addMenuItem(String id, String displayText, Color iconColor) {
        addMenuItem(id, displayText, iconColor, false);
    }
    
    /**
     * Add a menu item to the sidebar with premium status
     * 
     * @param id Unique identifier for the menu item
     * @param displayText Text to display
     * @param iconColor Color for the icon
     * @param isPremium Whether this is a premium feature
     */
    public void addMenuItem(String id, String displayText, Color iconColor, boolean isPremium) {
        JPanel item = new JPanel();
        item.setName(id);
        item.setLayout(new BoxLayout(item, BoxLayout.X_AXIS));
        item.setBackground(backgroundColor);
        item.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Store the display name and color for later use
        menuDisplayNames.put(id, displayText);
        menuItemColors.put(id, iconColor);
        premiumFeatures.put(id, isPremium);
        
        if (showIcons) {
            // Get icon URL for this menu item
            String iconUrl = ICON_URLS.getOrDefault(id, "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/icons/circle.svg");
            
            // Create a label with the icon
            JLabel iconLabel = new JLabel();
            iconLabel.setPreferredSize(new Dimension(iconSize, iconSize));
            
            // Load icon in a background thread
            SwingWorker<ImageIcon, Void> iconLoader = new SwingWorker<>() {
                @Override
                protected ImageIcon doInBackground() {
                    // Use a grayed out icon color for premium features that are not accessible
                    Color actualIconColor = isPremium ? new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), 120) : iconColor;
                    return IconLoader.loadIcon(iconUrl, iconSize, iconSize, actualIconColor);
                }
                
                @Override
                protected void done() {
                    try {
                        iconLabel.setIcon(get());
                        item.revalidate();
                        item.repaint();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            iconLoader.execute();
            
            item.add(iconLabel);
            item.add(Box.createRigidArea(new Dimension(15, 0)));
        }
        
        // Menu text
        JLabel label = new JLabel(displayText);
        // Use grayed out text for premium features that are not accessible
        label.setForeground(isPremium ? DISABLED_TEXT_COLOR : textColor);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));

        item.add(label);
        
        // Add premium badge if this is a premium feature
        if (isPremium) {
            item.add(Box.createRigidArea(new Dimension(10, 0)));
            JLabel premiumLabel = new JLabel("PRO");
            premiumLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
            premiumLabel.setForeground(new Color(255, 215, 0)); // Gold color
            premiumLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 215, 0), 1, true),
                    BorderFactory.createEmptyBorder(1, 3, 1, 3)
            ));
            item.add(premiumLabel);
        }
        
        item.add(Box.createHorizontalGlue());

        // Add hover and selection effects only for non-premium or accessible premium items
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Only respond to clicks for non-premium items or if premium is accessible
                if (!isPremium || isPremiumAccessible(id)) {
                    selectMenuItem(id);
                } else {
                    // For premium features without access, show premium dialog
                    if (onPremiumFeatureClicked != null) {
                        onPremiumFeatureClicked.accept(id);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Only show hover for non-premium items or if premium is accessible
                if (!isPremium || isPremiumAccessible(id)) {
                    if (!id.equals(currentSelection)) {
                        item.setBackground(hoverColor);
                    }
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!id.equals(currentSelection)) {
                    item.setBackground(backgroundColor);
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Store menu item reference for selection handling
        menuItems.put(id, item);
        add(item);
        add(Box.createRigidArea(new Dimension(0, spacing)));
    }
    
    /**
     * Add flexible space in the sidebar
     */
    public void addFlexibleSpace() {
        add(Box.createVerticalGlue());
    }
    
    /**
     * Add any custom component to the sidebar
     * 
     * @param component The component to add
     */
    public void addComponent(JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(component);
    }
    
    /**
     * Select a menu item programmatically
     * 
     * @param id The ID of the menu item to select
     * @return true if the menu item was found and selected
     */
    public boolean selectMenuItem(String id) {
        if (!menuItems.containsKey(id)) {
            return false;
        }
        
        // Deselect previous item
        if (currentSelection != null && menuItems.containsKey(currentSelection)) {
            JPanel previousItem = menuItems.get(currentSelection);
            previousItem.setBackground(backgroundColor);
        }
        
        // Select new item
        currentSelection = id;
        JPanel newItem = menuItems.get(id);
        newItem.setBackground(selectedColor);
        
        // Notify listeners
        if (onSelectionChanged != null) {
            onSelectionChanged.accept(id);
        }
        
        return true;
    }
    
    /**
     * Get the currently selected menu item ID
     */
    public String getSelectedItem() {
        return currentSelection;
    }
    
    /**
     * Set callback for selection changes
     * 
     * @param callback Function to call when selection changes
     */
    public void setOnSelectionChanged(Consumer<String> callback) {
        this.onSelectionChanged = callback;
    }
    
    /**
     * Set the background color of the sidebar
     * 
     * @param color The new background color
     */
    public void setMenuBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
        
        // Update non-selected menu items
        for (Map.Entry<String, JPanel> entry : menuItems.entrySet()) {
            if (!entry.getKey().equals(currentSelection)) {
                entry.getValue().setBackground(color);
            }
        }
    }
    
    /**
     * Set the color used for selected menu items
     * 
     * @param color The new selected color
     */
    public void setSelectedColor(Color color) {
        this.selectedColor = color;
        
        // Update currently selected menu item if there is one
        if (currentSelection != null && menuItems.containsKey(currentSelection)) {
            menuItems.get(currentSelection).setBackground(color);
        }
    }
    
    /**
     * Set the color used for hover effects
     * 
     * @param color The new hover color
     */
    public void setHoverColor(Color color) {
        this.hoverColor = color;
    }
    
    /**
     * Set the color used for text
     * 
     * @param color The new text color
     */
    public void setTextColor(Color color) {
        this.textColor = color;
        
        // Update all labels
        for (Component c : getComponents()) {
            if (c instanceof JPanel) {
                for (Component inner : ((JPanel) c).getComponents()) {
                    if (inner instanceof JLabel) {
                        inner.setForeground(color);
                    }
                }
            }
        }
    }
    
    /**
     * Check if a menu item is a premium feature
     * 
     * @param id The ID of the menu item
     * @return true if the menu item is a premium feature
     */
    public boolean isPremiumFeature(String id) {
        return premiumFeatures.getOrDefault(id, false);
    }
    
    /**
     * Set callback for when a premium feature is clicked but not accessible
     */
    public void setOnPremiumFeatureClicked(Consumer<String> callback) {
        this.onPremiumFeatureClicked = callback;
    }
    
    /**
     * Update the premium access status for menu items
     * 
     * @param hasAccess Whether the user has premium access
     */
    public void updatePremiumAccess(boolean hasAccess) {
        System.out.println("🎮 SidebarMenu.updatePremiumAccess(" + hasAccess + ")");
        
        try {
            // Reset our tracking map
            premiumAccessible.clear();
            
            // Initialize all premium features with the current access status
            for (String id : menuItems.keySet()) {
                if (isPremiumFeature(id)) {
                    System.out.println("🎮 Updating premium menu item: " + id + " with access: " + hasAccess);
                    premiumAccessible.put(id, hasAccess);
                    
                    // Get the menu item panel
                    JPanel item = menuItems.get(id);
                    if (item != null) {
                        // Force update of all components in the panel
                        for (Component comp : item.getComponents()) {
                            if (comp instanceof JLabel) {
                                JLabel label = (JLabel) comp;
                                // Don't update the PRO badge color
                                if (!label.getText().equals("PRO")) {
                                    label.setForeground(hasAccess ? textColor : DISABLED_TEXT_COLOR);
                                    System.out.println("🎮 Updated label for " + id + ": " + label.getText());
                                }
                            }
                        }
                        
                        // Force repaint
                        item.invalidate();
                        item.validate();
                        item.repaint();
                    }
                }
            }
            
            // Force full repaint of the sidebar
            invalidate();
            validate();
            repaint();
            
            // Debug output of premium access status
            System.out.println("🎮 Premium access status after update:");
            for (String id : premiumAccessible.keySet()) {
                System.out.println("🎮   " + id + ": " + premiumAccessible.get(id));
            }
        } catch (Exception e) {
            System.err.println("Error updating premium access: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Check if a premium feature is accessible
     */
    private boolean isPremiumAccessible(String id) {
        boolean isPremium = isPremiumFeature(id);
        Boolean storedAccess = premiumAccessible.get(id);
        boolean isAccessible = !isPremium || (storedAccess != null && storedAccess);
        
        System.out.println("🔍 Access check for " + id + ": isPremium=" + isPremium 
            + ", stored access=" + storedAccess 
            + ", final result=" + isAccessible);
        
        return isAccessible;
    }
}
