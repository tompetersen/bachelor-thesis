package jProtocol.tls12;

import jProtocol.Abstract.JProtocolProtocolBuilder;
import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.View.keyvaluetree.KeyValueTree;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.states.TlsClientStateMachine;
import jProtocol.tls12.model.states.TlsServerStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachineEvent;
import jProtocol.tls12.model.states.TlsStateMachineEvent.TlsStateMachineEventType;
import jProtocol.tls12.view.TlsClientView;
import jProtocol.tls12.view.TlsServerView;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;

public class Tls12Startup implements Observer, JProtocolViewProvider<TlsCiphertext> {
	
	private TlsStateMachine _client;
	private TlsStateMachine _server;
	
	private TlsClientView _clientView;
	private TlsServerView _serverView;

	public Tls12Startup() {
		_client = new TlsClientStateMachine();
		_client.addObserver(this);
		_server = new TlsServerStateMachine();
		_server.addObserver(this);
		
		_clientView = new TlsClientView(_client);
		_serverView = new TlsServerView(_server);
		
		JProtocolProtocolBuilder<TlsCiphertext> builder = new JProtocolProtocolBuilder<>(_client, _server, this);
	}

	public static void main(String[] args) {
		new Tls12Startup();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof TlsStateMachineEvent) {
			TlsStateMachineEventType type = ((TlsStateMachineEvent)arg1).getEventType();
			
			if (arg0 == _client && type == TlsStateMachineEventType.connection_established) {
//				_client.sendData(new TlsApplicationData("3,14159265".getBytes(StandardCharsets.US_ASCII)));
			}
			if (arg0 == _server && type == TlsStateMachineEventType.connection_established) {
//				_server.sendData(new TlsApplicationData("23.42.1337".getBytes(StandardCharsets.US_ASCII)));
			}
		}
	}

	@Override
	public JComponent getDetailedViewForProtocolDataUnit(TlsCiphertext pdu) {
		KeyValueTree tree = new KeyValueTree("TlsCiphertext", false);
		tree.setKeyValueObjectList(pdu.getViewData());
		tree.expandAll();
		
		return tree.getView();
	}

	@Override
	public JComponent getViewForClientStateMachine() {
		return _clientView.getView();
	}

	@Override
	public JComponent getViewForServerStateMachine() {
		return _serverView.getView();
	}

	@Override
	public void updateServerView() {
		_serverView.updateView();
	}

	@Override
	public void updateClientView() {
		_clientView.updateView();
	}
}
