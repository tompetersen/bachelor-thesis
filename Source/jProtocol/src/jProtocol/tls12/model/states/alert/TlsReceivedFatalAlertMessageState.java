package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.states.TlsStateMachine;

public class TlsReceivedFatalAlertMessageState extends TlsAlertState {

	public TlsReceivedFatalAlertMessageState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public TlsAlertMessage getAlertMessageToSend() {
		return null;
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		_stateMachine.notifyObserversOfStateChanged();
	}
}
