package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsAlert;

public class TlsReceivedCloseNotifyState extends TlsState {

	public TlsReceivedCloseNotifyState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return true;
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		/*
		 *  Either party may initiate a close by sending a close_notify alert.
		 *  Any data received after a closure alert is ignored.
		 *  chapter 7.2.1, p.29 TLS1.2  
		 */
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		TlsAlertMessage message = new TlsAlertMessage(TlsAlert.close_notify, false);
		sendTlsMessage(message);
		
		//TODO:setState(TlsStateMachine.INITIAL_SERVER_STATE)
	}
	
	

}
