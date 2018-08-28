package lap;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public interface Lap extends Comparable<Lap>, Serializable, Cloneable {
	long getElapsed();
	
	Duration getAsDuration();
	
	long getTimeStampMills();
	
	Instant getTimeStampInstant();
	
	LapContext getContext();
	
	LapStatistics getLapStats();
	
	public default boolean query(LapPredicate predicate) {
		return predicate.test(this);
	}
	
	Lap clone();
}
