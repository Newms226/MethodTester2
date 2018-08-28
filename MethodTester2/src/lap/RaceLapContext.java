package lap;

import java.util.Comparator;

import tools.NumberTools;

public class RaceLapContext implements LapContext {
	
	public final int ROUND_NUMBER;
	
	private final Lap LAP;
	
	public RaceLapContext(Lap lap, int roundNumber) {
		ROUND_NUMBER = roundNumber;
		LAP = lap;
	}
	
	public Lap getLap() {
		return LAP;
	}

	@Override
	public String getIDString() {
		return NumberTools.format(ROUND_NUMBER);
	}
}
