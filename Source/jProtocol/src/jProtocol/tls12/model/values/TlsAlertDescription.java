package jProtocol.tls12.model.values;

import java.util.HashMap;
import java.util.Map;

public class TlsAlertDescription {
	
	public enum Alert {
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
		unsupported_extension		
	}
	
	private static Map<Alert, Byte> _alerts;
	
	static {
		_alerts = new HashMap<Alert, Byte>();
		
		_alerts.put(Alert.close_notify,					(byte)0); 	
		_alerts.put(Alert.unexpected_message,			(byte)10);	//fatal
		_alerts.put(Alert.bad_record_mac,				(byte)20);	//fatal
		_alerts.put(Alert.decryption_failed_RESERVED,	(byte)21);
		_alerts.put(Alert.record_overflow,				(byte)22);	//fatal
		_alerts.put(Alert.decompression_failure,		(byte)30);	//fatal
		_alerts.put(Alert.handshake_failure,			(byte)40);	//fatal
		_alerts.put(Alert.no_certificate_RESERVED,		(byte)41);
		_alerts.put(Alert.bad_certificate,				(byte)42);
		_alerts.put(Alert.unsupported_certificate,		(byte)43);
		_alerts.put(Alert.certificate_revoked,			(byte)44);
		_alerts.put(Alert.certificate_expired,			(byte)45);
		_alerts.put(Alert.certificate_unknown,			(byte)46);
		_alerts.put(Alert.illegal_parameter,			(byte)47);	//fatal
		_alerts.put(Alert.unknown_ca,					(byte)48);
		_alerts.put(Alert.access_denied,				(byte)49);	//fatal
		_alerts.put(Alert.decode_error,					(byte)50);	//fatal
		_alerts.put(Alert.decrypt_error,				(byte)51);	//fatal
		_alerts.put(Alert.export_restriction_RESERVED,	(byte)60);
		_alerts.put(Alert.protocol_version,				(byte)70);	//fatal
		_alerts.put(Alert.insufficient_security,		(byte)71);	//fatal
		_alerts.put(Alert.internal_error,				(byte)80);	//fatal
		_alerts.put(Alert.user_canceled,				(byte)90);	//warning
		_alerts.put(Alert.no_renegotiation,				(byte)100);	//warning
		_alerts.put(Alert.unsupported_extension,		(byte)110);	//fatal
	}
	
	public static Alert alertFromValue(byte b) {
		for (Alert a : Alert.values()) {
			if (b == _alerts.get(a)) {
				return a;
			}
		}
		throw new IllegalArgumentException("No Alert for value " + b + "!");
	}
	
	public static byte valueFromAlert(Alert alert) {
		if (alert == null) {
			throw new IllegalArgumentException("Alert must not be null!");
		}
		return _alerts.get(alert);
	}
}