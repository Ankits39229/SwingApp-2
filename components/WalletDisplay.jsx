import { useState, useEffect } from 'react';

const WalletDisplay = () => {
  const [account, setAccount] = useState('');

  const connectWallet = async () => {
    if (typeof window.ethereum !== 'undefined') {
      try {
        // Request account access
        const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
        setAccount(accounts[0]);
      } catch (error) {
        console.error('Error connecting to MetaMask:', error);
      }
    } else {
      alert('Please install MetaMask!');
    }
  };

  useEffect(() => {
    connectWallet();
    
    // Listen for account changes
    if (window.ethereum) {
      window.ethereum.on('accountsChanged', accounts => {
        setAccount(accounts[0]);
      });
    }
  }, []);

  return (
    <div style={{
      position: 'absolute',
      top: '20px',
      right: '20px',
      padding: '10px',
      background: '#f0f0f0',
      borderRadius: '5px',
      fontSize: '14px'
    }}>
      {account ? 
        `${account.slice(0, 6)}...${account.slice(-4)}` : 
        'Connect Wallet'}
    </div>
  );
};

export default WalletDisplay; 