package jProtocol.Abstract.View;

import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.helper.ByteHelper;
import jProtocol.helper.GridBagConstraintsHelper;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

public class ProtocolDataUnitView<T extends ProtocolDataUnit> {

	private class PduTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -8649020330505765296L;
		
		@Override
		public String getColumnName(int column) {
			return "Message";
		}

		@Override
		public int getRowCount() {
			return _pdus.size();
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			T pdu = _pdus.get(rowIndex);
			return (pdu.hasBeenSentByClient() ? "-> " : "<- ") + pdu.toString();
			
		}
	}

	private JPanel _view;
	private JPanel _pduView;
	private JLabel _pduBytesLabel;
	private PduTableModel _tableModel;
	private List<T> _pdus = new ArrayList<T>();
	private JProtocolViewProvider<T> _provider;

	public ProtocolDataUnitView(JProtocolViewProvider<T> provider) {
		_provider = provider;
		
		_view = new JPanel(new BorderLayout());

		createPduList();
		createPduDetailView();
	}
	
	private void createPduList() {
		_tableModel = new PduTableModel();
		final JTable table = new JTable(_tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setPreferredScrollableViewportSize(new Dimension(200,200));
		table.setPreferredSize(new Dimension(200, 200));
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent lse) {
		    	int row = table.getSelectedRow();
				if (row > -1 && row < _pdus.size()) {
					T pdu = _pdus.get(row);
					
					JComponent newPduView = _provider.getDetailedViewForProtocolDataUnit(pdu);
					setPduView(newPduView);
					
					//TODO:
					String bytes = ByteHelper.bytesToHexString(pdu.getBytes());
					int length = Math.min(bytes.length(), 20);
					_pduBytesLabel.setText(bytes.substring(0, length) + "...");
					
				}
		    }
		});
		
		//table.setPreferredScrollableViewportSize(new Dimension(500, 200));
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollPane.setMinimumSize(new Dimension(100, 100));
		//scrollPane.setMaximumSize(new Dimension(100, 100));
		
		table.setFillsViewportHeight(true);
		_view.add(scrollPane, BorderLayout.CENTER);
	}
	
	private void createPduDetailView() {
		_pduView = new JPanel();
		
		_pduBytesLabel = new JLabel();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Bytes", _pduBytesLabel);
		tabbedPane.addTab("Details", _pduView);
		
		_pduView.add(new JLabel("-"));
		_view.add(tabbedPane, BorderLayout.PAGE_END);
	}
	
	private void setPduView(JComponent pduView) {
		_pduView.removeAll();
		_pduView.add(pduView);
		
		_pduView.revalidate();
		_pduView.repaint();
	}

	public void setProtocolDataUnits(List<T> pdus) {
		_pdus = pdus;
		_tableModel.fireTableDataChanged();
	}

	public JComponent getView() {
		return _view;
	}
}
