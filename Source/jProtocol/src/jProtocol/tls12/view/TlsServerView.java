package jProtocol.tls12.view;

import jProtocol.tls12.model.states.TlsStateMachine;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TlsServerView {

	private TlsStateMachine _server;
	private TlsStateMachineTreeView _treeView;
	
	public TlsServerView(TlsStateMachine server) {
		_server = server;
		_treeView = new TlsStateMachineTreeView(server, "Server");
	}
	
	public void updateView() {
		_treeView.updateView();
	}
	
	public JComponent getView() {
		JScrollPane pane = new JScrollPane( _treeView.getView());
		return pane;
	}
	
}
