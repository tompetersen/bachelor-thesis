package jProtocol.tls12.model;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.model.fragments.TlsFragment;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsVersion;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TlsCiphertext extends ProtocolDataUnit {

	private TlsMessage _message;

	private TlsContentType _contentType; // 1 byte
	private TlsVersion _version; // 2 bytes
	private short _length; // 2 bytes
	private TlsFragment _fragment;

	private byte[] _bytes;

	/**
	 * Creates a TLS ciphertext object.
	 * 
	 * @param message the encapsulated message for displaying purposes
	 * @param version the used TLS version
	 * @param fragment the fragment containing the message
	 */
	public TlsCiphertext(TlsMessage message, TlsVersion version, TlsFragment fragment) {
		_version = version;
		_message = message;
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

	/**
	 * Returns the content type of the encapsulated message.
	 * 
	 * @return the content type
	 */
	public TlsContentType getContentType() {
		return _contentType;
	}

	/**
	 * Returns the used TLS version.
	 * 
	 * @return the TLS version
	 */
	public TlsVersion getVersion() {
		return _version;
	}

	/**
	 * The length of the TLS record content (not including content type, version
	 * and the length field itself).
	 * 
	 * @return the length
	 */
	public short getLength() {
		return _length;
	}

	/**
	 * Returns the fragment containing the message and encryption cipher suite
	 * values.
	 * 
	 * @return the fragment
	 */
	public TlsFragment getFragment() {
		return _fragment;
	}

	/**
	 * The bytes to be send over the channel.
	 * 
	 * @return the bytes
	 */
	public byte[] getMessageBytes() {
		return _bytes;
	}

	@Override
	public String toString() {
		return "[" + _contentType.toString() + "] " + _message.toString();
	}

	/*
	 * View Data
	 */
	/**
	 * Returns a list of key value objects containing the in this ciphertext
	 * contained information.
	 * 
	 * @return the key value object list
	 */
	public List<KeyValueObject> getViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();

		KeyValueObject kvo = new KeyValueObject("ContentType", _contentType.toString());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/TLS12_ContentType.html"));
		result.add(kvo);

		kvo = new KeyValueObject("Version", _version.toString());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/TLS12_Version.html"));
		result.add(kvo);

		kvo = new KeyValueObject("Length", Short.toString(_length));
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/TLS12_MessageLength.html"));
		result.add(kvo);

		result.add(_fragment.getViewData(_message));

		return result;
	}

	@Override
	public String getTitle() {
		return toString();
	}

	@Override
	public String getSubtitle() {
		return _fragment.getFragmentName();
	}

}
