package jProtocol.SimpleEchoProtocol;


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
