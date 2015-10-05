package jProtocol.Abstract.Model;

public abstract class State<T extends ProtocolDataUnit> {

	private StateMachine<T> _stateMachine;

	/**
	 * Creates a state for the state machine.
	 * 
	 * @param stateMachine the state machine
	 */
	public State(StateMachine<T> stateMachine) {
		_stateMachine = stateMachine;
	}

	/**
	 * Sends a protocol data unit from the state machine.
	 * 
	 * @param pdu the protocol data unit
	 */
	public void sendMessage(T pdu) {
		_stateMachine.sendMessage(pdu);
	}

	/**
	 * Sets the current state in the state machine this state belongs to. Must
	 * not be called if this state is not the current state.
	 * 
	 * @param stateIdentifier the state identifier for the new state
	 */
	public void setState(int stateIdentifier) {
		_stateMachine.setState(stateIdentifier, this);
	}

	/**
	 * Gets called when this state is the current state of the state machine and
	 * a protocol data unit is received.
	 * 
	 * @param pdu the protocol data unit
	 */
	public abstract void receiveMessage(T pdu);

	/**
	 * Gets called when this state becomes the current state of a state machine.
	 */
	public void onEnter() {
	};

	/**
	 * Gets called when this state was the current state of a state machine but
	 * the current state is changed.
	 */
	public void onLeave() {
	};

}
