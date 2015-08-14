package jProtocol.tls12.model.values;

public enum TlsAlert {
	close_notify 				((byte)0), 	
	unexpected_message 			((byte)10),	//fatal
	bad_record_mac 				((byte)20),	//fatal
	decryption_failed_RESERVED 	((byte)21),
	record_overflow 			((byte)22),	//fatal
	decompression_failure 		((byte)30),	//fatal
	handshake_failure 			((byte)40),	//fatal
	no_certificate_RESERVED 	((byte)41),
	bad_certificate 			((byte)42),
	unsupported_certificate 	((byte)43),
	certificate_revoked 		((byte)44),
	certificate_expired 		((byte)45),
	certificate_unknown 		((byte)46),
	illegal_parameter 			((byte)47),	//fatal
	unknown_ca 					((byte)48),
	access_denied 				((byte)49),	//fatal
	decode_error 				((byte)50),	//fatal
	decrypt_error 				((byte)51),	//fatal
	export_restriction_RESERVED	((byte)60),
	protocol_version 			((byte)70),	//fatal
	insufficient_security		((byte)71),	//fatal
	internal_error 				((byte)80),	//fatal
	user_canceled 				((byte)90),	//warning
	no_renegotiation 			((byte)100),//warning
	unsupported_extension 		((byte)110);
	
	public static TlsAlert alertFromValue(byte b) {
		for (TlsAlert a : TlsAlert.values()) {
			if (b == a.getValue()) {
				return a;
			}
		}
		throw new IllegalArgumentException("No Alert for value " + b + "!");
	}	
		
	private byte _value;
		
	private TlsAlert(byte value) {
		_value = value;
	}
	
	public byte getValue() {
		return _value;
	}
}