package jProtocol.tls12.model.states;

import jProtocol.tls12.model.messages.TlsMessage;

public class InitialServerState extends TlsState {

	public InitialServerState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		// TODO Auto-generated method stub
		
	}
}
