package jProtocol.Abstract.Model;

public class CommunicationChannel<T extends ProtocolDataUnit> {

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
		if (sender == _server) {
			_client.receiveMessage(pdu);
		}
		else {
			_server.receiveMessage(pdu);
		}
	}
	
}
