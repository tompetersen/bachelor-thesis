package jProtocol.tls12.view;

import jProtocol.Abstract.View.HtmlInfoUpdater;
import jProtocol.Abstract.View.UiConstants;
import jProtocol.Abstract.View.resources.ImageLoader;
import jProtocol.helper.ButtonHelper;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsApplicationData;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TlsClientView {

	private TlsStateMachine _clientStateMachine;
	private TlsStateMachineTreeView _treeView;
	private JPanel _view;
	
	private final JButton _connectButton;
	private final JButton _sendButton;
	private final JButton _closeButton;
	
	private boolean _isSending = false;
	
	/**
	 * A view for the TLS client state machine.
	 * 
	 * @param client the client
	 * @param htmlInfoUpdater an info updater to set the info view content
	 */
	public TlsClientView(final TlsStateMachine client, HtmlInfoUpdater htmlInfoUpdater) {
		_clientStateMachine = client;
		
		_view = new JPanel();
		_view.setLayout(new BorderLayout(0, 10));
		_view.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		_view.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UiConstants.VIEW_BORDER_COLOR));
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headerPanel.add(new JLabel(new ImageIcon(ImageLoader.getClientIcon(32, 32))));
		JLabel headerLabel = new JLabel("Client");
		headerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		headerPanel.add(headerLabel);
		headerPanel.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		_view.add(headerPanel, BorderLayout.NORTH);
		
		_treeView = new TlsStateMachineTreeView(client, htmlInfoUpdater, "Client");
		_view.add(_treeView.getView(), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		GridLayout layout = new GridLayout(3, 1);
		layout.setVgap(5);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPanel.setLayout(layout);
		buttonPanel.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		
		final int buttonImageSize = UiConstants.BUTTON_IMAGE_SIZE;
		
		_connectButton = ButtonHelper.createImageButton("Connect", 
				new ImageIcon(ImageLoader.getConnectIcon(buttonImageSize, buttonImageSize)), 
				new ActionListener() {
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
		buttonPanel.add(_connectButton);
		
		_sendButton = ButtonHelper.createImageButton("Send data", 
				new ImageIcon(ImageLoader.getSendIcon(buttonImageSize, buttonImageSize)),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								_isSending = true;
								setButtonEnabledValues(false, false, false);
								client.sendData(new TlsApplicationData("Daten, Daten, Daten!".getBytes(StandardCharsets.US_ASCII)));
								_isSending = false;
								setButtonEnabledValues(false, true, true);
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
								client.closeConnection();
							}
						}).start();
					}
				});
		_closeButton.setEnabled(false);
		buttonPanel.add(_closeButton);
		buttonPanel.setAlignmentX(SwingConstants.LEFT);
		
		_view.add(buttonPanel, BorderLayout.SOUTH);
		
		updateView();
	}
	
	/**
	 * Updates the client state machine view with the current client state.
	 */
	public void updateView() {
		_treeView.updateView();
		
		TlsStateType type = _clientStateMachine.getCurrentTlsState();
		if (type.isInitialState()) {
			setButtonEnabledValues(true, false, false);
		}
		else if (type.isEstablishedState() && !_isSending) {
			setButtonEnabledValues(false, true, true);
		}
		else {
			setButtonEnabledValues(false, false, false);
		}
	}
	
	private void setButtonEnabledValues(boolean connect, boolean send, boolean close) {
		_connectButton.setEnabled(connect);
		_sendButton.setEnabled(send);
		_closeButton.setEnabled(close);
	}
	
	/**
	 * Returns the client state machine view.
	 * 
	 * @return the view
	 */
	public JComponent getView() {
		return _view;
	}

}
