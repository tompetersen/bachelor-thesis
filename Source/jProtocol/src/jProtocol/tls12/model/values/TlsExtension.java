package jProtocol.tls12.model.values;

public class TlsExtension {
	
/*	 
    struct {
         ExtensionType extension_type;
         opaque extension_data<0..2^16-1>;
     } Extension;
     enum {
         signature_algorithms(13), (65535)
     } ExtensionType;
*/
	
	public static final int EXTENSION_LENGTH_FIELD_LENGTH = 2;
	public static final int EXTENSION_TYPE_FIELD_LENGTH = 2;
	
	private short _extensionType;
	private byte[] _data;
	
	public TlsExtension(short extensionType, byte[] extensionData) {
		super();
		
		_extensionType = extensionType;
		_data = extensionData;
	}

	public short getExtensionType() {
		return _extensionType;
	}

	public byte[] getExtensionData() {
		return _data;
	}	
}
