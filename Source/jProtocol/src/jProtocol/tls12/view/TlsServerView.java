package jProtocol.tls12.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jProtocol.tls12.model.states.TlsStateMachine;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TlsServerView {

	private TlsStateMachine _server;
	private TlsStateMachineTreeView _treeView;
	private JPanel _view;
	
	public TlsServerView(TlsStateMachine server) {
		_server = server;
		
		_view = new JPanel();
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		
		_treeView = new TlsStateMachineTreeView(server, "Server");
		_view.add(_treeView.getView());
		
		JButton connectButton = new JButton("Send...");
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						
					}
				}).start();
			}
		});
		
		_view.add(connectButton);
	}
	
	public void updateView() {
		_treeView.updateView();
	}
	
	public JComponent getView() {
		return _view;
	}
	
}
