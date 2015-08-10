package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.values.TlsAlertDescription;
import jProtocol.tls12.model.values.TlsAlertDescription.Alert;
import jProtocol.tls12.model.values.TlsContentType.ContentType;

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
public class TlsAlertMessage implements TlsMessage {

	private Alert _description;
	private boolean _isFatal;
	
	public TlsAlertMessage(Alert description, boolean isFatal) {
		if (description == null) {
			throw new IllegalArgumentException("Alert description must not be null!");
		}
		_description = description;
		_isFatal = isFatal;
	}
	
	@Override
	public ContentType getContentType() {
		return ContentType.Alert;
	}

	@Override
	public byte[] getBytes() {
		byte level = (byte) (_isFatal ? 2 : 1); 
		byte description = TlsAlertDescription.valueFromAlert(_description);
		byte[] result = {level, description}; 
		
		return result;
	}
	
}
