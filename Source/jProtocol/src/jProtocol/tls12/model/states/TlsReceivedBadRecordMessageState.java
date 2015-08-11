package jProtocol.tls12.model.states;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsAlertDescription.Alert;

public class TlsReceivedBadRecordMessageState extends TlsState {

	public TlsReceivedBadRecordMessageState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		//Ignore incoming messages
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		TlsAlertMessage message = new TlsAlertMessage(Alert.bad_record_mac, true);
		sendTlsMessage(message);
		
		//setState(TlsStateMachine.INITIAL_SERVER_STATE);
	}

}
