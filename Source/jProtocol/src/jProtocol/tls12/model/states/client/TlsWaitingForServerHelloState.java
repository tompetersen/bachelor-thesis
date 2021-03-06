package jProtocol.tls12.model.states.client;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerHelloMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsVersion;

public class TlsWaitingForServerHelloState extends TlsState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
	public TlsWaitingForServerHelloState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.server_hello);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsServerHelloMessage serverHello = (TlsServerHelloMessage)message;
		_stateMachine.addHandshakeMessageForVerifyData(serverHello);
		
		MyLogger.info("Received Server Hello!");
		
		boolean fullHandshake = true;
		if (fullHandshake) {
			setServerHelloValues(serverHello);
			setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_CERTIFICATE_STATE);
		}
		else {
			//TODO: Used for abbreviated handshake -> implement if necessary
		}
	}

	private void setServerHelloValues(TlsServerHelloMessage message) {
		TlsVersion version = message.getServerVersion();
		if (_stateMachine.isSupportedVersion(version)) {
			_stateMachine.setVersion(version);
		}
		else {
			//TODO: no supported version
		}
		_stateMachine.setServerRandom(message.getServerRandom());
		_stateMachine.setSessionId(message.getSessionId());
		_stateMachine.setPendingCipherSuite(message.getCipherSuite());
		
		_stateMachine.notifyObserversOfStateChanged();
	}
	
}
