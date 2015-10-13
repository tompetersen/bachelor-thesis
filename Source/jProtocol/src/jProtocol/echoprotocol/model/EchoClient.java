package jProtocol.echoprotocol.model;

import jProtocol.Abstract.Model.State;
import jProtocol.Abstract.Model.StateMachine;
import java.util.ArrayList;
import java.util.List;

public class EchoClient extends StateMachine<EchoMessage> {

	public final Integer RECEIVE_STATE = 1;
	public final Integer SEND_STATE = 2;

	private List<String> _receivedMessages;

	public EchoClient() {
		_receivedMessages = new ArrayList<>();

		addState(RECEIVE_STATE, new ReceiveState(this));
		addState(SEND_STATE, new SendState(this));

		setState(SEND_STATE);
	}

	public List<String> getReceivedMessages() {
		return _receivedMessages;
	}

	public void sendEchoRequest(String payload) {
		setState(RECEIVE_STATE);

		EchoMessage pdu = new EchoMessage(payload);

		sendMessage(pdu);
	}

	private class SendState extends State<EchoMessage> {

		public SendState(EchoClient stateMachine) {
			super(stateMachine);
		}

		@Override
		public void receiveMessage(EchoMessage pdu) {
			System.err.println("Client: Unexpected message received in SendState!");
		}
	}

	private class ReceiveState extends State<EchoMessage> {
		private EchoClient _stateMachine;

		public ReceiveState(EchoClient stateMachine) {
			super(stateMachine);
			_stateMachine = stateMachine;
		}

		@Override
		public void receiveMessage(EchoMessage pdu) {
			_receivedMessages.add(pdu.getPayload());
			_stateMachine.notifyObserversOfStateChanged();

			setState(SEND_STATE);
		}
	}

}
