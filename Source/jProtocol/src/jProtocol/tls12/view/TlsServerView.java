package jProtocol.tls12.view;

import jProtocol.Abstract.View.HtmlInfoUpdater;
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

	private TlsStateMachineTreeView _treeView;
	private JPanel _view;
	
	/**
	 * A view for the TLS server state machine.
	 * 
	 * @param server the server
	 * @param htmlInfoUpdater an info updater to set the info view content
	 */
	public TlsServerView(final TlsStateMachine server, HtmlInfoUpdater htmlInfoUpdater) {
		_view = new JPanel();
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		
		_treeView = new TlsStateMachineTreeView(server, htmlInfoUpdater, "Server");
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
	
	/**
	 * Updates the server state machine view with the current server state.
	 */
	public void updateView() {
		_treeView.updateView();
	}
	
	/**
	 * Returns the server state machine view.
	 * 
	 * @return the view
	 */
	public JComponent getView() {
		return _view;
	}
	
}
