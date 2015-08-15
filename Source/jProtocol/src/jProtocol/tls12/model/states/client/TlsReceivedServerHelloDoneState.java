package jProtocol.tls12.model.states.client;

import jProtocol.tls12.model.messages.TlsChangeCipherSpecMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;

public class TlsReceivedServerHelloDoneState extends TlsState {

	public TlsReceivedServerHelloDoneState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return false;
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		throw new RuntimeException("Client should not receive messages in ReceivedServerHelloDoneState!");
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		sendClientKeyExchangeMessage();
		sendChangeCipherSpecMessage();
		sendFinishedMessage();
		
		setState(TlsStateMachine.CLIENT_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE);
	}
	
	private void sendClientKeyExchangeMessage() {
		//TODO: KeyExchange
	}
	
	private void sendFinishedMessage() {
		byte[] verifyData = _stateMachine.getVerifyDataToSend();
		TlsFinishedMessage message = new TlsFinishedMessage(verifyData);
		sendTlsMessage(message);
	}

	private void sendChangeCipherSpecMessage() {
		TlsMessage message = new TlsChangeCipherSpecMessage();
		sendTlsMessage(message);
	}
}
