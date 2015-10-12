package jProtocol.tls12.model.states.client;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsCertificateMessage;
import jProtocol.tls12.model.states.TlsClientStateMachine;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.util.List;

public class TlsWaitingForServerCertificateState extends TlsState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
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
		
		TlsClientStateMachine clientStateMachine = (TlsClientStateMachine) _stateMachine;
		
		List<TlsCertificate> certificateList = certMessage.getCertificates();
		clientStateMachine.setCertificateList(certificateList);
		
		TlsCertificate serverCert = certificateList.get(0);
		byte[] rsaPublicKey = serverCert.getRsaPublicKey();
		clientStateMachine.setRsaCipherForPublicKey(rsaPublicKey);
		
		_stateMachine.notifyObserversOfStateChanged();
		
		if (_stateMachine.needsServerKeyExchangeMessage()) {
			setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_KEY_EXCHANGE_STATE);
		}
		else {
			setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE);
		}
		
	}

}
