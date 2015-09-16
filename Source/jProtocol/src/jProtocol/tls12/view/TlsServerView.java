package jProtocol.tls12.view;

import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.values.TlsApplicationData;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class TlsServerView {

	private TlsStateMachine _server;
	private TlsStateMachineTreeView _treeView;
	private JPanel _view;
	
	public TlsServerView(final TlsStateMachine server) {
		_server = server;
		
		_view = new JPanel();
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		
		_treeView = new TlsStateMachineTreeView(server, "Server");
		_view.add(_treeView.getView());
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton button = new JButton("Send");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						server.sendData(new TlsApplicationData("Daten, Daten, Daten!".getBytes(StandardCharsets.US_ASCII)));
					}
				}).start();
			}
		});
		buttonPanel.add(button);
		
		button = new JButton("Close");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						server.closeConnection();
					}
				}).start();
			}
		});
		buttonPanel.add(button);
		
		_view.add(buttonPanel);
	}
	
	public void updateView() {
		_treeView.updateView();
	}
	
	public JComponent getView() {
		return _view;
	}
	
}
