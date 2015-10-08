package jProtocol.Abstract.View.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HtmlAboutLoader {

	/**
	 * Loads HTML about content (contained in the same package).
	 * 
	 * @return the about content or an error message, if the document could not be read
	 */
	public static String getHtmlAboutContent() {
		InputStream in = HtmlAboutLoader.class.getResourceAsStream("about.html");

		if (in != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			StringBuilder sb = new StringBuilder();
			
			try {
			    String line = reader.readLine();
			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = reader.readLine();
			    }
			}
			catch (IOException e) {
				sb.append("Could not read file: " + e.getLocalizedMessage());
			} finally {
			    try {
					reader.close();
				}
				catch (IOException e) { }
			}
			
			return sb.toString();
		}
		else {
			return "Resource not found: about.hmtl";
		}
	}

}
