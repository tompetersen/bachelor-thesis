package jProtocol.tls12.model.exceptions;

public class TlsDecodeErrorException extends TlsException {

	private static final long serialVersionUID = 8478373784951520558L;

	public TlsDecodeErrorException(String reason) {
		super(reason);
	}

}
