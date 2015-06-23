package jProtocol.Abstract.View;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class ProtocolDataUnitView {

	protected JPanel _view;
	
	public ProtocolDataUnitView() {
		_view = new JPanel();
	}
	
	public JComponent getView() {
		return _view;
	}
	
	
	
}
