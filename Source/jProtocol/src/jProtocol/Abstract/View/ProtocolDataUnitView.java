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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class ProtocolDataUnitView<T extends ProtocolDataUnit> {

	private HtmlInfoUpdater _htmlInfoUpdater;
	private ViewProvider<T> _provider;
	
	private JTabbedPane _pduTabbedPane;
	
	private JPanel _view;
	private JScrollPane _pduView;
	private JLabel _pduBytesLabel;
	
	private JPanel _pduListView;
	private JScrollPane _pduListViewScroller;
	private List<PduView> _pduViews;
	
	private int _currentShownPduIndex;
	private List<T> _sentPdus;
	private T _pduToSend;
	
	private JButton _editBytesButton;

	/**
	 * Creates a view for showing a protocol data unit list and detail view.
	 * 
	 * @param provider a view provider
	 * @param htmlInfoUpdater an info updater to set the info view content
	 */
	public ProtocolDataUnitView(ViewProvider<T> provider, HtmlInfoUpdater htmlInfoUpdater) {
		_provider = provider;
		_htmlInfoUpdater = htmlInfoUpdater;

		_view = new JPanel(new BorderLayout(5, 5));
		_view.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		
		_currentShownPduIndex = -1;
		_pduViews = new ArrayList<PduView>();

		createHeaderPanel();
		createPduList();
		createPduDetailView();
	}
	
	private void createHeaderPanel() {
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headerPanel.add(new JLabel(new ImageIcon(ImageLoader.getMessageIcon(32, 32))));
		JLabel headerLabel = new JLabel("Messages");
		headerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		headerPanel.add(headerLabel);
		headerPanel.setBackground(UiConstants.VIEW_BACKGROUND_COLOR);
		
		_view.add(headerPanel, BorderLayout.PAGE_START);
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
		_pduTabbedPane = new JTabbedPane();
		
		_pduTabbedPane.setMinimumSize(new Dimension(300, 300));
		_pduTabbedPane.setMaximumSize(new Dimension(300, 300));
		_pduTabbedPane.setPreferredSize(new Dimension(300, 300));

		_pduTabbedPane.addTab("Bytes", getBytesTab());
		_pduTabbedPane.addTab("Details", getDetailsTab());
		_pduTabbedPane.addTab("Actions", getActionTab());		
		_view.add(_pduTabbedPane, BorderLayout.PAGE_END);
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
		
		_editBytesButton = new JButton("Edit Bytes", new ImageIcon(ImageLoader.getEditIcon(UiConstants.BUTTON_IMAGE_SIZE, UiConstants.BUTTON_IMAGE_SIZE)));
		_editBytesButton.setHorizontalTextPosition(JButton.RIGHT);
		_editBytesButton.setVerticalTextPosition(JButton.CENTER);
		_editBytesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		_editBytesButton.setEnabled(false);
		_editBytesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editPduBytes();
			}
		});
		actionTab.add(_editBytesButton);
		
		return actionTab;
	}
	
	private void editPduBytes() {
		if (_currentShownPduIndex > -1) {
			final T pdu = (_currentShownPduIndex < _sentPdus.size()) ? _sentPdus.get(_currentShownPduIndex) : _pduToSend;
			byte[] currentBytes = pdu.getBytes();
			
			final JDialog dialog = new JDialog();
			dialog.setTitle("Edit bytes");
			dialog.setModal(true);
			dialog.setSize(200, 200);
			dialog.setLocationRelativeTo(_pduTabbedPane);
			
			JPanel dialogContent = new JPanel(new BorderLayout(5,5));
			dialogContent.setBackground(Color.WHITE);
			
			final JTextArea textArea = new JTextArea(ByteHelper.bytesToHexString(currentBytes));
			textArea.setLineWrap(true);
			textArea.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
				      char c = e.getKeyChar();
				      if (!isHexChar(c)) {
				         e.consume();  // ignore event
				      }
				   }
			});
			dialogContent.add(textArea, BorderLayout.CENTER);
			
			JButton button = ButtonHelper.createImageButton("End editing",  
					new ImageIcon(ImageLoader.getEditIcon(20, 20)),
					new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String content = textArea.getText();
							if (content.length() % 2 == 0) {
								pdu.setAlteredBytes(ByteHelper.hexStringToBytes(content));
								dialog.dispose();
								setPduInView(pdu);
							}
							else {
								JOptionPane.showMessageDialog(dialog, "Hex representation of the message must have an even length!", "Bad input", JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			dialogContent.add(button, BorderLayout.SOUTH);
			
			dialog.add(dialogContent);
			dialog.setVisible(true);
		}
	}
	
	private boolean isHexChar(char c) {
		c = Character.toLowerCase(c);
		return (c >= '0' && c <= '9') || (c >= 'a' && c<= 'f');
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
		setByteTabValue(pdu);
		_editBytesButton.setEnabled(pdu == _pduToSend);
	}

	private void setPduDetailView(JComponent pduView) {
		_pduView.setViewportView(pduView);

		_pduView.revalidate();
		_pduView.repaint();
	}

	private void setByteTabValue(T pdu) {
		String bytes = ByteHelper.bytesToHexString(pdu.getBytes());
		
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
		_pduBytesLabel.setForeground(pdu.hasAlteredBytes() ? UiConstants.ALTERED_BYTES_FOREGROUND_COLOR : Color.BLACK);
	}
}
