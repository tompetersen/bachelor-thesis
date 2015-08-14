package jProtocol.tls12.model.states.client;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;

public class TlsReceivedServerHelloDoneState extends TlsState {

	public TlsReceivedServerHelloDoneState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return false;
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		throw new RuntimeException("Client should not receive messages in ReceivedServerHelloDoneState!");
	}

	@Override
	public void onEnter() {
		super.onEnter();
		//TODO: Send ClientKeyExchange, ChangeCipherSpec, Finished
	}
}
