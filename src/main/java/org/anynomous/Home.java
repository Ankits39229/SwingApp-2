package main.java.org.anynomous;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Home extends JPanel {
    // Enhanced color palette
    private static final Color BACKGROUND_COLOR = new Color(18, 18, 24);
    private static final Color SURFACE_COLOR = new Color(28, 28, 36);
    private static final Color ACCENT_COLOR = new Color(82, 145, 255);
    private static final Color SECONDARY_COLOR = new Color(138, 80, 246);
    private static final Color TERTIARY_COLOR = new Color(255, 94, 131);
    private static final Color SUCCESS_COLOR = new Color(46, 213, 115);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color SECONDARY_TEXT = new Color(170, 170, 190);
    
    private Timer statsTimer;
    private Timer particleTimer;
    private Timer pulseTimer;
    private float cpuUsage = 0;
    private float memoryUsage = 0;
    private float diskUsage = 0;
    private float pulseEffect = 0;
    private JPanel statsPanel;
    private JPanel welcomePanel;
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    
    // Particle class for welcome section animation
    private class Particle {
        float x, y;
        float size;
        float speed;
        float alpha;
        Color color;
        
        Particle() {
            reset();
            // Start at random positions
            y = random.nextFloat() * welcomePanel.getHeight();
        }
        
        void reset() {
            x = -10;
            y = random.nextFloat() * welcomePanel.getHeight();
            size = 2 + random.nextFloat() * 4;
            speed = 0.5f + random.nextFloat() * 1.5f;
            alpha = 0.1f + random.nextFloat() * 0.4f;
            
            // Random color from our palette
            Color[] colors = {ACCENT_COLOR, SECONDARY_COLOR, TERTIARY_COLOR};
            color = colors[random.nextInt(colors.length)];
        }
        
        void update() {
            x += speed;
            if (x > welcomePanel.getWidth()) {
                reset();
            }
        }
        
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                (int)(alpha * 255)));
            g2d.fill(new Ellipse2D.Float(x, y, size, size));
        }
    }

    public Home() {
        setupPanel();
        initializeComponents();
        startAnimations();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Welcome Section
        addWelcomeSection(contentPanel);
        addSpacing(contentPanel, 40);
        
        // Stats Section
        addStatsSection(contentPanel);
        addSpacing(contentPanel, 40);
        
        // Quick Actions
        addQuickActions(contentPanel);

        // Wrap in scroll pane
        JScrollPane scrollPane = createScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addWelcomeSection(JPanel panel) {
        welcomePanel = new JPanel(new BorderLayout(15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(SURFACE_COLOR.getRed(), SURFACE_COLOR.getGreen(), SURFACE_COLOR.getBlue(), 200),
                    getWidth(), getHeight(), new Color(BACKGROUND_COLOR.getRed(), BACKGROUND_COLOR.getGreen(), BACKGROUND_COLOR.getBlue(), 100)
                );
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                
                // Draw animated particles
                for (Particle p : particles) {
                    p.draw(g2d);
                }
                
                // Subtle glow effect
                float glowSize = 100 + 20 * (float)Math.sin(pulseEffect);
                RadialGradientPaint glow = new RadialGradientPaint(
                    getWidth() * 0.2f, getHeight() * 0.5f, glowSize,
                    new float[] {0.0f, 1.0f},
                    new Color[] {
                        new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 30),
                        new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 0)
                    }
                );
                g2d.setPaint(glow);
                g2d.fill(new Rectangle2D.Float(0, 0, getWidth(), getHeight()));
            }
        };
        welcomePanel.setOpaque(false);
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        welcomePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Text shadow
                g2d.setFont(getFont());
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawString(getText(), 2, getHeight() - 4);
                
                // Gradient text
                GradientPaint textGradient = new GradientPaint(
                    0, 0, TEXT_COLOR,
                    getWidth(), 0, ACCENT_COLOR
                );
                g2d.setPaint(textGradient);
                g2d.drawString(getText(), 0, getHeight() - 6);
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Your system is protected");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(SECONDARY_TEXT);

        welcomePanel.add(titleLabel, BorderLayout.NORTH);
        welcomePanel.add(subtitleLabel, BorderLayout.CENTER);
        panel.add(welcomePanel);
        
        // Initialize particles
        for (int i = 0; i < 15; i++) {
            particles.add(new Particle());
        }
    }

    private void addStatsSection(JPanel panel) {
        statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        addStatCard("CPU", cpuUsage, ACCENT_COLOR);
        addStatCard("Memory", memoryUsage, SECONDARY_COLOR);
        addStatCard("Disk", diskUsage, TERTIARY_COLOR);

        panel.add(statsPanel);
    }

    private void addStatCard(String title, float value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background with subtle gradient
                GradientPaint cardGradient = new GradientPaint(
                    0, 0, SURFACE_COLOR,
                    0, getHeight(), new Color(SURFACE_COLOR.getRed() - 5, SURFACE_COLOR.getGreen() - 5, SURFACE_COLOR.getBlue())
                );
                g2d.setPaint(cardGradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                
                // Subtle highlight at top
                g2d.setColor(new Color(255, 255, 255, 15));
                g2d.fillRect(0, 0, getWidth(), 1);

                // Progress circle
                int size = Math.min(getWidth(), getHeight()) - 60;
                int x = (getWidth() - size) / 2;
                int y = 45;

                // Background circle
                g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2d.drawArc(x, y, size, size, 0, 360);

                // Progress arc with glow
                g2d.setColor(color);
                g2d.drawArc(x, y, size, size, 90, -(int)(360 * value));
                
                // Glow effect at the end of the arc
                double angle = Math.toRadians(90 - 360 * value);
                int glowX = x + size/2 + (int)(Math.cos(angle) * size/2);
                int glowY = y + size/2 - (int)(Math.sin(angle) * size/2);
                
                // Pulse the glow
                float glowSize = 10 + 3 * (float)Math.sin(pulseEffect * 2);
                RadialGradientPaint glow = new RadialGradientPaint(
                    glowX, glowY, glowSize,
                    new float[] {0.0f, 1.0f},
                    new Color[] {
                        color,
                        new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)
                    }
                );
                g2d.setPaint(glow);
                g2d.fill(new Ellipse2D.Float(glowX - glowSize, glowY - glowSize, glowSize * 2, glowSize * 2));

                // Percentage text
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
                g2d.setColor(TEXT_COLOR);
                String percentage = String.format("%.0f%%", value * 100);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(percentage, 
                    x + (size - fm.stringWidth(percentage)) / 2,
                    y + (size + fm.getHeight()) / 2);
            }
        };
        card.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(SECONDARY_TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, BorderLayout.NORTH);

        statsPanel.add(card);
    }

    private void addQuickActions(JPanel panel) {
        JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        addActionButton(actionsPanel, "Scan", "🔍", ACCENT_COLOR);
        addActionButton(actionsPanel, "Update", "⭐", SECONDARY_COLOR);
        addActionButton(actionsPanel, "Settings", "⚙️", TERTIARY_COLOR);

        panel.add(actionsPanel);
    }

    private void addActionButton(JPanel panel, String text, String icon, Color color) {
        JPanel button = new JPanel(new BorderLayout(10, 0)) {
            private boolean isHovered = false;
            private float hoverIntensity = 0f;
            private Timer hoverTimer;

            {
                setOpaque(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        startHoverAnimation();
                    }
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        startHoverAnimation();
                    }
                });
            }
            
            private void startHoverAnimation() {
                if (hoverTimer != null && hoverTimer.isRunning()) {
                    hoverTimer.stop();
                }
                hoverTimer = new Timer(20, e -> {
                    if (isHovered && hoverIntensity < 1f) {
                        hoverIntensity += 0.1f;
                    } else if (!isHovered && hoverIntensity > 0f) {
                        hoverIntensity -= 0.1f;
                    } else {
                        hoverTimer.stop();
                    }
                    repaint();
                });
                hoverTimer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Button background
                g2d.setColor(SURFACE_COLOR);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Hover effect
                if (hoverIntensity > 0) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                        (int)(50 * hoverIntensity)));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                    
                    // Animated border
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                        (int)(200 * hoverIntensity)));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 11, 11));
                }
            }
        };
        button.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel iconLabel = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Draw glow
                g2d.setFont(getFont());
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                    (int)(100 * (0.7 + 0.3 * Math.sin(pulseEffect * 2)))));
                g2d.drawString(getText(), 1, getHeight() - 1);
                
                // Draw icon
                g2d.setColor(color);
                g2d.drawString(getText(), 0, getHeight() - 2);
            }
        };
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(TEXT_COLOR);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        button.add(iconLabel, BorderLayout.WEST);
        button.add(textLabel, BorderLayout.CENTER);
        panel.add(button);
    }

    private JScrollPane createScrollPane(JPanel content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Custom ScrollBar UI
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), 
                    ACCENT_COLOR.getBlue(), 120);
                this.trackColor = BACKGROUND_COLOR;
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
                if (thumbBounds.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fill(new RoundRectangle2D.Float(thumbBounds.x, thumbBounds.y,
                        thumbBounds.width, thumbBounds.height, 8, 8));
                
                // Add subtle highlight
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fill(new RoundRectangle2D.Float(thumbBounds.x, thumbBounds.y,
                        thumbBounds.width, 2, 2, 2));
                g2.dispose();
            }
        });

        return scrollPane;
    }

    private void addSpacing(JPanel panel, int height) {
        panel.add(Box.createRigidArea(new Dimension(0, height)));
    }

    private void startAnimations() {
        // Stats animation
        statsTimer = new Timer(50, e -> {
            cpuUsage = 0.3f + (float)(Math.sin(System.currentTimeMillis() / 2000.0) * 0.1);
            memoryUsage = 0.5f + (float)(Math.sin(System.currentTimeMillis() / 3000.0) * 0.1);
            diskUsage = 0.7f + (float)(Math.sin(System.currentTimeMillis() / 4000.0) * 0.1);
            statsPanel.repaint();
        });
        
        // Particle animation
        particleTimer = new Timer(30, e -> {
            for (Particle p : particles) {
                p.update();
            }
            welcomePanel.repaint();
        });
        
        // Pulse effect animation
        pulseTimer = new Timer(40, e -> {
            pulseEffect += 0.05f;
            welcomePanel.repaint();
            statsPanel.repaint();
        });
        
        statsTimer.start();
        particleTimer.start();
        pulseTimer.start();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (statsTimer != null) statsTimer.stop();
        if (particleTimer != null) particleTimer.stop();
        if (pulseTimer != null) pulseTimer.stop();
    }
}
