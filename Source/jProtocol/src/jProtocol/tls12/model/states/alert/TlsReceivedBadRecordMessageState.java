package jProtocol.tls12.model.states.alert;

import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsAlertDescription.Alert;

public class TlsReceivedBadRecordMessageState extends TlsAlertState {

	public TlsReceivedBadRecordMessageState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public TlsAlertMessage getAlertMessageToSend() {
		return new TlsAlertMessage(Alert.bad_record_mac, true);
	}
}
