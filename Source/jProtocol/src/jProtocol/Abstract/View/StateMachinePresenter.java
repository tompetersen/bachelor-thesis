package jProtocol.Abstract.View;

import javax.swing.JComponent;

public class StateMachinePresenter {

	private StateMachineView _view;
	
	public StateMachinePresenter(JComponent stateMachineView) {
		_view = new StateMachineView(stateMachineView);
	}
	
	public JComponent getView() {
		return _view.getView();
	}
}
