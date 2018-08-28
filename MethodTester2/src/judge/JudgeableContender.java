package judge;

import contestant.Contender;
import laplist.LapList;

public interface JudgeableContender extends Contender, LapList, Comparable<JudgeableContender> {
	
	Contender getContender();
	
	LapList getLapSet();
	
	int getWinCount();
	
	void increaseWinCount();
	
	default int compareTo(JudgeableContender other) {
		if (getWinCount() < other.getWinCount()) return -1;
		if (getWinCount() > other.getWinCount()) return 1;
		return 0;
	}
}
