// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract PremiumFeatures {
    address public owner;
    mapping(address => bool) public premiumUsers;
    uint256 public premiumFee = 0.01 ether; // Fee for premium access (can be adjusted)
    
    event PremiumPurchased(address indexed user, uint256 amount, uint256 timestamp);
    
    constructor() {
        owner = msg.sender;
    }
    
    // Modifier to ensure only the owner can call certain functions
    modifier onlyOwner() {
        require(msg.sender == owner, "Only the owner can call this function");
        _;
    }
    
    // Function to purchase premium access
    function purchasePremium() external payable {
        require(msg.value >= premiumFee, "Insufficient payment amount");
        require(!premiumUsers[msg.sender], "User already has premium access");
        
        premiumUsers[msg.sender] = true;
        emit PremiumPurchased(msg.sender, msg.value, block.timestamp);
    }
    
    // Check if a user has premium access
    function hasPremiumAccess(address user) external view returns (bool) {
        return premiumUsers[user];
    }
    
    // Allow owner to grant premium access to a specific user (for testing or promotions)
    function grantPremiumAccess(address user) external onlyOwner {
        premiumUsers[user] = true;
    }
    
    // Allow owner to revoke premium access
    function revokePremiumAccess(address user) external onlyOwner {
        premiumUsers[user] = false;
    }
    
    // Allow owner to update the premium fee
    function updatePremiumFee(uint256 newFee) external onlyOwner {
        premiumFee = newFee;
    }
    
    // Allow owner to withdraw funds from the contract
    function withdraw() external onlyOwner {
        payable(owner).transfer(address(this).balance);
    }
} 