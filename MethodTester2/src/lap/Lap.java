package lap;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;

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
	 * Method is designed to reject or accept certain laps based on the conditions within the 
	 * passed predicate.
	 * 
	 * @param predicate to test this lap for
	 * 
	 * @return result of the {@code LapPredicate} test
	 */
	default boolean query(LapPredicate predicate) {
		return predicate.test(this);
	}
	
	default Duration getDuration() {
		return Duration.ofNanos(getElapsed());
	}
	
	/**
	 * Method should return the number of this lap, its' elapsed time, & the deviation if it has
	 * been calculated.
	 * 
	 * @return A {@code String} representing this Lap.
	 */
	String toString();
	
	/**
	 * 
	 * @return
	 */
	Lap clone();
	
	long getTimestamp();
	
	default Instant getTimestampInstant() {
		return Instant.ofEpochMilli(getTimestamp());
	}
	
	/**
	 * Returns if this {@code Lap} is part of a sequence. Implementing classes should only return
	 * {@code true} if laps occur in sequence, without any parallel or concurrent nature.
	 * 
	 * By default, any {@link ParallelAutoRaceLap} returns {@code false}.
	 * 
	 * @return Whether this {@code Lap} is sequential.
	 */
	boolean isSequential();
	
	String getIDString();
	
//	/**********************************************************************************************
//	 *                                                                                            *
//	 *                               STATIC METHODS AND FIELDS                                    *
//	 *                                                                                            *
//	 **********************************************************************************************/
//	
//	
//	public static Lap ofValue(long value) {
//		return new TimeStampedLap(value);
//	}
	
	public static void main(String[] args) {
//		System.out.println(Duration.ofNanos((long) nanoConversionRate).toString());
//		System.out.println(nanosecondsToString( ((double)1000000000  * 2)));
		
//		String str = "3.4";
//		for (int i = 0; i < 120; i++) {
//			str += 1;
//			System.out.println(Double.parseDouble(str));
//		}
	}
}
