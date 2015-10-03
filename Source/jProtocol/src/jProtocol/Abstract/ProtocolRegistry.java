package jProtocol.Abstract;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.tls12.Tls12Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtocolRegistry {

	private Map<String, JProtocolBuilder<? extends ProtocolDataUnit>> _protocolMap;
	
	public ProtocolRegistry() {
		_protocolMap = new HashMap<>();
		
		//TODO: register protocols
		
		Tls12Provider provider = new Tls12Provider();
		_protocolMap.put("TLS 1.2", new JProtocolBuilder<>(provider, provider));
	}
	
	public List<String> getProtocolList() {
		return new ArrayList<>(_protocolMap.keySet());
	}
	
	public JProtocolBuilder<? extends ProtocolDataUnit> getProtocolBuilderForName(String name) {
		return _protocolMap.get(name);
	}
	
}
