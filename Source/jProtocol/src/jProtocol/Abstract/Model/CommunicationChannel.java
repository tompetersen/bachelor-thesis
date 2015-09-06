package jProtocol.Abstract.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CommunicationChannel<T extends ProtocolDataUnit> extends Observable {

	//TODO: circular reference problem!
	
	private StateMachine<T> _client;
	private StateMachine<T> _server;
	
	private List<T> _sentPdus;
	
	/**
	 * Creates a communication channel between client and server.
	 * 
	 * @param client the client state machine
	 * @param server the server state machine
	 */
	public CommunicationChannel(StateMachine<T> client, StateMachine<T> server) {
		_sentPdus = new ArrayList<>();
		
		if (client == null || server == null) {
			throw new IllegalArgumentException("Client and server must not be null!");
		}
		
		_client = client;
		_server = server;
	}
	
	synchronized public void sendMessage(T pdu, StateMachine<T> sender) {
		boolean clientMessage = (sender == _client);
		pdu.setSentByClient(clientMessage);
		_sentPdus.add(pdu);
		
		setChanged();
		notifyObservers();	
		
		if (clientMessage) {
			_server.receiveMessage(pdu);
		}
		else {
			_client.receiveMessage(pdu);
		}
	}
	
	public List<T> getSentProtocolDataUnits() {
		return _sentPdus;
	}
	
}
