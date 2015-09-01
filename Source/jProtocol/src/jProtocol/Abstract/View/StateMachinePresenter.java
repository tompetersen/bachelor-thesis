package jProtocol.Abstract.View;

import javax.swing.JComponent;

public class StateMachinePresenter {

	private StateMachineView _view;
	
	public StateMachinePresenter(String title) {
		_view = new StateMachineView(title);
		
	}
	
	public JComponent getView() {
		return _view.getView();
	}

}
