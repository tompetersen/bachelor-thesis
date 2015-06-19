package jProtocol;

import java.util.HashMap;
import java.util.Map;

public abstract class StateMachine {
	
	private State _currentState;
	protected Map<Integer, State> _states = new HashMap<Integer, State>();
	private CommunicationChannel _channel;
	
	public StateMachine(CommunicationChannel channel) {
		_channel = channel;
	}
	
	public void setState(Integer state) {
		State newState = _states.get(state);
		_currentState = newState;
	}
	
	public void receiveMessage(ProtocolDataUnit pdu) {
		_currentState.receiveMessage(pdu);
	}
	
	public void sendMessage(ProtocolDataUnit pdu) {
		_channel.sendMessage(pdu, this);
	}
}
