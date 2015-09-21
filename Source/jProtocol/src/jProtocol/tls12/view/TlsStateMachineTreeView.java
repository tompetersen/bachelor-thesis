package jProtocol.tls12.view;

import jProtocol.Abstract.View.HtmlInfoUpdater;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.Abstract.View.keyvaluetree.KeyValueTree;
import jProtocol.helper.GridBagConstraintsHelper;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import java.awt.Color;
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
	private JTextField _protocolStateView;
	private JTextField _statusView;
	
	public TlsStateMachineTreeView(TlsStateMachine stateMachine, HtmlInfoUpdater infoUpdater, String title) {
		_stateMachine = stateMachine;
		
		_view = new JPanel();
		_view.setLayout(new GridBagLayout());
		
		_tree = new KeyValueTree(title, infoUpdater, true);
		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(0, 0, 1);
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		JScrollPane pane = new JScrollPane(_tree.getView());
		_view.add(pane, constraints);
		
		_statusView = new JTextField("");
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 1, 1);
		_view.add(_statusView, constraints);
		
		_protocolStateView = new JTextField("Computed master secret!");
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 2, 1);
		_view.add(_protocolStateView, constraints);
	}
	
	public void updateView() {
		List<KeyValueObject> newUpdateList =  _stateMachine.getViewData();		
		_tree.setKeyValueObjectList(newUpdateList);
		
		TlsStateType type = _stateMachine.getCurrentTlsState();
		if (type.isHandshakeState()) {
			_statusView.setText("Performing handshake");
			_statusView.setBackground(Color.YELLOW);
		}
		else if (type.isEstablishedState()) {
			_statusView.setText("Connection established");
			_statusView.setBackground(Color.GREEN);
		}
		else if (type.isCloseState()) {
			_statusView.setText("Closing connection");
			_statusView.setBackground(Color.GRAY);
		}
		else if (type.isErrorState()) {
			_statusView.setText("Error occured");
			_statusView.setBackground(Color.RED);
		}
	}
	
	public JComponent getView() {
		return _view;
	}	
}