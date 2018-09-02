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
		Validity x = test.elements.stream().collect(() -> new Validity(.1), Validity::accept, Validity::combine);
		endTime = System.nanoTime();
		System.out.println("Validated "+ runFor + " results in " + Laps.nanosecondsToString(endTime - startTime));
		System.out.println();
		System.out.println(x.getPercentPass());
		System.out.println("Pass: " + x.passCount);
	}
	
	public static void main(String[] args) {
		timeAdditionAndValidation(100);
	}
}
