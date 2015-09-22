package jProtocol.Abstract;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.View.HtmlInfoUpdater;
import javax.swing.JComponent;

public interface JProtocolViewProvider<T extends ProtocolDataUnit> {
	
	/**
	 * Should return a view with details of the protocol data unit.
	 * 
	 * @param pdu the protocol data unit
	 * @param htmlInfoUpdater an updater to set the info window content
	 * 
	 * @return the detailed view 
	 */
	public JComponent getDetailedViewForProtocolDataUnit(T pdu, HtmlInfoUpdater htmlInfoUpdater);
	
	/**
	 * Should return a view displaying the client state.
	 * 
	 * @param htmlInfoUpdater an updater to set the info window content
	 * 
	 * @return the client view
	 */
	public JComponent getViewForClientStateMachine(HtmlInfoUpdater htmlInfoUpdater);

	/**
	 * Should return a view displaying the server state.
	 * 
	 * @param htmlInfoUpdater an updater to set the info window content
	 * 
	 * @return the server view
	 */
	public JComponent getViewForServerStateMachine(HtmlInfoUpdater htmlInfoUpdater);
	
	/**
	 * Will be called when the server state machines notifyObserversOfStateChanged method is called.
	 * The server view should be updated to display the new state.
	 */
	public void updateServerView();
	
	/**
	 * Will be called when the client state machines notifyObserversOfStateChanged method is called.
	 * The client view should be updated to display the new state.
	 */
	public void updateClientView();
}
