package main.java.org.anynomous;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Clean extends JPanel {
    // Modern color scheme - Purple/Blue theme
    private static final Color BACKGROUND_COLOR = new Color(13, 13, 20);
    private static final Color CARD_BACKGROUND = new Color(20, 20, 35);
    private static final Color PRIMARY_COLOR = new Color(111, 76, 255);    // Purple
    private static final Color SECONDARY_COLOR = new Color(64, 85, 255);   // Blue
    private static final Color ACCENT_COLOR = new Color(86, 192, 255);     // Light Blue
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color SECONDARY_TEXT = new Color(176, 176, 200);
    private static final Color PROGRESS_BACKGROUND = new Color(30, 30, 50);

    private Timer pulseTimer;
    private float pulseScale = 1.0f;
    private float progressAngle = 0f;
    private float headerGlow = 0f;

    public Clean() {
        setupPanel();
        initializeComponents();
        startAnimations();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(25, 25, 25, 25));
    }

    private void initializeComponents() {
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        // Add header section with animated background
        addHeaderSection(mainContent, gbc);

        // Add cleaning cards with responsive layout
        addCleaningCards(mainContent, gbc);

        // Wrap in scroll pane with custom UI
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Custom scrollbar UI
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        
        add(scrollPane, BorderLayout.CENTER);
    }

    // Custom ScrollBar UI class
    private class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = PRIMARY_COLOR;
            this.trackColor = PROGRESS_BACKGROUND;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if(thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
            
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(thumbColor);
            g2.fill(new RoundRectangle2D.Float(thumbBounds.x, thumbBounds.y,
                    thumbBounds.width, thumbBounds.height, 10, 10));
            g2.dispose();
        }
    }

    private void addHeaderSection(JPanel panel, GridBagConstraints gbc) {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Animated gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 30),
                    getWidth(), getHeight(), new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 30)
                );
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                // Animated glow effects
                int glowSize = (int)(200 * pulseScale);
                float alpha = (float)(0.3 + 0.1 * Math.sin(headerGlow));
                g2d.setColor(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 
                    (int)(alpha * 255)));
                g2d.fillOval(getWidth()/2 - glowSize/2, -glowSize/2, glowSize, glowSize);

                // Secondary glow
                glowSize = (int)(150 * (2-pulseScale));
                g2d.setColor(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 
                    (int)(alpha * 200)));
                g2d.fillOval(getWidth()/4 - glowSize/2, -glowSize/3, glowSize, glowSize);
            }
        };
        headerPanel.setBorder(new EmptyBorder(40, 30, 40, 30));
        headerPanel.setOpaque(false);

        // Animated title with glow effect
        JLabel titleLabel = new JLabel("System Cleanup") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Draw glow
                g2d.setFont(getFont());
                g2d.setColor(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 
                    (int)(50 * pulseScale)));
                g2d.drawString(getText(), 2, getHeight() - 4);
                
                // Draw main text
                g2d.setColor(TEXT_COLOR);
                g2d.drawString(getText(), 0, getHeight() - 6);
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Animated subtitle
        JLabel subtitleLabel = new JLabel("Optimize your system performance");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(SECONDARY_TEXT);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(headerPanel, gbc);
    }

    private void addCleaningCards(JPanel panel, GridBagConstraints gbc) {
        // Responsive grid panel
        JPanel cardsPanel = new JPanel(new GridBagLayout());
        cardsPanel.setOpaque(false);

        // Add cards with responsive constraints
        addResponsiveCard(cardsPanel, createCleaningCard("Quick Clean", "Temporary Files", 
            "Clean temporary files to free up disk space", "PythonScripts/clean/temp.py"), 0);
        addResponsiveCard(cardsPanel, createCleaningCard("Deep Clean", "Prefetch Files", 
            "Remove prefetch files to improve performance", "PythonScripts/clean/prefetch.py"), 1);
        addResponsiveCard(cardsPanel, createCleaningCard("Full Clean", "Complete Cleanup", 
            "Perform a thorough system cleanup", "PythonScripts/clean/temp_prefetch.py"), 2);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(cardsPanel, gbc);
    }

    private void addResponsiveCard(JPanel panel, JPanel card, int position) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = position;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(card, gbc);
    }

    private JPanel createCleaningCard(String title, String subtitle, String description, String script) {
        // Create a custom panel class with progress functionality
        class CleaningCardPanel extends JPanel {
            private boolean isHovered = false;
            private float progress = 0f;
            private Timer progressTimer;

            CleaningCardPanel() {
                setLayout(new BorderLayout(15, 15));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw card background
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                // Draw hover effect
                if (isHovered) {
                    g2d.setColor(PRIMARY_COLOR);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                }

                // Draw progress circle
                int size = 80;
                int x = (getWidth() - size) / 2;
                int y = 40;

                // Draw background circle
                g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.setColor(PROGRESS_BACKGROUND);
                g2d.drawArc(x, y, size, size, 0, 360);

                // Draw progress
                g2d.setColor(PRIMARY_COLOR);
                g2d.drawArc(x, y, size, size, 90, -(int)(progressAngle * progress));
            }

            public void startProgress() {
                progress = 0f;
                if (progressTimer != null) progressTimer.stop();
                
                progressTimer = new Timer(50, e -> {
                    progress += 0.01f;
                    if (progress >= 1f) {
                        progress = 1f;
                        progressTimer.stop();
                    }
                    repaint();
                });
                progressTimer.start();
            }
        }

        // Create instance of our custom panel
        CleaningCardPanel card = new CleaningCardPanel();
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(300, 400));

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitleLabel.setForeground(SECONDARY_TEXT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(SECONDARY_TEXT);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button
        JButton cleanButton = new JButton("Start Cleaning") {
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(PRIMARY_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(SECONDARY_COLOR);
                } else {
                    g2d.setColor(PRIMARY_COLOR);
                }

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                super.paintComponent(g);
            }
        };
        cleanButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cleanButton.setForeground(TEXT_COLOR);
        cleanButton.setPreferredSize(new Dimension(200, 40));
        cleanButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cleanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cleanButton.addActionListener(e -> {
            card.startProgress();
            startCleanup(script);
        });

        // Add components
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(cleanButton);
        contentPanel.add(Box.createVerticalGlue());

        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }

    private void startCleanup(String script) {
        new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("python", script);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startAnimations() {
        pulseTimer = new Timer(50, e -> {
            pulseScale = 1.0f + 0.1f * (float)Math.sin(System.currentTimeMillis() / 1000.0);
            headerGlow += 0.05f;
            progressAngle = 360f;
            repaint();
        });
        pulseTimer.start();
    }

    // Add a resize listener for responsive adjustments
    @Override
    public void addNotify() {
        super.addNotify();
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    revalidate();
                    repaint();
                }
            });
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (pulseTimer != null) {
            pulseTimer.stop();
        }
    }
}