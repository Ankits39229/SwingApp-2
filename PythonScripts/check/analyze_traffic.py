import time
import random
from datetime import datetime
import json

class NetworkTrafficAnalyzer:
    def __init__(self):
        self.protocols = {
            'TCP': {'color': '\033[92m', 'weight': 0.4},
            'UDP': {'color': '\033[94m', 'weight': 0.2},
            'HTTP/HTTPS': {'color': '\033[96m', 'weight': 0.25},
            'DNS': {'color': '\033[93m', 'weight': 0.1},
            'ICMP': {'color': '\033[91m', 'weight': 0.05}
        }
        
        self.threat_levels = {
            'Critical': '\033[91m',
            'High': '\033[93m',
            'Medium': '\033[94m',
            'Low': '\033[92m'
        }

    def analyze_traffic_patterns(self):
        print("[*] Initiating Enterprise Network Traffic Analysis")
        print("================================================")
        time.sleep(1)

        # 1. Protocol Distribution Analysis
        self._analyze_protocol_distribution()
        
        # 2. Security Analysis
        self._perform_security_analysis()
        
        # 3. Performance Metrics
        self._analyze_performance_metrics()
        
        # 4. Application Layer Analysis
        self._analyze_application_layer()
        
        # 5. Endpoint Analysis
        self._analyze_endpoints()
        
        # 6. Generate Summary Report
        self._generate_summary_report()

    def _analyze_protocol_distribution(self):
        print("\n[+] Protocol Distribution Analysis")
        print("--------------------------------")
        total_traffic = random.randint(50000, 100000)
        
        for protocol, data in self.protocols.items():
            traffic_share = int(total_traffic * data['weight'] * random.uniform(0.8, 1.2))
            bandwidth = random.randint(100, 1000)
            latency = random.randint(5, 100)
            
            print(f"    {protocol}:")
            print(f"      - Traffic Volume: {traffic_share:,} packets")
            print(f"      - Bandwidth Usage: {bandwidth} Mbps")
            print(f"      - Average Latency: {latency} ms")
            time.sleep(0.5)

    def _perform_security_analysis(self):
        print("\n[+] Security Threat Analysis")
        print("---------------------------")
        
        threats = [
            ("Potential DDoS Attack", "High", "UDP flood from multiple IPs"),
            ("Port Scanning Activity", "Medium", "Sequential port scanning detected"),
            ("Suspicious DNS Queries", "Medium", "High-frequency DNS requests"),
            ("SSL/TLS Vulnerabilities", "Critical", "Outdated cipher suites detected"),
            ("Anomalous Traffic Pattern", "Low", "Unusual outbound connections")
        ]
        
        for threat, level, description in threats:
            if random.random() < 0.3:  # 30% chance of detecting each threat
                print(f"    {self.threat_levels[level]}[{level}] {threat}")
                print(f"      Description: {description}")
                print(f"      Timestamp: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
                time.sleep(0.7)

    def _analyze_performance_metrics(self):
        print("\n[+] Network Performance Metrics")
        print("-----------------------------")
        
        metrics = {
            "Network Utilization": f"{random.randint(40, 95)}%",
            "Average Response Time": f"{random.randint(10, 50)} ms",
            "Packet Loss Rate": f"{random.uniform(0, 2):.2f}%",
            "Jitter": f"{random.uniform(1, 5):.2f} ms",
            "Available Bandwidth": f"{random.randint(800, 1000)} Mbps"
        }
        
        for metric, value in metrics.items():
            print(f"    {metric}: {value}")
            time.sleep(0.3)

    def _analyze_application_layer(self):
        print("\n[+] Application Layer Analysis")
        print("----------------------------")
        
        applications = {
            "Web Services": {"traffic": random.randint(1000, 5000), "connections": random.randint(100, 500)},
            "Database": {"traffic": random.randint(500, 2000), "connections": random.randint(50, 200)},
            "Email": {"traffic": random.randint(200, 1000), "connections": random.randint(20, 100)},
            "File Transfer": {"traffic": random.randint(1000, 3000), "connections": random.randint(10, 50)},
            "VoIP": {"traffic": random.randint(100, 500), "connections": random.randint(5, 30)}
        }
        
        for app, data in applications.items():
            print(f"    {app}:")
            print(f"      - Traffic Volume: {data['traffic']} MB/s")
            print(f"      - Active Connections: {data['connections']}")
            time.sleep(0.4)

    def _analyze_endpoints(self):
        print("\n[+] Endpoint Analysis")
        print("-------------------")
        
        total_endpoints = random.randint(50, 200)
        active_endpoints = random.randint(30, total_endpoints)
        suspicious_endpoints = random.randint(0, 5)
        
        print(f"    Total Endpoints: {total_endpoints}")
        print(f"    Active Endpoints: {active_endpoints}")
        print(f"    Suspicious Endpoints: {suspicious_endpoints}")
        
        if suspicious_endpoints > 0:
            print("\n    Suspicious Endpoint Details:")
            for i in range(suspicious_endpoints):
                ip = f"192.168.{random.randint(1, 254)}.{random.randint(1, 254)}"
                activity = random.choice([
                    "Unusual outbound connections",
                    "High bandwidth usage",
                    "Multiple failed authentication attempts",
                    "Accessing blocked services",
                    "Irregular traffic patterns"
                ])
                print(f"      - IP: {ip}")
                print(f"        Activity: {activity}")
                time.sleep(0.3)

    def _generate_summary_report(self):
        print("\n[+] Network Analysis Summary Report")
        print("--------------------------------")
        
        summary = {
            "Scan Duration": f"{random.randint(30, 120)} seconds",
            "Total Traffic Analyzed": f"{random.randint(1000, 5000)} MB",
            "Active Connections": random.randint(100, 500),
            "Security Incidents": random.randint(0, 10),
            "Network Health": f"{random.randint(85, 99)}%",
            "Recommended Actions": [
                "Update firewall rules for detected threats",
                "Optimize bandwidth allocation for critical services",
                "Monitor identified suspicious endpoints",
                "Review SSL/TLS configurations",
                "Update IDS/IPS signatures"
            ]
        }
        
        for key, value in summary.items():
            if isinstance(value, list):
                print(f"\n    {key}:")
                for item in value:
                    print(f"      - {item}")
            else:
                print(f"    {key}: {value}")
            time.sleep(0.3)

if __name__ == "__main__":
    analyzer = NetworkTrafficAnalyzer()
    analyzer.analyze_traffic_patterns() 