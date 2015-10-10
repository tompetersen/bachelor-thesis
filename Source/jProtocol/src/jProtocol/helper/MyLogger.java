package jProtocol.helper;

import java.util.logging.Logger;

public class MyLogger {

	private static Logger _logger = Logger.getLogger("de.uni-hamburg.informatik.jprotocol");
	
	/**
	 * Logs an info message.
	 * 
	 * @param message the message
	 */
	public static void info(String message) {
		_logger.info(message);
	}
	
	/**
	 * Logs a severe message.
	 * 
	 * @param message the message
	 */
	public static void severe(String message) {
		_logger.severe(message);
	}

	/**
	 * Logs a warning message.
	 *  
	 * @param message the message
	 */
	public static void warning(String message) {
		_logger.warning(message);
	}
}
