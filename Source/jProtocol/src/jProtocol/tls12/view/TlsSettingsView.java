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
	private TlsCipherSuiteRegistry _cipherSuiteRegistry;
	
	public TlsSettingsView(TlsCipherSuiteRegistry cipherSuiteRegistry) {
		_cipherSuiteRegistry = cipherSuiteRegistry;
		
		_settingsView = new JPanel();
		_settingsView.setLayout(new BoxLayout(_settingsView, BoxLayout.Y_AXIS));
		_settingsView.setBackground(Color.WHITE);
		
		ButtonGroup group = new ButtonGroup();
		List<TlsCipherSuite> cipherSuiteList = cipherSuiteRegistry.allCipherSuites();
		for (TlsCipherSuite cs : cipherSuiteList) {
			JRadioButton rb = new JRadioButton(cs.getName());
			rb.setBackground(Color.WHITE);
			rb.setActionCommand(Short.toString(cs.getCode()));
			rb.addActionListener(this);
			
			group.add(rb);
			_settingsView.add(rb);
			
			if (_cipherSuiteRegistry.getPreferredCipherSuite() == cs.getCode()) {
				rb.setSelected(true);
			}
//			_cipherSuiteRegistry.setPreferredCipherSuite(cs.getCode());
		}
	}
	
	public JComponent getView() {
		return _settingsView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Short csCode = Short.parseShort(e.getActionCommand());
		_cipherSuiteRegistry.setPreferredCipherSuite(csCode);
	}
}
