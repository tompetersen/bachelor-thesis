package jProtocol.Abstract.View;

import jProtocol.Abstract.ViewProvider;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.View.resources.ImageLoader;
import jProtocol.helper.ButtonHelper;
import jProtocol.helper.ByteHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class ProtocolDataUnitView<T extends ProtocolDataUnit> {

	private HtmlInfoUpdater _htmlInfoUpdater;
	private ViewProvider<T> _provider;
	
	private JPanel _view;
	private JScrollPane _pduView;
	private JLabel _pduBytesLabel;
	
	private JPanel _pduListView;
	private JScrollPane _pduListViewScroller;
	private List<PduView> _pduViews;
	
	private int _currentShownPduIndex;
	private List<T> _sentPdus;
	private T _pduToSend;
	

	/**
	 * Creates a view for showing a protocol data unit list and detail view.
	 * 
	 * @param provider a view provider
	 * @param htmlInfoUpdater an info updater to set the info view content
	 */
	public ProtocolDataUnitView(ViewProvider<T> provider, HtmlInfoUpdater htmlInfoUpdater) {
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
		_pduListViewScroller.getVerticalScrollBar().setUnitIncrement(16);
		_pduListViewScroller.setPreferredSize(new Dimension(300, 300));

		_view.add(_pduListViewScroller, BorderLayout.CENTER);
	}

	private void createPduDetailView() {
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.setMinimumSize(new Dimension(300, 300));
		tabbedPane.setMaximumSize(new Dimension(300, 300));
		tabbedPane.setPreferredSize(new Dimension(300, 300));

		tabbedPane.addTab("Bytes", getBytesTab());
		tabbedPane.addTab("Details", getDetailsTab());
		tabbedPane.addTab("Actions", getActionTab());		
		_view.add(tabbedPane, BorderLayout.PAGE_END);
	}
	
	private JComponent getBytesTab(){
		_pduBytesLabel = new JLabel(" ");
		_pduBytesLabel.setVerticalAlignment(SwingConstants.TOP);
		_pduBytesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		_pduBytesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		_pduBytesLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JScrollPane scrollPane = new JScrollPane(_pduBytesLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		return scrollPane;
	}
	
	private JComponent getDetailsTab() {
		_pduView = new JScrollPane();
		_pduView.add(new JLabel("-"));
		
		return _pduView;
	}
	
	private JComponent getActionTab() {
		JPanel actionTab = new JPanel();
		actionTab.setLayout(new BoxLayout(actionTab, BoxLayout.Y_AXIS));
		actionTab.setBackground(Color.WHITE);
		
		JButton editButton = new JButton("Edit Bytes", new ImageIcon(ImageLoader.getEditIcon(UiConstants.BUTTON_IMAGE_SIZE, UiConstants.BUTTON_IMAGE_SIZE)));
		editButton.setHorizontalTextPosition(JButton.RIGHT);
		editButton.setVerticalTextPosition(JButton.CENTER);
		editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editPduBytes();
			}
		});
		actionTab.add(editButton);
		
		return actionTab;
	}
	
	private void editPduBytes() {
		if (_currentShownPduIndex > -1) {
			T pdu = (_currentShownPduIndex < _sentPdus.size()) ? _sentPdus.get(_currentShownPduIndex) : _pduToSend;
			byte[] currentBytes = pdu.getBytes();
			
			final JDialog dialog = new JDialog();
			dialog.setTitle("Edit bytes");
			dialog.setModal(true);
			dialog.setSize(200, 200);
			
			JPanel dialogContent = new JPanel(new BorderLayout(5,5));
			dialogContent.setBackground(Color.WHITE);
			
			JTextArea textArea = new JTextArea(ByteHelper.bytesToHexString(currentBytes));
			textArea.setLineWrap(true);
			dialogContent.add(textArea, BorderLayout.CENTER);
			
			JButton button = ButtonHelper.createImageButton("End editing",  
					new ImageIcon(ImageLoader.getEditIcon(20, 20)),
					new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							dialog.dispose();
						}
					});
			dialogContent.add(button, BorderLayout.SOUTH);
			
			dialog.add(dialogContent);
			dialog.setVisible(true);
			
			pdu.setAlteredBytes(ByteHelper.hexStringToBytes(textArea.getText()));
		}
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
	 * @param pdus
	 *            protocol data units which have already been sent
	 * @param pduToSend
	 *            the protocol data unit which will be sent next. Can be null.
	 */
	public void setProtocolDataUnitList(List<T> pdus, T pduToSend) {
		_sentPdus = pdus;
		_pduToSend = pduToSend;
		
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

	private PduView createSinglePduView(final T pdu, final int index, final Color defaultBackground) {
		final PduView pduView = new PduView(pdu.getTitle(), pdu.getSubtitle(), pdu.hasBeenSentByClient(), defaultBackground);

		pduView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// clear current selection bg color
				if (_currentShownPduIndex > -1) {
					PduView selectedPduView = _pduViews.get(_currentShownPduIndex);
					selectedPduView.setDefaultBackground();
				}

				// selection bg color
				_currentShownPduIndex = index;
				pduView.setCurrentBackground(UiConstants.LIST_CHOSEN_PDU_BACKGROUND);

				setPduInView(pdu);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				pduView.mouseEnteredView();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				pduView.mouseLeftView();
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
			blocks++;
			if (blocks % 8 == 0) {
				builder.append("<br />");
				blocks = 0;
			}
		}

		builder.append("</html>");

		_pduBytesLabel.setText(builder.toString());
	}
}
