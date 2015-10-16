package jProtocol.Abstract;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.View.UiConstants;
import jProtocol.Abstract.View.resources.ImageLoader;
import jProtocol.helper.GridBagConstraintsHelper;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PluginChooser {

	private ProtocolRegistry _protocolRegistry;
	private JDialog _pluginChooser;
	private JPanel _pluginChooserPanel;
	private String _currentProtocolName;
	private JComponent _currentSettingsView;

	public PluginChooser(ProtocolRegistry registry, JFrame parentFrame) {
		_protocolRegistry = registry;
		
		_pluginChooser = new JDialog(parentFrame, "Start Protocol");
		_pluginChooser.setSize(600, 400);
		_pluginChooser.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		_pluginChooser.setLocationRelativeTo(parentFrame);
		
		List<String> protocols = registry.getProtocolList();
		String[] columnNames = { "Protocol" };
		
		final Object[][] data = new Object[protocols.size()][1];
		int i = 0;
		for (String protocolName : protocols) {
			String[] dataField = new String[1];
			dataField[0] = protocolName;
			data[i] = dataField;
			i++;
		}

		JTable table = new JTable(data, columnNames);
		table.setPreferredSize(new Dimension(150, 150));
		table.setMaximumSize(new Dimension(150, 150));
		table.setMinimumSize(new Dimension(150, 150));
		table.setBorder(BorderFactory.createLineBorder(UiConstants.VIEW_BORDER_COLOR));
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		    	ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		    	int row = lsm.getMinSelectionIndex();
		        _currentProtocolName = (String)data[row][0];
		        
		        setSettingsViewForSelectedProtocol();
		    }
		});
		
		JButton chooseButton = new JButton("Start");
		chooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_pluginChooser.setVisible(false);
			}
		});
		
		_pluginChooserPanel = new JPanel(new GridBagLayout());
		_pluginChooserPanel.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		
		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(0, 0, 2);
		constraints.weighty = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.BOTH;
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		topPanel.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		JLabel iconLabel = new JLabel(new ImageIcon(ImageLoader.getApplicationIcon(UiConstants.APPLICATION_ICON_SIZE, UiConstants.APPLICATION_ICON_SIZE)));
		JLabel applicationTitle = new JLabel(UiConstants.APPLICATION_NAME);
		applicationTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		applicationTitle.setBorder(new EmptyBorder(0, 10, 0, 0));
		topPanel.add(iconLabel);
		topPanel.add(applicationTitle);
		_pluginChooserPanel.add(topPanel, constraints);
		
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 1, 1);
		constraints.weighty = 0;
		constraints.weightx = 0;
		constraints.fill = GridBagConstraints.BOTH;
		JLabel tableTitle = new JLabel("Available protocols");
		tableTitle.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		_pluginChooserPanel.add(tableTitle, constraints);
		
		constraints = GridBagConstraintsHelper.createNormalConstraints(1, 1, 1);
		constraints.weighty = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.BOTH;
		JLabel settingsTitle = new JLabel(""); //Protocol Settings");
		settingsTitle.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		_pluginChooserPanel.add(settingsTitle, constraints);
		
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 2, 1);
		constraints.weighty = 1;
		constraints.weightx = 0;
		constraints.fill = GridBagConstraints.BOTH;
		_pluginChooserPanel.add(table, constraints);

		_currentSettingsView = new JPanel();
		_currentSettingsView.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		_currentSettingsView.setBorder(BorderFactory.createLineBorder(UiConstants.VIEW_BORDER_COLOR));
		constraints = GridBagConstraintsHelper.createNormalConstraints(1, 2, 1);
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.BOTH;
		_pluginChooserPanel.add(_currentSettingsView, constraints);
		
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 3, 2);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0;
		_pluginChooserPanel.add(chooseButton, constraints);
		
		_pluginChooser.add(_pluginChooserPanel);
	}
	
	public void setSettingsViewForSelectedProtocol() {
		_pluginChooserPanel.remove(_currentSettingsView);
		
		_currentSettingsView = getSelectedProtocol().getSettingsView();
		_currentSettingsView.setBorder(BorderFactory.createCompoundBorder(new LineBorder(UiConstants.VIEW_BORDER_COLOR), new EmptyBorder(10, 10, 10, 10)));
		
		
		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(1, 2, 1);
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		_pluginChooserPanel.add(_currentSettingsView, constraints);
		
		_pluginChooserPanel.revalidate();
		_pluginChooserPanel.repaint();
	}
	
	public void showChooser() {
		_pluginChooser.setVisible(true);
	}
	
	public ProtocolBuilder<? extends ProtocolDataUnit> getSelectedProtocol() {
		return _protocolRegistry.getProtocolBuilderForName(_currentProtocolName);
	}

}
