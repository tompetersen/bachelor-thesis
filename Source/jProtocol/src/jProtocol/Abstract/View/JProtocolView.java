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
	
	private JPanel _clientStateMachineView;
	private JPanel _serverStateMachineView;
	private JPanel _protocolDataUnitView;
	
	public JProtocolView() {
		_frame = new JFrame("jProtocol");
		
		_view = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		_view.setLayout(layout);
		
		Insets insets = new Insets(5, 5, 5, 5);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		
		_clientStateMachineView = new JPanel();
		_clientStateMachineView.setBorder(border);
		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(0, 0, 1, insets);
		_view.add(_clientStateMachineView, constraints);
		
		_serverStateMachineView = new JPanel();
		_serverStateMachineView.setBorder(border);
		constraints = GridBagConstraintsHelper.createNormalConstraints(2, 0, 1, insets);
		_view.add(_serverStateMachineView, constraints);
		
		_protocolDataUnitView = new JPanel();
		_protocolDataUnitView.setBorder(border);
		constraints = GridBagConstraintsHelper.createNormalConstraints(1, 0, 1, insets);
		_view.add(_protocolDataUnitView, constraints);
		                      
		JPanel p = new JPanel();
		p.setBorder(border);
		JLabel label = new JLabel("Whatever...");
		p.add(label);
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 1, 3, insets);
		_view.add(p, constraints);
		
		_frame.add(_view);
	}
	
	public void show() {
		_frame.setSize(new Dimension(300, 300));
		_frame.setVisible(true);
	}
	
	public void setClientStateMachineView(JComponent clientView) {
		_clientStateMachineView.add(clientView);
	}
	
	public void setServerStateMachineView(JComponent serverView) {
		_serverStateMachineView.add(serverView);
	}
	
	public void setProtocolDataUnitView(JComponent pduView) {
		_protocolDataUnitView.add(pduView);
	}

}
