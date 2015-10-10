package jProtocol.tls12.model.exceptions;

@SuppressWarnings("serial")
public abstract class TlsException extends Exception {
	
	/**
	 * Creates a TLS exception with an exception reason.
	 * 
	 * @param reason the reason message
	 */
	public TlsException(String reason) {
		super(reason);
	}
	
}
