package jProtocol.tls12.model.states;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsAlertDescription.Alert;

public class TlsReceivedUnexpectedMessageState extends TlsState {

	public TlsReceivedUnexpectedMessageState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		//Ignore incoming messages
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		TlsAlertMessage message = new TlsAlertMessage(Alert.unexpected_message, true);
		sendTlsMessage(message);
		
		//setState(TlsStateMachine.INITIAL_SERVER_STATE);
	}
}
