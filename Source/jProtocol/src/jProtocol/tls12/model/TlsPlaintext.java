package jProtocol.tls12.model;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsVersion;

public class TlsPlaintext {

	private TlsMessage _message;
	
	private TlsContentType _contentType; 		//1 byte
	private TlsVersion _version; 			//2 bytes
	//Necessary?
	private short _length; 					//2 bytes
	private byte[] _fragment;
	
	/**
	 * Creates a plaintext from an existing message and the currently used TlsVersion.
	 * 
	 * @param message the message
	 * @param version the version
	 */
	public TlsPlaintext(TlsMessage message, TlsVersion version) {
		_message = message;
		_version = version;
		_contentType = message.getContentType();
		_fragment = _message.getBytes();
		_length = (short) _fragment.length;
	}
	
	public TlsPlaintext(byte[] messageBytes) throws TlsDecodeErrorException {
		//TODO: parse ContentType, Version, length(?), call message constructor with other bytes
		_contentType = null;
		_version = null;
		_length = 0;
		_fragment = null;
		
		_message = TlsMessage.parseTlsMessage(_fragment, _contentType);
	}
	
	public TlsContentType getContentType() {
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
