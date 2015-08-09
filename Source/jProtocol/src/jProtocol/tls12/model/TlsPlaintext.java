package jProtocol.tls12.model;

import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsVersion;
import jProtocol.tls12.model.values.TlsContentType.ContentType;

public class TlsPlaintext {

	private TlsMessage _message;
	
	private ContentType _contentType; 		//1 byte
	private TlsVersion _version; 			//2 bytes
	//Necessary?
	private short _length; 					//2 bytes
	private byte[] _fragment;
	
	public TlsPlaintext(TlsMessage message, TlsVersion version) {
		_message = message;
		_version = version;
		_contentType = message.getContentType();
		_fragment = _message.getBytes();
		_length = (short) _fragment.length;
	}
	
	public TlsPlaintext(byte[] content, TlsVersion version, ContentType contentType) {
		_message = null; //TODO: Where to parse the message?
		_version = version;
		_contentType = contentType;
		_fragment = content;
		_length = (short)content.length;
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

	public byte[] getFragment() {
		return _fragment;
	}

	public TlsMessage getMessage() {
		return _message;
	}
}
