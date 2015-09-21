package jProtocol.Abstract.View;

public class DefaultHtmlInfoUpdater implements HtmlInfoUpdater {

	private HtmlInfoView _infoView;
	
	public DefaultHtmlInfoUpdater() {
		super();
		
		_infoView = new HtmlInfoView();
	}

	public void setInfoHtmlBodyContent(String htmlInfoContent) {
		_infoView.setHtmlBodyContent(htmlInfoContent);
	}
	
	public HtmlInfoView getHtmlInfoView() {
		return _infoView;
	}

	@Override
	public void setInfoHtmlContent(String htmlInfoContent) {
		_infoView.setHtmlContent(htmlInfoContent);
	}

}
