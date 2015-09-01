package jProtocol.Abstract.View;

import javax.swing.JComponent;

public class ProtocolDataUnitPresenter {

	private ProtocolDataUnitView _view;
	
	public ProtocolDataUnitPresenter() {
		_view = new ProtocolDataUnitView();
	}
	
	public JComponent getView() {
		return _view.getView();
	}
	
}
