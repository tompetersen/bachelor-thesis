package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.states.TlsStateMachine;

public class TlsReceivedFatalAlertMessageState extends TlsAlertState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
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
