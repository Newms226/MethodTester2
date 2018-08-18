package result;

import java.io.Serializable;
import java.util.List;

import contestant.Contender;
import lap.Lap;
import lap.LapList;

public interface ResultSet extends Comparable<ResultSet>, Serializable, Cloneable {
	
	Contender getContender();
	
	LapList getLapSet();
	
	int getWinCount();
	
	void increaseWinCount();
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                   DELEGATION METHODS                                       *
	 *                                                                                            *
	 **********************************************************************************************/
	
	
	default double getTotalElapsedTime() {
		return getLapSet().getSum();
	}
	
	default double getAverageTime() {
		return getLapSet().getAverage();
	}
	
	default double getAverageVariance() {
		return getLapSet().getAverageVariance();
	}
	
	default List<Lap> getSlowest() {
		return getLapSet().getMin();
	}
	
	default List<Lap> getFastest() {
		return getLapSet().getMax();
	}
}
