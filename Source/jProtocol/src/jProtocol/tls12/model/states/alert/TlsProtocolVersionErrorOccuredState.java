package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsAlert;

public class TlsProtocolVersionErrorOccuredState extends TlsAlertState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
	public TlsProtocolVersionErrorOccuredState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public TlsAlertMessage getAlertMessageToSend() {
		return new TlsAlertMessage(TlsAlert.protocol_version, true);
	}

}
