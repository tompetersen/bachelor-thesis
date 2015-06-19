package jProtocol;

public class CommunicationChannel {

	private StateMachine _client;
	private StateMachine _server;
	
	public CommunicationChannel() {
		
	}
	
	public void setClient(StateMachine client) {
		_client = client;
	}
	
	public void setServer(StateMachine server) {
		_server = server;
	}
	
	synchronized public void sendMessage(ProtocolDataUnit pdu, StateMachine sender) {
		if (sender == _server) {
			_client.receiveMessage(pdu);
		}
		else {
			_server.receiveMessage(pdu);
		}
	}
	
}
