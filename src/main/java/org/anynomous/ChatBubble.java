package main.java.org.anynomous;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class ChatBubble extends JPanel {
    // Core colors
    private static final Color BUBBLE_COLOR = new Color(79, 147, 255);
    private static final Color BUBBLE_GRADIENT_END = new Color(117, 166, 255);
    private static final Color HOVER_COLOR = new Color(96, 165, 250);
    private static final Color HOVER_GRADIENT_END = new Color(143, 190, 255);
    private static final Color BACKGROUND_COLOR = new Color(17, 24, 39, 230);
    private static final Color HEADER_COLOR = new Color(31, 41, 55, 240);
    private static final Color TEXT_COLOR = new Color(243, 244, 246);
    
    // Base dimensions (will be scaled for responsiveness)
    private static final int BASE_BUBBLE_SIZE = 60;
    private int BUBBLE_SIZE = BASE_BUBBLE_SIZE;
    private int CORNER_RADIUS = BUBBLE_SIZE / 2;
    private int DIALOG_WIDTH = 380;
    private int DIALOG_HEIGHT = 580;
    
    // Animation properties
    private Point location;
    private boolean isDragging = false;
    private Point dragStart;
    private float pulseScale = 1.0f;
    private float appearScale = 0.0f;
    private float dialogOpacity = 0.0f;
    private List<RippleEffect> ripples = new ArrayList<>();
    private boolean hasNotification = false;
    private float notificationPulse = 0.0f;
    
    // Icon animation properties
    private float iconAnimProgress = 0f;
    private float iconAnimDirection = 1f;
    private int iconAnimState = 0;
    private final int ICON_ANIM_FRAMES = 3;
    private float iconBounceOffset = 0f;
    
    // Timers for animations
    private Timer pulseTimer;
    private Timer appearTimer;
    private Timer dialogAnimTimer;
    private Timer notificationTimer;
    private Timer iconAnimTimer;
    
    // UI components
    private JDialog chatDialog;
    private JPanel dialogContentPanel;
    private boolean isExpanded = false;
    private Assistant assistant;
    
    // Screen metrics for responsiveness
    private Dimension screenSize;
    private float scaleFactor = 1.0f;

    public ChatBubble() {
        setOpaque(false);
        
        // Get screen metrics for responsiveness
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        updateResponsiveMetrics();
        
        setPreferredSize(new Dimension(BUBBLE_SIZE, BUBBLE_SIZE));
        setupBubble();
        setupChatDialog();
        startAnimations();
        
        // Add component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateResponsiveMetrics();
                updateChatDialogLocation();
            }
        });
    }
    
    private void updateResponsiveMetrics() {
        // Calculate the scale factor based on screen resolution
        // Use the smaller dimension (width or height) to determine scale
        int minDimension = Math.min(screenSize.width, screenSize.height);
        
        // Base scale on a reference resolution of 1920x1080
        scaleFactor = Math.max(0.75f, Math.min(1.5f, minDimension / 1080f));
        
        // Scale UI components
        BUBBLE_SIZE = (int)(BASE_BUBBLE_SIZE * scaleFactor);
        CORNER_RADIUS = BUBBLE_SIZE / 2;
        DIALOG_WIDTH = (int)(380 * scaleFactor);
        DIALOG_HEIGHT = (int)(580 * scaleFactor);
        
        // Update the panel size
        setPreferredSize(new Dimension(BUBBLE_SIZE, BUBBLE_SIZE));
        
        // If dialog exists, update its size
        if (chatDialog != null) {
            chatDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        }
    }

    private void setupBubble() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
                isDragging = true;
                
                // Create ripple effect at click point
                RippleEffect ripple = new RippleEffect(e.getPoint());
                ripples.add(ripple);
                Timer rippleTimer = new Timer(16, evt -> {
                    ripple.scale += 0.15f;
                    ripple.opacity -= 0.03f;
                    if (ripple.opacity <= 0) {
                        ripples.remove(ripple);
                        ((Timer) evt.getSource()).stop();
                    }
                    repaint();
                });
                rippleTimer.start();
                
                // Accelerate icon animation on click
                iconAnimDirection = 3f;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                if (dragStart != null && 
                    dragStart.distance(e.getPoint()) < 5) {
                    toggleChat();
                }
                
                // Return icon animation to normal speed
                iconAnimDirection = 1f;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pulseScale = 1.1f;
                // Speed up icon animation on hover
                iconAnimDirection = 2f;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pulseScale = 1.0f;
                // Return to normal animation speed
                iconAnimDirection = 1f;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    Point current = getLocation();
                    setLocation(
                        current.x + e.getX() - dragStart.x,
                        current.y + e.getY() - dragStart.y
                    );
                    updateChatDialogLocation();
                }
            }
        });
        
        // Start with scale 0 for entry animation
        appearScale = 0.0f;
        appearTimer = new Timer(16, e -> {
            appearScale += 0.05f;
            if (appearScale >= 1.0f) {
                appearScale = 1.0f;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        appearTimer.start();
    }

    private void setupChatDialog() {
        chatDialog = new JDialog((Frame) null);
        chatDialog.setUndecorated(true);
        chatDialog.setBackground(new Color(0, 0, 0, 0));
        
        // Create the assistant panel with appropriate scaling
        assistant = new Assistant();
        
        // Create content panel with custom painting
        dialogContentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Apply opacity to dialog background
                AlphaComposite alphaComposite = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, dialogOpacity);
                g2d.setComposite(alphaComposite);
                
                // Dialog shadow
                int shadowSize = (int)(10 * scaleFactor);
                for (int i = shadowSize; i > 0; i--) {
                    float alpha = 0.1f * i / shadowSize;
                    g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255 * dialogOpacity)));
                    g2d.fill(new RoundRectangle2D.Float(
                        shadowSize - i, shadowSize - i,
                        getWidth() - (shadowSize - i) * 2,
                        getHeight() - (shadowSize - i) * 2,
                        15 * scaleFactor, 15 * scaleFactor
                    ));
                }
                
                // Create gradient background
                GradientPaint backgroundGradient = new GradientPaint(
                    0, 0, BACKGROUND_COLOR,
                    0, getHeight(), new Color(15, 20, 35, 230)
                );
                g2d.setPaint(backgroundGradient);
                g2d.fill(new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 
                    15 * scaleFactor, 15 * scaleFactor
                ));
                
                // Add subtle pattern
                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = 0; i < getHeight(); i += 4) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };
        
        dialogContentPanel.setOpaque(false);
        
        // Create custom header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Add components to content panel
        dialogContentPanel.add(headerPanel, BorderLayout.NORTH);
        dialogContentPanel.add(assistant, BorderLayout.CENTER);
        
        chatDialog.setContentPane(dialogContentPanel);
        chatDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        chatDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Header gradient background
                GradientPaint headerGradient = new GradientPaint(
                    0, 0, HEADER_COLOR,
                    getWidth(), 0, new Color(41, 51, 65, 240)
                );
                g2d.setPaint(headerGradient);
                
                float cornerRadius = 15 * scaleFactor;
                
                // Draw just the top part as rounded
                g2d.fill(new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight() + cornerRadius, cornerRadius, cornerRadius
                ));
                
                // Draw bottom part as rectangle to connect with the main panel
                g2d.fillRect(0, (int)cornerRadius, getWidth(), getHeight() - (int)cornerRadius);
                
                // Add subtle highlight at top
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillRect(0, 0, getWidth(), 1);
            }
        };
        
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(DIALOG_WIDTH, (int)(45 * scaleFactor)));
        headerPanel.setBorder(new EmptyBorder(
            (int)(8 * scaleFactor), 
            (int)(15 * scaleFactor), 
            (int)(8 * scaleFactor), 
            (int)(15 * scaleFactor)
        ));
        
        // Create header title
        JLabel titleLabel = new JLabel("Assistant");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, (int)(16 * scaleFactor)));
        titleLabel.setForeground(TEXT_COLOR);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, (int)(8 * scaleFactor), 0));
        buttonsPanel.setOpaque(false);
        
        // Create minimize button
        JLabel minimizeButton = createHeaderButton("−", e -> toggleChat());
        
        // Create close button
        JLabel closeButton = createHeaderButton("×", e -> toggleChat());
        
        // Add components
        buttonsPanel.add(minimizeButton);
        buttonsPanel.add(closeButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Make header draggable
        MouseAdapter dragAdapter = new MouseAdapter() {
            private Point dragStart;
            
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    Point dialogLocation = chatDialog.getLocation();
                    chatDialog.setLocation(
                        dialogLocation.x + e.getX() - dragStart.x,
                        dialogLocation.y + e.getY() - dragStart.y
                    );
                }
            }
        };
        
        headerPanel.addMouseListener(dragAdapter);
        headerPanel.addMouseMotionListener(dragAdapter);
        titleLabel.addMouseListener(dragAdapter);
        titleLabel.addMouseMotionListener(dragAdapter);
        
        return headerPanel;
    }
    
    private JLabel createHeaderButton(String text, ActionListener action) {
        JLabel button = new JLabel(text) {
            private boolean isHovered = false;
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                    
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (action != null) {
                            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "click"));
                        }
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isHovered) {
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }
                
                g2d.setColor(TEXT_COLOR);
                Font font = new Font("Segoe UI", Font.BOLD, (int)(16 * scaleFactor));
                g2d.setFont(font);
                
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                g2d.drawString(text, textX, textY);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension((int)(25 * scaleFactor), (int)(25 * scaleFactor));
            }
        };
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void toggleChat() {
        if (!isExpanded) {
            // Show dialog with animation
            updateChatDialogLocation();
            chatDialog.setVisible(true);
            dialogOpacity = 0.0f;
            
            if (dialogAnimTimer != null && dialogAnimTimer.isRunning()) {
                dialogAnimTimer.stop();
            }
            
            dialogAnimTimer = new Timer(16, e -> {
                dialogOpacity += 0.08f;
                if (dialogOpacity >= 1.0f) {
                    dialogOpacity = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                dialogContentPanel.repaint();
            });
            dialogAnimTimer.start();
            
            // Reset notification
            hasNotification = false;
        } else {
            // Hide dialog with animation
            if (dialogAnimTimer != null && dialogAnimTimer.isRunning()) {
                dialogAnimTimer.stop();
            }
            
            dialogAnimTimer = new Timer(16, e -> {
                dialogOpacity -= 0.08f;
                if (dialogOpacity <= 0.0f) {
                    dialogOpacity = 0.0f;
                    chatDialog.setVisible(false);
                    ((Timer) e.getSource()).stop();
                }
                dialogContentPanel.repaint();
            });
            dialogAnimTimer.start();
        }
        isExpanded = !isExpanded;
    }

    private void updateChatDialogLocation() {
        if (chatDialog != null) {
            Point bubbleLocation;
            try {
                bubbleLocation = getLocationOnScreen();
                
                // Ensure dialog stays within screen bounds
                int dialogX = bubbleLocation.x - chatDialog.getWidth() + BUBBLE_SIZE;
                int dialogY = bubbleLocation.y - chatDialog.getHeight();
                
                // Adjust if dialog would go off-screen
                if (dialogX < 0) dialogX = 0;
                if (dialogY < 0) dialogY = 0;
                if (dialogX + chatDialog.getWidth() > screenSize.width) {
                    dialogX = screenSize.width - chatDialog.getWidth();
                }
                if (dialogY + chatDialog.getHeight() > screenSize.height) {
                    dialogY = screenSize.height - chatDialog.getHeight();
                }
                
                chatDialog.setLocation(dialogX, dialogY);
            } catch (IllegalComponentStateException e) {
                // Component might not be showing on screen yet
            }
        }
    }

    private void startAnimations() {
        // Gentle pulse animation
        pulseTimer = new Timer(50, e -> {
            if (!isExpanded) {
                pulseScale = 1.0f + 0.03f * 
                    (float)Math.sin(System.currentTimeMillis() / 800.0);
            }
            repaint();
        });
        pulseTimer.start();
        
        // Notification pulse animation
        notificationTimer = new Timer(30, e -> {
            if (hasNotification) {
                notificationPulse += 0.07f;
                if (notificationPulse > Math.PI * 2) {
                    notificationPulse = 0;
                }
                repaint();
            }
        });
        notificationTimer.start();
        
        // Icon animation timer
        iconAnimTimer = new Timer(16, e -> {
            // Update icon animation progress
            iconAnimProgress += 0.02f * iconAnimDirection;
            
            // Bounce effect
            iconBounceOffset = (float)Math.sin(System.currentTimeMillis() / 300.0) * 2;
            
            // Cycle through animation states
            if (iconAnimProgress >= 1.0f) {
                iconAnimProgress = 0f;
                iconAnimState = (iconAnimState + 1) % ICON_ANIM_FRAMES;
            }
            
            repaint();
        });
        iconAnimTimer.start();
    }
    
    // Call this method to trigger a notification animation
    public void showNotification() {
        hasNotification = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
            RenderingHints.VALUE_RENDER_QUALITY);

        // Calculate actual bubble size with scale
        float actualScale = appearScale * (isExpanded ? 1.1f : pulseScale);
        int size = (int)(BUBBLE_SIZE * actualScale);
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        // Draw shadow
        int shadowSize = (int)(6 * scaleFactor);
        for (int i = shadowSize; i > 0; i--) {
            float alpha = 0.1f * i / shadowSize;
            g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255 * appearScale)));
            g2d.fill(new RoundRectangle2D.Float(
                x + i/2, y + i/2, 
                size - i, size - i, 
                CORNER_RADIUS, CORNER_RADIUS
            ));
        }

        // Draw bubble with gradient
        GradientPaint gradient = new GradientPaint(
            x, y, 
            isExpanded ? HOVER_COLOR : BUBBLE_COLOR,
            x, y + size, 
            isExpanded ? HOVER_GRADIENT_END : BUBBLE_GRADIENT_END
        );
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(
            x, y, size, size, 
            CORNER_RADIUS, CORNER_RADIUS
        ));
        
        // Draw highlight
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.fillArc(x, y, size, size/2, 0, 180);

        // Draw notification indicator if needed
        if (hasNotification && !isExpanded) {
            int dotSize = (int)(16 * scaleFactor);
            float pulseValue = (float)Math.sin(notificationPulse) * 0.5f + 1.0f;
            int pulseSize = (int)(dotSize * pulseValue);
            
            g2d.setColor(new Color(255, 77, 79));
            g2d.fillOval(
                x + size - dotSize, 
                y, 
                dotSize, 
                dotSize
            );
            
            // Draw pulse effect
            g2d.setColor(new Color(255, 77, 79, 
                (int)(128 * (1.0f - (pulseValue - 1.0f)))));
            g2d.fillOval(
                x + size - pulseSize/2 - dotSize/2, 
                y - pulseSize/2 + dotSize/2, 
                pulseSize, 
                pulseSize
            );
        }

        // Draw ripple effects
        for (RippleEffect ripple : ripples) {
            int rippleSize = (int)(size * ripple.scale);
            g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, ripple.opacity));
            g2d.setColor(Color.WHITE);
            g2d.fillOval(
                x + ripple.position.x - rippleSize/2,
                y + ripple.position.y - rippleSize/2,
                rippleSize,
                rippleSize
            );
            g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));
        }

        // Draw animated chat icon
        drawAnimatedChatIcon(g2d, x, y, size);
    }
    
    private void drawAnimatedChatIcon(Graphics2D g2d, int x, int y, int size) {
        int iconSize = (int)(size * 0.6f);
        int iconX = x + (size - iconSize) / 2;
        int iconY = y + (size - iconSize) / 2 + (int)iconBounceOffset;
        
        g2d.setColor(Color.WHITE);
        
        // Draw different icon states based on animation state
        switch (iconAnimState) {
            case 0:
                // Chat bubble with dots
                drawChatBubble(g2d, iconX, iconY, iconSize);
                drawAnimatedDots(g2d, iconX, iconY, iconSize, iconAnimProgress);
                break;
            case 1:
                // Chat bubble with message
                drawChatBubble(g2d, iconX, iconY, iconSize);
                drawAnimatedMessage(g2d, iconX, iconY, iconSize, iconAnimProgress);
                break;
            case 2:
                // Quick pulse animation
                float pulseScale = 0.8f + 0.2f * (float)Math.sin(iconAnimProgress * Math.PI * 2);
                drawChatBubble(g2d, 
                    (int)(iconX - iconSize * (pulseScale - 1) / 2), 
                    (int)(iconY - iconSize * (pulseScale - 1) / 2), 
                    (int)(iconSize * pulseScale));
                break;
        }
    }
    
    private void drawChatBubble(Graphics2D g2d, int x, int y, int size) {
        // Draw main bubble
        int bubbleSize = size;
        int cornerRadius = bubbleSize / 4;
        
        // Create speech bubble shape with tail
        Path2D.Float bubblePath = new Path2D.Float();
        bubblePath.moveTo(x + cornerRadius, y);
        bubblePath.lineTo(x + bubbleSize - cornerRadius, y);
        bubblePath.quadTo(x + bubbleSize, y, x + bubbleSize, y + cornerRadius);
        bubblePath.lineTo(x + bubbleSize, y + bubbleSize - cornerRadius - bubbleSize/5);
        bubblePath.quadTo(x + bubbleSize, y + bubbleSize - bubbleSize/5, 
                         x + bubbleSize - cornerRadius, y + bubbleSize - bubbleSize/5);
        
        // Add speech bubble tail
        bubblePath.lineTo(x + bubbleSize/2 + bubbleSize/6, y + bubbleSize - bubbleSize/5);
        bubblePath.lineTo(x + bubbleSize/2, y + bubbleSize);
        bubblePath.lineTo(x + bubbleSize/2 - bubbleSize/6, y + bubbleSize - bubbleSize/5);
        
        bubblePath.lineTo(x + cornerRadius, y + bubbleSize - bubbleSize/5);
        bubblePath.quadTo(x, y + bubbleSize - bubbleSize/5, x, y + cornerRadius);
        bubblePath.lineTo(x, y);
        bubblePath.quadTo(x, y, x + cornerRadius, y);
        bubblePath.closePath();
        
        // Draw bubble with subtle gradient
        GradientPaint bubbleGradient = new GradientPaint(
            x, y, new Color(255, 255, 255, 255),
            x, y + bubbleSize, new Color(255, 255, 255, 230)
        );
        g2d.setPaint(bubbleGradient);
        g2d.fill(bubblePath);
    }
    
    private void drawAnimatedDots(Graphics2D g2d, int x, int y, int size, float progress) {
        int dotSize = size / 8;
        int dotSpacing = dotSize * 2;
        int totalWidth = dotSize * 3 + dotSpacing * 2;
        
        int startX = x + (size - totalWidth) / 2;
        int dotY = y + size/2 - dotSize/2;
        
        // Save original composite
        Composite originalComposite = g2d.getComposite();
        
        for (int i = 0; i < 3; i++) {
            float dotProgress = (progress + i * 0.3f) % 1.0f;
            float dotScale = (float)(0.5f + 0.5f * Math.sin(dotProgress * Math.PI));
            
            // Calculate color with sine-based opacity (ensure opacity is between 0 and 1)
            float opacity = 0.3f + 0.7f * dotScale;
            opacity = Math.max(0.0f, Math.min(1.0f, opacity));
            
            g2d.setColor(new Color(30, 30, 30, (int)(opacity * 255)));
            
            // Calculate dot position with scale
            int dotX = startX + i * (dotSize + dotSpacing);
            int scaledSize = (int)(dotSize * (0.7f + 0.5f * dotScale));
            int adjustedX = dotX + (dotSize - scaledSize) / 2;
            int adjustedY = dotY + (dotSize - scaledSize) / 2;
            
            g2d.fillOval(adjustedX, adjustedY, scaledSize, scaledSize);
        }
        
        // Restore original composite
        g2d.setComposite(originalComposite);
    }
    
    private void drawAnimatedMessage(Graphics2D g2d, int x, int y, int size, float progress) {
        int lineWidth = (int)(size * 0.6f);
        int lineHeight = size / 12;
        int lineSpacing = lineHeight;
        int startX = x + (size - lineWidth) / 2;
        int startY = y + size/3;
        
        // Save original composite
        Composite originalComposite = g2d.getComposite();
        
        // Calculate how many lines to draw based on progress
        int lineCount = Math.min(3, (int)(progress * 3) + 1);
        
        for (int i = 0; i < lineCount; i++) {
            float lineProgress = Math.min(1.0f, (progress * 3) - i);
            int actualWidth = (int)(lineWidth * lineProgress);
            
            // Alternate line lengths
            if (i == 1) actualWidth = (int)(lineWidth * 0.7f * lineProgress);
            
            g2d.setColor(new Color(30, 30, 30));
            
            // Draw message line with rounded corners
            RoundRectangle2D.Float line = new RoundRectangle2D.Float(
                startX, startY + i * (lineHeight + lineSpacing),
                actualWidth, lineHeight, lineHeight / 2, lineHeight / 2
            );
            g2d.fill(line);
        }
        
        // Restore original composite
        g2d.setComposite(originalComposite);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (pulseTimer != null) {
            pulseTimer.stop();
        }
        if (appearTimer != null) {
            appearTimer.stop();
        }
        if (dialogAnimTimer != null) {
            dialogAnimTimer.stop();
        }
        if (notificationTimer != null) {
            notificationTimer.stop();
        }
        if (iconAnimTimer != null) {
            iconAnimTimer.stop();
        }
        if (chatDialog != null) {
            chatDialog.dispose();
        }
    }
    
    // Helper class for ripple effect
    private static class RippleEffect {
        Point position;
        float scale = 0.0f;
        float opacity = 1.0f;
        
        RippleEffect(Point position) {
            this.position = position;
        }
    }
} 