package round;

import lap.LapAbstraction;
import lap.TimeStamped;

public interface RoundAbstraction extends TimeStamped {
	RoundContext getRoundContext();
	
	LapAbstraction[] getLaps();
	
	int getRoundNumber();
}
