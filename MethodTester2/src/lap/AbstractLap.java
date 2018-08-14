package lap;

import java.util.function.Predicate;

public abstract class AbstractLap implements Lap {

	private long elapased;
	private double deviation;
	
	public AbstractLap(long startLap, long endLap) throws LapRangeException {
		this(endLap - startLap);
	}
	
	public AbstractLap(long elapsed) throws LapRangeException {
		LapRangeException.assertValid(elapsed);
		
		this.elapased = elapsed;
		deviation = Double.NaN;
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
		return Math.round(averageValue < elapased ? elapased - averageValue 
				                                  : averageValue - elapased);
	}

	@Override
	public boolean queryAverageBased(AverageBinaryFunction averageBasedBinaryFunction) {
		// TODO Auto-generated method stub
		return false;
	}
}
