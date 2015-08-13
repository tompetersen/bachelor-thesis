package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsAlert;

public class TlsDecryptErrorOccuredState extends TlsAlertState {

	public TlsDecryptErrorOccuredState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public TlsAlertMessage getAlertMessageToSend() {
		return new TlsAlertMessage(TlsAlert.decrypt_error, true);
	}

}
