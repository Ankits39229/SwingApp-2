import time
import socket
import datetime
import psutil

def get_active_connections():
    active_connections = []
    try:
        for proc in psutil.process_iter():
            try:
                pinfo = proc.as_dict(attrs=['pid', 'name'])
                if any(browser in pinfo['name'].lower() for browser in ['chrome', 'firefox', 'msedge', 'iexplore', 'opera']):
                    try:
                        connections = proc.net_connections()
                        for conn in connections:
                            if conn.status == 'ESTABLISHED' and hasattr(conn, 'raddr') and conn.raddr:
                                try:
                                    ip = conn.raddr.ip
                                    try:
                                        host = socket.gethostbyaddr(ip)[0]
                                    except:
                                        host = ip

                                    connection_info = {
                                        'host': host,
                                        'ip': ip,
                                    }

                                    if connection_info not in active_connections:
                                        active_connections.append(connection_info)
                                except:
                                    continue
                    except:
                        continue
            except (psutil.NoSuchProcess, psutil.AccessDenied):
                continue
    except Exception as e:
        print(f"Error in get_active_connections: {e}")

    return active_connections

def main():
    start_time = time.time()
    duration = 60  # 1 minute

    print("Starting network monitoring...")
    print("Press Ctrl+C to stop manually")
    print("-" * 70)

    try:
        while True:
            current_time = time.time()
            if current_time - start_time >= duration:
                print("\nTime limit (1 minute) reached. Exiting...")
                break

            print(f"\n[{datetime.datetime.now().strftime('%H:%M:%S')}] Active connections:")
            active_connections = get_active_connections()

            if active_connections:
                for conn in active_connections:
                    print(f"- Host: {conn['host']}")
                    print(f"  IP: {conn['ip']}")
                    print("-" * 50)
            else:
                print("No active connections detected")

            time.sleep(2)

    except KeyboardInterrupt:
        print("\nMonitoring stopped by user")

if __name__ == "__main__":
    main()
