package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsAlert;

public class TlsReceivedUnexpectedMessageState extends TlsAlertState {

	public TlsReceivedUnexpectedMessageState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}
	
	@Override
	public TlsAlertMessage getAlertMessageToSend() {
		return new TlsAlertMessage(TlsAlert.unexpected_message, true);
	}

}
