package jProtocol.Abstract.Model;

import java.util.HashMap;
import java.util.Map;

public abstract class StateMachine<T extends ProtocolDataUnit> {
	
	private State<T> _currentState;
	private Map<Integer, State<T>> _states = new HashMap<Integer, State<T>>();
	private CommunicationChannel<T> _channel;
	
	public StateMachine(CommunicationChannel<T> channel) {
		_channel = channel;
	}
	
	public void addState(Integer stateNumber, State<T> state) {
		_states.put(stateNumber, state);
	}
	
	protected void setState(Integer state) {
		_currentState.onLeave();
		
		State<T> newState = _states.get(state);
		_currentState = newState;
		
		_currentState.onEnter();
	}
	
	public void setState(Integer state, State<T> sender) {
		if (sender == _currentState) {
			setState(state);
		}
		else {
			throw new RuntimeException("Only current state is allowed to set state!");
		}
	}
	
	public void receiveMessage(T pdu) {
		_currentState.receiveMessage(pdu);
	}
	
	public void sendMessage(T pdu) {
		_channel.sendMessage(pdu, this);
	}
}
