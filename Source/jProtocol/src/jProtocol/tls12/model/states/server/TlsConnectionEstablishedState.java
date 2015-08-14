package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsAlert;

public class TlsConnectionEstablishedState extends TlsState{

	public TlsConnectionEstablishedState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isApplicationDataMessage(message) || isAlertMessageOfType(message, TlsAlert.close_notify);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		if (isApplicationDataMessage(message)) {
			processApplicationData((TlsApplicationDataMessage)message);
		}
		else if (isAlertMessageOfType(message, TlsAlert.close_notify)){
			closeNotifyAlertReceived();
		}
	}

	private void processApplicationData(TlsApplicationDataMessage m) {
		//TODO: ApplicationDataProcessing
	}
	
	private void closeNotifyAlertReceived() {
		setState(TlsStateMachine.RECEIVED_CLOSE_NOTIFY_STATE);
	}
	
	public void closeConnection() {
		TlsAlertMessage message = new TlsAlertMessage(TlsAlert.close_notify, false);
		sendTlsMessage(message);
		
		setState(TlsStateMachine.WAITING_FOR_CLOSE_NOTIFY_STATE);
	}
	
}
