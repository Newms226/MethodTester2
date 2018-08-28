package judge;

import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import contestant.Contender;
import lap2.LapPredicate;
import laplist.LapList;
import result.RaceOutcome;

public class JudgedOutcome implements RaceOutcome {
	Set<JudgeableContender> contenders;

	JudgedOutcome() {
		// TODO Auto-generated constructor stub
	}
	
	<T extends Contender> boolean register(T contender) {
		return 
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
	public SummaryStatistics getSummaryStatistics() {
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
