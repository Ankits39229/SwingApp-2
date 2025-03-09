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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class IconLoader {
    // Cache icons to avoid reloading
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    
    /**
     * Load an icon from a URL (works with SVG, ICO, PNG, etc.)
     */
    public static ImageIcon loadIcon(String url, int width, int height, Color colorFilter) {
        String cacheKey = url + width + height + (colorFilter != null ? colorFilter.getRGB() : 0);
        if (iconCache.containsKey(cacheKey)) {
            return iconCache.get(cacheKey);
        }
        
        try {
            if (url.toLowerCase().endsWith(".svg")) {
                ImageIcon icon = loadSvgFromUrl(url, width, height);
                if (colorFilter != null) {
                    icon = applyColorFilter(icon, colorFilter);
                }
                iconCache.put(cacheKey, icon);
                return icon;
            } else {
                // For other image types (ICO, PNG, etc.)
                URL imageUrl = new URL(url);
                BufferedImage originalImage = ImageIO.read(imageUrl);
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
        } catch (Exception e) {
            System.err.println("Error loading icon from " + url + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return a placeholder icon if loading fails
        return createPlaceholderIcon(width, height, colorFilter);
    }
    
    /**
     * Load an SVG file from a URL and convert to ImageIcon
     */
    private static ImageIcon loadSvgFromUrl(String url, int width, int height) throws IOException, TranscoderException {
        URL svgUrl = new URL(url);
        TranscoderInput input = new TranscoderInput(svgUrl.openStream());
        
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
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        
        // Apply color filter
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                if (alpha > 0) {
                    // Keep alpha but replace color
                    int newRgb = (alpha << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
                    image.setRGB(x, y, newRgb);
                }
            }
        }
        
        return new ImageIcon(image);
    }
    
    /**
     * Resize a BufferedImage to the specified dimensions
     */
    private static BufferedImage resize(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, original.getType());
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }
    
    /**
     * Create a placeholder icon when loading fails
     */
    private static ImageIcon createPlaceholderIcon(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (color == null) {
            color = Color.GRAY;
        }
        
        g2d.setColor(color);
        g2d.fillOval(2, 2, width - 4, height - 4);
        g2d.dispose();
        
        return new ImageIcon(image);
    }
}