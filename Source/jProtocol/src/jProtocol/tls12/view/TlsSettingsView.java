package jProtocol.tls12.view;

import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class TlsSettingsView implements ActionListener {

	private JPanel _settingsView;
	private Short _chosenCipherSuite;
	
	public TlsSettingsView(TlsCipherSuiteRegistry cipherSuiteRegistry) {
		_settingsView = new JPanel();
		_settingsView.setLayout(new BoxLayout(_settingsView, BoxLayout.Y_AXIS));
		_settingsView.setBackground(Color.WHITE);
		
		ButtonGroup group = new ButtonGroup();
		List<TlsCipherSuite> cipherSuiteList = cipherSuiteRegistry.allCipherSuites();
		for (TlsCipherSuite cs : cipherSuiteList) {
			JRadioButton rb = new JRadioButton(cs.getName());
			rb.setBackground(Color.WHITE);
			group.add(rb);
			_settingsView.add(rb);
			rb.setActionCommand(Short.toString(cs.getCode()));
			rb.addActionListener(this);
			rb.setSelected(true);
		}
	}
	
	public JComponent getView() {
		return _settingsView;
	}
	
	public short getChosenCipherSuite() {
		return _chosenCipherSuite;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Short csCode = Short.parseShort(e.getActionCommand());
		_chosenCipherSuite = csCode;
	}
}
