package jProtocol.tls12.view;

import jProtocol.Abstract.View.HtmlInfoUpdater;
import jProtocol.Abstract.View.UiConstants;
import jProtocol.Abstract.View.resources.ImageLoader;
import jProtocol.helper.ButtonHelper;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsApplicationData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class TlsServerView {

	private TlsStateMachine _serverStateMachine;
	private TlsStateMachineTreeView _treeView;
	private JPanel _view;
	
	private JButton _sendButton;
	private JButton _closeButton;
	
	/**
	 * A view for the TLS server state machine.
	 * 
	 * @param server the server
	 * @param htmlInfoUpdater an info updater to set the info view content
	 */
	public TlsServerView(final TlsStateMachine server, HtmlInfoUpdater htmlInfoUpdater) {
		_serverStateMachine = server;
		
		_view = new JPanel();
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		
		_treeView = new TlsStateMachineTreeView(server, htmlInfoUpdater, "Server");
		_view.add(_treeView.getView());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		final int buttonImageSize = UiConstants.BUTTON_IMAGE_SIZE;
		
		_sendButton = ButtonHelper.createImageButton("Send data", 
				new ImageIcon(ImageLoader.getSendIcon(buttonImageSize, buttonImageSize)),
				new ActionListener() {
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
		_sendButton.setEnabled(false);
		buttonPanel.add(_sendButton);
		
		_closeButton = ButtonHelper.createImageButton("Close", 
				new ImageIcon(ImageLoader.getCloseIcon(buttonImageSize, buttonImageSize)),
				new ActionListener() {
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
		_closeButton.setEnabled(false);
		buttonPanel.add(_closeButton);
		
		_view.add(buttonPanel);
	}
	
	/**
	 * Updates the server state machine view with the current server state.
	 */
	public void updateView() {
		_treeView.updateView();
		
		TlsStateType type = _serverStateMachine.getCurrentTlsState();
		if (type.isEstablishedState()) {
			setButtonEnabledValues(true, true);
		}
		else {
			setButtonEnabledValues(false, false);
		}
	}
	
	private void setButtonEnabledValues(boolean send, boolean close) {
		_sendButton.setEnabled(send);
		_closeButton.setEnabled(close);
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
