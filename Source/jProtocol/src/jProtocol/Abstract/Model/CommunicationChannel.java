package jProtocol.Abstract.Model;

public class CommunicationChannel<T extends ProtocolDataUnit> {

	//TODO: circular reference problem!
	
	private StateMachine<T> _client;
	private StateMachine<T> _server;
	
	public CommunicationChannel() {
		
	}
	
	public void setClient(StateMachine<T> client) {
		_client = client;
	}
	
	public void setServer(StateMachine<T> server) {
		_server = server;
	}
	
	synchronized public void sendMessage(T pdu, StateMachine<T> sender) {
		if (_server == null) {
			throw new RuntimeException("Server must be set in communication channel before sending messages!");
		}
		if (_client == null) {
			throw new RuntimeException("Client must be set in communication channel before sending messages!");
		}
		
		if (sender == _server) {
			_client.receiveMessage(pdu);
		}
		else {
			_server.receiveMessage(pdu);
		}
	}
	
}
