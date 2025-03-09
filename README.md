# Windows Security Suite

A modern, comprehensive security dashboard application built with Java Swing, providing enterprise-grade security and system maintenance features for Windows systems. This suite combines the power of Java for the main application with Python for advanced security features.

![Java Version](https://img.shields.io/badge/Java-17-orange)
![Python Version](https://img.shields.io/badge/Python-3.8%2B-blue)
![License](https://img.shields.io/badge/License-MIT-blue)
![Platform](https://img.shields.io/badge/Platform-Windows-lightgrey)

## 📋 Overview

Windows Security Suite is an all-in-one security solution that provides real-time monitoring, threat detection, and system optimization capabilities through an intuitive, modern interface.

### 🎯 Key Features

#### 🏠 Core Modules
- **Home Dashboard**
  - Real-time system status monitoring
  - Resource usage metrics
  - Security threat indicators
  - System health overview

- **Clean**
  - Temporary file cleanup
  - Registry optimization
  - Disk space management
  - System cache clearing

- **Troubleshoot**
  - System performance diagnostics
  - Network connectivity tests
  - Driver issue detection
  - Blue screen error analysis
  - Audio and device connectivity troubleshooting
  - Browser issues resolution

- **Firewall**
  - Advanced firewall management
  - Port monitoring and control
  - Application network access control
  - Network traffic analysis

#### 🛡️ Advanced Security Modules
- **Audit**
  - System security compliance checks
  - Security policy verification
  - User access auditing
  - System configuration analysis

- **Remediate**
  - Automated security fix implementation
  - Vulnerability patching
  - System hardening recommendations
  - Security policy enforcement

- **Malware Protection**
  - Real-time malware detection
  - Machine learning-based threat analysis
  - Process monitoring
  - Automated malware removal

- **AI Assistant**
  - Context-aware security recommendations
  - Guided troubleshooting
  - Automated system maintenance
  - Security best practices guidance

## 🔧 Technical Requirements

### Java Environment
- **JDK**: Version 17 or higher
- **Maven**: Version 3.6+
- **Memory**: Minimum 4GB RAM
- **Storage**: 500MB free space
- **OS**: Windows 10/11 (64-bit)

### Python Environment
- **Python**: Version 3.8 or higher
- **Virtual Environment**: Required for isolation

#### Python Dependencies
Create a `requirements.txt` file in the `PythonScripts` directory with:

```txt
psutil>=5.9.0
scikit-learn>=1.0.2
pandas>=1.5.0
numpy>=1.21.0
joblib>=1.1.0
tkinter
pillow>=9.0.0
requests>=2.28.0
python-dotenv>=0.19.0
```

## 🚀 Installation Guide

### 1. Basic Setup
```bash
# Clone the repository
git clone https://github.com/Ankits39229/SwingApp-2.git
cd SWINGAPP

# Build Java application
mvn clean install
```

### 2. Python Environment Setup
```bash
# Navigate to Python scripts directory
cd PythonScripts

# Create virtual environment
python -m venv .Venv

# Activate virtual environment
# On Windows:
.Venv\Scripts\activate

# Install requirements
pip install -r requirements.txt
```

### 3. Running the Application
```bash
# Method 1: Using Maven
mvn exec:java

# Method 2: Using JAR
java -jar target/swingapp-1.0-SNAPSHOT.jar
```

## 🏗️ Project Structure Explained
```SWINGAPP/
├── src/ # Java source files
│ └── main/
│ └── java/
│ └── org/anynomous/
│ ├── Main.java # Application entry point
│ ├── Clean.java # System cleaning module
│ ├── Troubleshoot.java # Diagnostics module
│ ├── Firewall.java # Network security
│ ├── Audit.java # Security auditing
│ ├── Remediate.java # Issue fixing
│ ├── Malware.java # Malware detection
│ └── Assistant.java # AI assistance
├── PythonScripts/ # Python-based features
│ ├── Malware/ # Malware detection scripts
│ │ ├── Process.py # Process monitoring
│ │ └── Folder.py # Directory scanning
│ └── check/ # System check scripts
├── AssistantScripts/ # AI assistant modules
├── Bat_Scripts/ # Windows batch utilities
├── pom.xml # Maven configuration
└── requirements.txt # Python dependencies
```

## 🔐 Security Features In-Depth

### System Protection
- Real-time process monitoring
- File system activity tracking
- Registry change detection
- Network traffic analysis

### Malware Detection
- Machine learning-based threat detection
- Behavioral analysis
- Signature-based scanning
- Real-time process monitoring

### System Optimization
- Automated maintenance
- Performance monitoring
- Resource usage optimization
- Startup management

### Network Security
- Firewall management
- Port monitoring
- Network traffic analysis
- Application network control

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- FlatLaf team for the modern UI components
- OSHI project for system information capabilities
- Python scikit-learn community for ML components
- All open-source contributors

## 📞 Support & Contact

- GitHub Issues: For bug reports and feature requests
- Email: ankits39220@gmail.com
- Documentation: [Here](https://github.com/Ankits39229/SwingApp-2/wiki)

## 🔄 Updates & Maintenance

- Regular security definition updates
- Quarterly feature releases
- Monthly security patches
- Continuous ML model improvements

---
Built for Windows security

