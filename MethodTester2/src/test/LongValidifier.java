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
	
	public static final double DEFAULT_ACCEPTABLE_RANGE = .1;
	
	public static final int MAX_SIZE = Integer.MAX_VALUE - 8;
	
	private static final Logger log = LogManager.getLogger();

	
	private ArrayList<TestPair<Long>> elements;
	
	private TestPair<Long> current;
	
	private Validity validifer;
	
	private boolean expectedSet;
	
	private double acceptableRange;
	
	public LongValidifier() {
		log.traceEntry();
		initalize(DEFAULT_INITAL_SIZE, DEFAULT_ACCEPTABLE_RANGE);
	}
	
	private final String singleConstructorMessage = "new({})";
	
	public LongValidifier(int initalSize) {
		log.traceEntry(singleConstructorMessage, initalSize);
		initalSizeTests(initalSize);
		initalize(initalSize, DEFAULT_ACCEPTABLE_RANGE);
	}
	
	public LongValidifier(double acceptableRange) {
		log.traceEntry(singleConstructorMessage, acceptableRange);
		acceptableRange = Validity.rangeTest(acceptableRange);
		initalize(DEFAULT_INITAL_SIZE, acceptableRange);
	}
	
	public LongValidifier(int initalSize, double acceptableRange) {
		log.traceEntry("new({}, {})", initalSize, acceptableRange);
		
		initalSizeTests(initalSize);
		acceptableRange = Validity.rangeTest(acceptableRange);
		
		initalize(initalSize, acceptableRange);
	}


	private void initalSizeTests(int initalSize) {
		log.traceEntry();
		if (initalSize <= 0) {
			throw log.throwing(new IllegalArgumentException("Size must be greater"
					+ " than zero: " + initalSize));
		} else if (initalSize > MAX_SIZE) {
			log.warn("Size to large, defaulting to MAX_SIZE: "
					+ NumberTools.format(initalSize));
			initalSize = MAX_SIZE;
		}
	}
	
	private void initalize(int size, double acceptableRange) {
		current = new TestPair<>();
		elements = new ArrayList<>(size);
		validifer = new Validity(acceptableRange);
		this.acceptableRange = acceptableRange;
		log.trace("Generated new " + getClass() + " with size " 
				+ NumberTools.format(size) + " and range +/-" 
				+ NumberTools.formatPercent(acceptableRange));
	}
	
	public void setExpected(long value) {
		log.traceEntry("setExpected(" + NumberTools.format(value) + ")");
		current.setExpected(value);
		expectedSet = true;
	}
	
	public void setResult(long value) throws IllegalArgumentException {
		log.traceEntry("setResult(" + NumberTools.format(value) + ")");
		if (expectedSet) {
			log.trace("Expected was set");
			current.setResult(value);
			elements.add(current);
			validifer.accept(current);
			
			current = new TestPair<>();
			expectedSet = false;
			
		} else {
			throw log.throwing(new IllegalArgumentException("Expected was not "
					+ "set. Cannot set result"));
		}
		
		log.traceExit();
	}
	
	Validity getPassFromRange(double accecptableRange) {
		log.traceEntry("examine from range {}", accecptableRange);
		final double finalRange = Validity.rangeTest(accecptableRange);
		return log.traceExit(
				elements.stream()
					.collect(() -> new Validity(finalRange),
							 Validity::accept, 
							 Validity::combine));
	}
	
	
	
	private static void timeAdditionAndValidation(int runFor) {
		LongValidifier test = new LongValidifier();
		long startTime = System.nanoTime();
		for (int i = 0; i < runFor; i++) {
			test.setExpected(i);
			test.setResult((long) ((double) i * 1.11));
		}
		long endTime = System.nanoTime();
		System.out.println("Added " + runFor + " results in " + Laps.nanosecondsToString(endTime - startTime));
		
		startTime = System.nanoTime();
		Validity x = test.validifer;
		endTime = System.nanoTime();
		System.out.println("Validated "+ runFor + " results in " + Laps.nanosecondsToString(endTime - startTime));
		System.out.println();
		System.out.println("%: " + (x.getPercentPass() * 100));
		System.out.println("Pass: " + x.getPassCount());
		System.out.println();
		System.out.println(test.elements);
	}
	
	public static void main(String[] args) {
		timeAdditionAndValidation(100);
	}
}
