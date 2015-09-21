package jProtocol.Abstract;

import javax.swing.JComponent;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.View.HtmlInfoUpdater;

public interface JProtocolViewProvider<T extends ProtocolDataUnit> {
	
	public JComponent getDetailedViewForProtocolDataUnit(T pdu, HtmlInfoUpdater htmlInfoUpdater);
	
	public JComponent getViewForClientStateMachine(HtmlInfoUpdater htmlInfoUpdater);
	
	public JComponent getViewForServerStateMachine(HtmlInfoUpdater htmlInfoUpdater);
	
	public void updateServerView();
	
	public void updateClientView();
}
