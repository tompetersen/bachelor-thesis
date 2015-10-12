package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;

public abstract class TlsAlertState extends TlsState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
	public TlsAlertState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		//Ignore incoming messages
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		//catch all incoming messages
		return true;
	}
	
	@Override
	public void onEnter() {
		super.onEnter();
		
		TlsAlertMessage alertMessage = getAlertMessageToSend();
		if (alertMessage != null) {
			sendTlsMessage(alertMessage);
		}
		//MyLogger.severe("Would send alert message: " + getAlertMessageToSend().getAlert().toString());
		//setTlsState(TlsStateType.INITIAL_SERVER_STATE);
	}
	
	/**
	 * Returns the TLS alert message, which should be send for the alert state.
	 * 
	 * @return the alert message
	 */
	public abstract TlsAlertMessage getAlertMessageToSend();

}
