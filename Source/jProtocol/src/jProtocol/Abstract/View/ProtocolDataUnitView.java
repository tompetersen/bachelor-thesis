package jProtocol.Abstract.View;

import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.helper.ByteHelper;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
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
		
		_view = new JPanel();
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		
		createPduList();
		createPduDetailView();
	}
	
	private void createPduList() {
		_tableModel = new PduTableModel();
		final JTable table = new JTable(_tableModel);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent lse) {
		    	int row = table.getSelectedRow();
				if (row > -1 && row < _pdus.size()) {
					T pdu = _pdus.get(row);
					setPduInView(pdu);
				}
		    }
		});
		
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setMinimumSize(new Dimension(300, 300));
		scrollPane.setMaximumSize(new Dimension(300, 300));
		
		_view.add(scrollPane, BorderLayout.CENTER);
	}
	
	private void setPduInView(T pdu) {
		JComponent newPduView = _provider.getDetailedViewForProtocolDataUnit(pdu);
		setPduDetailView(newPduView);
		
		String bytes = ByteHelper.bytesToHexString(pdu.getBytes());
		setByteTabValue(bytes);
	}
	
	private void createPduDetailView() {
		_pduView = new JPanel();
		
		_pduBytesLabel = new JLabel(" ");
		_pduBytesLabel.setVerticalAlignment(SwingConstants.TOP);
		_pduBytesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		_pduBytesLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scrollPane = new JScrollPane(_pduBytesLabel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setMinimumSize(new Dimension(300, 100));
		tabbedPane.setMaximumSize(new Dimension(300, 100));
		
		tabbedPane.addTab("Bytes", scrollPane);
		tabbedPane.addTab("Details", _pduView);
		
		_pduView.add(new JLabel("-"));
		_view.add(tabbedPane, BorderLayout.PAGE_END);
	}
	
	private void setByteTabValue(String bytes) {
		int length = bytes.length();
		StringBuilder builder = new StringBuilder("<html>");
		
		int blocks = 0;
		for (int i = 0; i < length; i += 4) {
			int endIdx = Math.min(length, i + 4);
			builder.append(" ");
			builder.append(bytes.substring(i, endIdx));
			blocks ++;
			if (blocks % 8 == 0) {
				builder.append("<br />");
				blocks = 0;
			}
		}
		
		builder.append("</html>");
		
		_pduBytesLabel.setText(builder.toString());
	}
	
	private void setPduDetailView(JComponent pduView) {
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
