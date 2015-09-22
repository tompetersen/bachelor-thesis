package jProtocol.Abstract.Model;

import jProtocol.Abstract.Model.events.Event;
import jProtocol.Abstract.Model.events.StateMachineStateChangedEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public abstract class StateMachine<T extends ProtocolDataUnit> extends Observable {
	
	private State<T> _currentState;
	private Map<Integer, State<T>> _states = new HashMap<Integer, State<T>>();
	private CommunicationChannel<T> _channel;
	
	/**
	 * Sets the communication channel for sending and receiving protocol data units.
	 * 
	 * @param channel the communication channel
	 */
	public void setCommunicationChannel(CommunicationChannel<T> channel) {
		_channel = channel;
	}
	
	/**
	 * Adds a possible state to this state machine.
	 * 
	 * @param stateNumber an identifier for the state
	 * @param state the state
	 */
	public void addState(Integer stateNumber, State<T> state) {
		_states.put(stateNumber, state);
	}
	
	protected void setState(Integer state) {
		if (_currentState != null) {
			_currentState.onLeave();
		}
		
		State<T> newState = _states.get(state);
		_currentState = newState;
		
		_currentState.onEnter();
	}
	
	/**
	 * Sets the current state of the state machine.
	 * 
	 * @param state the state identifier
	 * @param sender the sending state. Should be the calling state.
	 */
	public void setState(Integer state, State<T> sender) {
		if (sender == _currentState) {
			setState(state);
		}
		else {
			throw new RuntimeException("Only current state [" + _currentState.toString() + "] is allowed to set state!");
		}
	}
	
	/**
	 * Returns the current state.
	 * 
	 * @return the current state
	 */
	public State<T> getCurrentState() {
		return _currentState;
	}
	
	/**
	 * Returns if a state is the current state.
	 * 
	 * @param state the state identifier
	 * 
	 * @return true if the identifier is the identifier of the current state
	 */
	public boolean isCurrentState(Integer state) {
		return _currentState.equals(stateForValue(state));
	}
	
	private State<T> stateForValue(int value) {
		for (int i : _states.keySet()) {
			if (i == value) {
				return _states.get(i);
			}
		}
		throw new IllegalArgumentException("State for value " + value + " not found!");
	}
	
	/**
	 * Sends a protocol data unit to the current state.
	 * 
	 * @param pdu the protocol data unit
	 */
	public void receiveMessage(T pdu) {
		_currentState.receiveMessage(pdu);
	}
	
	/**
	 * Sends a protocol data unit via the communication channel. 
	 * 
	 * @param pdu the protocol data unit
	 */
	public void sendMessage(T pdu) {
		if (_channel == null) {
			throw new RuntimeException("Communication channel must be set before sending messages!");
		}
		_channel.sendMessage(pdu, this);
	}
	
	/**
	 * Notifies observers of this state machine about an event.
	 * 
	 * @param event the event
	 */
	public void notifyObserversOfEvent(Event event) {
		setChanged();
		notifyObservers(event);
	}
	
	/**
	 * Notifies observers of this state machine about a state changed event.
	 */
	public void notifyObserversOfStateChanged() {
		setChanged();
		notifyObservers(new StateMachineStateChangedEvent());
	}
	
}
