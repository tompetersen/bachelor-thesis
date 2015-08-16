package jProtocol.tls12.model.states.client;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsWaitingForFinishedState_Client extends TlsState {

	public TlsWaitingForFinishedState_Client(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.finished);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsFinishedMessage fm = (TlsFinishedMessage)message;
		
		if (_stateMachine.isCorrectVerifyData(fm.getVerifyData())) {
			setTlsState(TlsStateType.CONNECTION_ESTABLISHED_STATE);
		}
		else {
			setTlsState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE);
		}
	}

}
