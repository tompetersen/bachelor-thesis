package jProtocol.tls12.model.states.client;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsCertificateMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
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
		_stateMachine.addHandshakeMessageForVerifyData(certMessage);
		
		MyLogger.info("Received Server Certificate!");
		
		_stateMachine.setCertificateList(certMessage.getCertificates());
		
		boolean needsServerKeyExchangemessage = false;
		if (needsServerKeyExchangemessage) {
			setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_KEY_EXCHANGE_STATE);
		}
		else {
			setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE);
		}
		
	}

}
