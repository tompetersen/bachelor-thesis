package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsWaitingForFinishedState extends TlsState {

	public TlsWaitingForFinishedState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, HandshakeType.finished);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsFinishedMessage finishedMessage = (TlsFinishedMessage)message;

		if (_stateMachine.isCorrectVerifyData(finishedMessage.getVerifyData())) {
			setState(TlsStateMachine.RECEIVED_FINISHED_STATE);
		}
		else {
			setState(TlsStateMachine.DECRYPT_ERROR_OCCURED_STATE);
		}
	}

}
