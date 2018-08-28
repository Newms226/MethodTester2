package driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.FileAppender;

public class Driver {
	public final static Logger ROOT_LOGGER;
	
	public final static Logger TEST_CLIENT_LOGGER;
	
	public static final String TEST_CLIENT_LOGGER_STRING;
	
	public static final Appender DEFAULT_CONSOLE_APPENDER; 
	
	static {
		ROOT_LOGGER = LogManager.getRootLogger();
		TEST_CLIENT_LOGGER_STRING = TestClient.class.toString();
		TEST_CLIENT_LOGGER = LogManager.getLogger(TEST_CLIENT_LOGGER_STRING);
		
		
	}

	private Driver() {}

}
