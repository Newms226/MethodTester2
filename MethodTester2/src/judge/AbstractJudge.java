package judge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import contestant.Contender;
import lap.Lap;
import lap2.LapPredicate;
import laplist.JudgedLapList;
import laplist.LapList;
import result.RaceOutcome;
import result.RaceResults;

abstract class AbstractJudge implements Judge {
	
	/**
	 * Maps the {@link Contender} objects to the {@link LapList} which were registered with this
	 * {@code Judge} object
	 */
	protected Map<Contender, JudgedLapList> contenderMap;
	
	protected int count;
	
	private String name;
	
	/**
	 * No-Arg constructor for serialization of subclasses.
	 */
	public AbstractJudge() {}
	
	public AbstractJudge(String name) {
		contenderMap = new HashMap<Contender, JudgedLapList>();
		this.name = name;
	}

	@Override
	public Collection<Contender> getContenders() {
		return Collections.unmodifiableCollection(contenderMap.keySet());
	}

	@Override
	public int count() {
		return count;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void register(Contender contender, Contender... contenders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RaceOutcome judge(LapPredicate lapAccepter, AnalysisScheme judgingScheme, 
			List<? extends JudgeableContender> contenders) throws IndexOutOfBoundsException {

		
		// get valid size
		int size = Math.min(contenders.get(0).size(), contenders.get(1).size());
		for (int i = 2; i < contenders.size(); i++) {
			size = Math.min(size, contenders.get(i).size());
		}
		
		// Intialize tools
		RaceResults toReturn = new RaceResults();
		
		// set valid IDs
		for (int i = 0; i < contenders.size(); i++) {
			contenders.get(i).setID(i);
			toReturn.
		}
		
		// evaluate laps
		
	}
	
	protected abstract Contender evaluate(int index);

	@Override
	public Contender getTotalWinner() {
		// TODO Auto-generated method stub
		return null;
	}

}
