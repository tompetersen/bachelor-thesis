package jProtocol.echoprotocol.model;

import jProtocol.Abstract.Model.State;
import jProtocol.Abstract.Model.StateMachine;

public class EchoServer extends StateMachine<EchoProtocolDataUnit> {

	public final Integer RECEIVE_STATE = 1; 
	
	public EchoServer() {
		addState(RECEIVE_STATE, new ReceiveState(this));               
		setState(RECEIVE_STATE);
	}
	
	private class ReceiveState extends State<EchoProtocolDataUnit> {

		public ReceiveState(EchoServer stateMachine) {
			super(stateMachine);
		}

		@Override
		public void receiveMessage(EchoProtocolDataUnit pdu) {
				System.out.println("Server: Received request...");
				sendMessage(pdu);
		}
	}
}
