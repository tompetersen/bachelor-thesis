package jProtocol.tls12.model.states.common;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsAlert;

public class TlsWaitingForCloseNotifyState extends TlsState {

	public TlsWaitingForCloseNotifyState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		/*  TODO: close notify
		 *  Unless some other fatal alert has been transmitted, each party is 
		 *  required to send a close_notify alert before closing the write side 
		 *  of the connection. 
		 */
		return isAlertMessageOfType(message, TlsAlert.close_notify);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		//TODO: Successfully closed connection
	}

}
