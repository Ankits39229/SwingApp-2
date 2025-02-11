import subprocess

def get_open_ports():
    # Running the netstat command to get the list of open ports and connections
    try:
        result = subprocess.run(['netstat', '-an'], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        if result.stderr:
            print(f"Error: {result.stderr}")
            return

        # Parse the output to get all open connections
        lines = result.stdout.splitlines()
        open_connections = []

        for line in lines:
            # Look for lines that contain 'LISTENING' or 'ESTABLISHED' for open connections
            if "LISTENING" in line or "ESTABLISHED" in line:
                parts = line.split()
                
                # Ensure the line has the expected structure (at least 4 parts)
                if len(parts) >= 4:
                    protocol = parts[0]         # The protocol (TCP/UDP)
                    local_address = parts[1]    # The local address (IP:Port)
                    foreign_address = parts[2]  # The foreign address (IP:Port)
                    state = parts[3]            # The connection state (e.g., LISTENING)

                    # Extract the IP and port
                    try:
                        local_ip, local_port = local_address.split(':')
                        foreign_ip, foreign_port = foreign_address.split(':')
                    except ValueError:
                        continue  # Skip malformed lines
                    
                    open_connections.append({
                        "protocol": protocol,
                        "local_ip": local_ip,
                        "local_port": local_port,
                        "foreign_ip": foreign_ip,
                        "foreign_port": foreign_port,
                        "state": state
                    })

        # Print the detailed open connections
        if open_connections:
            print(f"{'Protocol':<10} {'Local Address':<30} {'Foreign Address':<30} {'State':<15}")
            print("-" * 85)  # Print a separator line
            for conn in open_connections:
                print(f"{conn['protocol']:<10} {conn['local_ip']}:{conn['local_port']:<25} "
                      f"{conn['foreign_ip']}:{conn['foreign_port']:<25} {conn['state']:<15}")
        else:
            print("No open ports or connections found.")

    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    get_open_ports()
