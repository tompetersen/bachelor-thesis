package jProtocol.tls12.model.exceptions;

@SuppressWarnings("serial")
public class TlsInvalidCipherSuiteException extends TlsException {

	public TlsInvalidCipherSuiteException(String reason) {
		super(reason);
	}

}
