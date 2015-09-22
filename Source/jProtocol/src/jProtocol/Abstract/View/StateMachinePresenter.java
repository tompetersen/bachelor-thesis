package jProtocol.Abstract.View;

import javax.swing.JComponent;

public class StateMachinePresenter {

	private StateMachineView _view;
	
	/**
	 * Creates a state machine presenter.
	 * 
	 * @param stateMachineView the view for the presenter
	 */
	public StateMachinePresenter(JComponent stateMachineView) {
		_view = new StateMachineView(stateMachineView);
	}
	
	/**
	 * Returns the state machine view.
	 * 
	 * @return the view
	 */
	public JComponent getView() {
		return _view.getView();
	}
}
