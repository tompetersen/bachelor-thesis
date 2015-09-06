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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class JProtocolView {

	private JFrame _frame;
	private JPanel _view;
	
	public JProtocolView(JComponent clientView, JComponent serverView, JComponent pduView) {
		_frame = new JFrame("jProtocol");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_view = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		_view.setLayout(layout);
		
		Insets insets = new Insets(5, 5, 5, 5);
		Border border = BorderFactory.createLineBorder(Color.BLACK);

		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(0, 0, 1, insets);
		clientView.setBorder(BorderFactory.createLineBorder(Color.RED));
		_view.add(clientView, constraints);

		constraints = GridBagConstraintsHelper.createNormalConstraints(2, 0, 1, insets);
		serverView.setBorder(BorderFactory.createLineBorder(Color.RED));
		_view.add(serverView, constraints);

		constraints = GridBagConstraintsHelper.createNormalConstraints(1, 0, 1, insets);
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.weightx = 0;
		pduView.setBorder(BorderFactory.createLineBorder(Color.RED));
		_view.add(pduView, constraints);
		                      
		JPanel p = new JPanel();
		p.setBorder(border);
		JLabel label = new JLabel("Whatever...");
		p.add(label);
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 1, 3, insets);
		_view.add(p, constraints);
		
		_frame.add(_view);
	}
	
	public void show() {
		_frame.setSize(new Dimension(800, 600));
		_frame.setVisible(true);
	}

}
