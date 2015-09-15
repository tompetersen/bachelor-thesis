package jProtocol.Abstract.View;

import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.helper.ByteHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class ProtocolDataUnitView<T extends ProtocolDataUnit> {

	private JPanel _view;
	private JPanel _pduView;
	private JLabel _pduBytesLabel;
	private JPanel _pduListView;
	private JScrollPane _pduListViewScroller;
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
		_pduListView = new JPanel();
		_pduListView.setLayout(new BoxLayout(_pduListView, BoxLayout.Y_AXIS));
		_pduListView.setBackground(Color.WHITE);
		
		_pduListViewScroller = new JScrollPane(_pduListView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_pduListViewScroller.setPreferredSize(new Dimension(300, 300));
		
		_view.add(_pduListViewScroller, BorderLayout.CENTER);
	}

	public void setProtocolDataUnits(List<T> pdus) {
		_pdus = pdus;
		
		refreshPduList();
	}
	
	private void refreshPduList() {
		for (int i = _pduListView.getComponentCount(); i < _pdus.size(); i++) {
			T pdu = _pdus.get(i);
			JComponent pduView = createSinglePduView(pdu);
			_pduListView.add(pduView);
		}
		
		_pduListViewScroller.validate();

		JScrollBar vertical = _pduListViewScroller.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());	
	}
	
	private JComponent createSinglePduView(final T pdu) {
		JPanel result = new JPanel();
		result.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		result.setBackground(Color.GRAY);
		
		result.setMaximumSize(new Dimension(280, 50));
		result.setMinimumSize(new Dimension(280, 50));
		result.setPreferredSize(new Dimension(280, 50));
		
		result.add(new JLabel((pdu.hasBeenSentByClient() ? "-> " : "<- ") + pdu.toString()));
		result.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPduInView(pdu);
			}
		});
		
		return result;
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
		tabbedPane.setPreferredSize(new Dimension(300, 100));
		
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

	public JComponent getView() {
		return _view;
	}
}
