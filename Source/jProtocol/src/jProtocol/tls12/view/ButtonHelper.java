package jProtocol.tls12.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonHelper {

	public static JButton createImageButton(String title, ImageIcon icon, ActionListener listener) {
		JButton result = new JButton(title, icon);
		
		result.setHorizontalTextPosition(JButton.RIGHT);
		result.setVerticalTextPosition(JButton.CENTER);
		result.setAlignmentX(Component.LEFT_ALIGNMENT);
		result.addActionListener(listener);
//		result.setMinimumSize(new Dimension(200, 40));
//		result.setPreferredSize(new Dimension(100, 30));
		
		return result;
	}
}
