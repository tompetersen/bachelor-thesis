package jProtocol.helper;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonHelper {

	public static JButton createImageButton(String title, ImageIcon icon, ActionListener listener) {
		JButton result = new JButton(title, icon);
		
		result.setHorizontalTextPosition(JButton.RIGHT);
		result.setVerticalTextPosition(JButton.CENTER);
//		result.setAlignmentX(Component.LEFT_ALIGNMENT);
		result.addActionListener(listener);
		
		return result;
	}
}
