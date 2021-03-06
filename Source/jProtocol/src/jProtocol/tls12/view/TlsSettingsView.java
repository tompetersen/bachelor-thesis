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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class TlsSettingsView implements ActionListener {

	private JPanel _settingsView;
	private TlsCipherSuiteRegistry _cipherSuiteRegistry;

	/**
	 * Creates a settings view for a TLS protocol.
	 * 
	 * @param cipherSuiteRegistry the chosem cipher suite is set in this
	 *            registry. It should be injected in the state machines.
	 */
	public TlsSettingsView(TlsCipherSuiteRegistry cipherSuiteRegistry) {
		_cipherSuiteRegistry = cipherSuiteRegistry;

		_settingsView = new JPanel();
		_settingsView.setLayout(new BoxLayout(_settingsView, BoxLayout.Y_AXIS));
		_settingsView.setBackground(Color.WHITE);
		_settingsView.add(new JLabel("<html><h1>TLS</h1>TLS (called SSL in earlier versions) is a protocol providing a secure connection over a network. It is used by a lot of application layer protocols like HTTPS or SFTP.<br />TLS cipher suites include the the asymmetric algorithm for exchanging keys and the symmetric cipher for message encryption. Please choose the cipher suite the connection should use:</html>"));

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
		}
	}

	/**
	 * Returns the settings view for the TLS protocol.
	 * 
	 * @return the settings view
	 */
	public JComponent getView() {
		return _settingsView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Short csCode = Short.parseShort(e.getActionCommand());
		_cipherSuiteRegistry.setPreferredCipherSuite(csCode);
	}
}
