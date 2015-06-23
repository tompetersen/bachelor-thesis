package jProtocol.SimpleEchoProtocol.View;

import jProtocol.Abstract.View.ProtocolDataUnitPresenter;
import jProtocol.SimpleEchoProtocol.Model.EchoProtocolDataUnit;

public class EchoProtocolDataUnitPresenter extends ProtocolDataUnitPresenter<EchoProtocolDataUnit, EchoProtocolDataUnitView> {

	public EchoProtocolDataUnitPresenter(EchoProtocolDataUnitView view) {
		super(view);
	}

	@Override
	protected void updateViewWithProtocolDataUnit(EchoProtocolDataUnit pdu,
			EchoProtocolDataUnitView view) {
		view.setPayload(pdu.getPayload());
		view.setVersion(pdu.getVersion());
	}
}
