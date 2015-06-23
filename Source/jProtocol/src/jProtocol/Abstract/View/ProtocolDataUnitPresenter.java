package jProtocol.Abstract.View;

import jProtocol.Abstract.Model.ProtocolDataUnit;

import javax.swing.JComponent;

public abstract class ProtocolDataUnitPresenter<T extends ProtocolDataUnit, V extends ProtocolDataUnitView> {

	private T _pdu;
	private V _view;
	
	public ProtocolDataUnitPresenter(V view) {
		_view = view;
	}
	
	public void setProtocolDataUnit(T pdu) {
		_pdu = pdu;
		updateViewWithProtocolDataUnit(pdu, _view);
	}
	
	protected abstract void updateViewWithProtocolDataUnit(T pdu, V view);
	
	public JComponent getView() {
		return _view.getView();
	}
	
}
