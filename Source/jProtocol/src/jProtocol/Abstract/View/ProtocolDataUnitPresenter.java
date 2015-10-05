package jProtocol.Abstract.View;

import jProtocol.Abstract.ViewProvider;
import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;

public class ProtocolDataUnitPresenter<T extends ProtocolDataUnit> implements Observer {

	private ProtocolDataUnitView<T> _view;
	private CommunicationChannel<T> _channel;
	
	/**
	 * A presenter for the protocol data unit view.
	 * 
	 * @param provider a view provider
	 * @param channel the communication channel which transfers the protocol data units
	 * @param htmlInfoUpdate an info updater to set the info view content
	 */
	public ProtocolDataUnitPresenter(ViewProvider<T> provider, CommunicationChannel<T> channel, HtmlInfoUpdater htmlInfoUpdate) {
		_channel = channel;
		_view = new ProtocolDataUnitView<T>(provider, htmlInfoUpdate);
		
		channel.addObserver(this);
	}
	
	/**
	 * Returns the view for the protocol data unit.
	 * 
	 * @return the view.
	 */
	public JComponent getView() {
		return _view.getView();
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		List<T> pdus = _channel.getSentProtocolDataUnits();
		T pduToSend = _channel.getPduToSend();
		
		_view.setProtocolDataUnitList(pdus, pduToSend);
	}
}
