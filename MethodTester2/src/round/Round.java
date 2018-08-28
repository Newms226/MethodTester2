package round;

import java.util.Map;

import contestant.Contender;
import lap.Lap;

public interface Round {
	
	Lap[] getLaps();
	
	int count();

	RoundContext getContext();
	
}
