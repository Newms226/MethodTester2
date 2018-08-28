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

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics;

import lap.Lap;
import lap.LapFactory;
import lap.LapPredicate;
import lap.LapRangeException;
import lap.Laps;

public interface LapList extends Cloneable, Serializable, Consumer<Lap> {
	
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
		accept(LapFactory.from(elapsed));
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
	 * @param endLap 
	 *           the end of the lap
	 * 
	 * @throws LapRangeException 
	 * 		     By the contract of the {@link Lap} interface, if startLap > endLap this method
	 *           will throw an exception.
	 */
	default void accept(long startLap, long endLap) throws LapRangeException {
		accept(LapFactory.from(endLap - startLap));
	}
	
	//TODO
//	default LapList filter(LapPredicate predicate) {
//		return stream().filter(predicate).collect(Collectors.toList());
//	}
	
//	/**
//	 * A convenience method which allows the client to create the Lap object externally.
//	 * 
//	 * @param lap the {@link Lap} object to be added
//	 */
//	void accept(Lap lap);
//	
//	default Map<Long, Lap> getIDMap() throws ClassCastException {
//		return stream().collect(Collectors.toMap(LAP -> {return LAP.getID().longValue();},
//		                                         LAP -> {return LAP;}));
//	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                RETURN COLLECTION METHODS                                   *
	 *                                                                                            *
	 *********************************************************************************************/

	/**
	 * Utility method for extracting the list of elapsed times as doubles to enable analysis by the
	 * Apache Stat package & other double array based APIs.
	 * 
	 * @return a {@code double} array of the Laps contained in this object.
	 */
	default double[] getAsDoubleArray() {
		return stream().mapToDouble(Lap::getElapsed).toArray();
	}
	
	/**
	 * Method to return a {@code double[]} object filtered through the predicate passed as a 
	 * parameter. All implementing classes should ensure that this method does not harm the 
	 * underlying list of laps, but merely provides a copy of the the laps for analysis.
	 * 
	 * @param predicate to filter the list as
	 * 
	 * @return a {@code double[]} filtered through the predicate
	 */
	default double[] getAsFilteredDoubleArray(LapPredicate predicate) {
		return stream().filter(predicate).mapToDouble(Lap::getElapsed).toArray();
	}
	
	/**
	 * Returns a {@code List<Lap>} of all the laps in this list. By this interface's contract, an
	 * empty list of {@code Laps} should return a null value. Further, this should return the 
	 * underlying collection, NOT a copy.
	 * 
	 * @return a {@code List<Lap>} of all the laps in this list, null otherwise.
	 */
	List<Lap> elements();
	
	default List<Lap> getDeepCopy() {
		if (isEmpty()) return Collections.emptyList();
		
		ArrayList<Lap> toReturn = new ArrayList<Lap>((int)size());
		for (Iterator<Lap> it = iterator(); it.hasNext(); ) { 
			toReturn.add(it.next().clone());
		}
		return toReturn;
	}
	
	Stream<Lap> stream();
	
	Iterator<Lap> iterator();
	
	default List<Lap> sublist(long floorTime, long ceelingTime) {
		return stream()
			.filter(LAP -> floorTime <= LAP.getElapsed() && LAP.getElapsed() <= ceelingTime)
			.sorted(Laps.ELAPSED_ORDER)
			.collect(Collectors.toList());
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
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                       LIST METHODS                                         *
	 *                                                                                            *
	 *********************************************************************************************/
	
	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ METHODS TO IMPLEMENT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	
	boolean contains(Lap lap, Comparator<Lap> comparator);
	
	Lap get(int i);
	
	int lastIndexOf(Lap lap, Comparator<Lap> comparator);
	
	int nextIndexOf(Lap lap, int indexExlusive, Comparator<Lap> comparator);
	
	int size();
	
	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DEFAULT METHODS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	
	default void acceptAll(Collection<Lap> laps) {
		for (Lap l: laps) accept(l);
	}
	
	default boolean contains(long elapsed) {
		return contains(LapFactory.from(elapsed), DEFAULT_COMPARATOR);
	}
	
	default boolean contains(Lap lap) {
		return contains(lap, DEFAULT_COMPARATOR);
	}
	
	default boolean containsAll(Collection<? extends Lap> collection) {
		for (Lap l: elements()) {
			if (!contains(l, Laps.NATURAL_ORDER)) return false;
		}
		return true;
	}
	
	default boolean containsAll(Collection<? extends Lap> collection, Comparator<Lap> comparator) {
		for (Lap l: elements()) {
			if (!contains(l, comparator)) return false;
		}
		return true;
	}
	
	default int indexOf(Lap lap) {
		return nextIndexOf(lap, -1, DEFAULT_COMPARATOR);
	}
	
	default int indexOf(Lap lap, Comparator<Lap> comparator) {
		return nextIndexOf(lap, -1, comparator);
	}
	
	default int nextIndexOf(Lap lap, int indexExlusive) {
		return nextIndexOf(lap, indexExlusive, DEFAULT_COMPARATOR);
	}
	
	default int lastIndexOf(Lap lap) {
		return lastIndexOf(lap, DEFAULT_COMPARATOR);
	}
	
	default boolean isEmpty() {
		return size() == 0;
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                SUMMARY STATISTICS METHODS                                  *
	 *                                                                                            *
	 *********************************************************************************************/

	
	/**
	 * An Apache {@link StatisticalSummary} object which summarizes the contents of this 
	 * {@code LapList}. Persistant implementations of this interface tend to return 
	 * {@link SynchronizedDescriptiveStatistics} objects while sequential, non-persistent 
	 * implementations tend to return {@link SummaryStatistics} objects. While a method can return 
	 * a {@link DescriptiveStatistics} object, this is not recommended as this interface is already
	 * designed to collect & store the results of a {@link Lap}.
	 * 
	 * @return a {@link StatisticalSummary} object representing the statistics of this list.
	 */
	StatisticalSummary getSummaryStatistics();
	
	/**
	 * Note that this method does not set the internal value of the deviation. This method is
	 * abstracted to <strong>only</strong> calculate the value of the deviation from the value
	 * passed into it. Thus, this method can be used by external code to evalutae the deviation 
	 * from some independent average without damaging the underlying data.
	 */
	double getDeviationFrom(double averageValue);
	
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
	
	public static final Comparator<Lap> DEFAULT_COMPARATOR = Laps.NATURAL_ORDER;
	
}
