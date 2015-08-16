package jProtocol.helper;

import java.util.logging.Logger;

public class MyLogger {

	private static Logger _logger = Logger.getLogger("de.uni-hamburg.informatik.jprotocol");
	
	public static void info(String message) {
		_logger.info(message);
	}
	
	public static void severe(String message) {
		_logger.severe(message);
	}

	public static void warning(String message) {
		_logger.warning(message);
	}
}
