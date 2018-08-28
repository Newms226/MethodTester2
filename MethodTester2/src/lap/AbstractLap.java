package lap;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;

import tools.NumberTools;

abstract class AbstractLap implements Lap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 216886856997130633L;
	
	protected long elapased;
	
	protected long timestamp;
	
	protected LapStatistics stats;
	
	public AbstractLap() {}
	
	public AbstractLap(long startLap, long endLap) throws LapRangeException {
		this(endLap - startLap);
	}
	
	public AbstractLap(long elapsed) throws LapRangeException {
		timestamp = System.currentTimeMillis();
		
		LapRangeException.assertValid(elapsed);
		
		this.elapased = elapsed;
		stats = new LapStatistics(this);
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
	public Duration getAsDuration() {
		return Duration.ofNanos(elapased);
	}

	@Override
	public long getTimeStampMills() {
		return timestamp;
	}

	@Override
	public Instant getTimeStampInstant() {
		return Instant.ofEpochMilli(timestamp);
	}

	@Override
	public LapStatistics getLapStats() {
		return stats;
	}
	
	@Override
	public String toString() {
		return getContext().getIDString() + ": " + Laps.nanosecondsToString(elapased)
			+ (stats.isDevationFromStandardCalculated() 
					? " σ: " + Laps.nanosecondsToString(stats.getDeviationFromStandard())
					: "");
	}
	
	@Override
	public Lap clone() {
		return new GenericLap(getElapsed());
	}
}
