package jProtocol.tls12.view;

import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.view.KeyValueTree.KeyValueObject;
import javax.swing.JComponent;

public class TlsStateMachineTreeView {

	private TlsStateMachine _stateMachine;
	private KeyValueTree _tree;
	
	public TlsStateMachineTreeView(TlsStateMachine stateMachine, String title) {
		_stateMachine = stateMachine;
		
		_tree = new KeyValueTree(title);
	}
	
	public void updateView() {
		_tree.removeAllNodes();
		
		for (KeyValueObject kvo : _stateMachine.getViewData()) {
			_tree.addKeyValueObjectNode(kvo);
		}
		
		_tree.updateTree();
	}
	
	public JComponent getView() {
		return _tree.getView();
	}

}
