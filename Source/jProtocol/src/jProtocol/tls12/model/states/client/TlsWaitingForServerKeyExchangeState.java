package jProtocol.tls12.model.states.client;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsWaitingForServerKeyExchangeState extends TlsState {

	public TlsWaitingForServerKeyExchangeState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.server_key_exchange);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		// TODO Auto-generated method stub
		
		setState(TlsStateMachine.CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE);
	}

}
