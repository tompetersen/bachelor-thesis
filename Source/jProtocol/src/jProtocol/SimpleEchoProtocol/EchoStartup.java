package jProtocol.SimpleEchoProtocol;

import jProtocol.SimpleEchoProtocol.Model.EchoClient;
import jProtocol.SimpleEchoProtocol.Model.EchoCommunicationChannel;
import jProtocol.SimpleEchoProtocol.Model.EchoServer;


public class EchoStartup {

	public static void main(String[] args) {
		EchoServer server = new EchoServer();
		EchoClient client = new EchoClient();
		
		EchoCommunicationChannel channel = new EchoCommunicationChannel(client, server);
		
		server.setCommunicationChannel(channel);
		client.setCommunicationChannel(channel);
		
		client.sendEchoRequest("Grml");
	}
}
