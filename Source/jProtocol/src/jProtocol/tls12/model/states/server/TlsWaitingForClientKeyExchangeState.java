package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsWaitingForClientKeyExchangeState extends TlsState {

	public TlsWaitingForClientKeyExchangeState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsClientKeyExchangeMessage clientKeyExchangeMessage = (TlsClientKeyExchangeMessage)message;
		//TODO: whatever...
		
		_stateMachine.addHandshakeMessageForVerifyData(message);
		
		setState(TlsStateMachine.WAITING_FOR_CHANGE_CIPHER_SPEC_STATE);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.client_key_exchange);
	}
}
