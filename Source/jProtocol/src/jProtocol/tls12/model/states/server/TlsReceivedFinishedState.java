package jProtocol.tls12.model.states.server;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsChangeCipherSpecMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
import jProtocol.tls12.model.values.TlsVerifyData;

public class TlsReceivedFinishedState extends TlsState {

	public TlsReceivedFinishedState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isApplicationDataMessage(message);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		MyLogger.info("Server cached application data message in ReceivedFinishedState.");
		_stateMachine.addCachedApplicationDataMessage((TlsApplicationDataMessage)message);
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		boolean fullHandshake = true;
		
		if (fullHandshake) {
			sendChangeCipherSpecMessage();
			sendFinishedMessage();
			
			setTlsState(TlsStateType.CONNECTION_ESTABLISHED_STATE);
		}
		else {
			//TODO: abbreviated handshake
		}
	}
	
	private void sendFinishedMessage() {
		TlsVerifyData verifyData = _stateMachine.getVerifyDataToSend();
		TlsFinishedMessage message = new TlsFinishedMessage(verifyData);
		sendTlsMessage(message);
	}

	private void sendChangeCipherSpecMessage() {
		TlsMessage message = new TlsChangeCipherSpecMessage();
		sendTlsMessage(message);
	}


}
