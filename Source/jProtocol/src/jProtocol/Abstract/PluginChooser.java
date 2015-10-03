package jProtocol.Abstract;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.helper.GridBagConstraintsHelper;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PluginChooser {

	private ProtocolRegistry _protocolRegistry;
	private JDialog _pluginChooser;
	private String _currentProtocolName;
	

	public PluginChooser(ProtocolRegistry registry, JFrame parentFrame) {
		_protocolRegistry = registry;
		
		_pluginChooser = new JDialog(parentFrame, "Start Protocol");
		_pluginChooser.setSize(300, 300);
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
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel selectionModel = table.getSelectionModel();

		selectionModel.addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		        int row = e.getFirstIndex();
		        _currentProtocolName = (String)data[row][0];
		    }
		});
		
		JButton chooseButton = new JButton("Start");
		chooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_pluginChooser.setVisible(false);
			}
		});
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(0, 0, 1);
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		panel.add(table, constraints);

		JPanel settings = new JPanel();
		settings.setBackground(Color.WHITE);
		constraints = GridBagConstraintsHelper.createNormalConstraints(1, 0, 1);
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		panel.add(settings, constraints);
		
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 1, 2);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0;
		panel.add(chooseButton, constraints);
		
		_pluginChooser.add(panel);
	}
	
	public void showChooser() {
		_pluginChooser.setVisible(true);
	}
	
	public JProtocolBuilder<? extends ProtocolDataUnit> getSelectedProtocol() {
		return _protocolRegistry.getProtocolBuilderForName(_currentProtocolName);
	}

}
