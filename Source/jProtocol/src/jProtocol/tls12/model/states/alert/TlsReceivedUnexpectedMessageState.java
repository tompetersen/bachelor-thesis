package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsAlertDescription.Alert;

public class TlsReceivedUnexpectedMessageState extends TlsAlertState {

	public TlsReceivedUnexpectedMessageState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}
	
	@Override
	public TlsAlertMessage getAlertMessageToSend() {
		return new TlsAlertMessage(Alert.unexpected_message, true);
	}

}
