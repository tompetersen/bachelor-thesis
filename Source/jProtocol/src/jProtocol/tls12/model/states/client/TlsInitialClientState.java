package jProtocol.tls12.model.states.client;

import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientHelloMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsExtension;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;
import java.util.ArrayList;
import java.util.List;

public class TlsInitialClientState extends TlsState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
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
	
	/**
	 * Starts a TLS connection.
	 */
	public void openConnection() {
		setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_STATE);
		
		sendClientHelloMessage();
	}
	
	private void sendClientHelloMessage() {
		byte[] randomBytes = TlsPseudoRandomNumberGenerator.nextBytes(28);
		TlsRandom random = new TlsRandom(randomBytes);
		_stateMachine.setClientRandom(random);
		
		TlsVersion version = _stateMachine.getHighestSupportedVersion();
		
		//TODO: Used for abbreviated handshake -> Implement if necessary
		byte[] sessionIdBytes = {};
		TlsSessionId sessionId = new TlsSessionId(sessionIdBytes);
		
		List<TlsCipherSuite> cipherSuites = _stateMachine.allCipherSuites();		
		
		//TODO: Used for TLS extensions -> implement if necessary
		List<TlsExtension> extensionList = new ArrayList<>();
		
		TlsClientHelloMessage clientHello = new TlsClientHelloMessage(version, random, sessionId, cipherSuites, extensionList);
		_stateMachine.addHandshakeMessageForVerifyData(clientHello);
		
		sendTlsMessage(clientHello);
	}	
}
