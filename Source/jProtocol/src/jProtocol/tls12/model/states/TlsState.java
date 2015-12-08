package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.State;
import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsAlert;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsHandshakeType;

public abstract class TlsState extends State<TlsCiphertext> {
	
	protected TlsStateMachine _stateMachine; 
	
	/**
	 * Creates a TLS state.
	 * 
	 * @param stateMachine the state machine this state belongs to
	 */
	public TlsState(TlsStateMachine stateMachine) {
		super(stateMachine);
		
		_stateMachine = stateMachine;
	}

	@Override
	public void receiveMessage(TlsCiphertext ciphertext) {
		try {
			byte[] ciphertextBytes = ciphertext.getBytes();
			TlsPlaintext plaintext = _stateMachine.ciphertextToPlaintext(ciphertextBytes);
			
			_stateMachine.increaseReadSequenceNumber();
			
			TlsMessage message = plaintext.getMessage();
			
			if (expectedTlsMessage(message)) {
				receivedTlsMessage(plaintext.getMessage());
			}
			else if (message instanceof TlsAlertMessage){
				handleAlertMessage((TlsAlertMessage)message);
			}
			else {
				MyLogger.severe(message.getContentType().toString() + " unexpected!");
				setTlsState(TlsStateType.RECEIVED_UNEXPECTED_MESSAGE_STATE);
			}
		} catch (TlsBadRecordMacException e) {
			MyLogger.severe(e.getLocalizedMessage());
			setTlsState(TlsStateType.RECEIVED_BAD_RECORD_MESSAGE_STATE);
		} catch (TlsBadPaddingException e) {
			MyLogger.severe(e.getLocalizedMessage());
			setTlsState(TlsStateType.RECEIVED_BAD_RECORD_MESSAGE_STATE);
		} catch (TlsDecodeErrorException e) {
			MyLogger.severe(e.getLocalizedMessage());
			setTlsState(TlsStateType.DECODE_ERROR_OCCURED_STATE);
		}
	}
	
	private void handleAlertMessage(TlsAlertMessage message) {
		MyLogger.severe("Received alert message: " + message.getAlert().toString());
		setTlsState(TlsStateType.RECEIVED_FATAL_ALERT_MESSAGE_STATE);
	}
	
	/**
	 * Messages will only be forwarded to the current state, if this method 
	 * returns true for the message. Otherwise an unexpected message alert 
	 * will be send.
	 * 
	 * @param message the message
	 * 
	 * @return true for each expected message, false otherwise
	 */
	public abstract boolean expectedTlsMessage(TlsMessage message);
	
	/**
	 * Will be called for received messages, where expectedTlsMessage returned true for.
	 * 
	 * @param message the message of expected type
	 */
	public abstract void receivedTlsMessage(TlsMessage message);

	/**
	 * Sends a TLS message from the state machine this state belongs to.
	 * 
	 * @param message the TLS message
	 */
	public void sendTlsMessage(TlsMessage message) {
		TlsPlaintext plaintext = new TlsPlaintext(message, _stateMachine.getVersion());
		TlsCiphertext ciphertext = _stateMachine.plaintextToCiphertext(plaintext);
		
		_stateMachine.increaseWriteSequenceNumber();
		
		_stateMachine.notifyObserversOfStateChanged();
		
		super.sendMessage(ciphertext);
	}
	
	/**
	 * Sets the current state of the state machine to state.
	 * 
	 * @param type the state describing value (TlsStateMachine.TLS_STATE)
	 */
	public void setTlsState(TlsStateType type) {
		_stateMachine.setTlsState(type, this);
	}
	
	/**
	 * Returns whether a message is a handshake message of a specific type. Convenience method. 
	 * 
	 * @param m the message
	 * @param type the handshake type
	 * 
	 * @return true, if the message is a handshake message of type
	 */
	public boolean isHandshakeMessageOfType(TlsMessage m, TlsHandshakeType type) {
		return (m.getContentType() == TlsContentType.Handshake) && (((TlsHandshakeMessage)m).getHandshakeType() == type);
	}
	
	/**
	 * Returns whether a message is a change cipher spec message. Convenience method. 
	 * 
	 * @param m the message
	 * 
	 * @return true, if the message is a change cipher spec message
	 */
	public boolean isChangeCipherSpecMessage(TlsMessage m) {
		return (m.getContentType() == TlsContentType.ChangeCipherSpec);
	}
	
	/**
	 * Returns whether a message is an alert message. Convenience method. 
	 * 
	 * @param m the message
	 * @param alert the alert
	 * 
	 * @return true, if the message is an alert message
	 */
	public boolean isAlertMessageOfType(TlsMessage m, TlsAlert alert) {
		return (m.getContentType() == TlsContentType.Alert) && (((TlsAlertMessage)m).getAlert() == alert);
	}
	
	/**
	 * Returns whether a message is an application data message. Convenience method. 
	 * 
	 * @param m the message
	 * 
	 * @return true, if the message is an application data message
	 */
	public boolean isApplicationDataMessage(TlsMessage m) {
		return (m.getContentType() == TlsContentType.ApplicationData);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
