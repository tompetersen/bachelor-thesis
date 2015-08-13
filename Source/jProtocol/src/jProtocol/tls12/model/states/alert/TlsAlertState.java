package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;

public abstract class TlsAlertState extends TlsState {

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
		
		sendTlsMessage(getAlertMessageToSend());
		
		//setState(TlsStateMachine.INITIAL_SERVER_STATE);
	}
	
	public abstract TlsAlertMessage getAlertMessageToSend();

}
