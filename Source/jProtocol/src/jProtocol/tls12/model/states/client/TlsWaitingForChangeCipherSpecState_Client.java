package jProtocol.tls12.model.states.client;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;

public class TlsWaitingForChangeCipherSpecState_Client extends TlsState {

	public TlsWaitingForChangeCipherSpecState_Client(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isChangeCipherSpecMessage(message) || isApplicationDataMessage(message);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		if (isApplicationDataMessage(message)) {
			MyLogger.info("Client cached application data message in WaitingForChangeCipherSpecState.");
			_stateMachine.addCachedApplicationDataMessage((TlsApplicationDataMessage) message);
		}
		else if (isChangeCipherSpecMessage(message)) {
			MyLogger.info("Client received Change Cipher Spec!");
			
			_stateMachine.changeReadStateToPendingState();
			
			setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_FINISHED_STATE);
		}
	}

}
