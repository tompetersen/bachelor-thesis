package jProtocol.Abstract.View;

/**
 * The default info updater which is used in the whole application and injected in client, server and protocol
 * data unit detail views. The HtmlInfoUpdater interface is used to hide view concerning methods from 
 * using protocols.
 * 
 * @author Tom Petersen, 2015
 */
public class DefaultHtmlInfoUpdater implements HtmlInfoUpdater {

	private HtmlInfoView _infoView;
	
	/**
	 * Creates a info updater.
	 */
	public DefaultHtmlInfoUpdater() {
		super();
		
		_infoView = new HtmlInfoView();
	}

	/**
	 * Returns the html info view.
	 * 
	 * @return the info view
	 */
	public HtmlInfoView getHtmlInfoView() {
		return _infoView;
	}
	
	@Override
	public void setInfoHtmlBodyContent(String htmlInfoContent) {
		_infoView.setHtmlBodyContent(htmlInfoContent);
	}

	@Override
	public void setInfoHtmlContent(String htmlInfoContent) {
		_infoView.setHtmlContent(htmlInfoContent);
	}

}
