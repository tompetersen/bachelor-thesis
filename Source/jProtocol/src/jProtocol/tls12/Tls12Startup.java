package jProtocol.tls12;

import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateMachineEvent;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateMachineEventType;
import jProtocol.tls12.model.values.TlsApplicationData;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import java.nio.charset.StandardCharsets;
import java.util.Observable;
import java.util.Observer;

public class Tls12Startup implements Observer {
	
	private TlsStateMachine _client;
	private TlsStateMachine _server;

	public Tls12Startup() {
		CommunicationChannel<TlsCiphertext> channel = new CommunicationChannel<>();
		
		_client = new TlsStateMachine(channel, TlsConnectionEnd.client);
		_client.addObserver(this);
		_server = new TlsStateMachine(channel, TlsConnectionEnd.server);
		_server.addObserver(this);
		
		channel.setClient(_client);
		channel.setServer(_server);
		
		_client.openConnection();

	}

	public static void main(String[] args) {
		new Tls12Startup();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof TlsStateMachineEvent)) {
			throw new RuntimeException("Catched invalid event!");
		}
		
		TlsStateMachineEventType type = ((TlsStateMachineEvent)arg1).getEventType();
		
		if (arg0 == _client && type == TlsStateMachineEventType.connection_established) {
			_client.sendData(new TlsApplicationData("3,14159265".getBytes(StandardCharsets.US_ASCII)));
		}
		if (arg0 == _server && type == TlsStateMachineEventType.connection_established) {
			_server.sendData(new TlsApplicationData("23.42.1337".getBytes(StandardCharsets.US_ASCII)));
		}
	}
}
