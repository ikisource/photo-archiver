package service;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 *
 * @author Olivier MATHE
 */
public class ThumbService {

	private static Logger logger = Logger.getLogger(ThumbService.class);

	public static BufferedImage resizeImage(final File file, final File thumb, int targetWidth, int targetHeight) throws IOException {

		BufferedImage srcImage = ImageIO.read(file);
		double determineImageScale = determineImageScale(srcImage.getWidth(), srcImage.getHeight(), targetWidth, targetHeight);
		BufferedImage dstImage = scaleImage(srcImage, determineImageScale);
		ImageIO.write(dstImage, "jpg", thumb);

		return dstImage;
	}

	private static BufferedImage scaleImage(final BufferedImage sourceImage, double scaledWidth) {

		Image scaledImage = sourceImage.getScaledInstance((int) (sourceImage.getWidth() * scaledWidth), (int) (sourceImage.getHeight() * scaledWidth), Image.SCALE_SMOOTH);
		BufferedImage bufferedImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.createGraphics();
		g.drawImage(scaledImage, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

	private static double determineImageScale(int sourceWidth, int sourceHeight, int targetWidth, int targetHeight) {

		double scalex = (double) targetWidth / sourceWidth;
		double scaley = (double) targetHeight / sourceHeight;
		return Math.min(scalex, scaley);
	}

}
