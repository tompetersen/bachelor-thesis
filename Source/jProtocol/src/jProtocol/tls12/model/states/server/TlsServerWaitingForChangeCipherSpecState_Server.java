package jProtocol.tls12.model.states.server;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;

public class TlsServerWaitingForChangeCipherSpecState_Server extends TlsState {

	public TlsServerWaitingForChangeCipherSpecState_Server(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isChangeCipherSpecMessage(message);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		MyLogger.info("Server received Change Cipher Spec!");
		
		_stateMachine.changeToPendingState();
		
		setTlsState(TlsStateType.SERVER_IS_WAITING_FOR_FINISHED_STATE);
	}

}
