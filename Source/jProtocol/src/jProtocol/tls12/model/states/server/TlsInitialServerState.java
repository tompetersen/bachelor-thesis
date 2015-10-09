package jProtocol.tls12.model.states.server;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientHelloMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsSessionId;

public class TlsInitialServerState extends TlsState {

	public TlsInitialServerState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsClientHelloMessage chm = (TlsClientHelloMessage)message;
		
		MyLogger.info("Received Client Hello!");
		
		if (_stateMachine.canPerformAbbreviatedHandshakeForSessionId(chm.getSessionId())) {
			//TODO: Used for abbreviated handshake -> implement if necessary
		}
		
		if (checkTlsVersion(chm)) {
			setClientHelloMessageValues(chm);
			_stateMachine.addHandshakeMessageForVerifyData(chm);
			
			setTlsState(TlsStateType.SERVER_RECEIVED_CLIENT_HELLO_STATE);
		}
		else {
			setTlsState(TlsStateType.PROTOCOL_VERSION_ERROR_OCCURED_STATE);
		}
	}
	
	private boolean checkTlsVersion(TlsClientHelloMessage chm) {
		return _stateMachine.isSupportedVersion(chm.getClientVersion());
	}
	
	private void setClientHelloMessageValues(TlsClientHelloMessage chm) {
		_stateMachine.setClientRandom(chm.getClientRandom());
		//TODO: Used for abbreviated handshake -> Implement if necessary
//		_stateMachine.setSessionId(chm.getSessionId());	
		_stateMachine.setSessionId(new TlsSessionId(TlsPseudoRandomNumberGenerator.nextBytes(10)));
		_stateMachine.setPendingCipherSuiteFromList(chm.getCipherSuites());	
		_stateMachine.setVersion(chm.getClientVersion());
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.client_hello);
	}
}
