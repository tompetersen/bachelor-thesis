package jProtocol.Abstract.View.keyvaluetree;

import jProtocol.Abstract.View.HtmlInfoUpdater;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
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

					boolean showInfoButton = kvo.getHtmlInfoContent() != null;
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
	private HtmlInfoUpdater _htmlInfoUpdater;

	/**
	 * Creates a key value tree. If highlightChangedFields is true, then key value objects 
	 * with changed values will be highlighted when the tree is updated.
	 * 
	 * @param title the title used for the tree root
	 * @param htmlInfoUpdater a info updater to set the content of the info view
	 * @param highlightChangedFields true, if changed fields should be highlighted during tree update
	 */
	public KeyValueTree(String title, HtmlInfoUpdater htmlInfoUpdater, boolean highlightChangedFields) {
		_rootNode = new DefaultMutableTreeNode(title);
		_htmlInfoUpdater = htmlInfoUpdater;
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
				String html = kvo.getHtmlInfoContent();
				if (html != null && _htmlInfoUpdater != null) {
					_htmlInfoUpdater.setInfoHtmlBodyContent(html);
				}
			}
		}
	}

	/**
	 * Updates the trees key value objects. If higlightChangedFields is true, the changed 
	 * objects will be highlighted.
	 * Expanded key value objects will stay expanded.
	 * 
	 * @param kvoList the new key value object list
	 */
	public void updateKeyValueObjectList(List<KeyValueObject> kvoList) {
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

	/**
	 * Returns the tree view with an enclosing JScrollPane.
	 * 
	 * @return the tree view
	 */
	public JComponent getView() {
		JScrollPane pane = new JScrollPane(_tree);
		pane.setBorder(BorderFactory.createLineBorder(Color.RED));
		return _tree;
	}

	/**
	 * Expands all key value objects with children.
	 */
	public void expandAll() {
		for (int i = 0; i < _tree.getRowCount(); i++) {
			_tree.expandRow(i);
		}
	}
}
