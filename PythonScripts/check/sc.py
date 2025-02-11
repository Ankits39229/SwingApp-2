import os
import yara
import tkinter as tk
from tkinter import filedialog
from threading import Thread
from multiprocessing.pool import ThreadPool

def perform_scan(start_directory):
    try:
        # Compile YARA rules
        yara_rules = yara.compile(filepath="PythonScripts/check/malware_rules.yara")

        # Limit large file size (50 MB)
        MAX_FILE_SIZE = 50 * 1024 * 1024  # 50 MB

        # Directories to skip
        skip_dirs = ["C:\\Windows", "C:\\Program Files", "C:\\Program Files (x86)"]

        # Function to scan a single file
        def scan_file(file_path):
            try:
                if os.path.getsize(file_path) > MAX_FILE_SIZE:
                    return None  # Skip large files

                matches = yara_rules.match(file_path)
                if matches:
                    return f"Threat found in: {file_path}\nMatches: {matches}"
            except Exception as e:
                return f"Could not scan {file_path}: {e}"

        # Collect file paths to scan
        file_paths = []
        for root_dir, _, files in os.walk(start_directory):
            if any(skip_dir in root_dir for skip_dir in skip_dirs):
                continue  # Skip predefined directories

            for file in files:
                if file.lower().endswith((".exe", ".dll", ".js", ".vbs", ".bat", ".py")):
                    file_paths.append(os.path.join(root_dir, file))

        # Use ThreadPool for parallel scanning
        pool = ThreadPool(processes=4)  # Use 4 threads (adjustable)
        results = []

        total_files = len(file_paths)
        for index, result in enumerate(pool.imap(scan_file, file_paths), 1):
            results.append(result)
            progress = (index / total_files) * 100
            print(f"Scan Progress: {progress:.2f}%")

        # Filter and display results
        threats = [result for result in results if result and "Threat found" in result]
        if threats:
            print("Malware threats found in the following files:")
            for threat in threats:
                print(threat)
        else:
            print("No threats detected.")
    except Exception as e:
        print(f"An error occurred: {e}")

def start_scan():
    # Tkinter root window (hidden) to use filedialog
    root = tk.Tk()
    root.withdraw()  # Hide the root window

    # Open directory selection dialog
    directory = filedialog.askdirectory(title="Select Directory to Scan")
    if directory:  # If a directory is selected
        print(f"Starting scan in directory: {directory}")
        perform_scan(directory)
    else:
        print("No directory selected. Exiting scan.")

if __name__ == "__main__":
    start_scan()
