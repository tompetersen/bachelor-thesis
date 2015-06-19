package jProtocol.SimpleEchoProtocol;

import jProtocol.CommunicationChannel;

public class EchoStartup {

	public static void main(String[] args) {
		CommunicationChannel channel = new CommunicationChannel();
		
		EchoServer server = new EchoServer(channel);
		EchoClient client = new EchoClient(channel);
		
		channel.setServer(server);
		channel.setClient(client);
		
		client.sendEchoRequest("Grml");
	}

}
