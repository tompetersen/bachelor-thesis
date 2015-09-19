package jProtocol.tls12.model.states.server;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.crypto.TlsRsaCipher;
import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage_DHE;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage_RSA;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsRsaEncryptedPreMasterSecret;

public class TlsWaitingForClientKeyExchangeState extends TlsState {

	public TlsWaitingForClientKeyExchangeState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		TlsClientKeyExchangeMessage clientKeyExchangeMessage = (TlsClientKeyExchangeMessage)message;
		
		MyLogger.info("Received Client Key Exchange!");
		
		TlsKeyExchangeAlgorithm algorithm = _stateMachine.getPendingCipherSuite().getKeyExchangeAlgorithm();
		if (algorithm == TlsKeyExchangeAlgorithm.rsa) {
			try {
				setPreMasterSecretFromRsaMessage((TlsClientKeyExchangeMessage_RSA)message);
			}
			catch (TlsAsymmetricOperationException e) {
				setTlsState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE);
				return;
			}
		}
		else if (algorithm == TlsKeyExchangeAlgorithm.dhe_rsa) {
			setPreMasterSecretFromDheMessage((TlsClientKeyExchangeMessage_DHE)message);
		}
		else {
			throw new UnsupportedOperationException("Key exchange algorithm [" + algorithm.toString() + "] not implemented yet!");
		}
		
		_stateMachine.addHandshakeMessageForVerifyData(clientKeyExchangeMessage);
		setTlsState(TlsStateType.SERVER_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE);
	}
	
	private void setPreMasterSecretFromRsaMessage(TlsClientKeyExchangeMessage_RSA rsaMessage) throws TlsAsymmetricOperationException {
		TlsRsaEncryptedPreMasterSecret encPreMasterSecret = rsaMessage.getRsaEncryptedPreMasterSecret();
		
		TlsRsaCipher cipher = _stateMachine.getRsaCipher();
		byte[] preMasterSecret = cipher.decrypt(encPreMasterSecret.getEncryptedPreMasterSecret());
		_stateMachine.computeMasterSecret(preMasterSecret);
	}
	
	private void setPreMasterSecretFromDheMessage(TlsClientKeyExchangeMessage_DHE dheMessage) {
		TlsClientDhPublicKey clientDhPublicKey = dheMessage.getDiffieHellmenClientPublicKey(); 
		
		try {
			_stateMachine.computePreMasterSecretForServerDhKeyAgreement(clientDhPublicKey);
		}
		catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.client_key_exchange);
	}
}
