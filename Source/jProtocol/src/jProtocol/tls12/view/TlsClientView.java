package jProtocol.tls12.view;

import jProtocol.tls12.model.states.TlsStateMachine;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TlsClientView {

	private TlsStateMachine _client;
	private JPanel _view;
	private JTextArea _label;
	
	public TlsClientView(TlsStateMachine client) {
		_view = new JPanel();
		_client = client;
		
		_label = new JTextArea(5,5);
		_view.add(_label);
	}
	
	public void updateView() {
		_label.setText(_client.getViewData());
	}
	
	public JComponent getView() {
		return _view;
	}

}
