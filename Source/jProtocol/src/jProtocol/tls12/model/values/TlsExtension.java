package jProtocol.tls12.model.values;

/**
 * A class representing possible TlsExtensions.
 * TODO: Used for tls extensions - implement subclasses if necessary or do it your way
 * 
 * @author Tom Petersen
 */
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
	
	/**
	 * Creates an extension object with a type and extension data.
	 * 
	 * @param extensionType the type
	 * @param extensionData the data
	 */
	public TlsExtension(short extensionType, byte[] extensionData) {
		super();
		
		_extensionType = extensionType;
		_data = extensionData;
	}

	/**
	 * Returns the extensions type.
	 * 
	 * @return the type
	 */
	public short getExtensionType() {
		return _extensionType;
	}

	/**
	 * Returns the extension data
	 * 
	 * @return the data
	 */
	public byte[] getExtensionData() {
		return _data;
	}	
}
