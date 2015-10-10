package jProtocol.tls12.model.exceptions;

/**
 * An exception used for errors during asymmetric operations.
 *  
 * @author Tom Petersen
 */
@SuppressWarnings("serial")
public class TlsAsymmetricOperationException extends TlsException {

	public TlsAsymmetricOperationException(String reason) {
		super(reason);
	}

}
