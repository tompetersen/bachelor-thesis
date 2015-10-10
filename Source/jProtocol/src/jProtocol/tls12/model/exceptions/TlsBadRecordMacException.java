package jProtocol.tls12.model.exceptions;

/**
 * An exception used for bad record MAC errors during decryption.
 *  
 * @author Tom Petersen
 */
@SuppressWarnings("serial")
public class TlsBadRecordMacException extends TlsException {

	public TlsBadRecordMacException(String reason) {
		super(reason);
	}

}
