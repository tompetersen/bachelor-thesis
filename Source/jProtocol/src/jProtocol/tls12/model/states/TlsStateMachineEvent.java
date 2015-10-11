package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.events.Event;

public class TlsStateMachineEvent extends Event {
	
	/**
	 * An enumeration of possible state machine events.
	 * 
	 * @author Tom Petersen
	 */
	public enum TlsStateMachineEventType {
		connection_established,
		connection_error,
		received_data,
		connection_closed
	}
	
	private TlsStateMachineEventType _eventType;

	/**
	 * Creates a state machine event 
	 * 
	 * @param eventType the type of the event
	 */
	public TlsStateMachineEvent(TlsStateMachineEventType eventType) {
		super();
		_eventType = eventType;
	}

	/**
	 * Returns the event type.
	 * 
	 * @return the event type
	 */
	public TlsStateMachineEventType getEventType() {
		return _eventType;
	}
}