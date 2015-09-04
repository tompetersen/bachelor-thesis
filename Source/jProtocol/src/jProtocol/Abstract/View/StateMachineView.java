package jProtocol.Abstract.View;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StateMachineView {

	private JPanel _view;
	
	public StateMachineView(String title, JComponent stateMachineView) {
		_view = new JPanel();
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel(title);
		_view.add(label);
		
		_view.add(stateMachineView);
	}

	public JComponent getView() {
		return _view;
	}
	
}
