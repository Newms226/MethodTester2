package test;

import java.awt.Desktop.Action;

public class TestPair<T extends Comparable<T>> {
	public static <T extends Comparable<T>> TestPair<T> from(T expected, T result) {
		TestPair<T> toReturn = new TestPair<>();
		toReturn.setExpected(expected);
		toReturn.setResult(result);
		return toReturn;
	}
	
	T expected, result;
	
	TestPair() {}
	
	void setExpected(T expected) {
		this.expected = expected;
	}
	
	T getExpected() {
		return expected;
	}
	
	void setResult(T result) {
		this.result = result;
	}
	
	T getResult() {
		return result;
	}
	
	boolean pass() {
		if (expected != null && result != null) {
			return expected.compareTo(result) == 0;
		}
		return false;
	}
	
	public TestPair<T> clone(){
		TestPair<T> toReturn = new TestPair<>();
		if (expected != null) {
			toReturn.setExpected(expected);
			toReturn.setResult(result);
		}
		return toReturn;
	}
	
	public String toString() {
		return "Expected: " + expected + " Actual: " + result;
	}
}
