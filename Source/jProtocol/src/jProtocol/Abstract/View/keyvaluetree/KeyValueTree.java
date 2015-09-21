package jProtocol.Abstract.View.keyvaluetree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class KeyValueTree implements TreeSelectionListener {

	@SuppressWarnings("serial")
	private class KeyValueCellRenderer extends DefaultTreeCellRenderer {
		JLabel keyLabel = new JLabel(" ");
		JLabel valueLabel = new JLabel(" ");
		JPanel renderer = new JPanel();
		JButton infoButton = new JButton("i");

		DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
		Color backgroundSelectionColor;
		Color backgroundNonSelectionColor;

		public KeyValueCellRenderer() {
			renderer.add(keyLabel);
			keyLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
			keyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

			renderer.add(infoButton);
			infoButton.setVisible(true);

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
					final KeyValueObject kvo = (KeyValueObject) userObject;
					keyLabel.setText(kvo.getKey());
					valueLabel.setText(kvo.getValue());

					boolean showInfoButton = kvo.getHtmlHelpContent() != null;
					infoButton.setVisible(showInfoButton);

					int neededWidth = keyLabel.getPreferredSize().width + valueLabel.getPreferredSize().width + infoButton.getPreferredSize().width + 10;
					renderer.setPreferredSize(new Dimension(neededWidth, 20));

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
		_tree.addTreeSelectionListener(this);

		KeyValueCellRenderer renderer = new KeyValueCellRenderer();
		// renderer.setClosedIcon(newIcon);
		// renderer.setOpenIcon(newIcon);
		_tree.setCellRenderer(renderer);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)_tree.getLastSelectedPathComponent();

		if (node != null) {
			Object nodeInfo = node.getUserObject();
			
			if (nodeInfo instanceof KeyValueObject) {
				KeyValueObject kvo = (KeyValueObject)nodeInfo;
				String html = kvo.getHtmlHelpContent();
				if (html != null) {
					openHelpWindow(html);
				}
			}
		}
	}

	public void openHelpWindow(String htmlContent) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(htmlContent);
		frame.add(label);
		frame.setVisible(true);
	}

	public void setKeyValueObjectList(List<KeyValueObject> kvoList) {
		removeAllNodes();

		if (_lastUpdateList != null && _highlightChangedFields) {
			setCorrectComparisonDependentColorForList(kvoList, _lastUpdateList);
		}

		for (KeyValueObject kvo : kvoList) {
			addKeyValueObjectToNode(kvo, _rootNode);
		}

		_lastUpdateList = kvoList;
		updateTree();
	}

	private void setCorrectComparisonDependentColorForObject(KeyValueObject newObj, KeyValueObject oldObj) {
		if (!newObj.equals(oldObj)) {
			newObj.setBackgroundColor(Color.YELLOW);
		}

		if (newObj.hasChildren()) {
			List<KeyValueObject> newChildren = newObj.getChildList();
			List<KeyValueObject> oldChildren = oldObj.getChildList();

			setCorrectComparisonDependentColorForList(newChildren, oldChildren);
		}
	}

	private void setCorrectComparisonDependentColorForList(List<KeyValueObject> newObjList, List<KeyValueObject> oldObjList) {
		if (newObjList != null) {
			if (oldObjList != null) {
				for (int i = 0; i < Math.max(newObjList.size(), oldObjList.size()); i++) {
					KeyValueObject newChildObj = (i < newObjList.size()) ? newObjList.get(i) : null;
					KeyValueObject oldChildObj = (i < oldObjList.size()) ? oldObjList.get(i) : null;

					if (newChildObj != null) {
						if (oldChildObj != null) {
							setCorrectComparisonDependentColorForObject(newChildObj, oldChildObj);
						}
						else {
							colorNodeRecursively(newChildObj);
						}
					}
				}
			}
			else {
				for (KeyValueObject newChild : newObjList) {
					colorNodeRecursively(newChild);
				}
			}
		}
	}

	private void colorNodeRecursively(KeyValueObject kvo) {
		kvo.setBackgroundColor(Color.YELLOW);

		if (kvo.hasChildren()) {
			for (KeyValueObject child : kvo.getChildList()) {
				colorNodeRecursively(child);
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
