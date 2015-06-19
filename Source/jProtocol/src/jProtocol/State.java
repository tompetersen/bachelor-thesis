package jProtocol;

public abstract class State<T extends ProtocolDataUnit> {
	
	private StateMachine<T> _stateMachine;
	
	public State(StateMachine<T> stateMachine) {
		_stateMachine = stateMachine;
	}
	
	public void sendMessage(T pdu) {
		_stateMachine.sendMessage(pdu);
	}
	
	public abstract void receiveMessage(T pdu);
	
}
