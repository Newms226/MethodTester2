package test;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.spi.LoggerContext;

import lap.Laps;
import menu.ConsoleMenu;
import tools.Log4JTools;
import tools.NumberTools;
import util.Averager;

public class LongValidifier {
	public static final int DEFAULT_INITAL_SIZE = 100;
	
	public static final int MAX_SIZE = Integer.MAX_VALUE - 8;
	
	private static final Logger log = LogManager.getLogger();

	
	private ArrayList<TestPair<Long>> elements;
	
	private TestPair<Long> current;
	
	private boolean expectedSet;
	
	public LongValidifier() {
		log.traceEntry("new()");
		initalize(DEFAULT_INITAL_SIZE);
	}
	
	public LongValidifier(int initalSize) {
		log.traceEntry("new({})", initalSize);
		if (initalSize <= 0) {
			throw log.throwing(new IllegalArgumentException("Size must be greater"
					+ " than zero: " + initalSize));
		} else if (initalSize > MAX_SIZE) {
			log.warn("Size to large, defaulting to MAX_SIZE: "
					+ NumberTools.format(initalSize));
			initalSize = MAX_SIZE;
		}
		
		initalize(initalSize);
	}
	
	private void initalize(int size) {
		current = new TestPair<>();
		elements = new ArrayList<>(size);
		log.trace("Generated new " + getClass() + " with size " 
				+ NumberTools.format(size));
	}
	
	public void setExpected(long value) {
		log.traceEntry("setExpected(" + NumberTools.format(value) + ")");
		current.expected = value;
		expectedSet = true;
	}
	
	public void setResult(long value) throws IllegalArgumentException {
		log.traceEntry("setResult(" + NumberTools.format(value) + ")");
		if (expectedSet) {
			log.trace("Expected was set");
			current.result = value;
			elements.add(current);
			current = new TestPair<>();
			expectedSet = false;
		} else {
			throw log.throwing(new IllegalArgumentException("Expected was not "
					+ "set. Cannot set result"));
		}
		
		log.traceExit();
	}
	
	static class Validity implements Consumer<TestPair<Long>> {
		static final Marker PASS = MarkerManager.getMarker("validity.pass");
		static final Marker FAIL = MarkerManager.getMarker("validity.fail");
		static final Marker ZERO = MarkerManager.getMarker("validity.zero");
		
		static final double MAX_PERCENT_RANGE = 1;
		
		private int passCount, count;
		
		private double acceptableRange;

		Validity(double acceptableRange) throws IllegalArgumentException {
			log.traceEntry("new({})", NumberTools.format(acceptableRange));
			if (acceptableRange < 0 || acceptableRange > MAX_PERCENT_RANGE) {
				throw log.throwing(new IllegalArgumentException("Acceptable "
						+ "range must be between 0 & 1"));
			}
			this.acceptableRange = acceptableRange;
			log.traceExit();
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
				System.out.println(testPair + " FAILED");
				log.info(FAIL, testPair + " fell outside the acceptable range.");
			}
			
			log.traceExit();
		}
		
		double getPercentPass() {
			return log.traceExit("percent pass: {}", ((double) passCount / count));
		}
		
		public String toString() {
			return "Pass: " + NumberTools.format(passCount) + " Total: " 
					+ NumberTools.format(count) + " Acceptable Range: " 
					+ NumberTools.formatPercent(acceptableRange);
		}
	}
	
	
	
	private static void timeAdditionAndValidation(int runFor) {
		LongValidifier test = new LongValidifier();
		long startTime = System.nanoTime();
		for (int i = 0; i < runFor; i++) {
			test.setExpected(i);
			test.setResult(i);
		}
		long endTime = System.nanoTime();
		System.out.println("Added " + runFor + " results in " + Laps.nanosecondsToString(endTime - startTime));
		
		startTime = System.nanoTime();
		Validity x = test.elements.stream().collect(() -> new Validity(.1), Validity::accept, Validity::combine);
		endTime = System.nanoTime();
		System.out.println("Validated "+ runFor + " results in " + Laps.nanosecondsToString(endTime - startTime));
		System.out.println();
		System.out.println(x.getPercentPass());
		System.out.println("Pass: " + x.passCount);
	}
}
