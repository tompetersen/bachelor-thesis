package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.StateMachine;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsConnectionState;
import jProtocol.tls12.model.TlsSecurityParameters;
import jProtocol.tls12.model.values.TlsConnectionEnd;

public class TlsStateMachine extends StateMachine<TlsCiphertext> {

	public static final int INITIAL_SERVER_STATE = 1; 
	
	private TlsSecurityParameters _parameters;
	private TlsConnectionState _connectionState;
	
	public TlsStateMachine(CommunicationChannel<TlsCiphertext> channel, TlsConnectionEnd entity) {
		super(channel);
		
		_parameters = new TlsSecurityParameters(entity);
		_connectionState = new TlsConnectionState(_parameters);
		
		createStates();
	}
	
	private void createStates() {
		//TODO: So oder anders?
		_states.put(INITIAL_SERVER_STATE, new InitialServerState(this));
	}

}
