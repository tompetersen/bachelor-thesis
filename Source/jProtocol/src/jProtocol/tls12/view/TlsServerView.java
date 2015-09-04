package jProtocol.tls12.view;

import jProtocol.tls12.model.states.TlsStateMachine;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TlsServerView {

	private TlsStateMachine _server;
	private JPanel _view;
	private JTextArea _label;
	
	public TlsServerView(TlsStateMachine server) {
		_view = new JPanel();
		_server = server;
		
		_label = new JTextArea(5,5);
		_view.add(_label);
	}
	
	public void updateView() {
		_label.setText(_server.getViewData());
	}
	
	public JComponent getView() {
		return _view;
	}
	
}
