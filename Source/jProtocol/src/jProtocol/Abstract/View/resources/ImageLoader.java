package jProtocol.Abstract.View.resources;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage getInfoIcon(int width, int height) {
		return loadImageForFileName("info1.png", width, height);
	}
	
	public static BufferedImage getSendIcon(int width, int height) {
		return loadImageForFileName("send3.png", width, height);
	}
	
	public static BufferedImage getCloseIcon(int width, int height) {
		return loadImageForFileName("cancel30.png", width, height);
	}
	
	public static BufferedImage getNextIcon(int width, int height) {
		return loadImageForFileName("angle2.png", width, height);
	}
	
	public static BufferedImage getNextNextIcon(int width, int height) {
		return loadImageForFileName("double30.png", width, height);
	}
	
	public static BufferedImage getConnectIcon(int width, int height) {
		return loadImageForFileName("screen156.png", width, height);
	}
	
	public static BufferedImage getEditIcon(int width, int height) {
		return loadImageForFileName("edit45.png", width, height);
	}
	
	public static BufferedImage getHandshakeIcon(int width, int height) {
		return loadImageForFileName("agreement2.png", width, height);
	}
	
	public static BufferedImage getEstablishedIcon(int width, int height) {
		return loadImageForFileName("lock91.png", width, height);
	}
	
	public static BufferedImage getErrorIcon(int width, int height) {
		return loadImageForFileName("triangle38.png", width, height);
	}
	
	public static BufferedImage getClientIcon(int width, int height) {
		return loadImageForFileName("flatscreen.png", width, height);
	}
	
	public static BufferedImage getServerIcon(int width, int height) {
		return loadImageForFileName("servers.png", width, height);
	}
	
	public static BufferedImage getMessageIcon(int width, int height) {
		return loadImageForFileName("email19.png", width, height);
	}
	
	public static BufferedImage getApplicationIcon(int width, int height) {
		return loadImageForFileName("Icon_32.png", width, height);
	}
	
	public static List<Image> getIconFileList(){
		ArrayList<Image> result = new ArrayList<>();
		
		result.add(loadOriginalImageForFileName("Icon_128.png"));
		result.add(loadOriginalImageForFileName("Icon_64.png"));
		result.add(loadOriginalImageForFileName("Icon_32.png"));
		result.add(loadOriginalImageForFileName("Icon_16.png"));
		
		return result;
	}
	
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
				return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			}
		}
		else {
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
	}
	
	private static BufferedImage loadOriginalImageForFileName(String filepath) {
		InputStream in = ImageLoader.class.getResourceAsStream(filepath);

		if (in != null) {
			try {
				return ImageIO.read(in);
			}
			catch (IOException e) {
				return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
			}
		}
		else {
			return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
		}
	}
}
