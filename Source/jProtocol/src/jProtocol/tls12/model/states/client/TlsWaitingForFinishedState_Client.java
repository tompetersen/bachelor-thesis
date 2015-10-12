package jProtocol.tls12.model.states.client;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsWaitingForFinishedState_Client extends TlsState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
	public TlsWaitingForFinishedState_Client(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.finished) || isApplicationDataMessage(message);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		if (isApplicationDataMessage(message)) {
			MyLogger.info("Client cached application data message in WaitingForFinishedState.");
			_stateMachine.addCachedApplicationDataMessage((TlsApplicationDataMessage)message);
		}
		else if (isHandshakeMessageOfType(message, TlsHandshakeType.finished)) {
			TlsFinishedMessage fm = (TlsFinishedMessage)message;
			
			MyLogger.info("Client received Finished!");
			
			if (_stateMachine.isCorrectVerifyData(fm.getVerifyData())) {
				setTlsState(TlsStateType.CONNECTION_ESTABLISHED_STATE);
			}
			else {
				setTlsState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE);
			}
		}
	}

}
