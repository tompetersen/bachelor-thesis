package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsWaitingForClientKeyExchangeState extends TlsState {

	public TlsWaitingForClientKeyExchangeState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsClientKeyExchangeMessage clientKeyExchangeMessage = (TlsClientKeyExchangeMessage)message;
		//TODO: whatever...
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, HandshakeType.client_key_exchange);
	}
}
