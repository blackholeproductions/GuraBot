package net.celestialgaze.GuraBot.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static ByteArrayOutputStream toBaos(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		return baos;
	}
	public static int average(BufferedImage image) {
		long big = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				big += image.getRGB(x, y);
			}
		}
		return Math.toIntExact(big / (image.getWidth()*image.getHeight()));
	}
	
	// Copied from Color class but made configurable
	public static Color brighter(Color color, double amount) {
		amount = 1 - amount;
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();

        
        int i = (int)(1.0/(1.0-amount));
        if ( r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return new Color(Math.min((int)(r/amount), 255),
                         Math.min((int)(g/amount), 255),
                         Math.min((int)(b/amount), 255),
                         alpha);
    }
	
	public static BufferedImage circle(BufferedImage square) {
		Map<Key, Object> rhs = new HashMap<Key, Object>();
		rhs.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rhs.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		square.createGraphics().setRenderingHints(rhs);
		BufferedImage circle = new BufferedImage(square.getWidth(), square.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D circleG2 = circle.createGraphics();
		circleG2.setRenderingHints(rhs);
		circleG2.setClip(new Ellipse2D.Float(0, 0, square.getWidth(), square.getHeight()));
		circleG2.drawImage(square, 0, 0, square.getWidth(), square.getHeight(), null);
		return circle;
	}
	public static void drawString(String text, int x, int y, Graphics2D g) {
		drawString(text, x, y, g, Alignment.LEFT);
	}
	public static void drawString(String text, int x, int y, Graphics2D g, Alignment align) {
		drawString(text, x, y, g, align, null, false);
	}
	public static void drawString(String text, int x, int y, Graphics2D g, Alignment align, Color color) {
		drawString(text, x, y, g, align, color, true);
	}
	public static void drawString(String text, int x, int y, Graphics2D g, Alignment align, Color color, boolean hasColor) {
		if (hasColor) g.setColor(color);
		switch (align) {
		case LEFT:
			g.drawString(text, x, y);
			break;
		case MIDDLE:
			g.drawString(text, x-(g.getFontMetrics().stringWidth(text)/2), y);
			break;
		case RIGHT:
			g.drawString(text, x-g.getFontMetrics().stringWidth(text), y);
			break;
		default:
			break;
		
		}
	}

	public enum Alignment {
		LEFT, MIDDLE, RIGHT;
	}
}
