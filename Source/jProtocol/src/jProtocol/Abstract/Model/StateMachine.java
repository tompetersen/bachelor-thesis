package jProtocol.Abstract.Model;

import java.util.HashMap;
import java.util.Map;

public abstract class StateMachine<T extends ProtocolDataUnit> {
	
	private State<T> _currentState;
	protected Map<Integer, State<T>> _states = new HashMap<Integer, State<T>>();
	private CommunicationChannel<T> _channel;
	
	public StateMachine(CommunicationChannel<T> channel) {
		_channel = channel;
	}
	
	public void setState(Integer state) {
		State<T> newState = _states.get(state);
		_currentState = newState;
	}
	
	public void receiveMessage(T pdu) {
		_currentState.receiveMessage(pdu);
	}
	
	public void sendMessage(T pdu) {
		_channel.sendMessage(pdu, this);
	}
}
