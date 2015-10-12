package jProtocol.tls12.view;

import jProtocol.Abstract.View.HtmlInfoUpdater;
import jProtocol.Abstract.View.UiConstants;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.Abstract.View.keyvaluetree.KeyValueTree;
import jProtocol.Abstract.View.resources.ImageLoader;
import jProtocol.helper.GridBagConstraintsHelper;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TlsStateMachineTreeView {

	private TlsStateMachine _stateMachine;
	private KeyValueTree _tree;
	private JPanel _view;
	private JLabel _statusText;
	private JPanel _statusPanel;
	private JLabel _statusImage;
	
	/**
	 * Creates a state machine state showing key value tree with a TLS status field.
	 * 
	 * @param stateMachine the state machine
	 * @param infoUpdater an info updater object
	 * @param title the title for the tree root
	 */
	public TlsStateMachineTreeView(TlsStateMachine stateMachine, HtmlInfoUpdater infoUpdater, String title) {
		_stateMachine = stateMachine;
		
		_view = new JPanel();
		_view.setLayout(new GridBagLayout());
		_view.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		
		_tree = new KeyValueTree(title, infoUpdater, true);
		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(0, 0, 1);
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		JScrollPane pane = new JScrollPane(_tree.getView());
		_view.add(pane, constraints);
		
		_statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
		_statusText = new JLabel("");
		_statusImage = new JLabel();
		_statusPanel.add(_statusImage);
		_statusPanel.add(_statusText);
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 1, 1);
		_view.add(_statusPanel, constraints);
	}
	
	/**
	 * Updates the state machine tree and status field.
	 */
	public void updateView() {
		List<KeyValueObject> newUpdateList =  _stateMachine.getViewData();		
		_tree.updateKeyValueObjectList(newUpdateList);
		
		int iconSize = 16;
		
		TlsStateType type = _stateMachine.getCurrentTlsState();
		if (type.isInitialState()) {
			_statusText.setText("Waiting for connection...");
			_statusPanel.setBackground(Color.WHITE);
		}
		else if (type.isHandshakeState()) {
			_statusText.setText("Performing handshake");
			_statusPanel.setBackground(TlsUiConstants.STATUS_HANDSHAKE_BACKGROUND);
			_statusImage.setIcon(new ImageIcon(ImageLoader.getHandshakeIcon(iconSize, iconSize)));
		}
		else if (type.isEstablishedState()) {
			_statusText.setText("Connection established");
			_statusPanel.setBackground(TlsUiConstants.STATUS_ESTABLISHED_BACKGROUND);
			_statusImage.setIcon(new ImageIcon(ImageLoader.getEstablishedIcon(iconSize, iconSize)));
		}
		else if (type.isCloseState()) {
			_statusText.setText("Closing connection");
			_statusPanel.setBackground(TlsUiConstants.STATUS_CLOSE_BACKGROUND);
			_statusImage.setIcon(new ImageIcon(ImageLoader.getCloseIcon(iconSize, iconSize)));
		}
		else if (type.isErrorState()) {
			_statusText.setText("Error occured");
			_statusPanel.setBackground(TlsUiConstants.STATUS_ERROR_BACKGROUND);
			_statusImage.setIcon(new ImageIcon(ImageLoader.getErrorIcon(iconSize, iconSize)));
		}
	}
	
	/**
	 * Returns the state machine tree view.
	 * 
	 * @return the view
	 */
	public JComponent getView() {
		return _view;
	}	
}