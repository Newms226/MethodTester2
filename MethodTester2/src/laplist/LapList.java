package laplist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import lap.Lap;
import lap.LapPredicate;
import lap.LapRangeException;
import lap.Laps;
import lap.NonIDedLap;

public interface LapList extends Cloneable, Serializable, Iterable<Lap>, Consumer<Lap> {
	
	/**
	 * Method for recoding a lap where the elapsed time has already been calculated by the client.
	 * <p>
	 * <strong>NOTE:</strong> By the {@link Lap} interface's contract, elapsed MUST BE NON-NEGATIVE
	 * 
	 * @param elapsed the elapsed time of the lap <strong>NON NEGATIVE</strong>
	 * 
	 * @throws LapRangeException if elapsed < 0
	 */
	default void accept(long elapsed) throws LapRangeException {
		accept(new NonIDedLap(elapsed));
	}
	
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
	 * @throws LapRangeException if startLap > endLap
	 */
	default void accept(long startLap, long endLap) throws LapRangeException {
		accept(new NonIDedLap(endLap - startLap));
	}
	
	/**
	 * A convenience method which allows the client to create the Lap object externally.
	 * 
	 * @param lap the {@link Lap} object to be added
	 */
	void accept(Lap lap);
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                   LIST & ARRAY METHODS                                     *
	 *                                                                                            *
	 **********************************************************************************************

//	/**
//	 * Utility method for extracting the list of elapsed times as doubles to enable analysis by the
//	 * Apache Stat package & other double array based APIs.
//	 * 
//	 * @return a {@code double} array of the Laps contained in this object.
//	 */
//	double[] getAsDoubleArray();
//	
//	/**
//	 * Method to return a {@code double[]} object filtered through the predicate passed as a 
//	 * parameter. All implementing classes should ensure that this method does not harm the 
//	 * underlying list of laps, but merely provides a copy of the the laps for analysis.
//	 * 
//	 * @param predicate to filter the list as
//	 * 
//	 * @return a {@code double[]} filtered through the predicate
//	 */
//	double[] getAsFilteredDoubleArray(LapPredicate predicate);
	
//	/**
//	 * Returns a {@code List<Lap>} of all the laps in this list. By this interface's contract, an
//	 * empty list of {@code Laps} should return a null value. Further, this should return the 
//	 * underlying collection, NOT a copy.
//	 * 
//	 * @return a {@code List<Lap>} of all the laps in this list, null otherwise.
//	 */
//	List<Lap> elements();
	
	default List<Lap> getDeepCopy() {
		if (isEmpty()) return Collections.emptyList();
		
		ArrayList<Lap> toReturn = new ArrayList<Lap>((int)size());
		for (Iterator<Lap> it = iterator(); it.hasNext(); ) { 
			toReturn.add(it.next().clone());
		}
		return toReturn;
	}
	
	/**
	 * Method to return a {@code LapList} object filtered through the predicate passed as a 
	 * parameter. All implementing classes should ensure that this method does not harm the 
	 * underlying list of laps, but merely provides a copy of the the laps for analysis.
	 * 
	 * @param predicate to filter the list through
	 * 
	 * @return a {@code LapList} containing {@link Lap}s filtered through the predicate
	 */
	default List<Lap> filterLaps(LapPredicate predicate) {
		return stream().filter(predicate).collect(Collectors.toList());
	}
	
	default boolean contains(long elapsed) {
		return contains(new NonIDedLap(elapsed), Laps.ELAPSED_ORDER);
	}
	
	default boolean contains(Lap lap) {
		return contains(lap, Laps.NATURAL_ORDER);
	}
	
	boolean contains(Lap lap, Comparator<Lap> comparator);
	
	default int indexOf(Lap lap) {
		return indexOf(lap, Laps.NATURAL_ORDER);
	}
	
	int indexOf(Lap lap, Comparator<Lap> comparator);
	
	default int nextIndexOf(Lap lap, int indexExlusive) {
		return nextIndexOf(lap, indexExlusive, Laps.NATURAL_ORDER);
	}
	
	int nextIndexOf(Lap lap, int indexExlusive, Comparator<Lap> comparator);
	
	default int lastIndexOf(Lap lap) {
		return lastIndexOf(lap, Laps.NATURAL_ORDER);
	}
	
	int lastIndexOf(Lap lap, Comparator<Lap> comparator);
	
	default Lap get(Lap lap) {
		return get(lap, Laps.NATURAL_ORDER);
	}
	
	Lap get(Lap lap, Comparator<Lap> comparator);
	
	Stream<Lap> stream();
	
	default void addAll(Collection<Lap> laps) {
		for (Lap l: laps) accept(l);
	}
	
	default boolean isEmpty() {
		return size() == 0;
	}
	
	default void sort() {
		sort(Laps.NATURAL_ORDER);
	}
	
	void sort(Comparator<Lap> comparator);

	default Map<Long, Lap> getIDMap() throws ClassCastException {
		return stream().collect(Collectors.toMap(LAP -> {return LAP.getID().longValue();},
		                                         LAP -> {return LAP;}));
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                SUMMARY STATISTICS METHODS                                  *
	 *                                                                                            *
	 **********************************************************************************************

	
	/**
	 * @return a {@link SummaryStatistics} object representing the statistics of this list 
	 */
	SummaryStatistics getSummaryStatistics();
	
	/**
	 * Note that this method does not set the internal value of the deviation. This method is
	 * abstracted to <strong>only</strong> calculate the value of the deviation from the value
	 * passed into it. Thus, this method can be used by external code to evalutae the deviation 
	 * from some independent average without damaging the underlying conditions.
	 */
	default double getDeviationFrom(double averageValue) {
		return 
	}
	
	/**
	 * Returns the total count of the laps in this object
	 * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@link SummaryStatistics} object under the hood to avoid VERY expensive overhead.
	 *  
	 * @return the count of the laps in this object
	 */
	default long size() {
		return getSummaryStatistics().getN();
	}
	
	/**
	 * Returns the average of the laps in this object
	 * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@link SummaryStatistics} object under the hood to avoid expensive overhead.
	 *  
	 * @return the average of the laps in this object, 0 if no laps are recorded
	 */
	default double getAverage() {
		return getSummaryStatistics().getMean();
	}
	
	/**
	 * Returns the maximum value contained in this list
     * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@link SummaryStatistics} object under the hood to avoid expensive overhead.
	 * 
	 * @return the maximum value contained in this list, 0 if no laps are recorded
	 */
	default long getMax() {
		return (long) getSummaryStatistics().getMax();
	}
	
	/**
	 * Returns the minimum value contained in this list.
	 * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@link SummaryStatistics} object under the hood to avoid expensive overhead.
	 * 
	 * @return the minimum value contained in this list, 0 if no laps are recorded
	 */
	default long getMin() {
		return (long) getSummaryStatistics().getMin();
	}
	
	/**
	 * Returns the sum of all the values contained in this list.
	 * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@link SummaryStatistics} object under the hood to avoid expensive overhead.
	 * 
	 * @return the sum of all laps contained in this list, 0 if no laps are recorded
	 */
	default double getSum() {
		return getSummaryStatistics().getSum();
	}
	
	/**
	 * <p>
	 * <strong>NOTE:</strong> This method should be overwritten by any class which does not use a 
	 * {@link SummaryStatistics} object under the hood to avoid expensive overhead.
	 * @return
	 */
	default double getVariance() {
		return getSummaryStatistics().getVariance();
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
	default LapList combine(LapList otherLapList) {
		// TODO
		return null;
	}
	
	List<Lap> getQuickestLaps();
	
	List<Lap> getSlowestLaps();
	
	default List<Lap> getSortedLaps() {
		return getSortedLaps(Laps.NATURAL_ORDER);
	}
	
	default List<Lap> getSortedLaps(Comparator<Lap> comparator) {
		return stream().sorted(comparator).collect(Collectors.toList());
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                      STATIC FEILDS                                         *
	 *                                                                                            *
	 *********************************************************************************************/
	

	
}
