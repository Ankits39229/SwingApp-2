import tkinter as tk
from tkinter import ttk, messagebox
import subprocess
import time
import ctypes
import os
from tkinter.font import Font

class MinimalTheme:
    # Minimalist color scheme
    BG_COLOR = "#1E1E1E"
    CARD_BG = "#252526"
    ACCENT = "#4CC2FF"
    TEXT = "#FFFFFF"
    SECONDARY_TEXT = "#AAAAAA"
    SUCCESS = "#4CAF50"
    ERROR = "#FF5252"
    
    # Font configurations
    TITLE_FONT = ("Helvetica", 16, "bold")
    NORMAL_FONT = ("Helvetica", 10)
    MONO_FONT = ("Courier", 14)

class MinimalButton(tk.Canvas):
    def __init__(self, parent, text, command, width=120, height=32):
        super().__init__(parent, width=width, height=height, 
                        bg=MinimalTheme.CARD_BG, highlightthickness=0)
        self.command = command
        self.width = width
        self.height = height
        
        # Create text
        self.create_text(width/2, height/2, text=text, 
                        fill=MinimalTheme.TEXT,
                        font=MinimalTheme.NORMAL_FONT)
        
        # Bind events
        self.bind("<Enter>", self.on_enter)
        self.bind("<Leave>", self.on_leave)
        self.bind("<Button-1>", self.on_click)
        
    def on_enter(self, e):
        self.configure(bg=MinimalTheme.ACCENT)
        
    def on_leave(self, e):
        self.configure(bg=MinimalTheme.CARD_BG)
        
    def on_click(self, e):
        self.command()

class PortBlockerUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Port Blocker")
        self.root.geometry("300x360")
        self.root.resizable(False, False)
        self.root.configure(bg=MinimalTheme.BG_COLOR)
        
        # Initialize blocking status to False
        self.is_blocking = False
        self.should_exit = False
        
        self.setup_ui()
        
    def setup_ui(self):
        # Main container
        main = tk.Frame(self.root, bg=MinimalTheme.BG_COLOR)
        main.pack(expand=True, fill='both', padx=20, pady=20)
        
        # Title
        tk.Label(main, text="PORT BLOCKER",
                font=MinimalTheme.TITLE_FONT,
                bg=MinimalTheme.BG_COLOR,
                fg=MinimalTheme.ACCENT).pack(pady=(0, 20))
        
        # Card container
        card = tk.Frame(main, bg=MinimalTheme.CARD_BG, padx=20, pady=20)
        card.pack(fill='x')
        
        # Duration input
        tk.Label(card, text="DURATION (seconds)",
                font=MinimalTheme.NORMAL_FONT,
                bg=MinimalTheme.CARD_BG,
                fg=MinimalTheme.SECONDARY_TEXT).pack(anchor='w')
        
        self.time_var = tk.StringVar(value="60")
        tk.Entry(card, textvariable=self.time_var,
                font=MinimalTheme.MONO_FONT,
                bg=MinimalTheme.BG_COLOR,
                fg=MinimalTheme.TEXT,
                insertbackground=MinimalTheme.TEXT,
                relief='flat',
                width=10).pack(pady=(5, 20))
        
        # Progress bar
        self.progress_frame = tk.Frame(card, height=2, 
                                     bg=MinimalTheme.BG_COLOR)
        self.progress_frame.pack(fill='x', pady=(0, 20))
        
        self.progress = tk.Frame(self.progress_frame, height=2,
                               bg=MinimalTheme.ACCENT)
        self.progress.place(relwidth=0)
        
        # Status
        self.status_var = tk.StringVar(value="Ready")
        self.status = tk.Label(card, textvariable=self.status_var,
                             font=MinimalTheme.NORMAL_FONT,
                             bg=MinimalTheme.CARD_BG,
                             fg=MinimalTheme.SUCCESS)
        self.status.pack(pady=(0, 20))
        
        # Buttons
        btn_frame = tk.Frame(card, bg=MinimalTheme.CARD_BG)
        btn_frame.pack(fill='x')
        
        MinimalButton(btn_frame, "START", self.start_blocking).pack(side='left')
        MinimalButton(btn_frame, "EXIT", self.exit_app).pack(side='right')

        # Gracefully close app when clicking the close button
        self.root.protocol("WM_DELETE_WINDOW", self.exit_app)

    def is_admin(self):
        try:
            return ctypes.windll.shell32.IsUserAnAdmin()
        except:
            return False
            
    def update_progress(self, value):
        """Update progress bar"""
        if self.should_exit or not self.root.winfo_exists():
            return  # Stop if the app should exit or window is destroyed
        try:
            self.progress.place(relwidth=value/100)
        except tk.TclError:  # Safeguard for any Tkinter destruction error
            pass
        
    def backup_firewall_rules(self, backup_file="firewall_rules.wfw"):
        if os.path.exists(backup_file):
            os.remove(backup_file)
        subprocess.run(["netsh", "advfirewall", "export", backup_file], check=True)
        
    def block_ports(self, duration):
        self.is_blocking = True
        try:
            # Add blocking rules
            subprocess.run([ "netsh", "advfirewall", "firewall", "add", "rule",
                             "name=BlockAllPorts", "dir=in", "action=block", 
                             "protocol=TCP", "localport=any"], check=True)
            subprocess.run([ "netsh", "advfirewall", "firewall", "add", "rule",
                             "name=BlockAllPorts", "dir=out", "action=block", 
                             "protocol=TCP", "localport=any"], check=True)
            
            # Update status during blocking
            for remaining in range(duration, 0, -1):
                if self.should_exit:  # Check if exit is requested
                    break
                progress = ((duration - remaining) / duration) * 100
                self.update_progress(progress)
                self.status_var.set(f"{remaining}s")
                self.status.configure(fg=MinimalTheme.ACCENT)
                self.root.update()
                time.sleep(1)
                
        finally:
            if self.is_blocking:  # Make sure to unblock if blocking was active
                subprocess.run([ "netsh", "advfirewall", "firewall", "delete", "rule", 
                                 "name=BlockAllPorts"], check=False)
            self.is_blocking = False
            self.update_progress(0)
            if not self.should_exit and self.root.winfo_exists():  # Ensure window is still active
                self.status_var.set("Ready")
                self.status.configure(fg=MinimalTheme.SUCCESS)

    def start_blocking(self):
        if not self.is_admin():
            self.status_var.set("Run as admin")
            self.status.configure(fg=MinimalTheme.ERROR)
            return
            
        try:
            duration = int(self.time_var.get())
            if duration <= 0:
                self.status_var.set("Invalid duration")
                self.status.configure(fg=MinimalTheme.ERROR)
                return
                
            if messagebox.askyesno("Confirm", f"Block ports for {duration}s?"):
                self.backup_firewall_rules()
                self.block_ports(duration)
                
        except ValueError:
            self.status_var.set("Invalid input")
            self.status.configure(fg=MinimalTheme.ERROR)
        except Exception as e:
            self.status_var.set("Error occurred")
            self.status.configure(fg=MinimalTheme.ERROR)

    def exit_app(self):
        """Handle proper cleanup and exit"""
        self.should_exit = True
        if self.is_blocking:
            self.is_blocking = False
            # Clean up any active firewall rules
            subprocess.run([ "netsh", "advfirewall", "firewall", "delete", "rule", 
                             "name=BlockAllPorts"], check=False)
        
        # Close the app gracefully if the root window exists
        if self.root.winfo_exists():
            self.root.quit()
            self.root.destroy()


if __name__ == "__main__":
    root = tk.Tk()
    app = PortBlockerUI(root)
    root.mainloop()
