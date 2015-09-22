package jProtocol.Abstract.View;

import javax.swing.JComponent;

public class StateMachineView {

	private JComponent _view;
	
	/**
	 * Creates a state machine view.
	 * 
	 * @param stateMachineView the view 
	 */
	public StateMachineView(JComponent stateMachineView) {		
		_view = stateMachineView;
	}

	/**
	 * Returns the state machine view.
	 * 
	 * @return the view
	 */
	public JComponent getView() {
		return _view;
	}
	
}
