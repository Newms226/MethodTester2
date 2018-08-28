package round;

import java.util.Arrays;
import java.util.Map;

import contestant.Contender;
import lap.Lap;
import lap.Laps;

public class RaceRound implements Round {
	public static RaceRound create(RoundContext context, Lap... laps) {
		return new RaceRound(laps, context);
	}
	
	private Lap[] roundLaps;
	private final RoundContext context;
	
	public RaceRound(Lap[] laps, RoundContext context) {
		roundLaps = Arrays.copyOf(laps, laps.length);
		this.context = context;
		Arrays.sort(roundLaps, Laps.ELAPSED_ORDER);
	}

	@Override
	public Lap[] getLaps() {
		return roundLaps;
	}

	@Override
	public int count() {
		return roundLaps.length;
	}

	@Override
	public RoundContext getContext() {
		return context;
	}


}
