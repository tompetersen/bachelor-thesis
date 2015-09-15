package jProtocol.Abstract.View;

import jProtocol.helper.GridBagConstraintsHelper;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class JProtocolView {

	private JFrame _frame;
	private JPanel _view;
	
	public JProtocolView(JComponent clientView, JComponent serverView, JComponent pduView, JComponent channelView) {
		_frame = new JFrame("jProtocol");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setBackground(Color.WHITE);
		
		_view = new JPanel(new GridBagLayout());
		_view.setBackground(Color.WHITE);
		
		Insets insets = new Insets(5, 5, 5, 5);
		Border border = BorderFactory.createLineBorder(Color.BLACK);

		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(0, 0, 1, insets);
		constraints.weighty = 1;
//		clientView.setBorder(BorderFactory.createLineBorder(Color.RED));
		_view.add(clientView, constraints);

		constraints = GridBagConstraintsHelper.createNormalConstraints(2, 0, 1, insets);
		constraints.weighty = 1;
//		serverView.setBorder(BorderFactory.createLineBorder(Color.RED));
		_view.add(serverView, constraints);

		constraints = GridBagConstraintsHelper.createNormalConstraints(1, 0, 1, insets);
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.weightx = 0;
		constraints.weighty = 1;
//		pduView.setBorder(BorderFactory.createLineBorder(Color.RED));
		_view.add(pduView, constraints);
		                      
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 1, 3, insets);
		constraints.weighty = 0;
		_view.add(channelView, constraints);
		
		_frame.add(_view);
	}
	
	public void show() {
		_frame.setSize(new Dimension(800, 600));
		_frame.setVisible(true);
	}

}
