package jProtocol.tls12.model;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.tls12.model.fragments.TlsFragment;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsVersion;
import java.nio.ByteBuffer;

public class TlsCiphertext extends ProtocolDataUnit {

	private TlsMessage _message;
	
	private TlsContentType _contentType; 		//1 byte
	private TlsVersion _version; 				//2 bytes
	private short _length; 						//2 bytes
	private TlsFragment _fragment;
	
	private byte[] _bytes;
	
	public TlsCiphertext(TlsMessage message, TlsVersion version, TlsFragment fragment) {
		_message = message;
		_version = version;
		_contentType = message.getContentType();
		_fragment = fragment;
		_length = (short) _fragment.getLength();
		
		setBytes();
	}
	
	private void setBytes() {
		ByteBuffer b = ByteBuffer.allocate(5 + _length);	
		
		b.put(_contentType.getValue());
		b.put(_version.getMajorVersion());
		b.put(_version.getMinorVersion());
		b.putShort(_length);
		b.put(_fragment.getBytes());
		
		_bytes = b.array();
	}
	
	public TlsContentType getContentType() {
		return _contentType;
	}

	public TlsVersion getVersion() {
		return _version;
	}

	/**
	 * The length sent in the TLS Record (not including content type, version and the length field itself).
	 * 
	 * @return the length
	 */
	public short getLength() {
		return _length;
	}

	public TlsFragment getFragment() {
		return _fragment;
	}

	public TlsMessage getMessage() {
		return _message;
	}
	
	/**
	 * The bytes to be send over the channel.
	 * 
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return _bytes;
	}
	
}
