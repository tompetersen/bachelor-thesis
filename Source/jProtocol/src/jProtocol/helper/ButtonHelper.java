package jProtocol.helper;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonHelper {

	/**
	 * Convenience method for creating a button object with a title, an icon and an ActionListener.
	 * 
	 * @param title the title
	 * @param icon the icon
	 * @param listener the action listener
	 * 
	 * @return the button object
	 */
	public static JButton createImageButton(String title, ImageIcon icon, ActionListener listener) {
		JButton result = new JButton(title, icon);
		
		result.setHorizontalTextPosition(JButton.RIGHT);
		result.setVerticalTextPosition(JButton.CENTER);
//		result.setAlignmentX(Component.LEFT_ALIGNMENT);
		result.addActionListener(listener);
		
		return result;
	}
}
