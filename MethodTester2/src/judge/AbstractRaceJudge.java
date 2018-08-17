package judge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import contestant.Contender;
import lap.JudgedLapList;
import lap.Lap;

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
	
	private transient LapResult[] lapResults;
	
	private Map<Integer, Contender> IDMap;
	
	private boolean started;
	
	private boolean ended;
	
	private boolean raceRan;
	
	private int tieCount;

	public AbstractRaceJudge() {
		IDMap = new HashMap<>();
	}
	
	@Override
	public void register(Contender contender, Contender... contenders) {
		ensureNotStarted("Cannot register a new contender");
		
		register(contender);
		
		for (Contender con: contenders) {
			register(con);
		}
		
	}
	
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

	@Override
	public Lap lap(Contender contestant, long elapsed) throws IllegalArgumentException {
		lapResults[contestant.getID()].setElapsed(elapsed);
		
		return contenderMap.get(contestant).lap(elapsed);
	}
	
	@Override
	public boolean isStarted() {return started;}
	
	@Override
	public boolean isEnded() {return ended;}

	@Override
	public void start() {
		ensureNotStarted("Cannot start an already started race");
		if (count < 2) {
			throw new RaceProcedureException("Can only judge with at least 2 Contenders."
					+ " Count: " + count);
		}
		
		started = true;
		
		lapResults = new LapResult[count];
		for (int i = 0; i < count; i++) {
			lapResults[i] = new LapResult(i);
		}
		
		winners = new int[count];
		
	}

	@Override
	public void end() {
		if (!started) {
			throw new RaceProcedureException("Race was never started.");
		}
		
		
		raceRan = true;
	}

	
	private int winnerID;
	
	@Override
	public Contender getLapWinner() {
		Arrays.sort(lapResults);
		
		if (lapResults[0] == lapResults[1]) {
			tieCount++;
			return new TiedContender();
		}
		
		winnerID = lapResults[0].ID;
		winners[winnerID]++;
		
		return IDMap.get(winnerID);
	}

	@Override
	public Contender getTotalWinner() {
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

}
