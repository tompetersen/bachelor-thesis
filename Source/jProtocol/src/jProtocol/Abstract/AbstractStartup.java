package jProtocol.Abstract;

import jProtocol.Abstract.View.JProtocolPresenter;

public class AbstractStartup {

	public static void main(String[] args) {
		JProtocolPresenter presenter = new JProtocolPresenter();
		presenter.showView();
	}

}
