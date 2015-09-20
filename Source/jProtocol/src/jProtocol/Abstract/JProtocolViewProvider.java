package jProtocol.Abstract;

import javax.swing.JComponent;
import jProtocol.Abstract.Model.ProtocolDataUnit;

public interface JProtocolViewProvider<T extends ProtocolDataUnit> {
	
	public JComponent getDetailedViewForProtocolDataUnit(T pdu);
	
	public JComponent getViewForClientStateMachine();
	
	public JComponent getViewForServerStateMachine();
	
	public void updateServerView();
	
	public void updateClientView();
}
