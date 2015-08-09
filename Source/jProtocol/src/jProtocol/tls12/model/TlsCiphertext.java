package jProtocol.tls12.model;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.tls12.model.fragments.TlsFragment;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsVersion;
import jProtocol.tls12.model.values.TlsContentType.ContentType;

public class TlsCiphertext extends ProtocolDataUnit {

	private TlsMessage _message;
	
	private ContentType _contentType; 		//1 byte
	private TlsVersion _version; 			//2 bytes
	private short _length; 					//2 bytes
	private TlsFragment _fragment;
	
	public TlsCiphertext(TlsMessage message, TlsVersion version, TlsFragment fragment) {
		_message = message;
		_version = version;
		_contentType = message.getContentType();
		_fragment = fragment;
		_length = (short) _fragment.getLength();
	}
	
	public ContentType getContentType() {
		return _contentType;
	}

	public TlsVersion getVersion() {
		return _version;
	}

	public short getLength() {
		return _length;
	}

	public TlsFragment getFragment() {
		return _fragment;
	}

	public TlsMessage getMessage() {
		return _message;
	}
	
}
