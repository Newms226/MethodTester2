package lap;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Duration;

public interface Lap extends Cloneable, Comparable<Lap>, Serializable {
	
	/**
	 * Get the elapsed time represented by this lap
	 * 
	 * @return {@code long} representing the elapsed time of this lap
	 */
	long getElapsed();
	
	/**
	 * Calculates the deviation from the average passed into the method. This method should be
	 * called after the finishing of a race, so that any call to {@link Lap#getDeviation()}
	 * does not return 0, which is its default. Custom implementations of this class 
	 * <strong>must</strong> set the flag returned by {@link Judge#deviationCalculated()}
	 * 
	 * @param averageValue the average over all runs
	 * 
	 * @return the deviation from the averageValue
	 */
	long getDeviation(double averageValue);
	
	boolean deviationCalculated();
	
	/**
	 * Returns the deviation from the average.
	 * 
	 * <strong>WARNING:</strong> if 0 is returned, the only way to tell if the valid result is 
	 * 0 without first calling {@link Judge#deviationCalculated()}
	 * 
	 * @return the deviation from the average, 0 if not calculated
	 */
	long getDeviation();
	
	/**
	 * Returns the run number of this Lap in the context of a race. Method which use {@code Lap}
	 * & {@link LapSet} objects outside the context of a race should return -1 to indicate that 
	 * this method does not have any functional use.
	 * 
	 * @return the run number of this {@code Lap} object
	 */
	int getID();
	
	/**
	 * Method is designed to reject or accept certain laps based on the conditions within the 
	 * passed predicate.
	 * 
	 * @param predicate to test this lap for
	 * 
	 * @return result of the {@code LapPredicate} test
	 */
	boolean query(LapPredicate predicate);
	
	/**
	 * Method should return the number of this lap & its elapsed time
	 * 
	 * @return A {@code String} representing this Lap.
	 */
	String toString();
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                               STATIC METHODS AND FIELDS                                    *
	 *                                                                                            *
	 **********************************************************************************************/
	
	/**
	 * Default seconds format object
	 */
	public static DecimalFormat secondsFormat = new DecimalFormat("#,##0.000000");
	
	/**
	 * Default nanoseconds format object
	 */
	public static DecimalFormat nanosecondsFormat = new DecimalFormat("#,##0");
	
	/**
	 * The conversion rate from nanoseconds to seconds
	 */
	public static double nanoConversionRate = Math.pow(10, 9);
	
	/**
	 * After this value, nanoseconds will print as a partial second (ex. 0.2345). Value is 
	 * equivalent to .1 second. 
	 */
	static double nanoCutoff = 100_000_000;
	
	/**
	 * Convenience method which converts the parameter it receives and converts it to a double for
	 * evaluation by {@link Lap#nanosecondsToString(double)}.
	 * 
	 * @param nanoseconds the nanoseconds to format
	 * 
	 * @return a formated {@code String} representing the {@code nanoseconds} parameter
	 */
	public static String nanosecondsToString(long nanoseconds) {
		return nanosecondsToString((double) nanoseconds);
	}
	
	/**
	 * Formats a given nanoseconds value into a string.
	 * 
	 * <strong>WARNING:</strong> {@code long} values returned by {@link System#nanoTime()} are only
	 * accurate when the time span is less than 292 years, according to the Oracle documentation.
	 * 
	 * @param nanoseconds the nanoseconds to format
	 * 
	 * @return a formated {@code String} representing the {@code nanoseconds} parameter
	 * 
	 * @author Michael Newman, Modified from {@link Duration#toString()} released by
	 *         Oracle
	 */
	public static String nanosecondsToString(double nanoseconds) throws IllegalArgumentException {
		if (nanoseconds == 0) return "0 nanoseconds";
		if (nanoseconds < 0) {
			throw new IllegalArgumentException("Cannot work with negative nanoseconds");
		}
		if (nanoseconds < nanoCutoff) {
    			return "NS: " + nanosecondsFormat.format(nanoseconds);
		}
		
		long days   = (long) (nanoseconds / (nanoConversionRate * 60 * 60 * 24));
        int hours   = (int)  (nanoseconds / (nanoConversionRate * 60 * 60) % 24);
        int minutes = (int)  (nanoseconds / (nanoConversionRate * 60) % 60);
        double secs =        (nanoseconds / nanoConversionRate % 60);
        
        StringBuilder buf = new StringBuilder(24);
        if (days != 0) {
        		buf.append("D: " + days + " ");
        }
        if (hours != 0) {
            buf.append("H: " + hours + " ");
        }
        if (minutes != 0) {
            buf.append("M: " + minutes + " ");
        }
        if (secs == 0) {
            return buf.toString();
        }
    		
        buf.append("S: " + secondsFormat.format(secs));
        while (buf.charAt(buf.length() - 1) == '0') {
            buf.setLength(buf.length() - 1);
        }
        if (buf.charAt(buf.length() - 1) == '.') {
        		buf.setLength(buf.length() - 1);
        }
        return buf.toString();
	}
	public static void main(String[] args) {
//		System.out.println(Duration.ofNanos((long) nanoConversionRate).toString());
		System.out.println(nanosecondsToString( ((double)1000000000  * 2)));
		
//		String str = "3.4";
//		for (int i = 0; i < 120; i++) {
//			str += 1;
//			System.out.println(Double.parseDouble(str));
//		}
	}
}
