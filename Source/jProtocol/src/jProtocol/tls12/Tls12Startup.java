package jProtocol.tls12;

import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsConnectionEnd;

public class Tls12Startup {
	
	private TlsStateMachine _client;
	private TlsStateMachine _server;

	public Tls12Startup() {
		CommunicationChannel<TlsCiphertext> channel = new CommunicationChannel<>();
		
		_client = new TlsStateMachine(channel, TlsConnectionEnd.client);
		_server = new TlsStateMachine(channel, TlsConnectionEnd.server);
		
		channel.setClient(_client);
		channel.setServer(_server);
		
		_client.openConnection();
	}

	public static void main(String[] args) {
		new Tls12Startup();
	}

}
