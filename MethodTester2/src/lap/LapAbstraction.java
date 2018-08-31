package lap;

import java.time.Duration;
import java.time.Instant;

public interface LapAbstraction extends TimeStamped {
	
	long getElapsed();
	
	LapStats getLapStatistics();
	
	default Duration getAsDuration() {
		return Duration.ofNanos(getElapsed());
	}

}
