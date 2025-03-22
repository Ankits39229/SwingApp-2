package main.java.org.anynomous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MetaMaskAuth extends JFrame {
    private JLabel statusLabel;
    private JButton connectButton;
    private JPanel mainPanel;
    private boolean isAuthenticated = false;
    private static final int MAX_AUTH_ATTEMPTS = 3;
    private static final int AUTH_TIMEOUT_SECONDS = 60;
    private int authAttempts = 0;
    private boolean authenticationInProgress = false;
    
    // Callback interfaces for authentication results
    public interface AuthenticationListener {
        void onAuthenticationSuccess();
        void onAuthenticationFailure(String reason);
    }
    
    private AuthenticationListener authListener;

    public MetaMaskAuth() {
        setTitle("Connect MetaMask Wallet");
        setSize(450, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent closing without proper handling

        // Create main panel with better styling
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Status label with improved text
        statusLabel = new JLabel("Please connect your MetaMask wallet to access premium features");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Connect button with better styling
        connectButton = new JButton("Connect MetaMask");
        connectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        connectButton.addActionListener(e -> connectMetaMask());

        // Add components to panel
        mainPanel.add(statusLabel, BorderLayout.CENTER);
        mainPanel.add(connectButton, BorderLayout.SOUTH);

        // Set content pane
        setContentPane(mainPanel);

        // Add window close listener
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (!isAuthenticated) {
                    // Simplified message that makes it clear the app won't exit
                    int result = JOptionPane.showConfirmDialog(MetaMaskAuth.this,
                        "Close wallet connection dialog? You can connect later from the application.",
                        "Close Dialog",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (result == JOptionPane.YES_OPTION) {
                        if (authListener != null) {
                            authListener.onAuthenticationFailure("User closed authentication dialog");
                        }
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
    }
    
    public void setAuthenticationListener(AuthenticationListener listener) {
        this.authListener = listener;
    }

    private void connectMetaMask() {
        // Prevent multiple simultaneous authentication attempts
        if (authenticationInProgress) {
            System.out.println("Authentication already in progress, ignoring new attempt");
            return;
        }

        if (authAttempts >= MAX_AUTH_ATTEMPTS) {
            JOptionPane.showMessageDialog(this,
                "Maximum authentication attempts reached. Application will exit.",
                "Authentication Failed",
                JOptionPane.ERROR_MESSAGE);
            
            if (authListener != null) {
                authListener.onAuthenticationFailure("Maximum authentication attempts reached");
            }
            dispose();
            return;
        }

        authenticationInProgress = true;
        connectButton.setEnabled(false);
        statusLabel.setText("Connecting to MetaMask... (Attempt " + (authAttempts + 1) + "/" + MAX_AUTH_ATTEMPTS + ")");

        // Generate a unique transaction ID for this authentication attempt
        String txId = "auth_" + System.currentTimeMillis();
        System.out.println("Starting authentication with txId: " + txId);
        
        // Create a fresh authentication status for this attempt
        AuthenticationEndpoint authEndpoint = AuthenticationEndpoint.getInstance();
        authEndpoint.createNewStatus(txId);
        
        // Open the MetaMask connection page with parameters that trigger authentication
        try {
            // Create URL with parameters to trigger MetaMask authentication immediately
            URI uri = new URI("http://localhost:8080/share.html?" + 
                               "action=authenticate" + 
                               "&txId=" + txId + 
                               "&callbackUrl=http://localhost:8080/api/auth/status");
            
            System.out.println("Opening browser with URL: " + uri);
            Desktop.getDesktop().browse(uri);
            
            // Start polling for authentication status
            pollAuthStatus(txId);
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error opening browser: " + e.getMessage());
            e.printStackTrace();
            handleAuthError("Error: Could not open MetaMask connection page");
            return;
        }
    }

    private void handleAuthError(String errorMessage) {
        authenticationInProgress = false;  // Reset the flag on error
        authAttempts++;
        statusLabel.setText(errorMessage);
        connectButton.setEnabled(true);
        connectButton.setText("Retry Connection");
        
        if (authAttempts >= MAX_AUTH_ATTEMPTS) {
            JOptionPane.showMessageDialog(this,
                "Maximum authentication attempts reached. You can try again later through the Connect Wallet button.",
                "Authentication Failed",
                JOptionPane.ERROR_MESSAGE);
            
            if (authListener != null) {
                authListener.onAuthenticationFailure("Maximum authentication attempts reached");
            }
            dispose();
        }
    }

    private void pollAuthStatus() {
        // Updated to call the overloaded method with default txId
        pollAuthStatus("default");
    }

    private void pollAuthStatus(String txId) {
        // Start the authentication endpoint server if not already running
        AuthenticationEndpoint authEndpoint = AuthenticationEndpoint.getInstance();
        System.out.println("Starting to poll auth status for txId: " + txId);
        
        CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();
            int pollCount = 0;
            
            try {
                while (!isAuthenticated) {
                    // Check for timeout
                    if (System.currentTimeMillis() - startTime > TimeUnit.SECONDS.toMillis(AUTH_TIMEOUT_SECONDS)) {
                        System.out.println("Authentication timeout for txId: " + txId);
                        SwingUtilities.invokeLater(() -> handleAuthError("Authentication timeout. Please try again or connect later."));
                        break;
                    }

                    // Get status from authentication endpoint
                    AuthenticationEndpoint.AuthenticationStatus status = authEndpoint.getStatus(txId);
                    pollCount++;
                    
                    // Log every poll for debugging during development
                    System.out.println("Poll #" + pollCount + " for txId " + txId + 
                                      ": authenticated=" + status.isAuthenticated() + 
                                      ", account=" + status.getAccount() + 
                                      ", error=" + status.getErrorReason());
                    
                    if (status.isAuthenticated() && status.getAccount() != null && !status.getAccount().isEmpty()) {
                        System.out.println("Authentication successful for txId: " + txId + " with account: " + status.getAccount());
                        isAuthenticated = true;
                        String account = status.getAccount();
                        
                        // Update the default authentication status with this account
                        // This ensures persistence across different parts of the app
                        AuthenticationEndpoint.AuthenticationStatus defaultStatus = authEndpoint.getStatus("default");
                        if (defaultStatus == null) {
                            defaultStatus = authEndpoint.createNewStatus("default");
                        }
                        if (defaultStatus != null) {
                            defaultStatus.setAuthenticated(true);
                            defaultStatus.setAccount(account);
                            System.out.println("✅ Updated default authentication status with account: " + account);
                        }
                        
                        // Use the setter method in Main to update the cached address
                        try {
                            main.java.org.anynomous.Main.setCachedUserAddress(account);
                            System.out.println("✅ Used Main.setCachedUserAddress to store: " + account);
                        } catch (Exception e) {
                            System.out.println("⚠️ Error calling Main.setCachedUserAddress: " + e.getMessage());
                            e.printStackTrace();
                        }
                        
                        SwingUtilities.invokeLater(() -> {
                            statusLabel.setText("Successfully authenticated with account: " + 
                                              shortenAddress(account));
                            
                            System.out.println("Authentication successful - opening application");
                            
                            // First call the success listener to open the application
                            if (authListener != null) {
                                authListener.onAuthenticationSuccess();
                            }
                            
                            // Then close the dialog after a short delay
                            new Thread(() -> {
                                try {
                                    Thread.sleep(500); // Shorter delay since we're closing after app opens
                                    SwingUtilities.invokeLater(() -> {
                                        System.out.println("Closing auth dialog");
                                        dispose();
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        });
                        authenticationInProgress = false;  // Reset the flag on success
                        break;
                    } else if (!status.getErrorReason().isEmpty()) {
                        System.out.println("Authentication failed for txId: " + txId + " with reason: " + status.getErrorReason());
                        SwingUtilities.invokeLater(() -> 
                            handleAuthError("Authentication failed: " + status.getErrorReason()));
                        break;
                    }
                    
                    Thread.sleep(500); // Poll twice per second
                }
            } catch (InterruptedException e) {
                System.err.println("Polling interrupted: " + e.getMessage());
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> handleAuthError("Error checking authentication status"));
            }
        });
    }
    
    private String shortenAddress(String address) {
        if (address == null || address.length() < 10) {
            return address;
        }
        return address.substring(0, 6) + "..." + address.substring(address.length() - 4);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MetaMaskAuth().setVisible(true);
        });
    }
} 