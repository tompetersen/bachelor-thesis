package jProtocol.Abstract;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.Model.StateMachine;

/**
 * A state machine provider provides client and server state machines which will
 * handle the work to be done in the protocol.
 * 
 * @author Tom Petersen
 * 
 * @param <T> The protocol data unit the protocol uses.
 */
public interface StateMachineProvider<T extends ProtocolDataUnit> {

	/**
	 * Should return the server state machine. This method will be called once
	 * when the protocol starts.
	 * 
	 * @return the server state machine
	 */
	public StateMachine<T> getServerStateMachine();

	/**
	 * Should return the client state machine. This method will be called once
	 * when the protocol starts.
	 * 
	 * @return the client state machine
	 */
	public StateMachine<T> getClientStateMachine();
}
