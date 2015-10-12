package jProtocol.tls12.model.states.client;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerHelloDoneMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsWaitingForServerHelloDoneState extends TlsState {

	/**
	 * Creates a state for a specific state machine.
	 * 
	 * @param stateMachine the state machine 
	 */
	public TlsWaitingForServerHelloDoneState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.server_hello_done);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsServerHelloDoneMessage helloDoneMessage = (TlsServerHelloDoneMessage)message;
		_stateMachine.addHandshakeMessageForVerifyData(helloDoneMessage);
		
		MyLogger.info("Received Server Hello Done!");
		
		setTlsState(TlsStateType.CLIENT_RECEIVED_SERVER_HELLO_DONE_STATE);
	}

}
