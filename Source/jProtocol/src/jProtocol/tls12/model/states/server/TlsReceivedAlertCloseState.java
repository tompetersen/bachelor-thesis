package jProtocol.tls12.model.states.server;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;

public class TlsReceivedAlertCloseState extends TlsState {

	public TlsReceivedAlertCloseState(TlsStateMachine stateMachine) {
		super(stateMachine);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		// TODO Auto-generated method stub

	}

}
