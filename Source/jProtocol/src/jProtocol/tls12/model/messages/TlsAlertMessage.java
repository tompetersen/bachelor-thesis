package jProtocol.tls12.model.messages;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsAlert;
import jProtocol.tls12.model.values.TlsContentType;
import java.util.ArrayList;

/*
 * struct {
 *        AlertLevel level;
 *        AlertDescription description;
 * } Alert;
 * 
 * See chapter 7.2, p.28 TLS 1.2
 * 
 * Error handling in the TLS Handshake protocol is very simple.  When an
   error is detected, the detecting party sends a message to the other
   party.  Upon transmission or receipt of a fatal alert message, both
   parties immediately close the connection.  Servers and clients MUST
   forget any session-identifiers, keys, and secrets associated with a
   failed connection.  Thus, any connection terminated with a fatal
   alert MUST NOT be resumed.
 */
public class TlsAlertMessage extends TlsMessage {

	private TlsAlert _alert;
	private boolean _isFatal;
	
	public TlsAlertMessage(TlsAlert description, boolean isFatal) {
		if (description == null) {
			throw new IllegalArgumentException("Alert description must not be null!");
		}
		_alert = description;
		_isFatal = isFatal;
	}
	
	public TlsAlertMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		
		if (unparsedContent.length != 2) {
			throw new TlsDecodeErrorException("Alert message must be 2 bytes long!");
		}
		
		if (unparsedContent[0] == 1) {
			_isFatal = false;
		}
		else if (unparsedContent[0] == 2) {
			_isFatal = true;
		}
		else {
			throw new TlsDecodeErrorException("First byte must be 1 (warning) or 2 (fatal)!");
		}
		
		try {
			_alert = TlsAlert.alertFromValue(unparsedContent[1]);
		} 
		catch (IllegalArgumentException e) {
			throw new TlsDecodeErrorException("Alert message must have valid alert description byte!");
		}
	}

	@Override
	public TlsContentType getContentType() {
		return TlsContentType.Alert;
	}

	@Override
	public byte[] getBytes() {
		byte level = (byte) (_isFatal ? 2 : 1); 
		byte description = _alert.getValue();
		byte[] result = {level, description}; 
		
		return result;
	}

	public TlsAlert getAlert() {
		return _alert;
	}

	public boolean isFatal() {
		return _isFatal;
	}

	@Override
	public String toString() {
		return _alert.toString() + (_isFatal ? " FATAL" : "");
	}

	@Override
	public KeyValueObject getViewData() {
		ArrayList<KeyValueObject> children = new ArrayList<KeyValueObject>();
		
		KeyValueObject kvo = new KeyValueObject("AlertLevel", _isFatal ? "2 - fatal" : "1 - warning");
		kvo.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/TLS12_Alert.html"));
		children.add(kvo);
		
		kvo = new KeyValueObject("AlertDescription", _alert.toString());
		kvo.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/TLS12_AlertDescription.html"));
		children.add(kvo);
		
		KeyValueObject result = new KeyValueObject("Content", children);
		result.setValue("TlsAlert");
		result.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/TLS12_Alert.html"));
		
		return result;
	}
	
}
