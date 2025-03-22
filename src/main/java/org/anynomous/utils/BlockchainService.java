package main.java.org.anynomous.utils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import java.math.BigInteger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;

public class BlockchainService {
    private static BlockchainService instance;
    private Web3j web3j;
    private Web3j fallbackWeb3j;
    private ContractGasProvider gasProvider;
    private String contractAddress;
    private final Map<String, Boolean> premiumAccessCache = new HashMap<>();
    private final java.util.Map<String, Long> cacheTimestamps = new java.util.HashMap<>();
    private final ExecutorService executorService;
    private boolean isShuttingDown = false;
    // Reduce cache expiration to just 10 seconds to ensure freshness
    private static final long CACHE_EXPIRATION_MS = 10000; // 10 seconds
    
    // Constants
    private static final String DEFAULT_CONTRACT_ADDRESS = "0x221280f7ba730992be16277ffe9d5dd6e16286a6";
    private static final String SEPOLIA_RPC_URL = "https://sepolia.infura.io/v3/d4758a4c6d244f1e9fbf1ac238c3df53";
    private static final String ALCHEMY_SEPOLIA_URL = "https://eth-sepolia.g.alchemy.com/v2/0xOxm7ZZZiIzlQr4Ka49_xRZxPpJl_x8c";

    private BlockchainService() {
        this(DEFAULT_CONTRACT_ADDRESS);
    }

    /**
     * Constructor with specified contract address
     * @param contractAddress The Ethereum contract address to use
     */
    public BlockchainService(String contractAddress) {
        System.out.println("🧠 BlockchainService initializing");
        this.executorService = Executors.newFixedThreadPool(4);
        
        // Initialize Web3j with the main Infura provider
        this.web3j = Web3j.build(new HttpService(SEPOLIA_RPC_URL));
        
        // Initialize a fallback provider with Alchemy
        this.fallbackWeb3j = Web3j.build(new HttpService(ALCHEMY_SEPOLIA_URL));
        
        // Use the provided contract address or a default one
        this.contractAddress = contractAddress;
        if (this.contractAddress == null || this.contractAddress.isEmpty()) {
            this.contractAddress = DEFAULT_CONTRACT_ADDRESS; // Default contract on Sepolia
        }
        
        System.out.println("🧠 Using contract address: " + this.contractAddress);
        
        // Set up gas provider for standard gas limits and prices
        this.gasProvider = new StaticGasProvider(
            Convert.toWei("1.5", Convert.Unit.GWEI).toBigInteger(),
            BigInteger.valueOf(3000000)
        );
        
        System.out.println("🧠 FINAL CONTRACT ADDRESS: " + this.contractAddress);
        
        // Add shutdown hook to clean up resources
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    /**
     * Properly shutdown the service and its thread pools
     */
    public void shutdown() {
        isShuttingDown = true;
        System.out.println("🧠 BlockchainService shutting down...");
        
        try {
            // First shut down the web3j instances
            if (web3j != null) {
                web3j.shutdown();
                web3j = null;
            }
            
            if (fallbackWeb3j != null) {
                fallbackWeb3j.shutdown();
                fallbackWeb3j = null;
            }
            
            // Then shut down our executor service
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                }
            }
            
            System.out.println("🧠 BlockchainService shutdown complete");
        } catch (Exception e) {
            System.err.println("Error during BlockchainService shutdown: " + e.getMessage());
        }
    }

    /**
     * Check if the given Ethereum address has premium access
     * @param walletAddress Ethereum wallet address to check
     * @return true if the address has premium access, false otherwise
     */
    public boolean checkPremiumAccess(String walletAddress) {
        if (isShuttingDown) {
            System.out.println("🧠 BlockchainService is shutting down, returning last known status");
            return premiumAccessCache.getOrDefault(walletAddress, false);
        }
        
        System.out.println("Checking premium access for: " + walletAddress);
        
        // Check the cache first to avoid blockchain queries for recent checks
        if (premiumAccessCache.containsKey(walletAddress)) {
            Long timestamp = cacheTimestamps.get(walletAddress);
            if (timestamp != null && System.currentTimeMillis() - timestamp < CACHE_EXPIRATION_MS) {
                System.out.println("🔵 Cache HIT for premium access check: " + walletAddress + " = " + premiumAccessCache.get(walletAddress));
                return premiumAccessCache.get(walletAddress);
            }
        }
        
        System.out.println("🔴 Cache MISS for premium access check: " + walletAddress + " - querying blockchain");
        
        try {
            boolean hasAccess = checkPremiumOnBlockchain(walletAddress);
            premiumAccessCache.put(walletAddress, hasAccess);
            cacheTimestamps.put(walletAddress, System.currentTimeMillis());
            return hasAccess;
        } catch (Exception e) {
            System.err.println("Error checking premium access: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Force refresh the premium access status from the blockchain
     * @param address Ethereum wallet address to check
     * @return true if the address has premium access, false otherwise
     */
    public boolean forceRefreshPremiumAccess(String address) {
        if (isShuttingDown) {
            System.out.println("🧠 BlockchainService is shutting down, returning last known status");
            return premiumAccessCache.getOrDefault(address, false);
        }
        
        System.out.println("🧹 Forcing cache clear for address: " + address);
        premiumAccessCache.remove(address);
        cacheTimestamps.remove(address);
        
        System.out.println("⛓️ Direct blockchain query for: " + address);
        try {
            boolean result = checkPremiumOnBlockchain(address);
            premiumAccessCache.put(address, result);
            cacheTimestamps.put(address, System.currentTimeMillis());
            System.out.println("✅ Force refresh result for " + address + ": " + result);
            return result;
        } catch (Exception e) {
            System.err.println("Error force refreshing premium access: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Shutdown in progress")) {
                System.out.println("🧠 BlockchainService is shutting down during refresh");
                return premiumAccessCache.getOrDefault(address, false);
            }
            return false;
        }
    }

    /**
     * Check premium access on blockchain
     * @param address Ethereum wallet address to check
     * @return true if the address has premium access, false otherwise
     */
    private boolean checkPremiumOnBlockchain(String address) {
        if (isShuttingDown) {
            throw new IllegalStateException("Shutdown in progress");
        }
        
        if (web3j == null) {
            throw new IllegalStateException("Web3j instance is null - service might be shutting down");
        }
        
        try {
            // Create function call to check premium access
            Function function = new Function(
                "hasPremiumAccess",
                Collections.singletonList(new Address(address)),
                Collections.singletonList(new TypeReference<Bool>() {})
            );
            
            String encodedFunction = FunctionEncoder.encode(function);
            
            // Create transaction to call the contract
            Transaction transaction = Transaction.createEthCallTransaction(
                address, // From address
                contractAddress, // To address
                encodedFunction // Data
            );
            
            // Send transaction
            EthCall ethCall = web3j.ethCall(
                transaction,
                DefaultBlockParameterName.LATEST
            ).sendAsync().get(30, TimeUnit.SECONDS);

            // Decode response
            List<Type> decoded = FunctionReturnDecoder.decode(
                ethCall.getValue(),
                function.getOutputParameters()
            );
            
            if (decoded.size() > 0) {
                Bool result = (Bool) decoded.get(0);
                boolean hasPremium = result.getValue();
                System.out.println("⛓️ Blockchain result for " + address + ": " + hasPremium);
                return hasPremium;
            }
            
            return false;
        } catch (Exception e) {
            if (isShuttingDown) {
                throw new IllegalStateException("Shutdown in progress", e);
            }
            
            System.err.println("Error checking premium on blockchain: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error checking premium access", e);
        }
    }

    /**
     * Directly check the contract without going through PremiumAccessManager
     * @param address Ethereum wallet address to check
     * @return true if the address has premium access, false otherwise
     */
    public boolean directContractCheck(String address) {
        if (isShuttingDown) {
            System.out.println("🧠 BlockchainService is shutting down, returning last known status");
            return premiumAccessCache.getOrDefault(address, false);
        }
        
        System.out.println("🛑 DIRECT CONTRACT CHECK for address: " + address);
        System.out.println("🛑 Using contract address: " + contractAddress);
        
        // Encode the function call
        Function function = new Function(
            "hasPremiumAccess",
            Collections.singletonList(new Address(address)),
            Collections.singletonList(new TypeReference<Bool>() {})
        );
        
        String encodedFunction = FunctionEncoder.encode(function);
        System.out.println("🛑 Encoded function call: " + encodedFunction);
        
        // Try with the primary provider first
        System.out.println("🔄 Trying primary Infura Sepolia provider...");
        try {
            if (web3j == null) {
                System.out.println("❌ Primary web3j instance is null - service might be shutting down");
                return premiumAccessCache.getOrDefault(address, false);
            }
            
            // Create call with address 0 as sender
            Transaction transaction = Transaction.createEthCallTransaction(
                "0x0000000000000000000000000000000000000000",
                contractAddress,
                encodedFunction
            );
            
            // Execute the call
            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get(10, TimeUnit.SECONDS);
            
            if (ethCall.hasError()) {
                System.err.println("Error from primary provider: " + ethCall.getError().getMessage());
                throw new Exception("Primary provider error: " + ethCall.getError().getMessage());
            }
            
            String rawResult = ethCall.getValue();
            System.out.println("📝 Raw contract response: " + rawResult);
            
            // Decode the result
            List<Type> decoded = FunctionReturnDecoder.decode(rawResult, function.getOutputParameters());
            
            if (decoded.size() > 0) {
                Bool result = (Bool) decoded.get(0);
                boolean hasAccess = result.getValue();
                System.out.println("✨ Direct contract check result: " + hasAccess);
                return hasAccess;
            }
            
            return false;
        } catch (Exception e) {
            if (isShuttingDown) {
                System.out.println("❌ Error with primary provider: " + e.getMessage());
                return premiumAccessCache.getOrDefault(address, false);
            }
            
            // If primary fails, try with fallback
            System.out.println("❌ Error with primary provider: " + e.getMessage());
            System.out.println("🔄 Trying Alchemy Sepolia...");
            
            try {
                if (fallbackWeb3j == null) {
                    System.out.println("❌ Fallback web3j instance is null - service might be shutting down");
                    return premiumAccessCache.getOrDefault(address, false);
                }
                
                // Create call with address 0 as sender
                Transaction transaction = Transaction.createEthCallTransaction(
                    "0x0000000000000000000000000000000000000000",
                    contractAddress,
                    encodedFunction
                );
                
                // Execute the call with fallback provider
                EthCall ethCall = fallbackWeb3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get(10, TimeUnit.SECONDS);
                
                if (ethCall.hasError()) {
                    System.err.println("Error from fallback provider: " + ethCall.getError().getMessage());
                    throw new Exception("Fallback provider error: " + ethCall.getError().getMessage());
                }
                
                String rawResult = ethCall.getValue();
                System.out.println("📝 Raw contract response from fallback: " + rawResult);
                
                // Decode the result
                List<Type> decodedResult = FunctionReturnDecoder.decode(rawResult, function.getOutputParameters());
                
                if (decodedResult.size() > 0) {
                    Bool resultValue = (Bool) decodedResult.get(0);
                    boolean hasAccess = resultValue.getValue();
                    System.out.println("✨ Fallback contract check result: " + hasAccess);
                    return hasAccess;
                }
                
                return false;
            } catch (Exception fallbackEx) {
                if (isShuttingDown) {
                    System.out.println("❌ Error with fallback provider: " + fallbackEx.getMessage());
                    return premiumAccessCache.getOrDefault(address, false);
                }
                
                // As a last resort, try using the user's address as the sender
                System.out.println("❌ Error with fallback provider: " + fallbackEx.getMessage());
                System.out.println("🔄 Trying with user address as from...");
                
                try {
                    if (web3j == null) {
                        System.out.println("❌ Primary web3j instance is null during last attempt - service might be shutting down");
                        return premiumAccessCache.getOrDefault(address, false);
                    }
                    
                    // Create call with user address as sender
                    Transaction transaction = Transaction.createEthCallTransaction(
                        address,
                        contractAddress,
                        encodedFunction
                    );
                    
                    // Execute the call
                    EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get(10, TimeUnit.SECONDS);
                    
                    if (ethCall.hasError()) {
                        System.err.println("Error with user address as sender: " + ethCall.getError().getMessage());
                        throw new Exception("User address sender error: " + ethCall.getError().getMessage());
                    }
                    
                    String rawResult = ethCall.getValue();
                    System.out.println("📝 Raw contract response with user sender: " + rawResult);
                    
                    // Decode the result
                    List<Type> decodedFinal = FunctionReturnDecoder.decode(rawResult, function.getOutputParameters());
                    
                    if (decodedFinal.size() > 0) {
                        Bool resultFinal = (Bool) decodedFinal.get(0);
                        boolean hasAccess = resultFinal.getValue();
                        System.out.println("✨ User address sender check result: " + hasAccess);
                        return hasAccess;
                    }
                    
                    return false;
                } catch (Exception userAddressEx) {
                    System.out.println("❌ Error with user address as from: " + userAddressEx);
                    userAddressEx.printStackTrace();
                    // All attempts failed, return false as a fallback
                    return false;
                }
            }
        }
    }

    /**
     * Get purchase URL for premium features
     * @return URL for dApp to purchase premium access
     */
    public String getPurchaseUrl() {
        // Use our local premium purchase page
        return "http://localhost:8080/premium-purchase.html";
    }

    /**
     * Get contract address
     * @return The contract address
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Get the Web3j instance
     * @return The Web3j instance
     */
    public Web3j getWeb3j() {
        return web3j;
    }
} 