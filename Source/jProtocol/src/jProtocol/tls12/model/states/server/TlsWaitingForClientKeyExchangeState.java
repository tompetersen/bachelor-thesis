package jProtocol.tls12.model.states.server;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.crypto.TlsRsaCipher;
import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage_RSA;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
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
		else {
			throw new RuntimeException("Key exchange algorithm [" + algorithm.toString() + "] not implemented yet!");
		}
		
		_stateMachine.addHandshakeMessageForVerifyData(clientKeyExchangeMessage);
		setTlsState(TlsStateType.SERVER_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE);
	}
	
	private void setPreMasterSecretFromRsaMessage(TlsClientKeyExchangeMessage_RSA rsaMessage) throws TlsAsymmetricOperationException {
		TlsRsaEncryptedPreMasterSecret encPreMasterSecret = rsaMessage.getRsaEncryptedPreMasterSecret();
		
		TlsRsaCipher cipher = _stateMachine.getRsaCipher();
		byte[] preMasterSecret = cipher.decrypt(encPreMasterSecret.getPreMasterSecret());
		_stateMachine.computeMasterSecret(preMasterSecret);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.client_key_exchange);
	}
}
