package jProtocol.tls12.model;

import java.util.HashMap;
import java.util.Map;

public abstract class TlsCipherSuite {
	
	private static Map<Short, TlsCipherSuite> _supportedCipherSuites;
	
	public TlsCipherSuite() {
		_supportedCipherSuites = new HashMap<Short, TlsCipherSuite>();
		
		//TODO: Add cipher suites (maybe automagically)
		//_supportedCipherSuites.put(key, value)
	}
	
	public static TlsCipherSuite cipherSuiteFromValue(short value) {
		TlsCipherSuite suite = _supportedCipherSuites.get(value);
		if (suite != null) {
			return suite;
		}
		else {
			throw new IllegalArgumentException("Cipher suite for value " + value + " not found!");
		}
	}
	
	public static short valueFromCipherSuite(TlsCipherSuite cipherSuite) {
		for (short s : _supportedCipherSuites.keySet()) {
			if (_supportedCipherSuites.get(s).getClass().equals(cipherSuite.getClass())) {
				return s;
			}
		}
		throw new IllegalArgumentException("Value for cipher suite " + cipherSuite.getName() + " not found!");
	}
	
	public abstract String getName();
	public abstract short getDefinitionValue();
	
}
