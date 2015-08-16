package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType;

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
	
	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.client_key_exchange;
	}

}
