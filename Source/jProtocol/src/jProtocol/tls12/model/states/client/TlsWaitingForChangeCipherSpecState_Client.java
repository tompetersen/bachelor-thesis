package jProtocol.tls12.model.states.client;

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
		return isChangeCipherSpecMessage(message);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		_stateMachine.changeToPendingState();
		
		setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_FINISHED_STATE);
	}

}
