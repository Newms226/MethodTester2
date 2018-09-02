package test;

import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import tools.Log4JTools;
import tools.NumberTools;

class Validity implements Consumer<TestPair<Long>> {
	
	/**
	 * Log4J logger for diagnostics
	 */
	private static final Logger log = LogManager.getLogger();
	
	/**
	 * Marker to indicate that a {@code TestPair} has passed the test
	 */
	static final Marker PASS = MarkerManager.getMarker("validity.pass");
	
	/**
	 * Marker to indicate that a {@code TestPair} has failed the test
	 */
	static final Marker FAIL = MarkerManager.getMarker("validity.fail");
	
	/**
	 * Marker to indicate that a {@code TestPair} is valid but represents 0
	 * in both its expected and resulting fields. Note that the calculation
	 * in {@link Validity#accept(TestPair)} uses 
	 * {@link NumberTools#percentDifference(double, double)} which involves
	 * dividing by zero. This would lead to a false negative when 
	 * Evaluating a {@code TestPair} with both fields set to 0; 
	 */
	static final Marker ZERO = MarkerManager.getMarker(
			"validity.pass.zero");
	
	static final double MAX_PERCENT_RANGE = 1;
	
	int passCount, count;

	private double acceptableRange;

	Validity(double acceptableRange) throws IllegalArgumentException {
		log.traceEntry("new({})", NumberTools.format(acceptableRange));
	
		this.acceptableRange = rangeTest(acceptableRange);
		log.traceExit();
	}
	
	static double rangeTest(double range) throws IllegalArgumentException {
		log.traceEntry("rangeTest({})", range);
		if (range < 0 || range > MAX_PERCENT_RANGE) {
			throw log.throwing(new IllegalArgumentException("Acceptable "
					+ "range must be between 0 & 1"));
		}
		return log.traceExit(Math.abs(range));
	}
	
	void combine(Validity other) {
		log.traceEntry("Combine with {}", other);
		Log4JTools.assertNonNull(other, log);
		
		passCount += other.passCount;
		count += other.count;
		log.traceExit();
	}


	@Override
	public void accept(TestPair<Long> testPair) {
		log.traceEntry("accept({})", testPair);
		Log4JTools.assertNonNull(testPair, log);
		
		if (testPair.getExpected() == 0 && testPair.getResult() == 0) {
			log.trace(ZERO, "Encountered valid test pair of value 0" 
					+ testPair);
			passCount++;
		}
		
		count++;
		if (Math.abs(
				NumberTools.percentDifference(testPair.getExpected(), 
						                      testPair.getResult()))
				<= acceptableRange) {
			passCount++;
			log.info(PASS, testPair + " fell within the acceptable range.");
		} else {
//			System.out.println(testPair + " FAILED");
			log.info(FAIL, testPair + " fell outside the acceptable range.");
		}
		
		log.traceExit();
	}
	
	double getPercentPass() {
		return log.traceExit("percent pass: {}", ((double) passCount / count));
	}
	
	public int getPassCount() {
		return passCount;
	}

	public int getCount() {
		return count;
	}
	
	public double getAcceptableRange() {
		return acceptableRange;
	}
	
	public String toString() {
		return "Pass: " + NumberTools.format(passCount) + " Total: " 
				+ NumberTools.format(count) + " Acceptable Range: " 
				+ NumberTools.formatPercent(acceptableRange);
	}
}