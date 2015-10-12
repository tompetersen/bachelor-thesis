package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsCertificateMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerHelloDoneMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerHelloMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerKeyExchangeMessage;
import jProtocol.tls12.model.states.TlsServerStateMachine;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsExtension;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsRandom;
import java.util.ArrayList;
import java.util.List;

public class TlsReceivedClientHelloState extends TlsState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
	public TlsReceivedClientHelloState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		setTlsState(TlsStateType.SERVER_IS_WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE);
		
		sendServerHello();
		sendServerCertificate();
		sendServerKeyExchange();
		sendServerHelloDone();
	}
	
	private void sendServerHello() {
		byte[] randomBytes = TlsPseudoRandomNumberGenerator.nextBytes(28);
		TlsRandom random = new TlsRandom(randomBytes);
		_stateMachine.setServerRandom(random);
		
		TlsHandshakeMessage serverHelloMessage = new TlsServerHelloMessage(
				_stateMachine.getVersion(), 
				random, 
				_stateMachine.getSessionId(), 
				_stateMachine.getPendingCipherSuite(),
				new ArrayList<TlsExtension>()); //TODO: Used for TLS extensions -> implement if necessary
		
		_stateMachine.addHandshakeMessageForVerifyData(serverHelloMessage);
		
		sendTlsMessage(serverHelloMessage);
	}
	
	private void sendServerCertificate() {
		List<TlsCertificate> certList = _stateMachine.getCertificateList();
		TlsHandshakeMessage message = new TlsCertificateMessage(certList);
		
		_stateMachine.addHandshakeMessageForVerifyData(message);
		sendTlsMessage(message);
	}
	
	private void sendServerKeyExchange() {
		TlsKeyExchangeAlgorithm algorithm = _stateMachine.getPendingCipherSuite().getKeyExchangeAlgorithm();
		
		if (algorithm == TlsKeyExchangeAlgorithm.dhe_rsa) {
			TlsServerStateMachine serverStateMachine = (TlsServerStateMachine) _stateMachine;
			TlsHandshakeMessage message;
			try {
				message = new TlsServerKeyExchangeMessage(serverStateMachine.getServerDhParams(), serverStateMachine.getSignedDhParams());
			}
			catch (TlsAsymmetricOperationException e) {
				//Invalid signature will fail verification on client side
				message = new TlsServerKeyExchangeMessage(serverStateMachine.getServerDhParams(), new byte[0]);
			}
			
			_stateMachine.addHandshakeMessageForVerifyData(message);
			sendTlsMessage(message);
		}
	}
	
	private void sendServerHelloDone() {
		TlsServerHelloDoneMessage message = new TlsServerHelloDoneMessage();
		
		_stateMachine.addHandshakeMessageForVerifyData(message);
		sendTlsMessage(message);
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
