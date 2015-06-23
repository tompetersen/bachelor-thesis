package jProtocol.SimpleEchoProtocol.View;

import jProtocol.Abstract.View.ProtocolDataUnitView;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class EchoProtocolDataUnitView extends ProtocolDataUnitView {

	private JLabel _version;
	private JLabel _payload;	
	
	public EchoProtocolDataUnitView() {
		_version = new JLabel();
		_payload = new JLabel();
		
		_view.setLayout(new BoxLayout(_view, BoxLayout.Y_AXIS));
		
		_view.add(_version);
		_view.add(_payload);
	}
	
	public void setVersion(int version) {
		_version.setText("Version: " + String.valueOf(version));
	}
	
	public void setPayload(String payload) {
		_payload.setText("Payload: " + payload);
	}
	
}
