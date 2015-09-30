package jProtocol.tls12.model.exceptions;

@SuppressWarnings("serial")
public class TlsBadRecordMacException extends TlsException {

	public TlsBadRecordMacException(String reason) {
		super(reason);
	}

}
