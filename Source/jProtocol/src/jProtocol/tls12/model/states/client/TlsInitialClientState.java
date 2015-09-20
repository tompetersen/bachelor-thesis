package jProtocol.tls12.model.states.client;

import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientHelloMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;
import java.util.List;

public class TlsInitialClientState extends TlsState {

	public TlsInitialClientState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return false;
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		throw new RuntimeException("Client must not receive messages in initial state.");
	}
	
	public void openConnection() {
		setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_STATE);
		
		sendClientHelloMessage();
	}
	
	private void sendClientHelloMessage() {
		byte[] randomBytes = TlsPseudoRandomNumberGenerator.nextBytes(28);
		TlsRandom random = new TlsRandom(randomBytes);
		_stateMachine.setClientRandom(random);
		
		TlsVersion version = _stateMachine.getHighestSupportedVersion();
		
		byte[] sessionIdBytes = TlsPseudoRandomNumberGenerator.nextBytes(10);
		TlsSessionId sessionId = new TlsSessionId(sessionIdBytes);
		
		List<TlsCipherSuite> cipherSuites = _stateMachine.allCipherSuites();		
		
		TlsClientHelloMessage clientHello = new TlsClientHelloMessage(version, random, sessionId, cipherSuites);
		_stateMachine.addHandshakeMessageForVerifyData(clientHello);
		
		sendTlsMessage(clientHello);
	}

	
	
}
