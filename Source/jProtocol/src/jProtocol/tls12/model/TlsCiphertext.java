package jProtocol.tls12.model;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.tls12.model.TlsContentType.ContentType;

public class TlsCiphertext extends ProtocolDataUnit {

	private TlsMessage _message;
	
	private ContentType _contentType; 		//1 byte
	private TlsVersion _version; 			//2 bytes
	private short _length; 					//2 bytes
	private TlsFragment _fragment;
	
	public TlsCiphertext(TlsPlaintext plaintext, TlsFragmentFactory fragmentFactory) {
		_contentType = plaintext.getContentType();
		_version = plaintext.getVersion();
		_message = plaintext.getMessage();
		
		_fragment = fragmentFactory.createFragmentFromPlaintext(plaintext);
		_length = (short)_fragment.getLength();
	}
	
}
