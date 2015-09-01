package jProtocol.SimpleEchoProtocol;

import jProtocol.SimpleEchoProtocol.Model.EchoClient;
import jProtocol.SimpleEchoProtocol.Model.EchoCommunicationChannel;
import jProtocol.SimpleEchoProtocol.Model.EchoServer;


public class EchoStartup {

	public static void main(String[] args) {
		EchoCommunicationChannel channel = new EchoCommunicationChannel();
		
		EchoServer server = new EchoServer(channel);
		EchoClient client = new EchoClient(channel);
		
		channel.setServer(server);
		channel.setClient(client);
		
		client.sendEchoRequest("Grml");
	}
}
