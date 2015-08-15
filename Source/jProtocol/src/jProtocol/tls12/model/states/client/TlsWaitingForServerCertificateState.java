package jProtocol.tls12.model.states.client;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsCertificateMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsWaitingForServerCertificateState extends TlsState {

	public TlsWaitingForServerCertificateState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.certificate);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsCertificateMessage certMessage = (TlsCertificateMessage)message;
		//TODO: Certificate
		
		boolean needsServerKeyExchangemessage = false;
		if (needsServerKeyExchangemessage) {
			setState(TlsStateMachine.CLIENT_IS_WAITING_FOR_SERVER_KEY_EXCHANGE_STATE);
		}
		else {
			setState(TlsStateMachine.CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE);
		}
		
	}

}
