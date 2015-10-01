package jProtocol.Abstract.View;

import jProtocol.helper.ColorHelper;
import jProtocol.helper.GridBagConstraintsHelper;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PduView {

	private JPanel _view;
	private Color _defaultBackground;
	private Color _preEnterBackground;
	
	public PduView(String title, String detailText, boolean sentByClient, Color defaultBackground) {
		_defaultBackground = defaultBackground;
		
		_view = new JPanel(new GridBagLayout());
		
		_view.setMaximumSize(new Dimension(280, 50));
		_view.setMinimumSize(new Dimension(280, 50));
		_view.setPreferredSize(new Dimension(280, 50));

		_view.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		_view.setBackground(defaultBackground);
		
		JPanel clientPanel = new JPanel();
		JPanel serverPanel = new JPanel();
		
		JLabel pduText = new JLabel(title);
		pduText.setFont(new Font("SansSerif", Font.BOLD, 12));
		JLabel pduDetailText = new JLabel(detailText);
		pduDetailText.setFont(new Font("SansSerif", Font.PLAIN, 12));
		
		Insets labelInsets = new Insets(2, 5, 2, 5);
		
		GridBagConstraints constraints = GridBagConstraintsHelper.createNormalConstraints(1, 0, 1, labelInsets);
		constraints.weightx = 1;
		_view.add(pduText, constraints);
		
		constraints = GridBagConstraintsHelper.createNormalConstraints(1, 1, 1, labelInsets);
		constraints.weightx = 1;
		_view.add(pduDetailText, constraints);
		
		Insets nullInsets = new Insets(0, 0, 0, 0);
		
		constraints = GridBagConstraintsHelper.createNormalConstraints(0, 0, 1, nullInsets);
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0;
		constraints.weighty = 1;
		_view.add(clientPanel, constraints);
		
		constraints = GridBagConstraintsHelper.createNormalConstraints(2, 0, 1, nullInsets);
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0;
		constraints.weighty = 1;
		_view.add(serverPanel, constraints);
		
		if (sentByClient) {
			clientPanel.setBackground(UiConstants.LIST_PDU_SENDER_BACKGROUND);
			serverPanel.setVisible(false);
			pduText.setHorizontalAlignment(SwingConstants.LEFT);
			pduDetailText.setHorizontalAlignment(SwingConstants.LEFT);
		}
		else {
			serverPanel.setBackground(UiConstants.LIST_PDU_SENDER_BACKGROUND);
			clientPanel.setVisible(false);
			pduText.setHorizontalAlignment(SwingConstants.RIGHT);
			pduDetailText.setHorizontalAlignment(SwingConstants.RIGHT);
		}
	}
	
	public void addMouseListener(MouseListener l) {
		_view.addMouseListener(l);
	}
	
	public void setCurrentBackground(Color background) {
		_preEnterBackground = background;
		_view.setBackground(background);
	}
	
	public void setDefaultBackground() {
		_view.setBackground(_defaultBackground);
	}

	public JComponent getView() {
		return _view;
	}
	
	public void mouseEnteredView() {
		_preEnterBackground = _view.getBackground();
		_view.setBackground(ColorHelper.slightlyBrighterColorForColor(_preEnterBackground));
	}
	
	public void mouseLeftView() {
		_view.setBackground(_preEnterBackground);
	}

}
