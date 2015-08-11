package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.State;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.messages.TlsMessage;

public abstract class TlsState extends State<TlsCiphertext> {
	
	private TlsStateMachine _stateMachine; 
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TlsBadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		receivedTlsMessage(plaintext.getMessage());
	}
	
	public abstract void receivedTlsMessage(TlsMessage message);

	public void sendTlsMessage(TlsMessage message) {
		TlsPlaintext plaintext = new TlsPlaintext(message, _stateMachine.getVersion());
		TlsCiphertext ciphertext = _stateMachine.plaintextToCiphertext(plaintext);
		
		super.sendMessage(ciphertext);
	}

	public void setState(int state) {
		_stateMachine.setState(state);
	}
}
