package jProtocol.Abstract.View;

import javax.swing.JComponent;

public class StateMachineView {

	private JComponent _view;
	
	public StateMachineView(String title, JComponent stateMachineView) {		
		_view = stateMachineView;
	}

	public JComponent getView() {
		return _view;
	}
	
}
