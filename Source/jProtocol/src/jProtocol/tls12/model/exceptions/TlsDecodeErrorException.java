package jProtocol.tls12.model.exceptions;

@SuppressWarnings("serial")
public class TlsDecodeErrorException extends TlsException {

	public TlsDecodeErrorException(String reason) {
		super(reason);
	}

}
