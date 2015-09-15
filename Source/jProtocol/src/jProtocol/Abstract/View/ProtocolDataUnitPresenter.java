package jProtocol.Abstract.View;

import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;

public class ProtocolDataUnitPresenter<T extends ProtocolDataUnit> implements Observer {

	private ProtocolDataUnitView<T> _view;
	private CommunicationChannel<T> _channel;
	
	public ProtocolDataUnitPresenter(JProtocolViewProvider<T> provider, CommunicationChannel<T> channel) {
		_channel = channel;
		_view = new ProtocolDataUnitView<T>(provider);
		
		channel.addObserver(this);
	}
	
	public JComponent getView() {
		return _view.getView();
	}

	@Override
	public void update(Observable o, Object arg) {
		List<T> pdus = _channel.getSentProtocolDataUnits();
		T pduToSend = _channel.getPduToSend();
		
		_view.setProtocolDataUnits(pdus, pduToSend);
	}
}
