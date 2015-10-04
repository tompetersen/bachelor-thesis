package jProtocol.echoprotocol.model;

import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.StateMachine;

public class EchoCommunicationChannel extends CommunicationChannel<EchoProtocolDataUnit> {

	public EchoCommunicationChannel(StateMachine<EchoProtocolDataUnit> client, StateMachine<EchoProtocolDataUnit> server) {
		super(client, server);
	}

}
