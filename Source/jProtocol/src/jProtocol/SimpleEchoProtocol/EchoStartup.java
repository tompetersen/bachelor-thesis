package jProtocol.SimpleEchoProtocol;

import jProtocol.SimpleEchoProtocol.Model.EchoClient;
import jProtocol.SimpleEchoProtocol.Model.EchoCommunicationChannel;
import jProtocol.SimpleEchoProtocol.Model.EchoProtocolDataUnit;
import jProtocol.SimpleEchoProtocol.Model.EchoServer;
import jProtocol.SimpleEchoProtocol.View.EchoProtocolDataUnitPresenter;
import jProtocol.SimpleEchoProtocol.View.EchoProtocolDataUnitView;

import javax.swing.JFrame;


public class EchoStartup {

	public static void main(String[] args) {
		EchoCommunicationChannel channel = new EchoCommunicationChannel();
		
		EchoServer server = new EchoServer(channel);
		EchoClient client = new EchoClient(channel);
		
		channel.setServer(server);
		channel.setClient(client);
		
		client.sendEchoRequest("Grml");
		
		showPduView();
	}
	
	public static void showPduView() {
		EchoProtocolDataUnit pdu = new EchoProtocolDataUnit();
		pdu.setPayload("PAYLOAD");
		pdu.setVersion(1);
		EchoProtocolDataUnitView view = new EchoProtocolDataUnitView();
		EchoProtocolDataUnitPresenter pres = new EchoProtocolDataUnitPresenter(view);
		pres.setProtocolDataUnit(pdu);
		
		JFrame frame = new JFrame("EchoProtocol");
		frame.add(pres.getView());
		//frame.setSize(new Dimension(100, 100));
		frame.setVisible(true);
	}

}
