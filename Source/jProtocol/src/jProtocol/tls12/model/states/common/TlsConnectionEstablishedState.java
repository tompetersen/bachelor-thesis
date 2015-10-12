package jProtocol.tls12.model.states.common;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsAlert;
import jProtocol.tls12.model.values.TlsApplicationData;

public class TlsConnectionEstablishedState extends TlsState{

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
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
		_stateMachine.notifyObserversOfStateChanged();
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
		
		_stateMachine.notifyObserversOfStateChanged();
	}

	private void processApplicationData(TlsApplicationDataMessage m) {
		_stateMachine.receivedData(m.getApplicationData());
	}
	
	private void closeNotifyAlertReceived() {
		setTlsState(TlsStateType.RECEIVED_CLOSE_NOTIFY_STATE);
	}
	
	/**
	 * Sends a TLS message from the state machine this state belongs to.
	 * 
	 * @param data the application data
	 */
	public void sendApplicationData(TlsApplicationData data) {
		TlsApplicationDataMessage message = new TlsApplicationDataMessage(data);
		sendTlsMessage(message);
	}
	
	/**
	 * Closes the connetion.
	 */
	public void closeConnection() {
		setTlsState(TlsStateType.WAITING_FOR_CLOSE_NOTIFY_STATE);
		
		TlsAlertMessage message = new TlsAlertMessage(TlsAlert.close_notify, false);
		sendTlsMessage(message);
	}
	
	
	
}
