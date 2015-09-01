package jProtocol.Abstract.View;

import javax.swing.JComponent;
import jProtocol.Abstract.Model.ProtocolDataUnit;

public interface JProtocolViewProvider<T extends ProtocolDataUnit> {
	
	public JComponent getDetailedViewForProtocolDataUnit(T pdu);
	
	//public List<T> getAllProtocolDataUnits();
	//better: store list in Communication Channel;
	
	public JComponent getViewForClientStateMachine();
	
	public JComponent getViewForServerStateMachine();
	
}
