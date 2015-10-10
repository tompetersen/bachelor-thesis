package jProtocol.tls12.model.exceptions;

/**
 * An exception used for errors when requesting an invalid cipher suite.
 *  
 * @author Tom Petersen
 */
@SuppressWarnings("serial")
public class TlsInvalidCipherSuiteException extends TlsException {

	public TlsInvalidCipherSuiteException(String reason) {
		super(reason);
	}

}
