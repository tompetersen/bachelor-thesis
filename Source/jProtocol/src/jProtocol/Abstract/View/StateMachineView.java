package jProtocol.Abstract.View;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StateMachineView {

	private JPanel _view;
	
	public StateMachineView(String title) {
		_view = new JPanel();
		JLabel label = new JLabel(title);
		_view.add(label);
	}

	public JComponent getView() {
		return _view;
	}
	
}
