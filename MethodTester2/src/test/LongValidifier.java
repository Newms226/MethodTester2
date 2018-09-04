package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.spi.LoggerContext;

import lap.Laps;
import menu.ConsoleMenu;
import tools.FileTools;
import tools.Log4JTools;
import tools.NumberTools;
import util.Averager;

public class LongValidifier {
	public static final int DEFAULT_INITAL_SIZE = 100;
	
	public static final double DEFAULT_ACCEPTABLE_RANGE = .1;
	
	public static final int MAX_SIZE = Integer.MAX_VALUE - 8;
	
	private static final Logger log = LogManager.getLogger();

	
	ArrayList<TestPair<Long>> elements;
	
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
		log.traceExit();
	}
	
	//THIS WILL GO OUT OF BOUNDS IF MORE THAN Integer.MAX_VALUE reached
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
			addTestPair(current);
			
			current = new TestPair<>();
			expectedSet = false;
		} else {
			throw log.throwing(new IllegalArgumentException("Expected was not "
					+ "set. Cannot set result"));
		}
		
		log.traceExit();
	}
	
	public void setEnMas(long expected, Collection<Long> results) {
		for (Long l: results) {
			setExpected(expected);
			setResult(l);
		}
	}
	
	private void addTestPair(TestPair<Long> toAdd) {
		elements.add(toAdd);
		validifer.accept(toAdd);
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
	
	Validity getValidity() {
		return validifer;
	}
	
	public String toString() {
		return toString(new StringBuilder());
	}
	
	String toString(StringBuilder buf) {
		TestPair<Long> temp;
		for (int i = 0; i < elements.size(); i++) {
			buf.append(elements.get(i) + "\n");
		}
		return buf.toString();
	}
	
	String toCSV() {
		return toCSV(new StringBuilder()).toString();
	}
	
	StringBuilder toCSV(StringBuilder builder) {
		for (int i = 0; i < elements.size(); i++) {
			builder.append(elements.get(i).toCSV() + "\n");
		}
		return builder;
	}
	
	
	public static LongValidifier fromCSV(File file) {
		LongValidifier toReturn = new LongValidifier();
		try(Scanner read = new Scanner(file)) {
			// ACCOUNT FOR THE TIME STAMP!
			while (read.hasNextLine()) {
				toReturn.addTestPair(TestPair.fromLongCSV(read.nextLine()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return toReturn;
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
		System.out.println();
		
		x = test.getPassFromRange(.1);
		System.out.println("%: " + (x.getPercentPass() * 100));
		System.out.println("Pass: " + x.getPassCount());
	}
	
	static LongValidifier randomThreadTest(int runFor, double acceptableRange, int maxMillsValue, int offset) {
		LongValidifier test = new LongValidifier(runFor, acceptableRange);
		Random ranGen = new Random();
		long startTest = System.nanoTime();
		long startLap, endLap;
		int randomValue;
		double millisToNano = Math.pow(10, 6);
		for (int i = 0; i < runFor; i++) {
			randomValue = ranGen.nextInt(maxMillsValue) + offset;
			System.out.println((i+1) + ". Random Value: " + randomValue);
			test.setExpected((long)((double) randomValue * millisToNano));
			startLap = System.nanoTime();
			try {
				Thread.sleep(randomValue);
			} catch (InterruptedException e) {
				throw new Error("FAILED WITH RAN: " + randomValue + " when i = " + i);
			}
			endLap = System.nanoTime();
			test.setResult(endLap - startLap);
		}
		Validity valid = test.getValidity();
		Validity dup = test.getPassFromRange(acceptableRange);
		System.out.println("EQUALS CHECK: " + dup.equals(valid));
		System.out.println("CURRENT: " + valid + "\nDUP: " + dup);
//		for (double i = 0.0; i < 1; i += .025) {
//			System.out.println(NumberTools.format(i) + ": " + test.getPassFromRange(i));
//		}
//		System.out.println(valid);
//		System.out.println();
		return test;
		
	}
	
	static final double millisToNano = Math.pow(10, 6);
	
	static long millisToNanos(long millis) {
		// TODO: Overflow!
		return (long) (millis * millisToNano);
	}
	
	static final String ROOT_DIR = "/Users/Michael/Documents/IO/timerUnitTests";
	
	// TODO: Range check!
	static LongValidifier sequentialThreadtest(int startValue, int endValue,
			int runFor, double acceptableRange) {
		LongValidifier test = new LongValidifier(runFor, acceptableRange);
		Random ranGen = new Random();
		long startTest = System.nanoTime();
		long startLap, endLap;
		int value;
		
		int failures = 0;
		FileSet fileSet = getFileSet("sequential");
		
		List<Long> values;
		for (int n = startValue; n < endValue; n++) {
			values = new ArrayList<>(runFor);
			for (int i = 0; i < runFor; i++) {
				value = n;
				System.out.println(n + "." + i);
				test.setExpected((long)((double) value * millisToNano));
				startLap = System.nanoTime();
				try {
					Thread.sleep(value);
				} catch (InterruptedException e) {
					e.printStackTrace();
					failures++;
				}
				endLap = System.nanoTime();
				test.setResult(endLap - startLap);
				values.add(endLap - startLap);
			}
			
			writeToFile(fileSet, values, n, runFor);
		}
		Validity valid = test.getValidity();
		Validity dup = test.getPassFromRange(acceptableRange);
		System.out.println("EQUALS CHECK: " + dup.equals(valid));
		System.out.println("CURRENT: " + valid + "\nDUP: " + dup);
		System.out.println("FAILURE COUNT: " + failures);
		return test;
	}
	
	static final int MAX_TIME_OUT = 1000 * 60 * 60 * 24;
	
	static final int PROGRESS_REPORT = 1000 * 30;
	
	// TODO! Valid regrex for spaces
	private static FileSet getFileSet(String subDirectory) {
		return getFileSet(ROOT_DIR, subDirectory.replaceAll(" ", ""));
	}
	
	private static FileSet getFileSet(String rootDirectory, String subDirectory) {
		try {
			return new FileSet(rootDirectory, subDirectory);
		} catch (IOException e) {
			// TODO: Error throw
			throw log.throwing(new Error(e.toString()));
		}
	}
	
	static class FileSet {
		final File dir;
		File nDir, file;
		final String subDirectory;
		
		FileSet(String rootDirectory, String subDirectory) throws IOException {	
			FileTools.assertValidDirectory(rootDirectory); // exception to be caught
			dir = new File(rootDirectory);
			this.subDirectory = subDirectory;
		}
		
		File getRootDirectory() {
			return dir;
		}
		
		private File getNDirectory(Number n) throws RuntimeException {
			String str;
			if (n instanceof Double | n instanceof Float) {
				str = n.doubleValue() + "";
			} else {
				str = n.longValue() + "";
				
			}
			
			nDir = new File(dir + "/" + subDirectory + "/" + str);
			if (!nDir.exists() || !nDir.isDirectory()) {
				if (!nDir.mkdirs()) {
					throw log.throwing(new RuntimeErrorException(new Error(), 
						"Failure to make dirctories at" + nDir));
				}
			}
			return log.traceExit(nDir);
		}
		
		// TODO: Regrex to erase ALL spaces & Instant: remove special chars
		File getFile(int n, int runFor) {
			return FileTools.getUniqueTxtFile(getNDirectory(new Integer(n)) +"/" 
					+ runFor + "_" + Instant.now().toString().replaceAll(" ", "") +".txt");
		}
	}
	
	// TODO: RANGE CHECK
	static LongValidifier sequentialTHREADEDtest(int startValue, int endValue,
			int runFor, double acceptableRange) {
		LongValidifier l = new LongValidifier((endValue - startValue) * runFor, acceptableRange);
		long startLap, endLap;
		Vector<Long> values;
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<Long>> nResults = null;
		List<Wait> nCallableTasks;
		List<Long> longs;
		FileSet fileSet = getFileSet("threaded");
		for (int n = startValue; n < endValue; n++) {
			nCallableTasks = new ArrayList<>(runFor);
			for (int i = 0; i < runFor; i++) {
				nCallableTasks.add(new Wait(n, i));
			}
			
//			System.out.println("Generated " + nCallableTasks + " (" +
//					NumberTools.percentDifferenceAsString(runFor, nCallableTasks.size()) + ")");
			
			try {
				nResults = executor.invokeAll(nCallableTasks);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.catching(e);
//				throw new Error();
			} finally {
				longs = nResults.stream().map(
			    		value -> {
						try {
							return value.get();
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							log.catching(Level.WARN, e);
							return null;
						}
					}).filter(lo -> lo != null).collect(Collectors.toList());
				l.setEnMas(millisToNanos(n), longs);
				writeToFile(fileSet, 
						    longs,
						    n, 
						    runFor);
			}
		}
		Validity valid = l.getValidity();
		Validity dup = l.getPassFromRange(acceptableRange);
		System.out.println("EQUALS CHECK: " + dup.equals(valid));
		System.out.println("CURRENT: " + valid + "\nDUP: " + dup);
		
		return l;
	}
	
	private static <T> void writeToFile(FileSet fileSet, Iterable<T> values, int n, int runFor) {
		File file = fileSet.getFile(n, runFor);
		try {
			FileTools.writeToFile(new PrintWriter(file), getFileText(values, n));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static final String EXPECTED = "EXP:";
	
	private static <T> String getFileText(Iterable<T> values, int expectedValue) {
		StringBuilder buf = new StringBuilder(EXPECTED + expectedValue + "\n");
		if (values == null) return buf.toString();
		
		for (T t: values) {
			buf.append(t + "\n");
		}
		return buf.toString();
	}
	
	static class Wait implements Callable<Long>{

		final long millis;
		final int n;
		
		Wait(long millis, int n){
			this.millis = millis;
			this.n = n;
		}
		
		@Override
		public Long call() throws Exception {
			System.out.println(this);
			long startLap = System.nanoTime();
			Thread.sleep(millis);
			long endLap = System.nanoTime();
			// TODO Overflow
			return endLap - startLap;
		}
		
		public String toString() {
			return millis + "." + n;
		}
	}
	
	public static void main(String[] args) {
		randomThreadTest(1000, .1, 400, 100);
	}
}
