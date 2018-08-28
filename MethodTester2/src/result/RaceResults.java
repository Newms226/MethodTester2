package result;

import java.util.List;
import java.util.Map;
import java.util.Set;

import contestant.Contender;
import judge.AnalysisScheme;
import judge.JudgeableContender;
import lap.LapPredicate;
import laplist.LapList;

public class RaceResults implements RaceOutcome {
	List<JudgeableContender> contenders;

	public RaceResults() {
		// TODO Auto-generated constructor stub
	}
	
	boolean register(JudgeableContender contender) {
		
	}

	@Override
	public Contender getWinner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Contender> getContenders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnalysisScheme getAnalysisScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LapPredicate getLapPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFullResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Contender, LapList> extractResults() {
		// TODO Auto-generated method stub
		return null;
	}

}
