package jProtocol.tls12.model.exceptions;

@SuppressWarnings("serial")
public class TlsBadPaddingException extends TlsException {

	public TlsBadPaddingException(String reason) {
		super(reason);
	}

}
