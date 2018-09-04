package lap;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Comparator;

/**
 * Static Class containing useful methods surrounding the {@link Lap} interface 
 * @author Michael
 *
 */
public class Laps {
	
	private Laps() {} // prevent instance creation
	
	/**
	 * Default seconds format object
	 */
	public static final DecimalFormat secondsFormat = new DecimalFormat("#,##0.000000");
	
	/**
	 * Default nanoseconds format object
	 */
	public static final DecimalFormat nanosecondsFormat = new DecimalFormat("#,##0");
	
	/**
	 * The conversion rate from nanoseconds to seconds
	 */
	public static final double nanoConversionRate = Math.pow(10, 9);
	
	/**
	 * An empty, generic lap.
	 */
//	public static final Lap EMPTY_LAP = new GenericLap(0);
	
	
	/**
	 * After this value, nanoseconds will print as a partial second (ex. 0.2345). Value is 
	 * equivalent to .1 second. 
	 */
	static double nanoCutoff = 100_000_000;
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                       COMPARATORS                                          *
	 *                                                                                            *
	 **********************************************************************************************/
	
	/**
	 * Ordering by the value returned by {@link Lap#getElapsed()}
	 */
	public static final Comparator<Lap> ELAPSED_ORDER = 
			(a, b) -> {
				if (a.getElapsed() < b.getElapsed()) return -1;
				if (a.getElapsed() > b.getElapsed()) return 1;
				return 0;
			};
	
	/**
	 * Natural order. Sorts first by ID, then by elapsed time
	 */
//	public static final Comparator<Lap> NATURAL_ORDER = Comparator.naturalOrder();
	
			
	/**********************************************************************************************
	 *                                                                                            *
	 *                                         METHODS                                            *
	 *                                                                                            *
	 **********************************************************************************************/

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
		if (nanoseconds == 0) return "NS: 0";
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
}
