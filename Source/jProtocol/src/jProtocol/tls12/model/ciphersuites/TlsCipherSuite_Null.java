package jProtocol.tls12.model.ciphersuites;


public class TlsCipherSuite_Null extends TlsStreamCipherSuite {

	/*
	 * If the cipher suite is TLS_NULL_WITH_NULL_NULL, encryption consists of the identity operation 
	 * (i.e., the data is not encrypted, and the MAC size is zero, implying that no MAC is used).
	 * 
	 * 6.2.3.1. p.22
	 */
	
	@Override
	public String getName() {
		return "TLS_NULL_WITH_NULL_NULL";
	}

	@Override
	public short getDefinitionValue() {
		return 0x0000;
	}

	
}
