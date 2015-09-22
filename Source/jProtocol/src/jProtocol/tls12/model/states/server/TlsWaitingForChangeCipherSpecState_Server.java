package jProtocol.tls12.model.states.server;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;

public class TlsWaitingForChangeCipherSpecState_Server extends TlsState {

	public TlsWaitingForChangeCipherSpecState_Server(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isChangeCipherSpecMessage(message) || isApplicationDataMessage(message);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		if (isApplicationDataMessage(message)) {
			MyLogger.info("Server cached application data message in WaitingForChangeCipherSpecState.");
			_stateMachine.addCachedApplicationDataMessage((TlsApplicationDataMessage)message);
		}
		else if (isChangeCipherSpecMessage(message)) {
			MyLogger.info("Server received Change Cipher Spec!");
		
			_stateMachine.changeReadStateToPendingState();
			
			_stateMachine.notifyObserversOfStateChanged();
		
			setTlsState(TlsStateType.SERVER_IS_WAITING_FOR_FINISHED_STATE);
		}
	}

}
