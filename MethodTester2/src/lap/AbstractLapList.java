package lap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Vector;

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


	@Override
	public double[] getAsDoubleArray() {
		return laps.stream().mapToDouble(Lap::getElapsed).toArray();
	}

	@Override
	public List<Lap> getLaps() {
		ArrayList<Lap> toReturn = new ArrayList<Lap>((int)getCount());
		for (Lap l: laps) toReturn.add(new ImmutableLap(l.getElapsed()));
		return toReturn;
	}

	@Override
	public Map<Integer, Lap> getIDLapMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LongSummaryStatistics getSummaryStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lap lap(long elapsed) throws LapRangeException {
		tempLap = new ImmutableLap(elapsed); // throws LapRangeException
		summaryStatistics.accept(elapsed);
		laps.add(tempLap);
		
		return tempLap;
	}

	@Override
	public Lap lap(Lap lap) {
		summaryStatistics.accept(lap.getElapsed());
		laps.add(tempLap);
		
		return tempLap;
	}
}
