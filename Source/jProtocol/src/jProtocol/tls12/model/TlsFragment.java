package jProtocol.tls12.model;

public abstract class TlsFragment {

	protected byte[] _content;
	
	public TlsFragment(byte[] content) {
		_content = content;
	}
	
	public int getLength() {
		return _content.length;
	}
	
	public abstract byte[] getBytes();
}
