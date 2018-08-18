package lap;

import java.util.function.Predicate;

public class RaceLap extends AbstractLap {

	private static final long serialVersionUID = 6008370315732668905L;

	public RaceLap(int ID, long elapsed) throws LapRangeException {
		super(ID, elapsed);
	}
	
	public RaceLap(int ID, long startLap, long endLap) throws LapRangeException {
		super(ID, endLap - startLap);
	}
}
