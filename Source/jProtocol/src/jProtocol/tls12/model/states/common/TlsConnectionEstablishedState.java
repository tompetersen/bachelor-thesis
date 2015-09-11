package jProtocol.tls12.model.states.common;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
import jProtocol.tls12.model.values.TlsAlert;
import jProtocol.tls12.model.values.TlsApplicationData;

public class TlsConnectionEstablishedState extends TlsState{

	public TlsConnectionEstablishedState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}
	
	

	@Override
	public void onEnter() {
		super.onEnter();
		
		for (TlsApplicationDataMessage message : _stateMachine.getCachedApplicationDataMessages()) {
			receivedTlsMessage(message);
		}
		
		_stateMachine.clearCachedApplicationDataMessage();
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
		_stateMachine.receivedData(m.getApplicationData());
	}
	
	private void closeNotifyAlertReceived() {
		setTlsState(TlsStateType.RECEIVED_CLOSE_NOTIFY_STATE);
	}
	
	public void sendApplicationData(TlsApplicationData data) {
		TlsApplicationDataMessage message = new TlsApplicationDataMessage(data);
		sendTlsMessage(message);
	}
	
	public void closeConnection() {
		TlsAlertMessage message = new TlsAlertMessage(TlsAlert.close_notify, false);
		sendTlsMessage(message);
		
		setTlsState(TlsStateType.WAITING_FOR_CLOSE_NOTIFY_STATE);
	}
	
	
	
}
