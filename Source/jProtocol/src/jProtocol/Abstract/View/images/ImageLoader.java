package jProtocol.Abstract.View.images;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageLoader {

	/**
	 * Loads images from the current package  
	 * 
	 * @param filepath the filename and path of the image
	 * 
	 * @return the image content or an error message, if the image could not be read
	 */
	private static BufferedImage loadImageForFileName(String filepath, int newWidth, int newHeight) {
		InputStream in = ImageLoader.class.getResourceAsStream(filepath);

		if (in != null) {
			try {
				BufferedImage img = ImageIO.read(in);

				BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = resizedImage.createGraphics();
				
				g.setComposite(AlphaComposite.Src);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				
				g.drawImage(img, 0, 0, newWidth, newHeight, null);
				g.dispose();
				
				return resizedImage;
			}
			catch (IOException e) {
				return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
			}
		}
		else {
			return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
		}
	}

	public static BufferedImage getInfoIcon(int width, int height) {
		return loadImageForFileName("info1.png", width, height);
	}
}
