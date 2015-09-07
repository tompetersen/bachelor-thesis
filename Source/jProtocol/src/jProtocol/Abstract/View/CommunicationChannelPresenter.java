package jProtocol.Abstract.View;

import java.util.Observable;
import java.util.Observer;
import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.Model.events.ChannelReceivedMessageEvent;
import jProtocol.Abstract.View.CommunicationChannelView.CommunicationChannelViewListener;
import javax.swing.JComponent;

public class CommunicationChannelPresenter<T extends ProtocolDataUnit> implements Observer, CommunicationChannelViewListener {

	private CommunicationChannelView _view;
	private CommunicationChannel<T> _channel;
	
	public CommunicationChannelPresenter(CommunicationChannel<T> channel) {
		_view = new CommunicationChannelView(this);
		_channel = channel;
	}
	
	public JComponent getView() {
		return _view.getView();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ChannelReceivedMessageEvent && o == _channel) {
			_view.enableNextStepView();
		}
	}

	@Override
	public void nextStepClicked() {
		_channel.sendNextMessage();
	}

	@Override
	public void nextStepsClicked() {
		_channel.sendAllMessagesWithoutBreak();
	}

}
