package lap;

import java.util.Comparator;

public final class RaceLap extends AbstractLap {
	
	public static final Comparator<RaceLap> ID_ORDER = (a, b) -> {
		if (a.getRoundNumber() < b.getRoundNumber()) return -1;
		if (a.getRoundNumber() > b.getRoundNumber()) return 1;
		return 0;
	};
	
	public static final Comparator<Lap> CASTED_ID_ORDER = (a, b) -> {
		if ( ((RaceLap)a).getRoundNumber() < ((RaceLap)b).getRoundNumber()) return -1;
		if ( ((RaceLap)a).getRoundNumber() > ((RaceLap)b).getRoundNumber()) return 1;
		return 0;
	};


	/**
	 * 
	 */
	private static final long serialVersionUID = -3529908862230386894L;
	
	private final RaceLapContext context;

	public RaceLap(int roundNumber, long startLap, long endLap) throws LapRangeException {
		super(startLap, endLap);
		context = new RaceLapContext(this, roundNumber);
	}

	public RaceLap(int roundNumber, long elapsed) throws LapRangeException {
		super(elapsed);
		context = new RaceLapContext(this, roundNumber);
	}
	
	public int getRoundNumber() {
		return context.ROUND_NUMBER;
	}

	@Override
	public RaceLapContext getContext() {
		return context;
	}
}
