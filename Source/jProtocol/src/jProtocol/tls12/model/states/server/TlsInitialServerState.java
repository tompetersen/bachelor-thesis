package jProtocol.tls12.model.states.server;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientHelloMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsInitialServerState extends TlsState {

	public TlsInitialServerState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsClientHelloMessage chm = (TlsClientHelloMessage)message;
		
		MyLogger.info("Received Client Hello!");
		
		if (_stateMachine.canPerformAbbreviatedHandshakeForSessionId(chm.getSessionId())) {
			//TODO: Abbreviated Handshake
		}
		setClientHelloMessageValues(chm);
		_stateMachine.addHandshakeMessageForVerifyData(message);
		
		setTlsState(TlsStateType.SERVER_RECEIVED_CLIENT_HELLO_STATE);
	}
	
	private void setClientHelloMessageValues(TlsClientHelloMessage chm) {
		_stateMachine.setPendingClientRandom(chm.getClientRandom());
		_stateMachine.setPendingSessionId(chm.getSessionId());	
		_stateMachine.setPendingCipherSuiteFromList(chm.getCipherSuites());	
		_stateMachine.setPendingVersion(chm.getClientVersion());
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.client_hello);
	}
}
