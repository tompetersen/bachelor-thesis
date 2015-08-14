package jProtocol.tls12.model.states.client;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerHelloMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsVersion;

public class TlsWaitingForServerHelloState extends TlsState {

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
		
		boolean fullHandshake = true;
		if (fullHandshake) {
			setServerHelloValues(serverHello);
			setState(TlsStateMachine.CLIENT_IS_WAITING_FOR_CERTIFICATE_STATE);
		}
		else {
			//TODO: abbreviated
		}
	}

	private void setServerHelloValues(TlsServerHelloMessage message) {
		TlsVersion version = message.getServerVersion();
		if (_stateMachine.isSupportedVersion(version)) {
			_stateMachine.setPendingVersion(version);
		}
		else {
			//TODO: ...
		}
		_stateMachine.setPendingServerRandom(message.getServerRandom());
		_stateMachine.setPendingSessionId(message.getSessionId());
		_stateMachine.setPendingCipherSuite(message.getCipherSuite());
	}
	
}
