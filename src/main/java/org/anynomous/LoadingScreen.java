package main.java.org.anynomous;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import com.sun.awt.AWTUtilities;

public class LoadingScreen extends JWindow {
    private final JProgressBar progressBar;
    private final JLabel statusLabel;
    private double rotationAngle = 0;
    private final Timer animationTimer;
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    // Enhanced color scheme
    private static final Color ACCENT_COLOR = new Color(32, 34, 37); // Dark accent color
    private static final Color GREEN_COLOR = new Color(0, 255, 0); // Bright green
    private static final Color DARK_GREEN = new Color(0, 180, 0); // Darker green for contrast
    private static final Color LIGHT_GREEN = new Color(144, 238, 144); // Light green
    private static final Color BG_COLOR = new Color(25, 27, 29); // Dark background

    // Pulse animation variables
    private double pulseScale = 1.0;
    private double pulseDelta = 0.01;
    private double glowIntensity = 0;
    private double glowDelta = 0.02;

    // Particle class for background effects
    private class Particle {
        float x, y;
        float speed;
        float size;
        float alpha;
        float alphaChange;

        public Particle() {
            reset();
        }

        public void reset() {
            x = random.nextFloat() * getWidth();
            y = random.nextFloat() * getHeight();
            speed = 0.2f + random.nextFloat() * 0.8f;
            size = 1f + random.nextFloat() * 3f;
            alpha = 0.1f + random.nextFloat() * 0.3f;
            alphaChange = 0.001f + random.nextFloat() * 0.005f;
        }

        public void update() {
            y -= speed;
            alpha -= alphaChange;

            if (y < 0 || alpha <= 0) {
                reset();
                y = getHeight();
            }
        }

        public void draw(Graphics2D g2d) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(GREEN_COLOR);
            g2d.fill(new Ellipse2D.Float(x, y, size, size));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    public LoadingScreen() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

        // Enable window transparency
        if (getGraphicsConfiguration().getDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
            setOpacity(1.0f);
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Don't call super.paintComponent to keep panel transparent
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        // Initialize particles
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle());
        }

        // Create animated logo panel
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Don't call super.paintComponent to keep panel transparent
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Calculate center position
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                // Draw glow effect
                if (glowIntensity > 0) {
                    RadialGradientPaint glow = new RadialGradientPaint(
                            new Point2D.Float(centerX, centerY),
                            100f,
                            new float[] {0.0f, 1.0f},
                            new Color[] {
                                    new Color(0, 255, 0, (int)(50 * glowIntensity)),
                                    new Color(0, 255, 0, 0)
                            }
                    );
                    g2d.setPaint(glow);
                    g2d.fillOval(centerX - 100, centerY - 100, 200, 200);
                }

                // Draw rotating circles with pulse effect
                g2d.setStroke(new BasicStroke(2));

                // Outer rotating ring
                g2d.setColor(DARK_GREEN);
                g2d.rotate(rotationAngle, centerX, centerY);
                double outerRadius = 55 * pulseScale;
                for (int i = 0; i < 12; i++) {
                    double angle = (Math.PI * 2 * i) / 12;
                    int x = (int) (centerX + outerRadius * Math.cos(angle));
                    int y = (int) (centerY + outerRadius * Math.sin(angle));
                    int dotSize = (i % 3 == 0) ? 8 : 5; // Varying sizes
                    g2d.fillOval(x - dotSize/2, y - dotSize/2, dotSize, dotSize);
                }
                g2d.rotate(-rotationAngle, centerX, centerY);

                // Middle rotating ring
                g2d.setColor(GREEN_COLOR);
                g2d.rotate(-rotationAngle * 0.7, centerX, centerY);
                double middleRadius = 40 * pulseScale;
                for (int i = 0; i < 8; i++) {
                    double angle = (Math.PI * 2 * i) / 8;
                    int x = (int) (centerX + middleRadius * Math.cos(angle));
                    int y = (int) (centerY + middleRadius * Math.sin(angle));
                    g2d.fillOval(x - 4, y - 4, 7, 7);
                }
                g2d.rotate(rotationAngle * 0.7, centerX, centerY);

                // Inner rotating ring
                g2d.setColor(LIGHT_GREEN);
                g2d.rotate(rotationAngle * 1.3, centerX, centerY);
                double innerRadius = 25 * pulseScale;
                for (int i = 0; i < 6; i++) {
                    double angle = (Math.PI * 2 * i) / 6;
                    int x = (int) (centerX + innerRadius * Math.cos(angle));
                    int y = (int) (centerY + innerRadius * Math.sin(angle));
                    g2d.fillOval(x - 3, y - 3, 5, 5);
                }
                g2d.rotate(-rotationAngle * 1.3, centerX, centerY);

                // Draw connecting lines
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(new Color(0, 255, 0, 40));
                for (int i = 0; i < 6; i++) {
                    double angle = (Math.PI * 2 * i) / 6;
                    int x1 = (int) (centerX + (innerRadius-5) * Math.cos(angle));
                    int y1 = (int) (centerY + (innerRadius-5) * Math.sin(angle));
                    int x2 = (int) (centerX + (outerRadius+5) * Math.cos(angle));
                    int y2 = (int) (centerY + (outerRadius+5) * Math.sin(angle));
                    g2d.drawLine(x1, y1, x2, y2);
                }

                // Draw C2 text with glow effect
                drawTextWithGlow(g2d, "C2", centerX, centerY, new Font("Arial", Font.BOLD, 48));

                g2d.dispose();
            }

            private void drawTextWithGlow(Graphics2D g2d, String text, int x, int y, Font font) {
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();

                // Draw glow
                float glowSize = 3f;
                for (float i = glowSize; i > 0; i -= 0.5f) {
                    float alpha = 0.1f * (glowSize - i + 1) * (float)glowIntensity;
                    g2d.setColor(new Color(0f, 1f, 0f, alpha));
                    g2d.setFont(font.deriveFont(font.getSize() + i));
                    FontMetrics metrics = g2d.getFontMetrics();
                    int width = metrics.stringWidth(text);
                    g2d.drawString(text,
                            x - width/2,
                            y + textHeight/4);
                }

                // Draw text shadow
                g2d.setFont(font);
                g2d.setColor(new Color(0, 50, 0, 100));
                g2d.drawString(text,
                        x - textWidth/2 + 2,
                        y + textHeight/4 + 2);

                // Draw text
                g2d.setColor(new Color(190, 255, 190));
                g2d.drawString(text,
                        x - textWidth/2,
                        y + textHeight/4);

                // Draw highlight
                g2d.setColor(new Color(255, 255, 255, 70));
                g2d.setFont(font.deriveFont(font.getSize() - 1f));
                FontMetrics metrics = g2d.getFontMetrics();
                int width = metrics.stringWidth(text);
                g2d.drawString(text,
                        x - width/2,
                        y + textHeight/4 - 1);
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(400, 150));
        panel.add(logoPanel, BorderLayout.CENTER);

        // Create elegant progress bar
        progressBar = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw progress bar background with glow
                g2d.setColor(new Color(35, 39, 42, 150));
                RoundRectangle2D background = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.fill(background);

                // Draw progress with gradient and animation
                int width = (int) (getWidth() * (getValue() / 100.0));

                // Animated gradient with shift based on rotation
                float gradientShift = (float)(rotationAngle / (2 * Math.PI));
                GradientPaint gradient = new GradientPaint(
                        width * gradientShift, 0, GREEN_COLOR,
                        width * (gradientShift + 0.5f), getHeight(), DARK_GREEN
                );
                g2d.setPaint(gradient);

                RoundRectangle2D progressShape = new RoundRectangle2D.Float(0, 0, width, getHeight(), 10, 10);
                g2d.fill(progressShape);

                // Add shine effect to progress bar
                if (width > 0) {
                    g2d.setColor(new Color(255, 255, 255, 60));
                    g2d.setClip(progressShape);
                    g2d.fillRect(0, 0, width, getHeight()/3);
                    g2d.setClip(null);
                }

                // Draw glow around progress
                if (width > 0) {
                    g2d.setColor(new Color(GREEN_COLOR.getRed(), GREEN_COLOR.getGreen(), GREEN_COLOR.getBlue(), 30));
                    g2d.setStroke(new BasicStroke(3f));
                    g2d.draw(new RoundRectangle2D.Float(0, 0, width, getHeight(), 10, 10));
                }

                // Draw progress text with shadow
                if (isStringPainted()) {
                    String progressText = getString();
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(progressText);
                    int textHeight = fm.getHeight();

                    // Draw text shadow
                    g2d.setColor(new Color(0, 0, 0, 120));
                    g2d.drawString(progressText,
                            (getWidth() - textWidth) / 2 + 1,
                            (getHeight() + textHeight) / 2 - fm.getDescent() + 1);

                    // Draw text
                    g2d.setColor(LIGHT_GREEN);
                    g2d.drawString(progressText,
                            (getWidth() - textWidth) / 2,
                            (getHeight() + textHeight) / 2 - fm.getDescent());
                }

                g2d.dispose();
            }

            @Override
            public void setValue(int n) {
                int oldValue = getValue();
                super.setValue(n);
                if (n > oldValue) {
                    // Flash the glow effect when progress increases
                    glowIntensity = 1.0;
                }
            }
        };
        progressBar.setOpaque(false);
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 12));
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressBar.setForeground(GREEN_COLOR);

        // Status label with animated dots
        statusLabel = new JLabel("Loading...") {
            private int dotCount = 0;
            private long lastDotTime = 0;

            @Override
            protected void paintComponent(Graphics g) {
                long now = System.currentTimeMillis();
                if (now - lastDotTime > 500) {
                    dotCount = (dotCount + 1) % 4;
                    lastDotTime = now;

                    StringBuilder text = new StringBuilder(getText().replaceAll("\\.+$", ""));
                    for (int i = 0; i < dotCount; i++) {
                        text.append(".");
                    }
                    super.setText(text.toString());
                }

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int textWidth = fm.stringWidth(text);

                // Draw text shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(text, (getWidth() - textWidth) / 2 + 1, fm.getAscent() + 1);

                // Draw text with gradient
                GradientPaint textGradient = new GradientPaint(
                        0, 0, LIGHT_GREEN,
                        0, getHeight(), GREEN_COLOR
                );
                g2d.setPaint(textGradient);
                g2d.drawString(text, (getWidth() - textWidth) / 2, fm.getAscent());

                g2d.dispose();
            }
        };
        statusLabel.setForeground(GREEN_COLOR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Bottom panel with glossy effect
        JPanel bottomPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw subtle background
                g2d.setColor(new Color(25, 30, 35, 120));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Add glossy highlight
                g2d.setColor(new Color(255, 255, 255, 15));
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() / 2 - 5, 8, 8);

                g2d.dispose();
            }
        };
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        progressPanel.setOpaque(false);
        progressPanel.add(progressBar);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);

        bottomPanel.add(progressPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(statusPanel);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        // Start animation timer with more complex animations
        animationTimer = new Timer(16, e -> {
            // Rotate elements
            rotationAngle += 0.03;
            if (rotationAngle >= 2 * Math.PI) {
                rotationAngle = 0;
            }

            // Pulse animation
            pulseScale += pulseDelta;
            if (pulseScale > 1.05 || pulseScale < 0.95) {
                pulseDelta = -pulseDelta;
            }

            // Glow animation
            if (glowIntensity > 0) {
                glowIntensity -= glowDelta;
                if (glowIntensity < 0) {
                    glowIntensity = 0;
                }
            }

            // Update particles
            for (Particle p : particles) {
                p.update();
            }

            // Repaint components
            repaint();
        });
        animationTimer.start();
    }

    public void updateProgress(int progress, String status) {
        // Animate progress bar smoothly
        final int targetProgress = progress;
        final int currentProgress = progressBar.getValue();

        if (targetProgress > currentProgress) {
            Timer progressTimer = new Timer(10, null);
            progressTimer.addActionListener(e -> {
                int value = progressBar.getValue();
                if (value < targetProgress) {
                    progressBar.setValue(value + 1);
                    // Increase glow briefly when progressing
                    glowIntensity = Math.max(glowIntensity, 0.3);
                } else {
                    ((Timer)e.getSource()).stop();
                }
            });
            progressTimer.start();
        } else {
            progressBar.setValue(progress);
        }

        statusLabel.setText(status);
    }

    // Make sure to stop the animation timer when the window is disposed
    @Override
    public void dispose() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        super.dispose();
    }
}