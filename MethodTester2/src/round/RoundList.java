package round;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lap.Lap;
import laplist.LapList;

public class RoundList {
	private List<Round> rounds;
	
	private RoundContext context;
	
	private int length;
	
	public RoundList(RoundContext context, List<LapList> lapLists) {
		length = context.contenderCount();
		rounds = new ArrayList<>(context.raceSize());
		
		Lap[] tempArray;
		for (int i = 0; i < context.raceSize(); i++) {
			tempArray = new Lap[length];
			for (int l = 0; l < context.contenderCount(); l++) {
				tempArray[l] = lapLists.get(l).get(i);
			}
			rounds.add(RaceRound.create(context, tempArray));
		}
	}
	
	public Round getRound(int i) throws IndexOutOfBoundsException {
		return rounds.get(i);
	}
	
	public RoundContext getRoundContext() {
		return context;
	}

}
