package jProtocol.Abstract.View.keyvaluetree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class KeyValueTree {

	@SuppressWarnings("serial")
	private class KeyValueCellRenderer extends DefaultTreeCellRenderer {
		JLabel keyLabel = new JLabel(" ");
		JLabel valueLabel = new JLabel(" ");
		JPanel renderer = new JPanel();

		DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
		Color backgroundSelectionColor;
		Color backgroundNonSelectionColor;

		public KeyValueCellRenderer() {
			renderer.add(keyLabel);
			keyLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

			renderer.add(valueLabel);
			valueLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
			valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

			renderer.setLayout(new BoxLayout(renderer, BoxLayout.X_AXIS));
			// renderer.setBorder(BorderFactory.createLineBorder(Color.RED));

			backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
			backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			Component returnValue = null;

			if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
				Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
				if (userObject instanceof KeyValueObject) {
					KeyValueObject kvo = (KeyValueObject) userObject;
					keyLabel.setText(kvo.getKey());
					valueLabel.setText(kvo.getValue());

					int neededWidth = keyLabel.getPreferredSize().width + valueLabel.getPreferredSize().width + 10;
					renderer.setPreferredSize(new Dimension(neededWidth, 15));

					if (selected) {
						renderer.setBackground(backgroundSelectionColor);
					}
					else if (kvo.getBackgroundColor() != null) {
						renderer.setBackground(kvo.getBackgroundColor());
					}
					else {
						renderer.setBackground(backgroundNonSelectionColor);
					}

					renderer.setEnabled(tree.isEnabled());
					returnValue = renderer;
				}
				else if (!leaf) {
					String title = (String) userObject;
					JLabel label = new JLabel(title);
					label.setFont(new Font("SansSerif", Font.PLAIN, 12));
					returnValue = label;
				}
			}

			if (returnValue == null) {
				returnValue = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			}

			return returnValue;
		}
	}

	private JTree _tree;
	private DefaultMutableTreeNode _rootNode;
	private List<KeyValueObject> _lastUpdateList;
	private boolean _highlightChangedFields;

	public KeyValueTree(String title, boolean highlightChangedFields) {
		_rootNode = new DefaultMutableTreeNode(title);
		_highlightChangedFields = highlightChangedFields;

		_tree = new JTree(_rootNode);
		_tree.putClientProperty("JTree.lineStyle", "None");
		_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		KeyValueCellRenderer renderer = new KeyValueCellRenderer();
		// renderer.setClosedIcon(newIcon);
		// renderer.setOpenIcon(newIcon);
		_tree.setCellRenderer(renderer);
	}

	public void setKeyValueObjectList(List<KeyValueObject> kvoList) {
		removeAllNodes();

		if (_lastUpdateList != null && _highlightChangedFields) {
			for (int i = 0; i < Math.min(kvoList.size(), _lastUpdateList.size()); i++) {
				KeyValueObject newObj = kvoList.get(i);
				KeyValueObject oldObj = _lastUpdateList.get(i);

				setCorrectComparisonDependentColor(newObj, oldObj);
			}
		}

		for (KeyValueObject kvo : kvoList) {
			addKeyValueObjectToNode(kvo, _rootNode);
		}

		_lastUpdateList = kvoList;
		updateTree();
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
			else if (newChildren != null) {
				for (KeyValueObject newChild : newChildren) {
					// TODO: recursive for other protocols?
					newChild.setBackgroundColor(Color.YELLOW);
				}
			}
		}
	}

	private void addKeyValueObjectToNode(KeyValueObject kvo, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(kvo);

		if (kvo.hasChildren()) {
			for (KeyValueObject kvoChild : kvo.getChildList()) {
				addKeyValueObjectToNode(kvoChild, newNode);
			}
		}

		node.add(newNode);
	}

	private void removeAllNodes() {
		_rootNode.removeAllChildren();
	}

	private void updateTree() {
		boolean[] expanded = new boolean[_tree.getRowCount()];
		for (int i = 0; i < _tree.getRowCount(); i++) {
			expanded[i] = _tree.isExpanded(_tree.getPathForRow(i));
		}

		DefaultTreeModel model = (DefaultTreeModel) _tree.getModel();
		model.reload();

		for (int i = 0; i < _tree.getRowCount(); i++) {
			if (i < expanded.length && expanded[i]) {
				_tree.expandRow(i);
			}
		}
	}

	public JComponent getView() {
		JScrollPane pane = new JScrollPane(_tree);
		pane.setBorder(BorderFactory.createLineBorder(Color.RED));
		return _tree;
	}

	public void expandAll() {
		for (int i = 0; i < _tree.getRowCount(); i++) {
			_tree.expandRow(i);
		}
	}
}
