package jProtocol.tls12.model.states.server;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsWaitingForFinishedState_Server extends TlsState {

	public TlsWaitingForFinishedState_Server(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.finished);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsFinishedMessage finishedMessage = (TlsFinishedMessage)message;
		
		MyLogger.info("Server received Finished!");

		if (_stateMachine.isCorrectVerifyData(finishedMessage.getVerifyData())) {
			setTlsState(TlsStateType.SERVER_RECEIVED_FINISHED_STATE);
		}
		else {
			setTlsState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE);
		}
	}

}
