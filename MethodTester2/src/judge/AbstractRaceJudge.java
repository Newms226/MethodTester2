package judge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import contestant.Contender;
import contestant.TiedContender;
import lap.JudgedLapList;
import lap.Lap;
import lap.LapList;
import lap.LapPredicate;

public class AbstractRaceJudge extends AbstractJudge implements RaceJudge {
	
	private static class LapResult implements Comparable<LapResult> {
		private final int ID;
		private long elapsed;
		
		private LapResult(int ID) {
			this.ID = ID;
		}
		
		private void setElapsed(long elapsed) {
			this.elapsed = elapsed;
		}
		
		@Override
		public int compareTo(LapResult o) {
			if (elapsed < o.elapsed) return -1;
			if (elapsed > o.elapsed) return 1;
			return 0;
		}
	}
	
	private int[] winners;
	
	private Map<Integer, Contender> IDMap;
	
	private boolean started;
	
	private boolean ended;
	
	private boolean raceRan;
	
	private int tieCount;

	public AbstractRaceJudge(String name) {
		super(name);
		IDMap = new HashMap<>();
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                   PROCEDURE METHODS                                        *
	 *                                                                                            *
	 **********************************************************************************************/
	
	@Override
	public Lap lap(Contender contestant, long elapsed) throws IllegalArgumentException {
		if (elapsed < 0) {
			throw new IllegalArgumentException("Elapsed cannot be negative: " + elapsed);
		}
		
		return contenderMap.get(contestant).lap(elapsed);
	}
	
	@Override
	public void register(Contender contender, Contender... contenders) {
		ensureNotStarted("Cannot register a new contender");
		
		register(contender);
		
		for (Contender con: contenders) {
			register(con);
		}
		
	}
	
	@Override
	public void start() {
		ensureNotStarted("Cannot start an already started race");
		if (count < 2) {
			throw new RaceProcedureException("Can only judge with at least 2 Contenders."
					+ " Count: " + count);
		}
		
		started = true;
		winners = new int[count];
	}

	@Override
	public void end() {
		if (!started) {
			throw new RaceProcedureException("Race was never started.");
		}
		
		
		raceRan = ended = true;
	}
	
	@Override
	public boolean isStarted() {return started;}
	
	@Override
	public boolean isEnded() {return ended;}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                     ANALYSIS METHODS                                       *
	 *                                                                                            *
	 **********************************************************************************************/
	
	@Override
	public Contender getTotalWinner() {
		if (!raceRan) {
			throw new RaceProcedureException("Race was never ran");
		}
		if (!ended) {
			throw new RaceProcedureException("Cannot get total winner if race has not finished");
		}
		
		Map<Integer, LapList> lapResultMap = 
		
		
		
		
		
		
		int winnerID;

		// TODO: Ensure ended!
		winnerID = 0;
		
		for (int i = 1; i < count; i++) {
			if (winners[winnerID] < winners[i]) {
				winnerID = i;
			} else if (winners[winnerID] == winners[i]) {
				return new TiedContender();
			}
		}
		
		return IDMap.get(winnerID);
	}

	@Override
	public void analyizeFrom(Map<Contender, LapList> contenders) {
		// TODO Auto-generated method stub
		
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
	public Judge filter(LapPredicate predicate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                      HELPER METHODS                                        *
	 *                                                                                            *
	 **********************************************************************************************/
	
	private void register(Contender contender) {
		contenderMap.put(contender, new JudgedLapList());
		contender.setID(count);
		IDMap.put(new Integer(count), contender);
		count++;
	}

	private void ensureNotStarted(String cause) {
		if (started) {
			throw new RaceProcedureException("Race has already started. " + cause);
		}
	}


	


	

		


	

}

//@Override
//public Contender getLapWinner() {
//	Arrays.sort(lapResults);
//	
//	if (lapResults[0] == lapResults[1]) {
//		tieCount++;
//		return new TiedContender();
//	}
//	
//	winnerID = lapResults[0].ID;
//	winners[winnerID]++;
//	
//	return IDMap.get(winnerID);
//}
