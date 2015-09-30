package jProtocol.helper;

import java.awt.Color;

/**
 * A class containing static methods round about colors.
 * 
 * @author Tom Petersen
 */
public class ColorHelper {

	private static final int BRIGHTER_SUMMAND = 10;
	private static final int COLOR_MAX_VALUE = 255;
	private static final int COLOR_MIN_VALUE = 0;

	/**
	 * Returns a text color with high contrast to the color parameter.
	 * 
	 * @param color
	 *            the color to create high contrast text color for
	 * @return a high contrast text color
	 */
	public static Color highContrastTextColorForColor(Color color) {
		return shouldReturnDarkColor(color) ? Color.BLACK : Color.WHITE;
	}

	/**
	 * Returns black or white depending on the higher contrast to color.
	 * 
	 * @param color
	 *            the color to create high contrast color for
	 * @return Color.BLACK OR Color.WHITE
	 */
	public static Color highContrastColorForColor(Color color) {
		return shouldReturnDarkColor(color) ? Color.BLACK : Color.WHITE;
	}

	private static boolean shouldReturnDarkColor(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();

		// more details here: http://de.wikipedia.org/wiki/YIQ-Farbmodell
		// and here: http://24ways.org/2010/calculating-color-contrast/
		int yiq = (r * 299 + g * 587 + b * 114) / 1000;
		return (yiq >= 128);
	}

	/**
	 * Returns a slightly brighter Color than the color parameter. Can be used
	 * for mouse over effects.
	 * 
	 * @param color
	 *            the color to create a brighter color for
	 * @return a slightly brighter color
	 */
	public static Color slightlyBrighterColorForColor(Color color) {
		int r = Math.min(color.getRed() + BRIGHTER_SUMMAND, COLOR_MAX_VALUE);
		int g = Math.min(color.getGreen() + BRIGHTER_SUMMAND, COLOR_MAX_VALUE);
		int b = Math.min(color.getBlue() + BRIGHTER_SUMMAND, COLOR_MAX_VALUE);

		return new Color(r, g, b);
	}

	/**
	 * Returns a slightly brighter Color than the color parameter. Can be used
	 * for mouse over effects.
	 * 
	 * @param color
	 *            the color to create a brighter color for
	 * @return a slightly brighter color
	 */
	public static Color slightlyDarkerColorForColor(Color color) {
		int r = Math.max(color.getRed() - BRIGHTER_SUMMAND, COLOR_MIN_VALUE);
		int g = Math.max(color.getGreen() - BRIGHTER_SUMMAND, COLOR_MIN_VALUE);
		int b = Math.max(color.getBlue() - BRIGHTER_SUMMAND, COLOR_MIN_VALUE);

		return new Color(r, g, b);
	}
}
