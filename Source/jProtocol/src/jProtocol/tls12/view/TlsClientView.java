package jProtocol.tls12.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jProtocol.tls12.model.states.TlsStateMachine;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TlsClientView {

	private TlsStateMachine _client;
	private TlsStateMachineTreeView _treeView;
	private JPanel _view;
	
	public TlsClientView(final TlsStateMachine client) {
		_client = client;
		
		_view = new JPanel();
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		_treeView = new TlsStateMachineTreeView(client, "Client");
		JScrollPane pane = new JScrollPane( _treeView.getView());
		_view.add(pane);
		
		JButton connectButton = new JButton("Connect...");
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						client.openConnection();
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
