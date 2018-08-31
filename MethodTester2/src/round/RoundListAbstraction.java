package round;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import lap.LapAbstraction;
import lap.LapListAbstraction;

/**
 * NOTE: ALL ROUND NUMBERS MUST BE ZERO-BASED!
 * @author Michael
 *
 */
public interface RoundListAbstraction {
	
	Iterator<LapAbstraction> iterator(String str);
	
	Stream<LapAbstraction> stream(String str);
	
	RoundContext getContext();
	
	void lap(String str, LapAbstraction lap);
	
	void lap(String str, long elapsed);
	
	LapAbstraction getLap(String str, int round);
	
	RoundAbstraction getRound(int round);
	
	int getIndex(String str, int round);
	
	LapListAbstraction getLapList(String str);
	
	Set<LapListAbstraction> getLapLists();
}
