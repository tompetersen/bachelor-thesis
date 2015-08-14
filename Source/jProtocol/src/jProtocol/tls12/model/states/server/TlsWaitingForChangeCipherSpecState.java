package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;

public class TlsWaitingForChangeCipherSpecState extends TlsState {

	public TlsWaitingForChangeCipherSpecState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isChangeCipherSpecMessage(message);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		_stateMachine.changeToPendingState();
		
		setState(TlsStateMachine.SERVER_IS_WAITING_FOR_FINISHED_STATE);
	}

}
