package jProtocol.Abstract.View;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProtocolDataUnitView {

	protected JPanel _view;
	
	public ProtocolDataUnitView() {
		_view = new JPanel();
		
		JLabel label = new JLabel("PDUs");
		_view.add(label);
	}
	
	public JComponent getView() {
		return _view;
	}
}
