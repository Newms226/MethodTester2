package lap;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

public interface LapList extends Cloneable, Serializable {
	
	/**
	 * Method for recoding a lap where the elapsed time has already been calculated by the client.
	 * <p>
	 * <strong>NOTE:</strong> By the {@link Lap} interface's contract, elapsed MUST BE NON-NEGATIVE
	 * 
	 * @param elapsed the elapsed time of the lap <strong>NON NEGATIVE</strong>
	 * 
	 * @return the {@code Lap} object created
	 * 
	 * @throws LapRangeException if startLap > endLap
	 */
	Lap lap(long elapsed);
	
	/**
	 * Default/Override method for adding a lap to this list without first calculating the elapsed 
	 * time.
	 * <p>
	 * This method is the same as {@link LapList#lap(long, long)} except it ensures the validity
	 * of the lap range <strong>by default </strong>
	 * <p>
	 * <strong>NOTE:</strong> By the {@link Lap} interface's contract, startLap &lt= endLap
	 * 
	 * @param startLap the beginning of the lap
	 * @param endLap the end of the lap
	 * 
	 * @return the {@code Lap} object created by the internal method {@link LapList#lap(long)}
	 * 
	 * @throws LapRangeException if startLap > endLap
	 */
	default Lap lap(long startLap, long endLap) throws LapRangeException {
		LapRangeException.assertValid(startLap, endLap);
		
		return lap(endLap - startLap);
	}
	
	/**
	 * A convenience method which allows the client to create the Lap object externally.
	 * 
	 * @param lap the {@link Lap} object to be added
	 * 
	 * @return
	 */
	Lap lap(Lap lap);
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                   LIST & ARRAY METHODS                                     *
	 *                                                                                            *
	 **********************************************************************************************

	/**
	 * Utility method for extracting the list of elapsed times as doubles to enable analysis by the
	 * Apache Stat package & other double array based APIs.
	 * 
	 * @return a {@code double} array of the Laps contained in this object.
	 */
	double[] getAsDoubleArray();
	
	/**
	 * Method to return a {@code double[]} object filtered through the predicate passed as a 
	 * parameter. All implementing classes should ensure that this method does not harm the 
	 * underlying list of laps, but merely provides a copy of the the laps for analysis.
	 * 
	 * @param predicate to filter the list as
	 * 
	 * @return a {@code double[]} filtered through the predicate
	 */
	double[] getAsFilteredDoubleArray(LapPredicate predicate);
	
	/**
	 * Returns a {@code List<Lap>} of all the laps in this list. By this interface's contract, an
	 * empty list of {@code Laps} should return a null value.
	 * 
	 * @return a {@code List<Lap>} of all the laps in this list, null otherwise.
	 */
	List<Lap> getLaps();
	
	/**
	 * Method to return a {@code LapList} object filtered through the predicate passed as a 
	 * parameter. All implementing classes should ensure that this method does not harm the 
	 * underlying list of laps, but merely provides a copy of the the laps for analysis.
	 * 
	 * @param predicate to filter the list through
	 * 
	 * @return a {@code LapList} containing {@link Lap}s filtered through the predicate
	 */
	LapList filterLaps(LapPredicate predicate);
	
	
	/**
	 * Method to 
	 * @param floor
	 * @param ceelingEXCLUSIVE
	 * @return
	 */
	LapList subList(int floor, int ceelingEXCLUSIVE);
	
	Map<Integer, Lap> getIDLapMap();
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                              SUMMARY STATISTICS METHODS                                    *
	 *                                                                                            *
	 **********************************************************************************************

	
	/**
	 * @return a {@code LongSummaryStatistics} object representing the statistics of this list 
	 */
	LongSummaryStatistics getSummaryStatistics();
	
	/**
	 * Returns the total count of the laps in this object
	 * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@code LongSummaryStatistics} object under the hood to avoid VERY expensive overhead.
	 *  
	 * @return the count of the laps in this object
	 */
	default long getCount() {
		return getSummaryStatistics().getCount();
	}
	
	/**
	 * Returns the average of the laps in this object
	 * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@code LongSummaryStatistics} object under the hood to avoid expensive overhead.
	 *  
	 * @return the average of the laps in this object, 0 if no laps are recorded
	 */
	default double getAverage() {
		return getSummaryStatistics().getAverage();
	}
	
//	/**
//	 * Returns the maximum value contained in this list
//     * <p>
//	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
//	 * {@code LongSummaryStatistics} object under the hood to avoid expensive overhead.
//	 * 
//	 * @return the maximum value contained in this list, 0 if no laps are recorded
//	 */
//	default long getMax() {
//		return getSummaryStatistics().getMax();
//	}
//	
//	/**
//	 * Returns the minimum value contained in this list.
//	 * <p>
//	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
//	 * {@code LongSummaryStatistics} object under the hood to avoid expensive overhead.
//	 * 
//	 * @return the minimum value contained in this list, 0 if no laps are recorded
//	 */
//	default long getMin() {
//		return getSummaryStatistics().getMin();
//	}
	
	/**
	 * Returns the sum of all the values contained in this list.
	 * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@code LongSummaryStatistics} object under the hood to avoid expensive overhead.
	 * 
	 * @return the sum of all laps contained in this list, 0 if no laps are recorded
	 */
	default double getSum() {
		return getSummaryStatistics().getSum();
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                       MISC METHODS                                         *
	 *                                                                                            *
	 *********************************************************************************************/
	 
	/**
	 * Combines two {@code LapList} objects and returns a new object. Note, any implementation of
	 * this method should leave this object in tact and simply return a new object.
	 * 
	 * @param otherLapList the {@code LapList} to combine with
	 * 
	 * @return a new {@code LapList} instance representing the combination of this object and the
	 *         other
	 */
	LapList combine(LapList otherLapList);

	/**
	 * Returns the <strong>pre-calculated</strong> average variance from the mean of this 
	 * {@code LapSet}
	 *  
	 * @return the average variance of this {@code LapSet}
	 */
	double getAverageVariance();
	
	List<Lap> getMin();
	
	List<Lap> getMax();
	
}
