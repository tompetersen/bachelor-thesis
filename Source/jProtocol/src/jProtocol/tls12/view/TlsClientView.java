package jProtocol.tls12.view;

import jProtocol.tls12.model.states.TlsStateMachine;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class TlsClientView {

	private TlsStateMachine _client;
	private TlsStateMachineTreeView _treeView;
	
	public TlsClientView(TlsStateMachine client) {
		_client = client;
		_treeView = new TlsStateMachineTreeView(client, "Client");
	}
	
	public void updateView() {
		_treeView.updateView();
	}
	
	public JComponent getView() {
		JScrollPane pane = new JScrollPane( _treeView.getView());
		return pane;
	}

}
