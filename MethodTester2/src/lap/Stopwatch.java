package lap;

public class Stopwatch extends AbstractLap {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1504672495481535195L;
	
	private long startLap;
	
	private long endLap;
	
	private GenericContext context;

	public Stopwatch() {
		context = new GenericContext(this);
	}
	
	public Stopwatch(long initalTime) throws LapRangeException {
		LapRangeException.assertValid(initalTime);
		context = new GenericContext(this);
		
		startLap = initalTime;
	}
	
	public void start() {
		startLap = System.nanoTime();
	}
	
	public long end() throws LapRangeException {
		endLap = System.nanoTime();
		elapased = endLap - startLap;
		
		LapRangeException.assertValid(elapased);
		
		return elapased;
	}

	@Override
	public LapContext getContext() {
		return context;
	}
	
	@Override
	public String toString() {
		return Laps.nanosecondsToString(elapased)
				+ (stats.isDevationFromStandardCalculated() 
						? " σ: " + Laps.nanosecondsToString(stats.getDeviationFromStandard())
						: "");
	}

}
