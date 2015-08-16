package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsCertificateMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerHelloMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerKeyExchangeMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsRandom;
import java.util.ArrayList;
import java.util.List;

public class TlsReceivedClientHelloState extends TlsState {

	public TlsReceivedClientHelloState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void onEnter() {
		super.onEnter();

		sendServerHello();
		sendServerCertificate();
		sendServerKeyExchange();
		
		setTlsState(TlsStateType.SERVER_IS_WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE);
	}
	
	private void sendServerHello() {
		byte[] randomBytes = TlsPseudoRandomNumberGenerator.nextBytes(28);
		TlsRandom random = new TlsRandom(randomBytes);
		_stateMachine.setPendingServerRandom(random);
		
		TlsMessage serverHelloMessage = new TlsServerHelloMessage(
				_stateMachine.getVersion(), 
				random, 
				_stateMachine.getPendingSessionId(), 
				_stateMachine.getPendingCipherSuite());
		
		_stateMachine.addHandshakeMessageForVerifyData(serverHelloMessage);
		sendTlsMessage(serverHelloMessage);
	}
	
	private void sendServerCertificate() {
		TlsCertificate certificate = TlsCertificate.generateCertificate();
		List<TlsCertificate> certList = new ArrayList<>();
		certList.add(certificate);
		
		TlsMessage message = new TlsCertificateMessage(certList);
		
		_stateMachine.addHandshakeMessageForVerifyData(message);
		sendTlsMessage(message);
	}
	
	private void sendServerKeyExchange() {
		//TODO: Send for DHE_RSA, ...
		boolean needsServerkeyExchangeMessage = false;
		if (needsServerkeyExchangeMessage) {
			TlsMessage message = new TlsServerKeyExchangeMessage();
			
			_stateMachine.addHandshakeMessageForVerifyData(message);
			sendTlsMessage(message);
		}
	}
	
	@Override
	public void receivedTlsMessage(TlsMessage message) {
		throw new RuntimeException("ReceivedTlsMessage() should not be called on ReceivedClientHelloState!");
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return false; // No messages expected
	}
}
