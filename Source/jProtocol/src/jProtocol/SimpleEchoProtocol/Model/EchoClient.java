package jProtocol.SimpleEchoProtocol.Model;

import jProtocol.Abstract.Model.State;
import jProtocol.Abstract.Model.StateMachine;

public class EchoClient extends StateMachine<EchoProtocolDataUnit> {

	public final Integer RECEIVE_STATE = 1;
	public final Integer SEND_STATE = 2;

	private String _lastSentPayload;

	public EchoClient(EchoCommunicationChannel channel) {
		super(channel);

		addState(RECEIVE_STATE, new ReceiveState(this));
		addState(SEND_STATE, new SendState(this));
		
		setState(SEND_STATE);
	}

	public void sendEchoRequest(String payload) {
		setState(RECEIVE_STATE);
		System.out.println("Client: sending request...");

		_lastSentPayload = payload;
		EchoProtocolDataUnit pdu = new EchoProtocolDataUnit();
		pdu.setPayload(payload);

		sendMessage(pdu);
	}

	private class SendState extends State<EchoProtocolDataUnit> {
		
		public SendState(EchoClient stateMachine) {
			super(stateMachine);
		}

		@Override
		public void receiveMessage(EchoProtocolDataUnit pdu) {
			System.err.println("Client: Unexpected message received in SendState!");
		}
	}

	private class ReceiveState extends State<EchoProtocolDataUnit> {
		
		public ReceiveState(EchoClient stateMachine) {
			super(stateMachine);
		}

		@Override
		public void receiveMessage(EchoProtocolDataUnit pdu) {
			if (pdu.getPayload().equals(_lastSentPayload)) {
				System.out.println("Client: Correct response received.");
			} else {
				System.err.println("Client: Incorrect response received!");
			}
			setState(SEND_STATE);
		}
	}

}
