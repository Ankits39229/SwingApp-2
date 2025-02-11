import os
import yara
import tkinter as tk
from tkinter import messagebox, filedialog, ttk
from threading import Thread
from multiprocessing.pool import ThreadPool

def perform_scan(start_directory, progress_var, progress_bar):
    try:
        # Compile YARA rules
        yara_rules = yara.compile(filepath="malware_rules.yara")

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

        # Update progress bar max value
        progress_var.set(0)
        progress_bar["maximum"] = len(file_paths)

        # Use ThreadPool for parallel scanning
        pool = ThreadPool(processes=4)  # Use 4 threads (adjustable)
        results = []

        for index, result in enumerate(pool.imap(scan_file, file_paths), 1):
            results.append(result)
            progress_var.set(index)

        # Filter and display results
        threats = [result for result in results if result and "Threat found" in result]
        if threats:
            messagebox.showinfo("Scan Completed", "\n".join(threats))
        else:
            messagebox.showinfo("Scan Completed", "No threats detected.")
    except Exception as e:
        messagebox.showerror("Error", f"An error occurred: {e}")

    # Update status label after scanning
    status_label.config(text="Scan Completed")

def start_scan():
    # Allow user to choose a directory
    directory = filedialog.askdirectory(title="Select Directory to Scan")
    if directory:
        status_label.config(text="Scanning... Please wait.")
        progress_var = tk.IntVar()
        progress_bar["variable"] = progress_var

        scan_thread = Thread(target=perform_scan, args=(directory, progress_var, progress_bar))
        scan_thread.start()

# Create the GUI
root = tk.Tk()
root.title("YARA Malware Scanner")
root.geometry("500x350")

# Add a label
label = tk.Label(root, text="Click 'Scan' to start malware scanning.\nChoose a directory to scan.", font=("Arial", 12))
label.pack(pady=20)

# Add a scan button
scan_button = tk.Button(root, text="Scan", command=start_scan, bg="blue", fg="white", font=("Arial", 12))
scan_button.pack(pady=10)

# Status label
status_label = tk.Label(root, text="Status: Idle", fg="green", font=("Arial", 10))
status_label.pack(pady=10)

# Progress bar
progress_bar = ttk.Progressbar(root, orient="horizontal", length=400, mode="determinate")
progress_bar.pack(pady=20)

# Run the application
root.mainloop()
