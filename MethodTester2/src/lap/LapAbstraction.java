package lap;

import java.time.Instant;

public interface LapAbstraction extends TimeStamped {
	
	long getElapsed();
	
	LapStats getLapStatistics();

}
