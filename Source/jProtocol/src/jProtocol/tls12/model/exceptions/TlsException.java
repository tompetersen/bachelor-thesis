package jProtocol.tls12.model.exceptions;

@SuppressWarnings("serial")
public abstract class TlsException extends Exception {
	
	public TlsException(String reason) {
		super(reason);
	}
	
}
