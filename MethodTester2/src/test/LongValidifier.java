package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.apache.commons.math3.ml.neuralnet.twod.util.TopographicErrorHistogram;
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
	
	public void addEnMas(long expected, Collection<Long> results) {
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
			builder.append(elements.get(i).toCSV() + ",");
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
	
	public static boolean equalsCheck(LongValidifier test, Validity valid, double acceptableRange) {
		Log4JTools.assertNonNull(test, log);
		Log4JTools.assertNonNull(valid, log);
		
		return equalsCheckPRIVATE(test, valid, acceptableRange);
	}
	
	/**
	 * Internal method to bypass null checks
	 * @param test
	 * @param valid
	 * @param acceptableRange
	 * @return
	 */
	private static boolean equalsCheckPRIVATE(LongValidifier test, Validity valid, double acceptableRange) {
		Validity dup = test.getPassFromRange(acceptableRange);
		boolean equal =  dup.equals(valid);
		
		if (equal) {
			log.info("Equals check passed >  Parameter: " + valid + "  Calculated: " + dup);
		} else {
			log.warn("Equals check FAILED >  Parameter: " + valid + "  Calculated: " + dup);
		}
		
		return equal;
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
		File dir = new File(ROOT_DIR + "/random");
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
		File file = new File(dir + "/" + runFor + "." + Instant.now() + ".txt");
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
		equalsCheckPRIVATE(test, test.getValidity(), acceptableRange);
		try {
			FileTools.writeToFile(new PrintWriter(file), test.toCSV());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.catching(Level.FATAL, e);
		}
		
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
	
	static final String SEQ_DIR = "sequential";
	
	// TODO: Range check!
	static LongValidifier sequentialThreadtest(int startValue, int endValue,
			int runFor, double acceptableRange) {
		LongValidifier test = new LongValidifier(runFor, acceptableRange);
		Random ranGen = new Random();
		long startTest = System.nanoTime();
		long startLap, endLap;
		int value;
		
		int failures = 0;
		FileSet fileSet = getFileSet(SEQ_DIR);
		
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
			
			writeToFile(fileSet.getMilliDirectory(n), values, runFor);
		}
		equalsCheckPRIVATE(test, test.getValidity(), acceptableRange);
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
		final String subDirectory;
		
		FileSet(String rootDirectory, String subDirectory) throws IOException {	
			FileTools.assertValidDirectory(rootDirectory); // exception to be caught
			dir = new File(rootDirectory);
			this.subDirectory = subDirectory;
		}
		
		File getRootDirectory() {
			return dir;
		}
		
		private File getMilliDirectory(Number millis) throws RuntimeException {
			String str;
			if (millis instanceof Double || millis instanceof Float) {
				str = millis.doubleValue() + "";
			} else {
				str = millis.longValue() + "";
				
			}
			
			File milliDir = new File(dir + "/" + subDirectory);
			assertValidDirectory(milliDir);
			
			return log.traceExit(new File(milliDir + "/" + str + ".txt"));
		}
		
		// TODO: Regrex to erase ALL spaces & Instant: remove special chars
		File getMilliFile(int millis, int runFor) {
			return FileTools.getUniqueTxtFile(getMilliDirectory(new Integer(millis)) +"/" 
					+ runFor + "_" + timestampToFileString());
		}
		
		File getRandomFile() {
			File ranDir = FileTools.getUniqueTxtFile(dir + "/" + subDirectory);
			assertValidDirectory(ranDir);
			return log.traceExit(new File(ranDir + "/" + timestampToFileString()));
		}
		
		private String timestampToFileString() {
			return Instant.now().toString().replaceAll(" ", "") +".txt";
		}
		
		private void assertValidDirectory(File directory) {
			if (!directory.exists() || !directory.isDirectory()) {
				if (!directory.mkdirs()) {
					throw log.throwing(new RuntimeErrorException(new Error(), // TODO
						"Failure to make dirctories at" + directory));
				}
			}
		}
	}
	
	static final String threaded_Seq = "threadedSequential";
	
	static LongValidifier sequentialTHREADEDTest(int runFor, double acceptableRange,
			int millisStart, int millisEnd) {
		LongValidifier l = new LongValidifier((millisEnd - millisStart) * runFor, acceptableRange);
		List<Future<Long>> futures;
		List<Long> longs;
		List<Wait> futureThreads;
		ExecutorService ex = Executors.newCachedThreadPool();
		FileSet files = getFileSet(ROOT_DIR, threaded_Seq);
		for (int milli = millisStart; milli < millisEnd; milli++) {
			futureThreads = new ArrayList<>();
			for (int n = 0; n < runFor; n++) {
				futureThreads.add(new Wait(milli, n));
			}
			
			try {
				futures = ex.invokeAll(futureThreads);
				
				writeToFile(files.getMilliDirectory(milli), 
						futures.stream()
							.map(future -> {
								try {
									return future.get();
								} catch (InterruptedException | ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									return null;
								}
							})
							.filter(LONG -> LONG != null)
							.collect(Collectors.toList()),
						runFor);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.catching(Level.FATAL, e);
			}
		}
		
		
		
		return l;
	}
	
	static void sequentialThreadPoolTest(long startMillis, long endMillis, 
			double acceptableRange, int runFor) throws InterruptedException {
		int size = (int) ((endMillis - startMillis) * runFor);
		BlockingQueue<Runnable> tasks = new ArrayBlockingQueue<Runnable>(size);
		ThreadPoolExecutor ex = new ThreadPoolExecutor(15, 100, 1000, TimeUnit.MILLISECONDS, tasks);
		Vector<Long> results = new Vector<>(size);
		for (; startMillis < endMillis; startMillis++) {
			for (int n = 0; n < runFor; n++) {
				System.out.println("Adding " + startMillis + "." + n);
				tasks.add(new ThreadPoolWait(startMillis, n, results));
			}
		}
		
		ex.shutdown();
		System.out.println("Awaiting termination...");
		ex.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		System.out.println(results.toString());
		
	}
	
	// TODO: RANGE CHECK
 	static LongValidifier randomTHREADEDtest(int runFor, double acceptableRange,
			int randomMaxValue, int minMillis) {
		LongValidifier l = new LongValidifier(runFor, acceptableRange);
		long startLap, endLap;
		Vector<Long> values;
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<TestPair<Long>>> nResults = null;
		List<RandomWait> nCallableTasks;
		FileSet fileSet = getFileSet("randomThreaded"); // TODO
		Random r = new Random();
		nCallableTasks = new ArrayList<>(runFor);
		for (int i = 0; i < runFor; i++) {
			nCallableTasks.add(new RandomWait(r.nextInt(randomMaxValue) + minMillis, i));
		}
		
//			System.out.println("Generated " + nCallableTasks + " (" +
//					NumberTools.percentDifferenceAsString(runFor, nCallableTasks.size()) + ")");
		
		try {
			nResults = executor.invokeAll(nCallableTasks);
			
			randomToFile(fileSet.getRandomFile(), 
				     nResults.stream()
				         // Extract object from Future< TestPair<Long> >
				     	.map(future -> {
							try {
								return future.get();
							} catch (InterruptedException | ExecutionException e) {
								// TODO Auto-generated catch block
								log.catching(Level.ERROR, e);
								return null;
							}})
				     	// Filter out any null values
				     	.filter(testPair -> testPair != null)
				     	// Extract CSV
				     	.map(testPair -> testPair.toCSV())
				     	// Collect
				     	.collect(Collectors.toList()),
				     runFor);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.catching(e);
			throw new Error();
		}
		
		equalsCheckPRIVATE(l, l.validifer, acceptableRange);
		
		return l;
	}
	
	private static <T> void writeToFile(File file, Iterable<T> values, int expected) {
		try {
			FileTools.writeToFile(new PrintWriter(file), getFileText(values, expected));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static <T> void randomToFile(File file, Iterable<T> values, int n) {
		try {
			FileTools.writeToFile(new PrintWriter(file), getRandomFileText(values, n));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static final String EXPECTED = "EXP:";
	
	private static <T> String getFileText(Iterable<T> values, int expectedValue) {
		StringBuilder buf = new StringBuilder(EXPECTED + millisToNanos(expectedValue) + "\n");
		if (values == null) return buf.toString();
		
		for (T t: values) {
			buf.append(t + "\n");
		}
		return buf.toString();
	}
	
	private static <T> String getRandomFileText(Iterable<T> values, int expectedValue) {
		if (values == null) return "";
		
		StringBuilder buf = new StringBuilder();
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
	
	static class ThreadPoolWait implements Runnable {
		
		final long millis;
		final int n;
		final Vector<Long> resultSet;
		
		ThreadPoolWait(long millis, int n, final Vector<Long> resultSet){
			this.n = n;
			this.millis = millis;
			this.resultSet = resultSet;
		}

		@Override
		public void run() {
			System.out.println(this);
			long startLap = System.nanoTime();
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				log.catching(Level.FATAL, e);
			}
			long endLap = System.nanoTime();
			// TODO Overflow
			resultSet.add(endLap - startLap);
		}
		
		public String toString() {
			return millis + "." + n;
		}
		
	}
	
	static class RandomWait implements Callable<TestPair<Long>>{

		final long millis;
		final int n;
		
		RandomWait(long millis, int n){
			this.millis = millis;
			this.n = n;
		}
		
		@Override
		public TestPair<Long> call() throws Exception {
			TestPair<Long> toReturn = new TestPair<>();
			toReturn.setExpected(millisToNanos(millis));
			System.out.println(this);
			long startLap = System.nanoTime();
			Thread.sleep(millis);
			long endLap = System.nanoTime();
			// TODO Overflow
			toReturn.setResult(endLap - startLap);
			return toReturn;
		}
		
		public String toString() {
			return n + "." + millis;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		sequentialThreadPoolTest(10, 20, .1, 100);
	}
}
