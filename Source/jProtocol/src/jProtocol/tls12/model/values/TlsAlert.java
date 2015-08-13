package jProtocol.tls12.model.values;

import java.util.HashMap;
import java.util.Map;

public enum TlsAlert {
		close_notify,
		unexpected_message,
		bad_record_mac,
		decryption_failed_RESERVED,
		record_overflow,
		decompression_failure,
		handshake_failure,
		no_certificate_RESERVED,
		bad_certificate,
		unsupported_certificate,
		certificate_revoked,
		certificate_expired,
		certificate_unknown,
		illegal_parameter,
		unknown_ca,
		access_denied,
		decode_error,
		decrypt_error,
		export_restriction_RESERVED,
		protocol_version,
		insufficient_security,
		internal_error,
		user_canceled,
		no_renegotiation,
		unsupported_extension;
	
	private static Map<TlsAlert, Byte> _alerts;
	
	static {
		_alerts = new HashMap<TlsAlert, Byte>();
		
		_alerts.put(TlsAlert.close_notify,					(byte)0); 	
		_alerts.put(TlsAlert.unexpected_message,			(byte)10);	//fatal
		_alerts.put(TlsAlert.bad_record_mac,				(byte)20);	//fatal
		_alerts.put(TlsAlert.decryption_failed_RESERVED,	(byte)21);
		_alerts.put(TlsAlert.record_overflow,				(byte)22);	//fatal
		_alerts.put(TlsAlert.decompression_failure,			(byte)30);	//fatal
		_alerts.put(TlsAlert.handshake_failure,				(byte)40);	//fatal
		_alerts.put(TlsAlert.no_certificate_RESERVED,		(byte)41);
		_alerts.put(TlsAlert.bad_certificate,				(byte)42);
		_alerts.put(TlsAlert.unsupported_certificate,		(byte)43);
		_alerts.put(TlsAlert.certificate_revoked,			(byte)44);
		_alerts.put(TlsAlert.certificate_expired,			(byte)45);
		_alerts.put(TlsAlert.certificate_unknown,			(byte)46);
		_alerts.put(TlsAlert.illegal_parameter,				(byte)47);	//fatal
		_alerts.put(TlsAlert.unknown_ca,					(byte)48);
		_alerts.put(TlsAlert.access_denied,					(byte)49);	//fatal
		_alerts.put(TlsAlert.decode_error,					(byte)50);	//fatal
		_alerts.put(TlsAlert.decrypt_error,					(byte)51);	//fatal
		_alerts.put(TlsAlert.export_restriction_RESERVED,	(byte)60);
		_alerts.put(TlsAlert.protocol_version,				(byte)70);	//fatal
		_alerts.put(TlsAlert.insufficient_security,			(byte)71);	//fatal
		_alerts.put(TlsAlert.internal_error,				(byte)80);	//fatal
		_alerts.put(TlsAlert.user_canceled,					(byte)90);	//warning
		_alerts.put(TlsAlert.no_renegotiation,				(byte)100);	//warning
		_alerts.put(TlsAlert.unsupported_extension,			(byte)110);	//fatal
	}
	
	public static TlsAlert alertFromValue(byte b) {
		for (TlsAlert a : TlsAlert.values()) {
			if (b == _alerts.get(a)) {
				return a;
			}
		}
		throw new IllegalArgumentException("No Alert for value " + b + "!");
	}
	
	public byte getValue() {
		return _alerts.get(this);
	}
}