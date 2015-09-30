package jProtocol.tls12.model.states;

public enum TlsStateType {
	//Server
	SERVER_INITIAL_STATE							(1),
	SERVER_RECEIVED_CLIENT_HELLO_STATE				(2),
	SERVER_IS_WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE	(3),
	SERVER_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE	(4),
	SERVER_IS_WAITING_FOR_FINISHED_STATE			(5),
	SERVER_RECEIVED_FINISHED_STATE					(6),
	
	//Client
	CLIENT_INITIAL_STATE							(51),
	CLIENT_IS_WAITING_FOR_SERVER_HELLO_STATE		(52),
	CLIENT_IS_WAITING_FOR_CERTIFICATE_STATE			(53),
	CLIENT_IS_WAITING_FOR_SERVER_KEY_EXCHANGE_STATE	(54),
	CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE	(55),
	CLIENT_RECEIVED_SERVER_HELLO_DONE_STATE			(56),
	CLIENT_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE	(57),
	CLIENT_IS_WAITING_FOR_FINISHED_STATE			(58),
	
	//Common
	CONNECTION_ESTABLISHED_STATE					(200),
	RECEIVED_CLOSE_NOTIFY_STATE						(300),
	WAITING_FOR_CLOSE_NOTIFY_STATE					(301),
	
	//Alert
	RECEIVED_UNEXPECTED_MESSAGE_STATE				(410),
	RECEIVED_BAD_RECORD_MESSAGE_STATE				(420),
	DECODE_ERROR_OCCURED_STATE						(450),
	DECRYPT_ERROR_OCCURED_STATE						(451);
	
	private int _type;
	
	private TlsStateType(int type) {
		_type = type;
	}

	public int getType() {
		return _type;
	}
	
	public boolean isInitialState() {
		return (_type == 1 || _type == 51);
	}
	
	public boolean isHandshakeState() {
		return (_type > 0 && _type < 100);
	}
	
	public boolean isEstablishedState() {
		return _type == 200;
	}
	
	public boolean isCloseState() {
		return _type >= 300 && _type < 400;
	}
	
	public boolean isErrorState() {
		return _type >= 400;
	}
}