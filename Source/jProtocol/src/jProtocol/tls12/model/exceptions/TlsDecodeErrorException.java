package jProtocol.tls12.model.exceptions;

/**
 * An exception used for decoding errors during parsing a received message.
 *  
 * @author Tom Petersen
 */
@SuppressWarnings("serial")
public class TlsDecodeErrorException extends TlsException {

	public TlsDecodeErrorException(String reason) {
		super(reason);
	}

}
