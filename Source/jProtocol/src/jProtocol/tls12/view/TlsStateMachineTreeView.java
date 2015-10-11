package jProtocol.tls12.view;

import jProtocol.Abstract.View.HtmlInfoUpdater;
import jProtocol.Abstract.View.UiConstants;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.Abstract.View.keyvaluetree.KeyValueTree;
import jProtocol.helper.GridBagConstraintsHelper;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TlsStateMachineTreeView {

	private TlsStateMachine _stateMachine;
	private KeyValueTree _tree;
	private JPanel _view;
	private JTextField _statusView;
	
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
		
		_statusView = new JTextField("");
		_statusView.setEditable(false);
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 1, 1);
		_view.add(_statusView, constraints);
	}
	
	/**
	 * Updates the state machine tree and status field.
	 */
	public void updateView() {
		List<KeyValueObject> newUpdateList =  _stateMachine.getViewData();		
		_tree.updateKeyValueObjectList(newUpdateList);
		
		TlsStateType type = _stateMachine.getCurrentTlsState();
		if (type.isHandshakeState()) {
			_statusView.setText("Performing handshake");
			_statusView.setBackground(TlsUiConstants.STATUS_HANDSHAKE_BACKGROUND);
		}
		else if (type.isEstablishedState()) {
			_statusView.setText("Connection established");
			_statusView.setBackground(TlsUiConstants.STATUS_ESTABLISHED_BACKGROUND);
		}
		else if (type.isCloseState()) {
			_statusView.setText("Closing connection");
			_statusView.setBackground(TlsUiConstants.STATUS_CLOSE_BACKGROUND);
		}
		else if (type.isErrorState()) {
			_statusView.setText("Error occured");
			_statusView.setBackground(TlsUiConstants.STATUS_ERROR_BACKGROUND);
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