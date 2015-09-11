package jProtocol.tls12.view;

import jProtocol.Abstract.View.KeyValueObject;
import jProtocol.Abstract.View.KeyValueTree;
import jProtocol.tls12.model.states.TlsStateMachine;
import java.awt.Color;
import java.util.List;
import javax.swing.JComponent;

public class TlsStateMachineTreeView {

	private TlsStateMachine _stateMachine;
	private KeyValueTree _tree;
	private List<KeyValueObject> _lastUpdateList;
	
	public TlsStateMachineTreeView(TlsStateMachine stateMachine, String title) {
		_stateMachine = stateMachine;
		
		_tree = new KeyValueTree(title);
	}
	
	public void updateView() {
		_tree.removeAllNodes();
		
		List<KeyValueObject> newUpdateList =  _stateMachine.getViewData();
		
		if (_lastUpdateList != null) {
			for (int i = 0; i < Math.min(newUpdateList.size(), _lastUpdateList.size()); i++) {
				KeyValueObject newObj = newUpdateList.get(i);
				KeyValueObject oldObj = _lastUpdateList.get(i);
				
				setCorrectComparisonDependentColor(newObj, oldObj);
			}
		}
		
		for (KeyValueObject kvo : newUpdateList) {
			_tree.addKeyValueObjectNode(kvo);
		}
		
		_lastUpdateList = newUpdateList;
		_tree.updateTree();
	}
	
	private void setCorrectComparisonDependentColor(KeyValueObject newObj, KeyValueObject oldObj) {
		if (!newObj.equals(oldObj)) {
			newObj.setBackgroundColor(Color.YELLOW);
		}
		
		if (newObj.hasChildren()) {
			List<KeyValueObject> newChildren = newObj.getChildList();
			List<KeyValueObject> oldChildren = oldObj.getChildList();
			if (newChildren != null && oldChildren != null) {
				for (int i = 0; i < Math.min(newChildren.size(), oldChildren.size()); i++) {
					KeyValueObject newChildObj = newChildren.get(i);
					KeyValueObject oldChildObj = oldChildren.get(i);
					
					setCorrectComparisonDependentColor(newChildObj, oldChildObj);
				}
			}
		}
	}
	
	public JComponent getView() {
		return _tree.getView();
	}

}
