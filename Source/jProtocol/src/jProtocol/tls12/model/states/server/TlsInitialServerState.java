package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientHelloMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsContentType.ContentType;
import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsInitialServerState extends TlsState {

	public TlsInitialServerState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		if (isClientHelloMessage(message)) {
			TlsClientHelloMessage chm = (TlsClientHelloMessage)message;
			//TODO: storeClientValues, ...
		}
		else {
			setState(TlsStateMachine.RECEIVED_UNEXPECTED_MESSAGE_STATE);
		}
	}
	
	private boolean isClientHelloMessage(TlsMessage m) {
		return (m.getContentType() == ContentType.Handshake) && (((TlsHandshakeMessage)m).getHandshakeType() == HandshakeType.client_hello);
	}
}
