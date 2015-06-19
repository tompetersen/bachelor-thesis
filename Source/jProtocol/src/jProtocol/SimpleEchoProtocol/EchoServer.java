package jProtocol.SimpleEchoProtocol;

import jProtocol.CommunicationChannel;
import jProtocol.ProtocolDataUnit;
import jProtocol.State;
import jProtocol.StateMachine;

public class EchoServer extends StateMachine {

	public final Integer RECEIVE_STATE = 1; 
	
	public EchoServer(CommunicationChannel channel) {
		super(channel);
		
		_states.put(RECEIVE_STATE, new ReceiveState());               
		setState(RECEIVE_STATE);
	}
	
	private class ReceiveState extends State {

		@Override
		public void receiveMessage(ProtocolDataUnit pdu) {
			if (pdu instanceof EchoProtocolDataUnit) { //argh, unschön: Generics bis zum Erbrechen?
				System.out.println("Server: Received request...");
				sendMessage(pdu);
			}
		}
		
	}
}
