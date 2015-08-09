package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.State;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.messages.TlsMessage;

public abstract class TlsState extends State<TlsCiphertext> {

	public TlsState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receiveMessage(TlsCiphertext pdu) {
		//TODO: Hier encrypten lassen? Oder in TlsStateMachine?
	}

	public abstract void receivedTlsMessage(TlsMessage message);
	
}
