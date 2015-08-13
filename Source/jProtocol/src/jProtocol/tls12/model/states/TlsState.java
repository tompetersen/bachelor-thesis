package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.State;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsContentType.ContentType;
import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public abstract class TlsState extends State<TlsCiphertext> {
	
	protected TlsStateMachine _stateMachine; 
	
	public TlsState(TlsStateMachine stateMachine) {
		super(stateMachine);
		
		_stateMachine = stateMachine;
	}

	@Override
	public void receiveMessage(TlsCiphertext ciphertext) {
		TlsPlaintext plaintext = null;
		try {
			plaintext = _stateMachine.ciphertextToPlaintext(ciphertext);
		} catch (TlsBadRecordMacException e) {
			setState(TlsStateMachine.RECEIVED_BAD_RECORD_MESSAGE_STATE);
		} catch (TlsBadPaddingException e) {
			setState(TlsStateMachine.RECEIVED_BAD_RECORD_MESSAGE_STATE);
		}
		
		TlsMessage message = plaintext.getMessage();
		
		if (expectedTlsMessage(message)) {
			receivedTlsMessage(plaintext.getMessage());
		}
		else {
			setState(TlsStateMachine.RECEIVED_UNEXPECTED_MESSAGE_STATE);
		}
	}
	
	public abstract boolean expectedTlsMessage(TlsMessage message);
	
	public abstract void receivedTlsMessage(TlsMessage message);

	public void sendTlsMessage(TlsMessage message) {
		TlsPlaintext plaintext = new TlsPlaintext(message, _stateMachine.getVersion());
		TlsCiphertext ciphertext = _stateMachine.plaintextToCiphertext(plaintext);
		
		super.sendMessage(ciphertext);
	}
	
	public void setState(int state) {
		_stateMachine.setState(state, this);
	}
	
	public boolean isHandshakeMessageOfType(TlsMessage m, HandshakeType type) {
		return (m.getContentType() == ContentType.Handshake) && (((TlsHandshakeMessage)m).getHandshakeType() == type);
	}
	
	public boolean isChangeCipherSpecMessage(TlsMessage m) {
		return (m.getContentType() == ContentType.ChangeCipherSpec);
	}
	
	public boolean isAlertMessage(TlsMessage m) {
		return (m.getContentType() == ContentType.Alert);
	}
	
	public boolean isApplicationDataMessage(TlsMessage m) {
		return (m.getContentType() == ContentType.ApplicationData);
	}
}
