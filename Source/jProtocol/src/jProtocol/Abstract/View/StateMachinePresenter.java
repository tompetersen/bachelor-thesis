package jProtocol.Abstract.View;

import javax.swing.JComponent;

public class StateMachinePresenter {

	private StateMachineView _view;
	
	public StateMachinePresenter(String title, JComponent stateMachineView) {
		_view = new StateMachineView(title, stateMachineView);
		
	}
	
	public JComponent getView() {
		return _view.getView();
	}
}
