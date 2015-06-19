package jProtocol;

public abstract class State {
	
	public abstract void receiveMessage(ProtocolDataUnit pdu);
	
}
