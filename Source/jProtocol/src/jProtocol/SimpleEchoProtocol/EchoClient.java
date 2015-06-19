package jProtocol.SimpleEchoProtocol;

import jProtocol.CommunicationChannel;
import jProtocol.ProtocolDataUnit;
import jProtocol.State;
import jProtocol.StateMachine;

public class EchoClient extends StateMachine {

	public final Integer RECEIVE_STATE = 1; 
	public final Integer SEND_STATE = 2; 
	
	private String _lastSentPayload;
	
	public EchoClient(CommunicationChannel channel) {
		super(channel);
		
		_states.put(RECEIVE_STATE, new ReceiveState());
		_states.put(SEND_STATE, new SendState());
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
	
	private class SendState extends State {
		@Override
		public void receiveMessage(ProtocolDataUnit pdu) {
			System.err.println("Client: Unexpected message received in SendState!");
		}
	}
	
	private class ReceiveState extends State {
		@Override
		public void receiveMessage(ProtocolDataUnit pdu) {
			if (pdu instanceof EchoProtocolDataUnit) { //argh, unschön: Generics bis zum Erbrechen?
				if ( ((EchoProtocolDataUnit)pdu).getPayload().equals(_lastSentPayload)) {
					System.out.println("Client: Correct response received.");
				}
				else {
					System.err.println("Client: Incorrect response received!");
				}
				setState(SEND_STATE);
			}
		}
	}

}
