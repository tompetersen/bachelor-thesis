package jProtocol.tls12.htmlinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TlsHtmlInfoLoader {

	/**
	 * Loads HTML documents from the current package  
	 * 
	 * @param filepath the filename and path of the document
	 * 
	 * @return the document content or an error message, if the document could not be read
	 */
	public static String loadHtmlInfoForFileName(String filepath) {
		InputStream in = TlsHtmlInfoLoader.class.getResourceAsStream(filepath);

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
			return "Resource not found: " + filepath;
		}
	}

}
