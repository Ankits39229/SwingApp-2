package main.java.org.anynomous;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.SwingWorker;
import java.awt.geom.*;
import java.awt.LinearGradientPaint;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

public class Audit extends JPanel {

    private static final Color PRIMARY_DARK = new Color(18, 18, 18);
    private static final Color SECONDARY_DARK = new Color(28, 28, 28);
    private static final Color ACCENT_COLOR = new Color(0, 122, 255);
    private static final Color ACCENT_HOVER = new Color(0, 102, 235);
    private static final Color DANGER_COLOR = new Color(220, 38, 38);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(179, 179, 179);
    private static final Color BORDER_COLOR = new Color(55, 65, 81);

    private static final Font BUTTON_FONT = new Font("SF Pro Text", Font.BOLD, 14);
    private static final Font LOG_FONT = new Font("SF Mono", Font.PLAIN, 13);


    private JPanel loadingPanel;
    private Timer animationTimer;
    private float animationAngle = 0;
    private JTextArea logArea;

    // Update the color scheme for a more professional look
    private static final Color[] PROFESSIONAL_COLORS = {
        new Color(41, 128, 185),    // Corporate Blue
        new Color(52, 152, 219),    // Light Blue
        new Color(24, 88, 141)      // Dark Blue
    };

    // Update gradient colors
    private static final Color GRADIENT_START = new Color(41, 128, 185);
    private static final Color GRADIENT_END = new Color(52, 152, 219);
    private static final Color GLOW_COLOR = new Color(41, 128, 185, 40);

    // Add subtle animation properties
    private float loadingProgress = 0.0f;
    private float buttonHoverAlpha = 0.0f;
    private Timer loadingTimer;


    // Add new animation-related fields
    private float pulseScale = 1.0f;
    private Timer pulseTimer;
    private boolean pulsing = false;
    private float progressValue = 0.0f;
    private Timer progressTimer;
    private final List<Particle> particles = new ArrayList<>();

    // Add these new color constants at the top of the class
    private static final Color[] GRADIENT_COLORS = {
        new Color(88, 86, 214),    // Purple
        new Color(0, 122, 255),    // Blue
        new Color(52, 199, 89),    // Green
        new Color(255, 149, 0),    // Orange
        new Color(255, 45, 85)     // Red
    };

    private static final Color NEON_GLOW = new Color(0, 255, 255, 30);
    private static final Color SUCCESS_COLOR = new Color(52, 199, 89);
    private static final Color WARNING_COLOR = new Color(255, 149, 0);

    // Add new animation fields
    private int colorIndex = 0;
    private Timer colorTransitionTimer;
    private float rippleScale = 0;
    private boolean showRipple = false;
    private List<StarParticle> stars = new ArrayList<>();

    // Add these new fields at the top of the class with other private fields
    private JButton viewAuditButton;
    private File currentAuditFile;
    private JButton shareAuditButton;
//    private PinataService pinataService;
//    private BlockchainService blockchainService;

//    private static final Logger logger = LogManager.getLogger(PinataService.class);

    public float getPulseScale() {
        return pulseScale;
    }

    public void setPulseScale(float pulseScale) {
        this.pulseScale = pulseScale;
    }

    public boolean isPulsing() {
        return pulsing;
    }

    public void setPulsing(boolean pulsing) {
        this.pulsing = pulsing;
    }

    public float getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(float progressValue) {
        this.progressValue = progressValue;
    }

    // Particle inner class for background animation
    private class Particle {
        float x, y;
        float speed;
        float size;
        float alpha;

        public Particle() {
            reset();
        }

        void reset() {
            x = (float) (Math.random() * getWidth());
            y = getHeight() + 10;
            speed = (float) (1 + Math.random() * 2);
            size = (float) (3 + Math.random() * 4);
            alpha = (float) (0.1 + Math.random() * 0.4);
        }

        void update() {
            y -= speed;
            if (y < -10) {
                reset();
            }
        }
    }

    // Add this new inner class for star particles
    private class StarParticle extends Particle {
        private float rotation;
        private float rotationSpeed;
        
        public StarParticle() {
            super();
            rotation = (float) (Math.random() * Math.PI * 2);
            rotationSpeed = (float) (Math.random() * 0.1 - 0.05);
        }
        
        @Override
        void update() {
            super.update();
            rotation += rotationSpeed;
        }
        
        void draw(Graphics2D g2d) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(rotation);
            
            // Draw a star shape
            for (int i = 0; i < 5; i++) {
                g2d.rotate(Math.PI * 2 / 5);
                g2d.drawLine(0, 0, 0, (int) size);
            }
            
            g2d.setTransform(old);
        }
    }

    public Audit() {
        setupMainPanel();
        setupAnimations();
        createLayout();
        initializeParticles();
        
    }

    private void setupMainPanel() {
        setBackground(PRIMARY_DARK);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 40, 40, 40));
    }

    private void setupAnimations() {
        // Loading animation
        loadingTimer = new Timer(16, e -> {
            animationAngle += 0.05;
            loadingProgress = Math.min(1.0f, loadingProgress + 0.01f);
            loadingPanel.repaint();
        });

        // Progress animation for loading
        progressTimer = new Timer(30, e -> {
            loadingProgress = (loadingProgress + 0.02f) % 1.0f;
            loadingPanel.repaint();
        });

        // Color transition timer with smoother changes
        colorTransitionTimer = new Timer(2000, e -> {
            colorIndex = (colorIndex + 1) % GRADIENT_COLORS.length;
            repaint();
        });
        colorTransitionTimer.start();

        // Particle animation with better performance
        // Update ripple with smoother animation
        Timer particleTimer = new Timer(16, e -> {
            particles.forEach(Particle::update);
            stars.forEach(StarParticle::update);

            // Update ripple with smoother animation
            if (showRipple) {
                rippleScale += 0.08f;
                if (rippleScale > 2.0f) {
                    showRipple = false;
                    rippleScale = 0;
                }
            }
            repaint();
        });
        particleTimer.start();
    }

    private void initializeParticles() {
        for (int i = 0; i < 30; i++) {
            particles.add(new Particle());
            if (i % 3 == 0) {
                stars.add(new StarParticle());
            }
        }
    }

    private void createLayout() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(PRIMARY_DARK);

        // Create loading panel
        loadingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = Math.min(getWidth(), getHeight()) - 40;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Draw elegant loading animation
                g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.setColor(GRADIENT_START);
                
                // Draw background circle
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                g2d.drawOval(x, y, size, size);
                
                // Draw progress arc
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                g2d.rotate(animationAngle, getWidth() / 2.0, getHeight() / 2.0);
                g2d.drawArc(x, y, size, size, 0, (int)(360 * loadingProgress));
            }
        };

        // Update the panel size
        loadingPanel.setPreferredSize(new Dimension(100, 100));
        loadingPanel.setMinimumSize(new Dimension(80, 80));
        loadingPanel.setVisible(false);
        loadingPanel.setOpaque(false);

        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(PRIMARY_DARK);

        JButton configButton = createModernButton("Configuration Audit", e -> {
            startLoading();
            checkScripts();
        });

        JButton networkButton = createModernButton("Network Audit", e -> {
            startLoading();
            executePythonScript("PythonScripts/check/sc.py");
        });

        buttonPanel.add(configButton);
        buttonPanel.add(networkButton);

        // Create log panel with modern styling
        JPanel logPanel = createModernLogPanel();

        // Create the view audit button (initially invisible)
        viewAuditButton = createModernButton("View Audit", e -> openAuditFile());
        viewAuditButton.setVisible(false);  // Hide initially
        
        // Create share button
        shareAuditButton = createModernButton("Share Audit", e -> shareAudit());
        shareAuditButton.setVisible(false);  // Hide initially
        
        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(loadingPanel);
        mainContent.add(Box.createVerticalStrut(30));
        mainContent.add(buttonPanel);
        mainContent.add(Box.createVerticalStrut(30));
        mainContent.add(logPanel);
        mainContent.add(Box.createVerticalStrut(30));
        mainContent.add(viewAuditButton);
        mainContent.add(shareAuditButton);

        add(mainContent, BorderLayout.CENTER);
    }

    private JButton createModernButton(String text, ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create subtle gradient background
                LinearGradientPaint gradient = new LinearGradientPaint(
                    0, 0, 0, getHeight(),
                    new float[]{0f, 1f},
                    new Color[]{GRADIENT_START, GRADIENT_END}
                );
                
                // Draw button background
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Draw subtle hover effect
                if (getModel().isRollover()) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, buttonHoverAlpha));
                    g2d.setColor(Color.WHITE);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                
                // Draw text with subtle shadow
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.drawString(getText(), textX + 1, textY + 1);
                g2d.setColor(TEXT_PRIMARY);
                g2d.drawString(getText(), textX, textY);
            }
        };

        // Add subtle hover animation
        Timer hoverTimer = new Timer(20, null);
        hoverTimer.addActionListener(e -> {
            if (button.getModel().isRollover()) {
                buttonHoverAlpha = Math.min(0.2f, buttonHoverAlpha + 0.02f);
            } else {
                buttonHoverAlpha = Math.max(0.0f, buttonHoverAlpha - 0.02f);
            }
            if (buttonHoverAlpha == 0.0f || buttonHoverAlpha == 0.2f) {
                hoverTimer.stop();
            }
            button.repaint();
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hoverTimer.start();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                hoverTimer.start();
            }
        });

        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_PRIMARY);
        button.setPreferredSize(new Dimension(200, 40));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        return button;
    }

    private JPanel createModernLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw subtle gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, SECONDARY_DARK,
                    0, getHeight(), SECONDARY_DARK.darker()
                );
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        
        logArea = new JTextArea();
        logArea.setBackground(SECONDARY_DARK);
        logArea.setForeground(TEXT_SECONDARY);
        logArea.setFont(LOG_FONT);
        logArea.setEditable(false);
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(SECONDARY_DARK);
        
        logPanel.add(scrollPane, BorderLayout.CENTER);
        return logPanel;
    }

    private void startLoading() {
        loadingPanel.setVisible(true);
        loadingProgress = 0.0f;
        loadingTimer.start();
        progressTimer.start();
        logArea.setText("");
    }

    private void stopLoading() {
        loadingTimer.stop();
        progressTimer.stop();
        loadingPanel.setVisible(false);
    }

    private void checkScripts() {
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                logArea.setText("");
                logArea.append("Starting configuration audit...\n");

                String[] scripts = {"2.1.py", "2.2.py", "2.3.py", "2.py", "3.1.py", "3.2.py", "9.1.py", "17.1.py",
                        "17.2.py", "18.1.py", "18.2.py", "18.3.py", "18.4.py", "18.5.py", "18.6.py", "18.7.py",
                        "18.8.py", "18.9.py", "19.1.py", "19.2.py", "19.3.py", "19.4"};

                for (String script : scripts) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("python", "PythonScripts/audit/" + script);
                        Process process = pb.start();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            publish(line);
                        }
                        process.waitFor();
                    } catch (Exception e) {
                        publish("Error running " + script + ": " + e.getMessage());
                    }
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks) {
                    logArea.append(line + "\n");
                }
            }


        };
        worker.execute();
    }

    private void executePythonScript(String scriptPath) {
        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
                pb.redirectErrorStream(true);

                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                int exitCode = process.waitFor();
                SwingUtilities.invokeLater(() -> logArea.append(String.format(
                        "[%s] Script '%s' executed with exit code %d\nOutput:\n%s\n",
                        getCurrentTimestamp(), scriptPath, exitCode, output.toString())));

            } catch (IOException | InterruptedException e) {
                SwingUtilities.invokeLater(() -> logArea.append(String.format(
                        "[%s] Error running script '%s': %s\n",
                        getCurrentTimestamp(), scriptPath, e.getMessage())));
                Thread.currentThread().interrupt();
            } finally {
                SwingUtilities.invokeLater(this::stopLoading);
            }
        }).start();
    }

    private String getCurrentTimestamp() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new Date());
    }



    // Add this method to open the audit file
    private void openAuditFile() {
        if (currentAuditFile != null && currentAuditFile.exists()) {
            try {
                Desktop.getDesktop().open(currentAuditFile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error opening audit file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Add method to share audit
    private void shareAudit() {
        if (currentAuditFile == null || !currentAuditFile.exists()) {
            showErrorDialog("No audit file available to share");
            return;
        }
        
        // Add file size check
        if (currentAuditFile.length() > 100 * 1024 * 1024) {
            showErrorDialog("File size exceeds 100MB limit");
            return;
        }
        
        // Show progress dialog with cancel option
        ProgressDialog progressDialog = createProgressDialog();
        progressDialog.setVisible(true);
        

    }

    // Helper methods for UI feedback
    private JDialog createLoadingDialog(String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setUndecorated(true);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(PRIMARY_DARK);
        
        JLabel label = new JLabel(message);
        label.setForeground(TEXT_PRIMARY);
        panel.add(label, BorderLayout.CENTER);
        
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        
        return dialog;
    }

    private void showShareSuccessDialog(String ipfsHash, String gatewayUrl) {
        String message = String.format("""
            Audit shared successfully!
            
            IPFS Hash: %s
            
            You can view your audit at:
            %s
            
            The audit has been uploaded to IPFS via Pinata and recorded on the Sepolia testnet.
            Anyone with this URL can access the audit report.
            """, ipfsHash, gatewayUrl);
        
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setBackground(null);
        textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        addCopyUrlButton(gatewayUrl, panel);
        
        JOptionPane.showMessageDialog(
            this,
            panel,
            "Share Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void addCopyUrlButton(String gatewayUrl, JPanel panel) {
        JButton copyButton = new JButton("Copy URL");
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(gatewayUrl);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(
                this,
                "URL copied to clipboard!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        panel.add(copyButton);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }



    private boolean isValidFileType(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".txt") || name.endsWith(".pdf") || name.endsWith(".json");
    }

    private static class ProgressDialog extends JDialog {
        private final JProgressBar progressBar;
        
        public ProgressDialog(Frame owner) {
            super(owner, "Sharing Audit", true);
            setLayout(new BorderLayout(10, 10));
            
            progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            add(progressBar, BorderLayout.CENTER);
            
            setSize(300, 80);
            setLocationRelativeTo(owner);
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        }
        
        public void setProgress(int value) {
            progressBar.setValue(value);
        }
    }

    private ProgressDialog createProgressDialog() {
        return new ProgressDialog((Frame) SwingUtilities.getWindowAncestor(this));
    }
}