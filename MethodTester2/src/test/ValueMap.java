package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.parser.ParseException;

import tools.Log4JTools;
import tools.NumberTools;

public class ValueMap implements Serializable {
	
	private static final Logger log = LogManager.getLogger();
	
	/**
	 * Maps expected values, to the resulting values
	 */
	Map<Long, DescriptiveStatistics> valueMap; 
	
	public void addValue(TestPair<Long> result) {
		addValue(result.getExpected(), result.getResult());
	}
	
	public void addValue(long expected, long result) {
		if (valueMap.containsKey(expected)) {
			valueMap.get(expected).addValue(result);
		} else {
			valueMap.put(expected, new DescriptiveStatistics()).addValue(result);
		}
	}
	
	public void addAll(LongValidifier v) {
		addAll(v.elements);
	}
	
	public void addAll(Collection<TestPair<Long>> collection) {
		for (TestPair<Long> t: collection) {
			addValue(t);
		}
	}
	
	public double getPercentChange(TestPair<Long> result) {
		if (valueMap.containsKey(result.getExpected())) {
			return NumberTools.percentDifference(
					valueMap.get(result.getExpected()).getMean(),
				    result.getResult()
				);	
		} else {
			return 0;
		}
	}
	
	static Marker READ_CSV = MarkerManager.getMarker("singleValue.read.CSV");
	static Marker READ_LINE_SV = MarkerManager.getMarker("singleValue.read.LSV");
	
	public int readSingleValueEntriesFromCVS(File file) throws Exception {
		int valuesRead = 0;
		try (Scanner read = new Scanner(file)){
			read.nextLine();
			read.nextLine();
			Long expected = Long.parseLong(read.nextLine().split(": ")[1].trim());
			log.trace("EXPECTED: " + expected);
			DescriptiveStatistics s = valueMap.computeIfAbsent(expected, t -> {return new DescriptiveStatistics();});
			String valuesString = read.nextLine();
			log.trace("Value String: " + valuesString);
			valuesString = valuesString.substring(1, valuesString.length() - 2);
			log.trace("After Boundary Removal: " + valuesString);
			String[] values = valuesString.split(",");
			
			for (String str: values) {
				System.out.println("FOUND " + str);
				s.addValue(Double.parseDouble(str.trim()));
				valuesRead++;
			}
		} catch (NoSuchElementException e) {
			log.catching(Level.FATAL, new ParseException("Reached end of the file unexpectedly", e));
		}
		return valuesRead;
	}
	
	public int readSingleValueEntriesFromLineSV(File file) throws Exception {
		int valuesRead = 0;
		try (Scanner read = new Scanner(file)){
			read.nextLine();
			read.nextLine();
			Long expected = Long.parseLong(read.nextLine().split(": ")[1].trim());
			System.out.println("EXPECTED: " + expected);
			DescriptiveStatistics s = valueMap.computeIfAbsent(expected, t -> {return new DescriptiveStatistics();});
			while (read.hasNextLine()) {
				String str = read.nextLine();
				System.out.println("FOUND " + str);
				s.addValue(Double.parseDouble(str.trim()));
				valuesRead++;
			}
		} catch (NoSuchElementException e) {
			log.catching(Level.FATAL, new ParseException("Reached end of the file unexpectedly", e));
		} 
		return valuesRead;
	}
	
	public void attemptToReadSingleValueEntires(File file) {
		log.traceEntry("Attempting to read from file {}", file);
		Log4JTools.assertNonNull(file, log);
		int expectedCount, actualCount;
		try {
			log.trace(READ_LINE_SV);
			try {
				expectedCount = Integer.parseInt(file.getName().split("\\.")[0]);
			} catch (NumberFormatException | PatternSyntaxException e) {
				throw log.throwing(Level.INFO, e);
			}
			actualCount = readSingleValueEntriesFromLineSV(file);
			if (actualCount != expectedCount) {
				throw new Exception("COUNT WAS OFF. Expected: " + NumberTools.format(expectedCount) + " Found: " + NumberTools.format(actualCount));
			}
		} catch (Exception e) {
			log.catching(Level.INFO, e);
			
			try {
				try {
					expectedCount = Integer.parseInt(file.getName().split("\\.")[1]);
				} catch (NumberFormatException | PatternSyntaxException e2) {
					throw log.throwing(Level.INFO, e2);
				}
				log.trace(READ_CSV);
				actualCount = readSingleValueEntriesFromCVS(file);
				if (actualCount != expectedCount) {
					throw new Exception("COUNT WAS OFF. Expected: " + NumberTools.format(expectedCount) + " Found: " + NumberTools.format(actualCount));
				}
			} catch (Exception e3) {
				log.catching(Level.FATAL, e3);
				System.out.println("FAILED TO READ: " + file);
				throw new Error(); // TODO
			}
		}
		
		log.traceExit();
	}
	
	public String toString() {
		return toString(new StringBuilder()).toString();
	}
	
	public StringBuilder toString(StringBuilder buf) {
		buf.append("MAPPING COUNT: " + valueMap.size() + "\n");
		for (Entry<Long, DescriptiveStatistics> e : valueMap.entrySet()) {
			buf.append(e.getKey() + "(" + NumberTools.format(e.getValue().getN()) + "):" + Arrays.toString(e.getValue().getValues()));
		}
		return buf;
	}
	
	static ValueMap testReadSingleFile(Long expectedValue, String fileLocation) {
		ValueMap test = new ValueMap();
		
		
	}
}
