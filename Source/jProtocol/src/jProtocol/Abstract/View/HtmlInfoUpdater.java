package jProtocol.Abstract.View;

public interface HtmlInfoUpdater {
	
	/**
	 * Sets the HTML body of the info view. A default Header will be added.
	 * 
	 * @param infoHtmlBodyContent the body content
	 */
	public void setInfoHtmlBodyContent(String infoHtmlBodyContent);
	
	/**
	 * Sets the complete HTML Document of the info view. The HTML header and other tags must be included to get a valid document.
	 * 
	 * @param htmlInfoContent the content
	 */
	public void setInfoHtmlContent(String htmlInfoContent);
}
