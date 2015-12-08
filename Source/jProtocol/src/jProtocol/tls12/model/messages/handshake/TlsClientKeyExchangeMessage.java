package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;

public abstract class TlsClientKeyExchangeMessage extends TlsHandshakeMessage {

	/*
	 struct {
          select (KeyExchangeAlgorithm) {
              case rsa:
                  EncryptedPreMasterSecret;
              case dhe_dss:
              case dhe_rsa:
              case dh_dss:
              case dh_rsa:
              case dh_anon:
                  ClientDiffieHellmanPublic;
          } exchange_keys;
      } ClientKeyExchange;
	 */
	
	/**
	 * Creates a client key exchange message by parsing sent bytes dependent on the current key exchange algorithm.
	 * 
	 * @param unparsedMessage the sent bytes
	 * @param algorithm the key exchange algorithm used in this connection
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 * 
	 * @return the parsed client key exchange message
	 */
	public static TlsClientKeyExchangeMessage parseClientKeyExchangeMessage(byte[] unparsedMessage, TlsKeyExchangeAlgorithm algorithm) throws TlsDecodeErrorException {
		if (algorithm == TlsKeyExchangeAlgorithm.rsa) {
			return new TlsClientKeyExchangeMessage_RSA(unparsedMessage);
		}
		else if (algorithm == TlsKeyExchangeAlgorithm.dhe_rsa) {
			return new TlsClientKeyExchangeMessage_DHE(unparsedMessage);
		}
		else {
			//TODO: Used for key exchange other than RSA or DHE_RSA -> Implement for other key exchange algorithms
			throw new UnsupportedOperationException("Parsing for key exchange algorithm " + algorithm.toString() + " not implemented yet!");
		}
	}
	
	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.client_key_exchange;
	}
}
