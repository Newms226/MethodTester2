package test;

import java.awt.Desktop.Action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tools.Log4JTools;
import tools.NumberTools;

public class TestPair<T> {
	/**
	 * Static factory method. Note that internals of this class will throw a 
	 * <tt>NullPointerException</tt> if either of the parameters are <tt>null</tt>
	 * 
	 * @param expected the expected result
	 * @param result the actual result
	 * 
	 * @return a <tt>TestPair</tt> representing the values passed in.
	 */
	public static <T> TestPair<T> from(T expected, T result) {
		TestPair<T> toReturn = new TestPair<>();
		toReturn.setExpected(expected);
		toReturn.setResult(result);
		return toReturn;
	}
	
	/**
	 * Logger for diagnostics
	 */
	private static final Logger log = LogManager.getLogger();
	
	/**
	 * Expected result
	 */
	private T expected;
	
	/**
	 * Actual result. 
	 */
	private T result;
	
	/**
	 * Default no-arg constructor
	 */
	TestPair() {}
	
	/**
	 * Message for log4j
	 */
	private String setExpectedMessage = "setExpected({})";
	
	/**
	 * Sets the expected value. This implementation DOES NOT allow 
	 * <tt>null</tt> values.
	 * 
	 * @param expected expected value. Non-null
	 */
	void setExpected(T expected) {
		log.traceEntry(setExpectedMessage, expected);
		Log4JTools.assertNonNull(expected, log);
		
		this.expected = expected;
		
		log.traceExit(setExpectedMessage, expected);
	}
	
	/**
	 * Returns the expected value. May return <tt>null</tt> if the value has
	 * not been set.
	 * 
	 * @return the expected value, or <tt>null</tt> if the value wasn't set.
	 */
	T getExpected() {
		return log.traceExit(expected);
	}
	
	/**
	 * Message for log4j
	 */
	private String setResultMessage = "setResult({})";
	
	/**
	 * Sets the actual, resulting value. This implementation DOES NOT allow 
	 * <tt>null</tt> values.
	 * 
	 * @param result actual, resulting value. Non-null
	 */
	void setResult(T result) {
		log.traceEntry(setResultMessage, result);
		Log4JTools.assertNonNull(result, log);
		
		this.result = result;
		
		log.traceExit(setResultMessage, result);
	}
	
	/**
	 * Returns the result value. May return <tt>null</tt> if the value has
	 * not been set.
	 * 
	 * @return the result value, or <tt>null</tt> if the value wasn't set.
	 */
	T getResult() {
		return log.traceExit(result);
	}
	
	/**
	 * Tests whether the values of result & expected are EXACTLY equal. Note 
	 * that this method uses the {@link Comparable#compareTo(Object)} method
	 * and thus may have unexpected results if the generic type underlying this
	 * class is not consistent with equals, as defined by Comparable.
	 * 
	 * @return expected == result
	 */
	boolean pass() {
		log.traceEntry();
		if (expected != null && result != null) {
			return log.traceExit(expected.equals(result));
		}
		
		log.info("At least one object was null. E: {} R: {}", expected, result);
		return log.traceExit(false);
	}
	
	public TestPair<T> clone(){
		TestPair<T> toReturn = new TestPair<>();
		if (expected != null) {
			toReturn.setExpected(expected);
			toReturn.setResult(result);
		}
		return toReturn;
	}
	
	public String toCSV() {
		return expected + "," + result;
	}
	
	//TODO: Error checking, imperfect user, GENERIC
	public static TestPair<Long> fromLongCSV(String str){
		String[] values = str.split(",");
		return TestPair.from(Long.parseLong(values[0]), Long.parseLong(values[1]));
	}
	
	public String toString() {
		return "Expected: " + expected + " Actual: " + result;
	}
}
