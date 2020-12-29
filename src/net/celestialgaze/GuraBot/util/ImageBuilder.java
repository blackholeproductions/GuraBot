package net.celestialgaze.GuraBot.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageBuilder {
	BufferedImage image;
	public ImageBuilder(BufferedImage image) {
		this.image = image;
	}
	
	public ImageBuilder setBackgroundColor(int color) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setRGB(x, y, color);
			}
		}
		return this;
	}
	public ImageBuilder drawLine(int x1, int y1, int x2, int y2) {
		return drawLine(x1, y1, x2, y2, 0x000000);
	}
	public ImageBuilder drawLine(int x1, int y1, int x2, int y2, int color) {
		double angle = Math.atan2(y2-y1, x2-x1) * 180/Math.PI,
		       distance = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
		for (int i = 0; i < distance; i++) {
			int cPosX = Math.toIntExact(Math.round(x1 + Math.cos(angle) * i));
			int cPosY = Math.toIntExact(Math.round(y1 + Math.sin(angle) * i));
			image.setRGB(cPosX, cPosY, color);
		}
		
		return this;
	}
	
	public ByteArrayOutputStream build() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		return baos;
	}
}
