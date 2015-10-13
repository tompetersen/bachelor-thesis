package jProtocol.echoprotocol;

import jProtocol.Abstract.StateMachineProvider;
import jProtocol.Abstract.ViewProvider;
import jProtocol.Abstract.Model.StateMachine;
import jProtocol.Abstract.View.HtmlInfoUpdater;
import jProtocol.echoprotocol.model.EchoClient;
import jProtocol.echoprotocol.model.EchoMessage;
import jProtocol.echoprotocol.model.EchoServer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class EchoProvider implements ViewProvider<EchoMessage>, StateMachineProvider<EchoMessage> {

	private EchoClient _client;
	private JTextArea _clientTextArea;
	
	@Override
	public StateMachine<EchoMessage> getServerStateMachine() {
		return new EchoServer();
	}

	@Override
	public StateMachine<EchoMessage> getClientStateMachine() {
		_client = new EchoClient(); 
		return _client;
	}

	@Override
	public JComponent getDetailedViewForProtocolDataUnit(EchoMessage pdu, HtmlInfoUpdater htmlInfoUpdater) {
		return new JLabel(pdu.getPayload());
	}

	@Override
	public JComponent getViewForClientStateMachine(HtmlInfoUpdater htmlInfoUpdater) {
		JPanel clientPanel = new JPanel(new BorderLayout());
		_clientTextArea = new JTextArea();
		JButton sendButton = new JButton("Send...");
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						_client.sendEchoRequest("DATEN!");
					}
				}).start();
			}
		});
		
		clientPanel.add(_clientTextArea, BorderLayout.CENTER);
		clientPanel.add(sendButton, BorderLayout.SOUTH);
		
		return clientPanel;
	}

	@Override
	public JComponent getViewForServerStateMachine(HtmlInfoUpdater htmlInfoUpdater) {
		return new JPanel();
	}

	@Override
	public void updateServerView() {
		// Nothing to do here
	}

	@Override
	public void updateClientView() {
		_clientTextArea.setText("");
		for (String text : _client.getReceivedMessages()) {
			_clientTextArea.append(text + "\n");
		}
	}

	@Override
	public JComponent getSettingsView() {
		// Nothing to show here
		return new JLabel("No settings for EchoProtocol!");
	}

	@Override
	public String getHtmlAboutContent() {
		return "<html><head></head><body><h1>Echo Protocol</h1><p>An example plugin!</p></body></html>";
	}

}
