package main.java.org.anynomous;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import main.java.org.anynomous.utils.PremiumAccessManager;

/**
 * A simple HTTP server to handle authentication status callbacks from MetaMask.
 */
public class AuthenticationEndpoint {
    private static AuthenticationEndpoint instance;
    private final HttpServer server;
    private final Map<String, AuthenticationStatus> authStatuses = new ConcurrentHashMap<>();
    
    private static final int SERVER_PORT = 8080;
    
    public static class AuthenticationStatus {
        private boolean authenticated = false;
        private String account = "";
        private String errorReason = "";
        
        public boolean isAuthenticated() {
            return authenticated;
        }
        
        public String getAccount() {
            return account;
        }
        
        public String getErrorReason() {
            return errorReason;
        }
        
        public void setAuthenticated(boolean authenticated) {
            this.authenticated = authenticated;
        }
        
        public void setAccount(String account) {
            this.account = account;
        }
        
        public void setErrorReason(String errorReason) {
            this.errorReason = errorReason;
        }
    }
    
    private AuthenticationEndpoint() throws IOException {
        server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        
        // Set up context for authentication status endpoint
        server.createContext("/api/auth/status", new AuthStatusHandler());
        
        // Set up context for serving the share.html file
        server.createContext("/share.html", new StaticFileHandler("/web/share.html"));
        
        // Set up context for serving the premium purchase page
        server.createContext("/premium-purchase.html", new StaticFileHandler("/web/premium-purchase.html"));
        
        // Set up context for premium purchase callback
        server.createContext("/api/premium/callback", new PremiumPurchaseHandler());
        
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        
        System.out.println("Authentication server started on port " + SERVER_PORT);
    }
    
    public static synchronized AuthenticationEndpoint getInstance() {
        if (instance == null) {
            try {
                instance = new AuthenticationEndpoint();
            } catch (IOException e) {
                throw new RuntimeException("Failed to start authentication server", e);
            }
        }
        return instance;
    }
    
    /**
     * Parse query parameters from a URL query string
     */
    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            System.out.println("Parsing query parameters: " + query);
            
            for (String param : query.split("&")) {
                String[] parts = param.split("=", 2);
                if (parts.length == 2) {
                    try {
                        // URL decode the parameter values
                        String key = java.net.URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                        String value = java.net.URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                        params.put(key, value);
                        System.out.println("  Param: " + key + " = " + value);
                    } catch (Exception e) {
                        System.err.println("Error decoding parameter: " + param);
                        // Fall back to non-decoded version
                        params.put(parts[0], parts[1]);
                    }
                }
            }
        }
        return params;
    }
    
    public AuthenticationStatus getStatus(String txId) {
        // We should only clear existing entries when explicitly requested,
        // not during normal status checks
        return authStatuses.computeIfAbsent(txId, k -> new AuthenticationStatus());
    }
    
    // Add a method to create a fresh status for a new auth attempt
    public AuthenticationStatus createNewStatus(String txId) {
        // Clear any existing status with this ID
        authStatuses.remove(txId);
        // Create and return a new status object
        return authStatuses.computeIfAbsent(txId, k -> new AuthenticationStatus());
    }
    
    /**
     * Resets the authentication status for the specified transaction ID
     * @param txId The transaction ID to reset
     * @return The new empty authentication status
     */
    public AuthenticationStatus resetStatus(String txId) {
        // Remove the existing status
        authStatuses.remove(txId);
        // Create a new status object (unauthenticated)
        AuthenticationStatus newStatus = new AuthenticationStatus();
        // Store it in the map
        authStatuses.put(txId, newStatus);
        System.out.println("Authentication status reset for txId: " + txId);
        return newStatus;
    }
    
    public void shutdown() {
        if (server != null) {
            server.stop(0);
        }
    }
    
    /**
     * Handler for authentication status requests.
     */
    private class AuthStatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // Get query parameters
                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = parseQueryParams(query);
                
                String txId = params.getOrDefault("txId", "default");
                String status = params.getOrDefault("status", "");
                
                System.out.println("Auth request received: txId=" + txId + ", status=" + status);
                
                // Handle different types of requests
                if (exchange.getRequestMethod().equalsIgnoreCase("GET") && 
                   (status == null || status.isEmpty())) {
                    // Client is requesting status
                    AuthenticationStatus authStatus = getStatus(txId);
                    
                    // Debug existing status contents
                    System.out.println("GET requested - Current status object for txId=" + txId + 
                                     ": authenticated=" + authStatus.isAuthenticated() + 
                                     ", account=" + authStatus.getAccount() + 
                                     ", error=" + authStatus.getErrorReason());
                    
                    String response = createStatusResponse(authStatus);
                    System.out.println("Returning status: " + response);
                    sendResponse(exchange, response);
                } else {
                    // MetaMask page is updating status
                    AuthenticationStatus authStatus = getStatus(txId);
                    
                    if ("authenticated".equals(status)) {
                        System.out.println("Setting authentication status to true for txId: " + txId);
                        authStatus.setAuthenticated(true);
                        String account = params.getOrDefault("account", "");
                        authStatus.setAccount(account);
                        System.out.println("Account value set: " + account);
                        
                        // Ensure the modified status is properly stored in the map
                        authStatuses.put(txId, authStatus);
                        
                        // For debugging - print the complete status object
                        System.out.println("Status object state after update: authenticated=" + authStatus.isAuthenticated() + 
                                        ", account=" + authStatus.getAccount() + 
                                        ", error=" + authStatus.getErrorReason());
                    } else if ("failed".equals(status)) {
                        System.out.println("Setting authentication status to failed for txId: " + txId);
                        authStatus.setErrorReason(params.getOrDefault("reason", "Unknown error"));
                        
                        // Ensure the modified status is properly stored in the map
                        authStatuses.put(txId, authStatus);
                    }
                    
                    String response = "Status updated";
                    sendResponse(exchange, response);
                }
            } catch (Exception e) {
                System.err.println("Error in auth handler: " + e.getMessage());
                e.printStackTrace();
                String errorResponse = "Error: " + e.getMessage();
                sendResponse(exchange, errorResponse, 500);
            } finally {
                exchange.close();
            }
        }
        
        private String createStatusResponse(AuthenticationStatus status) {
            if (status.isAuthenticated()) {
                return "{\"status\":\"authenticated\",\"account\":\"" + status.getAccount() + "\"}";
            } else if (!status.getErrorReason().isEmpty()) {
                return "{\"status\":\"failed\",\"reason\":\"" + status.getErrorReason() + "\"}";
            } else {
                return "{\"status\":\"pending\"}";
            }
        }
    }
    
    /**
     * Handler for serving static files like share.html.
     */
    private class StaticFileHandler implements HttpHandler {
        private final String resourcePath;
        
        public StaticFileHandler(String resourcePath) {
            this.resourcePath = resourcePath;
        }
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // Read the resource file
                byte[] fileContent = getClass().getResourceAsStream(resourcePath).readAllBytes();
                
                // Set content type
                exchange.getResponseHeaders().set("Content-Type", getContentType());
                
                // Send response
                exchange.sendResponseHeaders(200, fileContent.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileContent);
                os.close();
            } catch (Exception e) {
                String errorResponse = "Error: " + e.getMessage();
                sendResponse(exchange, errorResponse, 500);
            } finally {
                exchange.close();
            }
        }
        
        private String getContentType() {
            if (resourcePath.endsWith(".html")) {
                return "text/html";
            } else if (resourcePath.endsWith(".js")) {
                return "application/javascript";
            } else if (resourcePath.endsWith(".css")) {
                return "text/css";
            } else {
                return "application/octet-stream";
            }
        }
    }
    
    /**
     * Handler for premium purchase callbacks.
     */
    private class PremiumPurchaseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                System.out.println("Premium purchase callback received");
                
                // Get query parameters
                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = parseQueryParams(query);
                
                String account = params.getOrDefault("account", "");
                
                if (!account.isEmpty()) {
                    System.out.println("Premium purchase callback received for account: " + account);
                    
                    // Refresh premium access status
                    boolean updated = PremiumAccessManager.getInstance().refreshPremiumAccess(account);
                    System.out.println("Premium access updated: " + updated + " for account: " + account);
                    
                    // Find all active sessions with this account and refresh them
                    for (Map.Entry<String, AuthenticationStatus> entry : authStatuses.entrySet()) {
                        String sessionId = entry.getKey();
                        AuthenticationStatus status = entry.getValue();
                        
                        if (status != null && status.isAuthenticated()) {
                            String sessionAccount = status.getAccount();
                            if (sessionAccount != null && !sessionAccount.isEmpty() && 
                                account.equalsIgnoreCase(sessionAccount)) {
                                System.out.println("Refreshing premium status for session: " + sessionId + 
                                                   " with account: " + sessionAccount);
                                PremiumAccessManager.getInstance().refreshPremiumAccess(sessionAccount);
                            }
                        }
                    }
                    
                    String response = "{\"status\":\"success\",\"premium\":\"" + updated + "\"}";
                    sendResponse(exchange, response);
                } else {
                    System.err.println("Missing account parameter in premium purchase callback");
                    String response = "{\"status\":\"error\",\"message\":\"Missing account parameter\"}";
                    sendResponse(exchange, response, 400);
                }
            } catch (Exception e) {
                System.err.println("Error in premium purchase handler: " + e.getMessage());
                e.printStackTrace();
                String errorResponse = "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
                sendResponse(exchange, errorResponse, 500);
            } finally {
                exchange.close();
            }
        }
    }
    
    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 200);
    }
    
    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
} 