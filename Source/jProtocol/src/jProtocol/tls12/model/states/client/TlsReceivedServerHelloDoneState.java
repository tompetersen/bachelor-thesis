package jProtocol.tls12.model.states.client;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.messages.TlsChangeCipherSpecMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage_RSA;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine.TlsStateType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsRsaEncryptedPreMasterSecret;
import jProtocol.tls12.model.values.TlsVerifyData;
import jProtocol.tls12.model.values.TlsVersion;

public class TlsReceivedServerHelloDoneState extends TlsState {

	public TlsReceivedServerHelloDoneState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return false;
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		throw new RuntimeException("Client should not receive messages in ReceivedServerHelloDoneState!");
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE);
		
		sendClientKeyExchangeMessage();
		sendChangeCipherSpecMessage();
		sendFinishedMessage();
	}
	
	private void sendClientKeyExchangeMessage() {
		TlsKeyExchangeAlgorithm algorithm = _stateMachine.getPendingCipherSuite().getKeyExchangeAlgorithm();
		
		if (algorithm == TlsKeyExchangeAlgorithm.rsa) {
			TlsVersion version = _stateMachine.getVersion(); 
			byte[] preMasterSecret = ByteHelper.concatenate(version.getBytes(), TlsPseudoRandomNumberGenerator.nextBytes(46));
			_stateMachine.computePendingMasterSecret(preMasterSecret);
			
			sendRsaClientKeyExchangeMessage(preMasterSecret);
		}
		else {
			throw new RuntimeException("Key exchange algorithm [" + algorithm.toString() + "] not implemented yet!");
		}
	}
	
	private void sendRsaClientKeyExchangeMessage(byte[] premastersecret) {
		try {
			byte[] encryptedPreMasterSecretBytes = _stateMachine.getRsaCipher().encrypt(premastersecret);
			TlsRsaEncryptedPreMasterSecret encPreMasterSecret = new TlsRsaEncryptedPreMasterSecret(encryptedPreMasterSecretBytes);
			TlsClientKeyExchangeMessage_RSA message = new TlsClientKeyExchangeMessage_RSA(encPreMasterSecret);
			
			_stateMachine.addHandshakeMessageForVerifyData(message);
			sendTlsMessage(message);
		}
		catch (TlsAsymmetricOperationException e) {
			setTlsState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE);
		}
	}

	private void sendChangeCipherSpecMessage() {
		TlsMessage message = new TlsChangeCipherSpecMessage();
		sendTlsMessage(message);
		
		_stateMachine.changeToPendingState();
	}
	
	private void sendFinishedMessage() {
		TlsVerifyData verifyData = _stateMachine.getVerifyDataToSend();
		TlsFinishedMessage message = new TlsFinishedMessage(verifyData);
		sendTlsMessage(message);
	}
}
