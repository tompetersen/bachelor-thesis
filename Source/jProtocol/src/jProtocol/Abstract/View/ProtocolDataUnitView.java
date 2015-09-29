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
	private JScrollPane _pduView;
	private JLabel _pduBytesLabel;
	private JPanel _pduListView;
	private List<PduView> _pduViews;
	private JScrollPane _pduListViewScroller;
	private int _currentShownPduIndex;
	
	private HtmlInfoUpdater _htmlInfoUpdater;
	private JProtocolViewProvider<T> _provider;
	
	/**
	 * Creates a view for showing a protocol data unit list and detail view. 
	 * 
	 * @param provider a view provider
	 * @param htmlInfoUpdater an info updater to set the info view content
	 */
	public ProtocolDataUnitView(JProtocolViewProvider<T> provider, HtmlInfoUpdater htmlInfoUpdater) {
		_provider = provider;
		_htmlInfoUpdater = htmlInfoUpdater;
		
		_view = new JPanel();
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		_currentShownPduIndex = -1;
		_pduViews = new ArrayList<PduView>();
		
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
	
	private void createPduDetailView() {
		_pduView = new JScrollPane();
		
		_pduBytesLabel = new JLabel(" ");
		_pduBytesLabel.setVerticalAlignment(SwingConstants.TOP);
		_pduBytesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		_pduBytesLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scrollPane = new JScrollPane(_pduBytesLabel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setMinimumSize(new Dimension(300, 300));
		tabbedPane.setMaximumSize(new Dimension(300, 300));
		tabbedPane.setPreferredSize(new Dimension(300, 300));
		
		tabbedPane.addTab("Bytes", scrollPane);
		tabbedPane.addTab("Details", _pduView);
		
		_pduView.add(new JLabel("-"));
		_view.add(tabbedPane, BorderLayout.PAGE_END);
	}
	
	/**
	 * Returns the protocol data unit view.
	 * 
	 * @return the view
	 */
	public JComponent getView() {
		return _view;
	}

	/**
	 * Sets the protocol data units for the view.
	 * 
	 * @param pdus protocol data units which have already been sent
	 * @param pduToSend the protocol data unit which will be sent next. Can be null.
	 */
	public void setProtocolDataUnitList(List<T> pdus, T pduToSend) {
		_pduListView.removeAll();
		_pduViews.clear();
		
		for (int i = 0; i < pdus.size(); i++) {
			T pdu = pdus.get(i);
			PduView pduView = createSinglePduView(pdu, i, UiConstants.LIST_PDU_BACKGROUND);
			_pduViews.add(pduView);
			_pduListView.add(pduView.getView());
		}
		
		if (pduToSend != null) {
			PduView pduToSendView = createSinglePduView(pduToSend, pdus.size(), UiConstants.LIST_SENDING_PDU_BACKGROUND);
			_pduViews.add(pduToSendView);
			_pduListView.add(pduToSendView.getView());
		}
		
		if (_currentShownPduIndex > -1) {
			PduView pduView = _pduViews.get(_currentShownPduIndex);
			pduView.setCurrentBackground(UiConstants.LIST_CHOSEN_PDU_BACKGROUND);
		}
		
		_pduListViewScroller.validate();

		JScrollBar vertical = _pduListViewScroller.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());	
	}
	
	private PduView createSinglePduView(final T pdu, final int index, Color defaultBackground) {
		final PduView pduView = new PduView(pdu.toString(), "detailText", pdu.hasBeenSentByClient(), defaultBackground);
		
		pduView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//clear current selection bg color
				if (_currentShownPduIndex > -1) {
					PduView selectedPduView = _pduViews.get(_currentShownPduIndex);
					selectedPduView.setDefaultBackground();
				}
				
				//selection bg color
				_currentShownPduIndex = index;
				pduView.setCurrentBackground(UiConstants.LIST_CHOSEN_PDU_BACKGROUND);
				
				setPduInView(pdu);
			}
		});
		
		return pduView;
	}
	
	private void setPduInView(T pdu) {
		JComponent newPduView = _provider.getDetailedViewForProtocolDataUnit(pdu, _htmlInfoUpdater);
		setPduDetailView(newPduView);
		
		String bytes = ByteHelper.bytesToHexString(pdu.getBytes());
		setByteTabValue(bytes);
	}
	
	private void setPduDetailView(JComponent pduView) {
		_pduView.setViewportView(pduView);
		
		_pduView.revalidate();
		_pduView.repaint();
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
}
