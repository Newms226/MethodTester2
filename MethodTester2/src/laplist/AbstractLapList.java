package laplist;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import lap.Lap;

public class AbstractLapList implements LapList, Serializable {
	private static final long serialVersionUID = -2578975472803867444L;
	public static final int DEFAULT_SIZE = 1_000,
			                MAX_SIZE = Integer.MAX_VALUE - 8;
	
	private LongSummaryStatistics summaryStatistics;
	private ArrayList<Lap> laps;
	private Lap tempLap;

	
	/**
	 * Constructor enabling the creating of an AbstractLapList with the 
	 * {@link AbstractLapList#DEFAULT_SIZE} capacity.
	 * 
	 * @param initialSize the starting size of the list (must be non negative)
	 * 
	 * @throws IllegalArgumentException if array size is negative through {@link ArrayList} 
	 *                                  constructor
	 */
	public AbstractLapList() throws IllegalArgumentException {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * Constructor enabling the creating of an AbstractLapList with a specified capacity.
	 * 
	 * @param initialSize the starting size of the list (must be non negative)
	 * 
	 * @throws IllegalArgumentException if array size is negative through {@link ArrayList} 
	 *                                  constructor
	 */
	public AbstractLapList(int initialSize) throws IllegalArgumentException {
		summaryStatistics = new LongSummaryStatistics();
		laps = new ArrayList<>(initialSize);
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                       LAP METHODS                                          *
	 *                                                                                            *
	 **********************************************************************************************/
	
	@Override
	public void accept(Lap lap) throws NullPointerException {
		Objects.requireNonNull(lap);
		
		summaryStatistics.accept(lap.getElapsed());
		laps.add(tempLap);
	}

	/**********************************************************************************************
	 *                                                                                            *
	 *                                      LIST METHODS                                          *
	 *                                                                                            *
	 **********************************************************************************************/
	
	@Override
	public boolean contains(long elapsed) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean contains(Lap lap, Comparator<Lap> comparator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int indexOf(Lap lap, Comparator<Lap> comparator) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int nextIndexOf(Lap lap, int indexExlusive, Comparator<Lap> comparator) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(Lap lap, Comparator<Lap> comparator) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Lap get(Lap lap, Comparator<Lap> comparator) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Lap getID(int ID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Iterator<Lap> iterator() {
		return laps.iterator();
	}

	@Override
	public Stream<Lap> stream() {
		return laps.stream();
	}

	/**********************************************************************************************
	 *                                                                                            *
	 *                                   ANALYSIS METHODS                                         *
	 *                                                                                            *
	 **********************************************************************************************/
	
	@Override
	public SummaryStatistics getSummaryStatistics() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Lap> getQuickestLaps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Lap> getSlowestLaps() {
		// TODO Auto-generated method stub
		return null;
	}

}
