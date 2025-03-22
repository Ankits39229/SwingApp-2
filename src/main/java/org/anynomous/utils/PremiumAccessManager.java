package main.java.org.anynomous.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PremiumAccessManager {
    private static PremiumAccessManager instance;
    private final List<PremiumAccessListener> listeners = new ArrayList<>();
    private final BlockchainService blockchainService;
    private final Map<String, Boolean> accessCache = new HashMap<>();
    private volatile boolean isShuttingDown = false;
    
    // Add a listener for premium access changes
    public interface PremiumAccessListener {
        void onPremiumAccessUpdated(boolean hasPremiumAccess);
    }
    
    private final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    private final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutes
    
    // Singleton pattern
    private PremiumAccessManager() {
        blockchainService = new BlockchainService(null); // Use default contract address
        
        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                instance.shutdown();
            }
        }));
    }
    
    public static synchronized PremiumAccessManager getInstance() {
        if (instance == null) {
            instance = new PremiumAccessManager();
        }
        return instance;
    }
    
    /**
     * Properly shutdown the manager
     */
    public void shutdown() {
        if (isShuttingDown) {
            return;
        }
        
        isShuttingDown = true;
        System.out.println("🔒 PremiumAccessManager shutting down...");
    }
    
    /**
     * Check if a user has premium access
     * @param address Ethereum wallet address to check
     * @return true if the user has premium access
     */
    public boolean hasPremiumAccess(String address) {
        if (isShuttingDown) {
            return accessCache.getOrDefault(address, false);
        }
        
        if (address == null || address.isEmpty()) {
            return false;
        }
        
        // Check cache first
        if (accessCache.containsKey(address)) {
            return accessCache.get(address);
        }
        
        // Not in cache, check blockchain
        boolean hasAccess = blockchainService.checkPremiumAccess(address);
        accessCache.put(address, hasAccess);
        
        return hasAccess;
    }
    
    /**
     * Show a premium access dialog to the user
     * @param parentComponent Parent component for dialog
     * @param onPurchaseRequested Callback for when the user wants to purchase premium access
     */
    public void showPremiumFeatureDialog(Component parentComponent, Runnable onPurchaseRequested) {
        if (isShuttingDown) {
            return;
        }
        
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(parentComponent), "Premium Feature", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(parentComponent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Dialog content
        JLabel titleLabel = new JLabel("Premium Feature Required");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JTextArea messageArea = new JTextArea(
            "This feature requires premium access. To unlock this feature:\n\n" +
            "1. Connect your MetaMask wallet\n" +
            "2. Switch to Sepolia test network\n" +
            "3. Purchase premium access with Sepolia ETH\n\n" +
            "You can get Sepolia ETH from a faucet."
        );
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setBackground(mainPanel.getBackground());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton purchaseButton = new JButton("Purchase Access");
        purchaseButton.addActionListener(e -> {
            dialog.dispose();
            if (onPurchaseRequested != null) {
                onPurchaseRequested.run();
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(purchaseButton);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(messageArea, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Open browser to purchase premium access
     */
    public void openPurchasePage() {
        if (isShuttingDown) {
            return;
        }
        
        try {
            String url = blockchainService.getPurchaseUrl();
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error opening purchase page: " + e.getMessage());
            JOptionPane.showMessageDialog(
                null,
                "Could not open the premium purchase page. Please visit manually: " + 
                blockchainService.getPurchaseUrl(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Add a listener to be notified when premium access status changes
     * 
     * @param listener The listener to add
     */
    public void addPremiumAccessListener(PremiumAccessListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Remove a premium access listener
     * 
     * @param listener The listener to remove
     */
    public void removePremiumAccessListener(PremiumAccessListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Refresh premium access status for the given user address
     * 
     * @param address The Ethereum address to refresh
     * @return true if the user has premium access after refresh
     */
    public boolean refreshPremiumAccess(String address) {
        if (isShuttingDown) {
            System.out.println("🔒 PremiumAccessManager is shutting down - skipping refresh");
            return accessCache.getOrDefault(address, false);
        }
        
        if (address == null || address.isEmpty()) {
            return false;
        }
        
        try {
            // Clear cache for this address
            accessCache.remove(address);
            
            // Get fresh data from blockchain
            boolean hasAccess = blockchainService.forceRefreshPremiumAccess(address);
            accessCache.put(address, hasAccess);
            
            System.out.println("🔄 Premium access refreshed for " + address + ": " + hasAccess);
            
            // Notify listeners
            notifyListeners(hasAccess);
            
            return hasAccess;
        } catch (Exception e) {
            System.err.println("Error refreshing premium access: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the contract address used for premium features
     * 
     * @return The contract address
     */
    public String getContractAddress() {
        return blockchainService.getContractAddress();
    }
    
    /**
     * Get the blockchain service instance
     * @return The blockchain service instance
     */
    public BlockchainService getBlockchainService() {
        return blockchainService;
    }
    
    /**
     * Clear the premium access cache
     */
    public void clearCache() {
        accessCache.clear();
    }
    
    /**
     * Notify all listeners of a premium access status change
     * @param hasPremium Whether the user has premium access
     */
    private void notifyListeners(boolean hasPremium) {
        if (isShuttingDown) {
            return;
        }
        
        System.out.println("📢 Notifying listeners of premium access update: " + hasPremium);
        
        for (PremiumAccessListener listener : listeners) {
            try {
                System.out.println("📢 Notifying listener: " + listener.getClass().getSimpleName());
                listener.onPremiumAccessUpdated(hasPremium);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }
} 