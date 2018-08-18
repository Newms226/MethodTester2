package lap;

import java.util.function.Predicate;

import tools.NumberTools;

abstract class AbstractLap implements Lap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 216886856997130633L;
	
	protected long elapased;
	
	protected long deviation;
	
	protected boolean deviationCalculated;
	
	private int ID;
	
	public AbstractLap(int ID, long startLap, long endLap) throws LapRangeException {
		this(ID, endLap - startLap);
	}
	
	public AbstractLap(int ID, long elapsed) throws LapRangeException {
		LapRangeException.assertValid(elapsed);
		
		this.elapased = elapsed;
		this.ID = ID;
	}

	@Override
	public int compareTo(Lap o) {
		if (elapased < o.getElapsed()) return -1;
		if (elapased > o.getElapsed()) return 1;
		return 0;
	}

	@Override
	public long getElapsed() {
		return elapased;
	}

	@Override
	public long getDeviation(double averageValue) {
		deviationCalculated = true;
		deviation = Math.round(averageValue < elapased ? elapased - averageValue 
                : averageValue - elapased);
		return deviation;
	}

	@Override
	public boolean deviationCalculated() {
		return deviationCalculated;
	}

	@Override
	public long getDeviation() {
		return deviation;
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public boolean query(LapPredicate predicate) {
		return predicate.test(this);
	}
	
	@Override
	public String toString() {
		return NumberTools.format(ID) + ": " + Lap.nanosecondsToString(elapased) 
			+ (deviationCalculated ? " σ²: " + NumberTools.format(deviation) : "");
	}
}
