package main.java.org.anynomous.utils;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IconLoader {
    // Cache icons to avoid reloading
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    
    private static final String LOCAL_ICONS_PATH = "/icons/";
    
    /**
     * Load an icon from local resources (works with SVG, ICO, PNG, etc.)
     */
    public static ImageIcon loadIcon(String iconName, int width, int height, Color colorFilter) {
        String cacheKey = iconName + width + height + (colorFilter != null ? colorFilter.getRGB() : 0);
        if (iconCache.containsKey(cacheKey)) {
            return iconCache.get(cacheKey);
        }
        
        try {
            String localPath = LOCAL_ICONS_PATH + iconName;
            InputStream resourceStream = IconLoader.class.getResourceAsStream(localPath);
            if (resourceStream != null) {
                if (iconName.toLowerCase().endsWith(".svg")) {
                    TranscoderInput input = new TranscoderInput(resourceStream);
                    ImageIcon icon = loadSvgFromUrl(input, width, height);
                    if (colorFilter != null) {
                        icon = applyColorFilter(icon, colorFilter);
                    }
                    iconCache.put(cacheKey, icon);
                    return icon;
                } else {
                    BufferedImage originalImage = ImageIO.read(resourceStream);
                    if (originalImage != null) {
                        BufferedImage resizedImage = resize(originalImage, width, height);
                        ImageIcon icon = new ImageIcon(resizedImage);
                        if (colorFilter != null) {
                            icon = applyColorFilter(icon, colorFilter);
                        }
                        iconCache.put(cacheKey, icon);
                        return icon;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load local icon " + iconName + ": " + e.getMessage());
        }
        
        // Return a placeholder icon if loading fails
        return createPlaceholderIcon(width, height, colorFilter);
    }
    
    /**
     * Load an SVG from a TranscoderInput
     */
    private static ImageIcon loadSvgFromUrl(TranscoderInput input, int width, int height) throws TranscoderException {
        // Create an image to store the SVG in
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Create a transcoder that will be used to convert the SVG to an image
        ImageTranscoder transcoder = new PNGTranscoder() {
            @Override
            public BufferedImage createImage(int w, int h) {
                return image;
            }
        };
        
        // Set the transcoding hints
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);
        
        // Convert the SVG to an image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outputStream);
        transcoder.transcode(input, output);
        
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    /**
     * Apply a color filter to an ImageIcon
     */
    private static ImageIcon applyColorFilter(ImageIcon icon, Color color) {
        BufferedImage image = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(icon.getImage(), 0, 0, null);
        
        // Apply color filter
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                if (alpha > 0) {
                    image.setRGB(x, y, (alpha << 24) | (color.getRGB() & 0x00FFFFFF));
                }
            }
        }
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Resize an image to the specified dimensions
     */
    private static BufferedImage resize(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }
    
    /**
     * Create a placeholder icon when loading fails
     */
    private static ImageIcon createPlaceholderIcon(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Set background
        g2d.setColor(new Color(200, 200, 200, 50));
        g2d.fillRect(0, 0, width, height);
        
        // Draw placeholder shape
        g2d.setColor(color != null ? color : Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(2, 2, width - 4, height - 4);
        
        // Draw X
        g2d.drawLine(4, 4, width - 4, height - 4);
        g2d.drawLine(4, height - 4, width - 4, 4);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
}