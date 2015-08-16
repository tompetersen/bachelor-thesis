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
	
	public TlsCiphertext(TlsMessage message, TlsVersion version, TlsFragment fragment) {
		_message = message;
		_version = version;
		_contentType = message.getContentType();
		_fragment = fragment;
		_length = (short) _fragment.getLength();
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

	public TlsFragment getFragment() {
		return _fragment;
	}

	public TlsMessage getMessage() {
		return _message;
	}
	
	public byte[] getBytes() {
		ByteBuffer b = ByteBuffer.allocate(5 + _length);	
		
		b.put(_contentType.getValue());
		b.put(_version.getMajorVersion());
		b.put(_version.getMinorVersion());
		b.putShort(_length);
		b.put(_fragment.getBytes());
		
		return b.array();
	}
	
}
