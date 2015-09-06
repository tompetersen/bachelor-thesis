package jProtocol.tls12.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

	public static class KeyValueObject {
		private String _key;
		private String _value;
		private Color _backgroundColor;
		private List<KeyValueObject> _kvoList;

		public KeyValueObject(String key, String value) {
			super();
			this._key = key;
			this._value = value;
		}

		public KeyValueObject(String key, String value, Color backgroundColor) {
			this(key, value);
			this._backgroundColor = backgroundColor;
		}

		public KeyValueObject(String key, List<KeyValueObject> valueList) {
			this._key = key;
			this._kvoList = valueList;
		}

		public KeyValueObject(String key, List<KeyValueObject> valueList, Color backgroundColor) {
			this._key = key;
			this._kvoList = valueList;
			this._backgroundColor = backgroundColor;
		}

		public boolean hasChildren() {
			return (_kvoList != null && _kvoList.size() > 0);
		}
	}

	@SuppressWarnings("serial")
	private class KeyValueCellRenderer extends DefaultTreeCellRenderer { // implements
																	// TreeCellRenderer
																	// {
		JLabel keyLabel = new JLabel(" ");
		JLabel valueLabel = new JLabel(" ");
		JPanel renderer = new JPanel(); //new FlowLayout(FlowLayout.LEFT, 0, 0)
		
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

			backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
			backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			Component returnValue = null;

			if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
				Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
				if (userObject instanceof KeyValueObject) {
					KeyValueObject kvo = (KeyValueObject) userObject;
					keyLabel.setText(kvo._key);
					valueLabel.setText(kvo._value);
					
					int neededWidth = keyLabel.getPreferredSize().width + valueLabel.getPreferredSize().width + 10;
					renderer.setSize(new Dimension(neededWidth, 15));

					if (selected) {
						renderer.setBackground(backgroundSelectionColor);
					}
					else if (kvo._backgroundColor != null) {
						renderer.setBackground(kvo._backgroundColor);
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

	public KeyValueTree(String title) {
		_rootNode = new DefaultMutableTreeNode(title);
		
		_tree = new JTree(_rootNode);
		_tree.putClientProperty("JTree.lineStyle", "None");
		_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		KeyValueCellRenderer renderer = new KeyValueCellRenderer();
		// renderer.setClosedIcon(newIcon);
		// renderer.setOpenIcon(newIcon);
		_tree.setCellRenderer(renderer);
	}

	public void addKeyValueObjectNode(KeyValueObject kvo) {
		addKeyValueObjectToNode(kvo, _rootNode);
	}

	private void addKeyValueObjectToNode(KeyValueObject kvo, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(kvo);

		if (kvo.hasChildren()) {
			for (KeyValueObject kvoChild : kvo._kvoList) {
				addKeyValueObjectToNode(kvoChild, newNode);
			}
		}
		
		node.add(newNode);
	}

	public void removeAllNodes() {
		_rootNode.removeAllChildren();
	}

	public JComponent getView() {
		JScrollPane pane = new JScrollPane(_tree);
		pane.setBorder(BorderFactory.createLineBorder(Color.RED));
		return _tree;
	}
	
	public void updateTree() {
		DefaultTreeModel model = (DefaultTreeModel) _tree.getModel();
		model.reload();
		
		for (int i = 0; i < _tree.getRowCount(); i++) {
			_tree.expandRow(i);
		}
	}
}
