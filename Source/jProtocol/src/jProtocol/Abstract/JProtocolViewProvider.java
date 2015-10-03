package jProtocol.Abstract;

import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.View.HtmlInfoUpdater;
import javax.swing.JComponent;

/**
 * A view provider provides views which will be shown in the main application.
 * It needs views for the client, server and a protocol data unit dependent
 * detail view, as well as methods to handle client and server view updates.
 * 
 * @author Tom Petersen
 *
 * @param <T> The protocol data unit the protocol uses.
 */
public interface JProtocolViewProvider<T extends ProtocolDataUnit> {
	
	/**
	 * Should return a view with details of the protocol data unit. This method will be called for
	 * every protocol data unit which is selected in the protocol data unit list.
	 * 
	 * @param pdu the protocol data unit
	 * @param htmlInfoUpdater an updater to set the info view content
	 * 
	 * @return the detailed view 
	 */
	public JComponent getDetailedViewForProtocolDataUnit(T pdu, HtmlInfoUpdater htmlInfoUpdater);
	
	/**
	 * Should return a view displaying the client state. This method will be only be called once
	 * on application startup, so the view should be stored to react to state updates.
	 * 
	 * @param htmlInfoUpdater an updater to set the info view content
	 * 
	 * @return the client view
	 */
	public JComponent getViewForClientStateMachine(HtmlInfoUpdater htmlInfoUpdater);

	/**
	 * Should return a view displaying the server state. This method will only be called once
	 * on application startup, so the view should be stored to react to state updates.
	 * 
	 * @param htmlInfoUpdater an updater to set the info view content
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
	
	/**
	 * Should return a view displaying (optional) settings for the protocol. Will be shown in 
	 * the start protocol view. Settings changes must be handled by the provider itself.  
	 * 
	 * @return a preferences view
	 */
	public JComponent getSettingsView();
}
