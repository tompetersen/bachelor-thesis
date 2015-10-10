package jProtocol.tls12.model.exceptions;

/**
 * An exception used for bad padding errors during decryption.
 *  
 * @author Tom Petersen
 */
@SuppressWarnings("serial")
public class TlsBadPaddingException extends TlsException {

	public TlsBadPaddingException(String reason) {
		super(reason);
	}

}
