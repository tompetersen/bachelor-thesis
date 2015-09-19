package jProtocol.tls12.model.states.server;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsVerifyData;

public class TlsWaitingForFinishedState_Server extends TlsState {

	public TlsWaitingForFinishedState_Server(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.finished) || isApplicationDataMessage(message);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		if (isApplicationDataMessage(message)) {
			MyLogger.info("Server cached application data message in WaitingForFinishedState.");
			_stateMachine.addCachedApplicationDataMessage((TlsApplicationDataMessage)message);
		}
		else if (isHandshakeMessageOfType(message, TlsHandshakeType.finished)) {
			TlsFinishedMessage finishedMessage = (TlsFinishedMessage)message;
			
			MyLogger.info("Server received Finished!");

			TlsVerifyData verifyData = finishedMessage.getVerifyData();
			if (_stateMachine.isCorrectVerifyData(verifyData)) {
				setTlsState(TlsStateType.SERVER_RECEIVED_FINISHED_STATE);
			}
			else {
				setTlsState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE);
			}
		}
	}

}
