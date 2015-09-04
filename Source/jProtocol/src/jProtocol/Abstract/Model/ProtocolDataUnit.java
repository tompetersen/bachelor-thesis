package jProtocol.Abstract.Model;

public abstract class ProtocolDataUnit {
	
	private boolean _sentByClient;

	public boolean hasBeenSentByClient() {
		return _sentByClient;
	}

	public void setSentByClient(boolean sentByClient) {
		_sentByClient = sentByClient;
	}
	
	public abstract byte[] getBytes();
}
