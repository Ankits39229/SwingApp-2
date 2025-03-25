import { useState, useEffect } from 'react';

const ProfileSection = () => {
  const [account, setAccount] = useState('');

  const connectWallet = async () => {
    if (typeof window.ethereum !== 'undefined') {
      try {
        const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
        setAccount(accounts[0]);
      } catch (error) {
        console.error('Error connecting to MetaMask:', error);
      }
    }
  };

  useEffect(() => {
    // Check if already connected
    const checkConnection = async () => {
      if (typeof window.ethereum !== 'undefined') {
        const accounts = await window.ethereum.request({ method: 'eth_accounts' });
        if (accounts.length > 0) {
          setAccount(accounts[0]);
        }
      }
    };
    
    checkConnection();

    // Listen for account changes
    if (window.ethereum) {
      window.ethereum.on('accountsChanged', accounts => {
        setAccount(accounts[0] || '');
      });
    }
  }, []);

  return (
    <div className="profile-section">
      <div className="wallet-info">
        {account ? (
          <>
            <span className="wallet-label">Wallet: </span>
            <span className="wallet-address">{`${account.slice(0, 6)}...${account.slice(-4)}`}</span>
          </>
        ) : (
          <button onClick={connectWallet} className="connect-button">
            Connect Wallet
          </button>
        )}
      </div>
    </div>
  );
};

export default ProfileSection; 