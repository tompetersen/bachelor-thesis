package jProtocol.tls12.model;

import jProtocol.tls12.model.TlsSecurityParameters.BulkCipherAlgorithm;
import jProtocol.tls12.model.TlsSecurityParameters.CipherType;
import jProtocol.tls12.model.TlsSecurityParameters.MacAlgorithm;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite_Null;

import java.util.HashMap;
import java.util.Map;

public abstract class TlsCipherSuite {
	
	private static Map<Short, TlsCipherSuite> _supportedCipherSuites;
	
	static {
		_supportedCipherSuites = new HashMap<Short, TlsCipherSuite>();
		
		//TODO: Add cipher suites (maybe automagically)
		_supportedCipherSuites.put((short) 0, new TlsCipherSuite_Null());
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
	
	public abstract CipherType getCipherType();
	public abstract BulkCipherAlgorithm getBulkCipherAlgorithm();
	public abstract byte getEncryptKeyLength();
	public abstract byte getBlockLength();
	public abstract byte getFixedIvLength();
	public abstract byte getRecordIvLength();
	public abstract MacAlgorithm getMacAlgorithm();
	public abstract byte getMacLength();
	public abstract byte getMacKeyLength();
}
