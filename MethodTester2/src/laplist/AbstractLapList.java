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

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import lap.Lap;

public class AbstractLapList implements LapList, Serializable {
	private static final long serialVersionUID = -2578975472803867444L;
	public static final int DEFAULT_SIZE = 1_000,
			                MAX_SIZE = Integer.MAX_VALUE - 8;
	
	private SummaryStatistics summaryStatistics;
	
	private ArrayList<Lap> laps;
	
	private Lap tempLap;
	
	private long fastestTime;
	
	private List<Lap> fastestLaps;
	
	private long slowestTime;
	
	private List<Lap> slowestLaps;

	
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
		summaryStatistics = new SummaryStatistics();
		laps = new ArrayList<>(initialSize);
		
		slowestLaps = new ArrayList<>();
		fastestLaps = new ArrayList<>();
		
		slowestTime = 0;
		fastestTime = Long.MAX_VALUE;
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                       LAP METHODS                                          *
	 *                                                                                            *
	 **********************************************************************************************/
	
	@Override
	public void accept(Lap lap) throws NullPointerException {
		Objects.requireNonNull(lap);
		
		summaryStatistics.addValue(lap.getElapsed());
		laps.add(tempLap);
		
		maintain(lap);
	}
	
	private void maintain(Lap lap) {
		if (fastestTime > lap.getElapsed()) {
			fastestTime = lap.getElapsed();
			fastestLaps = new ArrayList<>();
			fastestLaps.add(lap);
		} else if (fastestTime == lap.getElapsed()) {
			fastestLaps.add(lap);
		}
		
		if (slowestTime < lap.getElapsed()) {
			slowestTime = lap.getElapsed();
			slowestLaps = new ArrayList<>();
			slowestLaps.add(lap);
		} else if (slowestTime == lap.getElapsed()) {
			slowestLaps.add(lap);
		}
	}

	/**********************************************************************************************
	 *                                                                                            *
	 *                                      LIST METHODS                                          *
	 *                                                                                            *
	 **********************************************************************************************/
	
	@Override
	public boolean contains(Lap lap, Comparator<Lap> comparator) {
		if (lap == null) return false;
		
		for (Lap l: laps) {
			if (comparator.compare(lap, l) == 0) return true;
		}
		
		// else
		return false;
	}
	
	@Override
	public Lap get(int i) throws ArrayIndexOutOfBoundsException {
		return laps.get(i);
	}

	@Override
	public int nextIndexOf(Lap lap, int indexExlusive, Comparator<Lap> comparator) {
		if (lap == null) return -1;
		
		for (int i = indexExlusive + 1; i < size(); i++) {
			if (comparator.compare(laps.get(i), lap) == 0) return i;
		}
		
		// else
		return -1;
	}

	@Override
	public int lastIndexOf(Lap lap, Comparator<Lap> comparator) {
		if (lap == null) return -1;
		
		for (int i = size() - 1; i >= 0; i++) {
			if (comparator.compare(laps.get(i), lap) == 0) return i;
		}
		
		return -1;
	}
	
	@Override
	public Iterator<Lap> iterator() {
		return new LapIterator();
	}

	@Override
	public Stream<Lap> stream() {
		return laps.stream();
	}
	
	private class LapIterator implements Iterator<Lap> {
		private int index;

		@Override
		public boolean hasNext() {
			return index < size();
		}

		@Override
		public Lap next() {
			return laps.get(index++);
		}
		
	}

	/**********************************************************************************************
	 *                                                                                            *
	 *                                   ANALYSIS METHODS                                         *
	 *                                                                                            *
	 **********************************************************************************************/
	
	@Override
	public SummaryStatistics getSummaryStatistics() {
		return summaryStatistics;
	}
	
	public List<Lap> getQuickestLaps() {
		return Collections.unmodifiableList(fastestLaps);
	}

	@Override
	public List<Lap> getSlowestLaps() {
		return Collections.unmodifiableList(slowestLaps);
	}

	@Override
	public List<Lap> elements() {
		return Collections.unmodifiableList(laps);
	}

	@Override
	public int size() {
		return laps.size();
	}

	@Override
	public double getDeviationFrom(double averageValue) {
		// TODO Auto-generated method stub
		return 0;
	}

}
