package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.events.Event;

public class TlsStateMachineEvent extends Event {
	
	public enum TlsStateMachineEventType {
		connection_established,
		connection_error,
		received_data,
		connection_closed
	}
	
	private TlsStateMachineEventType _eventType;

	public TlsStateMachineEvent(TlsStateMachineEventType eventType) {
		super();
		_eventType = eventType;
	}

	public TlsStateMachineEventType getEventType() {
		return _eventType;
	}
}