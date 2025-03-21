package main.java.org.anynomous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import com.sun.awt.AWTUtilities;

public class LoadingScreen extends JWindow {
    private final JLabel statusLabel;
    private double rotationAngle = 0;
    private final Timer animationTimer;
    private final List<JoyParticle> particles = new ArrayList<>();
    private final Random random = new Random();

    // Brighter professional green theme color palette
    private static final Color PRIMARY_COLOR = new Color(0, 170, 70); // Brighter professional green
    private static final Color SECONDARY_COLOR = new Color(0, 140, 60); // Medium green
    private static final Color ACCENT_COLOR1 = new Color(120, 220, 120); // Light bright green
    private static final Color ACCENT_COLOR2 = new Color(60, 200, 100); // Bright medium green
    private static final Color ACCENT_COLOR3 = new Color(180, 255, 180); // Very light green
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // Pure white for text
    private static final Color LOGO_GLOW = new Color(160, 255, 160, 120); // Brighter glow color

    // Animation properties
    private double bounceHeight = 0;
    private double bounceDelta = 0.3;
    private double pulseScale = 1.0;
    private double pulseDelta = 0.005;
    private double glowIntensity = 0;

    // Array of brighter green shades for particles
    private final Color[] GREEN_COLORS = {
        PRIMARY_COLOR, SECONDARY_COLOR, ACCENT_COLOR1, ACCENT_COLOR2, ACCENT_COLOR3,
        new Color(50, 220, 100),  // Bright green
        new Color(100, 240, 130)  // Brighter green
    };

    // Professional particle class with brighter effects
    private class JoyParticle {
        float x, y;
        float xSpeed, ySpeed;
        float rotation;
        float rotationSpeed;
        float size;
        Color color;
        Shape shape;
        float alpha = 0.6f; // Slightly higher alpha for brightness

        public JoyParticle() {
            reset();
        }

        public void reset() {
            x = random.nextFloat() * getWidth();
            y = getHeight() + random.nextFloat() * 50;
            
            xSpeed = -0.8f + random.nextFloat() * 1.6f; 
            ySpeed = -0.6f - random.nextFloat() * 1.0f;
            
            size = 3 + random.nextFloat() * 7;
            
            rotation = random.nextFloat() * 360;
            rotationSpeed = -1f + random.nextFloat() * 2f;
            
            // Brighter colors with higher alpha
            color = new Color(
                GREEN_COLORS[random.nextInt(GREEN_COLORS.length)].getRed(),
                GREEN_COLORS[random.nextInt(GREEN_COLORS.length)].getGreen(),
                GREEN_COLORS[random.nextInt(GREEN_COLORS.length)].getBlue(),
                40 + random.nextInt(60) // More visible
            );
            
            int shapeType = random.nextInt(3);
            switch (shapeType) {
                case 0: // Circle
                    shape = new Ellipse2D.Float(-size/2, -size/2, size, size);
                    break;
                case 1: // Square
                    shape = new Rectangle2D.Float(-size/2, -size/2, size, size);
                    break;
                case 2: // Diamond
                    shape = createDiamond(size);
                    break;
            }
            
            alpha = 0.4f + random.nextFloat() * 0.3f; // Higher alpha for brightness
        }
        
        private Shape createDiamond(float size) {
            Path2D path = new Path2D.Float();
            path.moveTo(0, -size/2);
            path.lineTo(size/2, 0);
            path.lineTo(0, size/2);
            path.lineTo(-size/2, 0);
            path.closePath();
            return path;
        }

        public void update() {
            x += xSpeed;
            y += ySpeed;
            rotation += rotationSpeed;
            
            xSpeed += -0.03f + random.nextFloat() * 0.06f;
            xSpeed = Math.max(-1.2f, Math.min(1.2f, xSpeed));
            
            alpha -= 0.003f;

            if (y < -size || alpha <= 0) {
                reset();
            }
        }

        public void draw(Graphics2D g2d) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));
            
            g2d.setColor(color);
            g2d.fill(shape);
            
            // Add a subtle bright highlight for more luminosity
            g2d.setColor(new Color(255, 255, 255, 100));
            if (shape instanceof Ellipse2D.Float) {
                g2d.fillOval((int)(-size/4), (int)(-size/4), (int)(size/2), (int)(size/2));
            }
            
            g2d.setTransform(old);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    public LoadingScreen() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0)); // Fully transparent window background

        // Initialize authentication server early to ensure it's ready
        try {
            AuthenticationEndpoint.getInstance();
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize authentication server: " + e.getMessage());
        }

        // Create main panel with custom painting - completely transparent
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Completely transparent
                
                // Draw brighter particles
                for (JoyParticle particle : particles) {
                    particle.draw(g2d);
                }
            }
        };
        mainPanel.setOpaque(false);
        
        // Center panel for logo and animations - transparent
        JPanel centerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2 + (int)bounceHeight;
                
                // Draw brighter logo background
                drawBrighterLogoBg(g2d, centerX, centerY);
                
                // Draw brighter ring animation
                drawBrighterRings(g2d, centerX, centerY);
                
                // Draw professional logo "C²" with increased brightness
                drawBrighterLogo(g2d, centerX, centerY);
            }
            
            private void drawBrighterLogoBg(Graphics2D g2d, int centerX, int centerY) {
                // Create a brighter circular backdrop for the logo
                int bgSize = 140;
                
                // Outer glow with increased brightness
                RadialGradientPaint outerGlow = new RadialGradientPaint(
                    centerX, centerY, bgSize,
                    new float[]{0.4f, 1.0f},
                    new Color[]{
                        new Color(120, 255, 150, 15), // Brighter outer glow
                        new Color(0, 0, 0, 0)
                    }
                );
                g2d.setPaint(outerGlow);
                g2d.fillOval(centerX - bgSize, centerY - bgSize, bgSize * 2, bgSize * 2);
                
                // Inner brighter backdrop
                int innerSize = 80;
                
                // Brighter gradient background
                RadialGradientPaint innerGlow = new RadialGradientPaint(
                    centerX, centerY, innerSize,
                    new float[]{0.0f, 0.8f},
                    new Color[]{
                        new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), 
                                  PRIMARY_COLOR.getBlue(), 25), // Increased alpha
                        new Color(ACCENT_COLOR1.getRed(), ACCENT_COLOR1.getGreen(), 
                                 ACCENT_COLOR1.getBlue(), 5)
                    }
                );
                g2d.setPaint(innerGlow);
                g2d.fillOval(centerX - innerSize, centerY - innerSize, innerSize * 2, innerSize * 2);
            }
            
            private void drawBrighterRings(Graphics2D g2d, int centerX, int centerY) {
                // Draw brighter, more visible rings
                
                // Outer ring - brighter green
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), 
                                      SECONDARY_COLOR.getBlue(), 70)); // Increased visibility
                g2d.draw(new Ellipse2D.Double(centerX - 75, centerY - 75, 150, 150));
                
                // Middle ring - rotating with dashed style
                float[] dashes = {3f, 7f};
                g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashes, 
                                             (float)((rotationAngle * 1.5) % (2 * Math.PI))));
                g2d.setColor(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), 
                                      PRIMARY_COLOR.getBlue(), 80)); // Brighter
                g2d.draw(new Ellipse2D.Double(centerX - 60, centerY - 60, 120, 120));
                
                // Inner ring - brighter light green
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(new Color(ACCENT_COLOR1.getRed(), ACCENT_COLOR1.getGreen(), 
                                      ACCENT_COLOR1.getBlue(), 60)); // Brighter
                g2d.draw(new Ellipse2D.Double(centerX - 45, centerY - 45, 90, 90));
                
                // Add an extra bright pulsing ring for more luminosity
                g2d.setStroke(new BasicStroke(1.0f));
                float pulseRadius = (float)(90 * pulseScale);
                g2d.setColor(new Color(180, 255, 200, 30 + (int)(20 * Math.sin(rotationAngle * 2)))); // Pulsing alpha
                g2d.draw(new Ellipse2D.Double(
                    centerX - pulseRadius/2, centerY - pulseRadius/2, 
                    pulseRadius, pulseRadius
                ));
            }
            
            private void drawBrighterLogo(Graphics2D g2d, int centerX, int centerY) {
                // Use a professional font for the logo
                Font logoFont = new Font("Arial", Font.BOLD, 58);
                try {
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    String[] fonts = ge.getAvailableFontFamilyNames();
                    for (String font : fonts) {
                        if (font.equals("Segoe UI") || font.equals("Century Gothic") || 
                            font.equals("Verdana") || font.equals("Helvetica")) {
                            logoFont = new Font(font, Font.BOLD, 58);
                            break;
                        }
                    }
                } catch (Exception e) {
                    // Fallback to Arial
                }
                
                g2d.setFont(logoFont);
                
                // Draw brighter glow effect
                if (glowIntensity > 0) {
                    drawBrighterLogoGlow(g2d, "C2", centerX, centerY, logoFont);
                } else {
                    // Always have a subtle glow for brightness
                    float tempGlow = 0.3f;
                    drawBrighterLogoGlow(g2d, "C2", centerX, centerY, logoFont);
                }
                
                // Draw logo text with brighter gradient fill
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth("C2");
                int textHeight = fm.getHeight();
                int textX = centerX - textWidth / 2;
                int textY = centerY + fm.getHeight() / 4;
                
                // Create a brighter gradient for the text
                GradientPaint textGradient = new GradientPaint(
                    textX, textY - textHeight, 
                    new Color(255, 255, 255), // Pure white at top
                    textX, textY, 
                    new Color(220, 255, 230)  // Brighter green-white at bottom
                );
                
                // Create a text shape to fill with gradient
                TextLayout textLayout = new TextLayout("C2", logoFont, g2d.getFontRenderContext());
                Shape textShape = textLayout.getOutline(
                    AffineTransform.getTranslateInstance(textX, textY)
                );
                
                // Fill with brighter gradient
                g2d.setPaint(textGradient);
                g2d.fill(textShape);
                
                // Add a subtle outline for definition
                g2d.setColor(new Color(80, 180, 100, 120)); // Brighter outline
                g2d.setStroke(new BasicStroke(0.7f));
                g2d.draw(textShape);
                
                // Add a bright highlight effect
                Shape clipOld = g2d.getClip();
                g2d.setClip(textShape);
                g2d.setColor(new Color(255, 255, 255, 120)); // Brighter highlight
                g2d.fillRect(textX, textY - textHeight, textWidth, textHeight / 3);
                g2d.setClip(clipOld);
            }
            
            private void drawBrighterLogoGlow(Graphics2D g2d, String text, int x, int y, Font font) {
                FontMetrics fm = g2d.getFontMetrics(font);
                int textX = x - fm.stringWidth(text) / 2;
                int textY = y + fm.getHeight() / 4;
                
                // Create a copy of the graphics object
                Graphics2D g2dCopy = (Graphics2D) g2d.create();
                
                // Brighter professional glow effect
                g2dCopy.setFont(font);
                
                // Brighter outer glow
                float alpha = (float)(0.2 * (glowIntensity > 0 ? glowIntensity : 0.3));
                
                // Use brighter colors for the glow
                Color[] glowColors = {
                    new Color(20, 200, 100, 100),  // Bright green
                    new Color(100, 230, 130, 80),  // Brighter green
                    new Color(160, 255, 180, 60)   // Very bright green
                };
                
                // Multiple layers of glow for more brightness
                for (int i = 0; i < glowColors.length; i++) {
                    for (int j = 0; j < 2; j++) { // Double-layer the glow
                        double angle = Math.PI / 3 * i + rotationAngle / 2;
                        int offsetX = (int)(Math.cos(angle) * (2 + j));
                        int offsetY = (int)(Math.sin(angle) * (2 + j));
                        
                        g2dCopy.setColor(new Color(
                            glowColors[i].getRed(), 
                            glowColors[i].getGreen(), 
                            glowColors[i].getBlue(),
                            (int)(glowColors[i].getAlpha() * alpha * (1.0 - j * 0.3))
                        ));
                        
                        g2dCopy.drawString(text, textX + offsetX, textY + offsetY);
                    }
                }
                
                // Add an extra bright center glow
                g2dCopy.setColor(new Color(255, 255, 255, (int)(40 * alpha)));
                g2dCopy.drawString(text, textX, textY);
                
                // Cleanup
                g2dCopy.dispose();
            }
        };
        centerPanel.setOpaque(false);
        
        // Brighter status message
        statusLabel = new JLabel("Initializing system...") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Draw status text with professional style
                String text = getText();
                
                // Use a professional font
                g2d.setFont(new Font("Arial", Font.PLAIN, 14));
                FontMetrics fm = g2d.getFontMetrics();
                
                // Center the text
                int width = getWidth();
                int x = (width - fm.stringWidth(text)) / 2;
                int y = fm.getAscent();
                
                // Draw subtle shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.drawString(text, x + 1, y + 1);
                
                // Draw text with a brighter professional green
                g2d.setColor(new Color(80, 220, 130)); // Brighter green
                g2d.drawString(text, x, y);
            }
        };
        statusLabel.setForeground(new Color(80, 220, 130)); // Brighter green
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Create a panel for the status label
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Layout components
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        setContentPane(mainPanel);
        
        // Initialize particles - slightly more for brightness
        for (int i = 0; i < 45; i++) {
            particles.add(new JoyParticle());
        }
        
        // Start animation
        animationTimer = new Timer(20, e -> {
            rotationAngle = (rotationAngle + 0.01) % (2 * Math.PI);
            
            bounceHeight += bounceDelta;
            if (bounceHeight > 3 || bounceHeight < -3) {
                bounceDelta = -bounceDelta;
            }
            
            pulseScale += pulseDelta;
            if (pulseScale > 1.05 || pulseScale < 0.95) {
                pulseDelta = -pulseDelta;
            }

            if (glowIntensity > 0) {
                glowIntensity -= 0.01;
                if (glowIntensity < 0) glowIntensity = 0;
            }
            
            for (JoyParticle particle : particles) {
                particle.update();
            }
            
            repaint();
        });
        animationTimer.start();
    }

    public void updateProgress(int progress, String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            
            // Add brighter glow effect for status updates
            if (progress % 25 == 0 && progress > 0) {
                Timer glowTimer = new Timer(30, null);
                glowTimer.addActionListener(new ActionListener() {
                    int frames = 0;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        glowIntensity = 1.0; // Full glow
                        
                        // Add a few bright particles
                        if (frames < 3) {
                            for (int i = 0; i < 5; i++) {
                                JoyParticle particle = new JoyParticle();
                                particle.y = getHeight() / 2;
                                particle.x = getWidth() / 2;
                                particle.ySpeed = -1f - random.nextFloat() * 1.5f;
                                particle.alpha = 0.6f; // Brighter particles
                                particles.add(particle);
                            }
                        }
                        
                        frames++;
                        if (frames >= 5) {
                            glowTimer.stop();
                        }
                    }
                });
                glowTimer.start();
            }

            // When loading is complete (progress = 100), show MetaMaskAuth
            if (progress >= 100) {
                statusLabel.setText("Waiting for MetaMask authentication...");
            }
        });
    }
    
    @Override
    public void dispose() {
        animationTimer.stop();
        super.dispose();
    }

    private void initializeAuthentication() {
        // No need to create a new auth window - it will be managed by Main.java
        statusLabel.setText("Waiting for authentication...");
    }
}