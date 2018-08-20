package lap;

public class Stopwatch implements Lap {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1504672495481535195L;
	
	private long startLap;
	
	private long endLap;
	
	private long elapsed;

	public Stopwatch() {
		startLap = System.nanoTime();
	}
	
	public Stopwatch(long initalTime) {
		LapRangeException.assertValid(initalTime);
		
		startLap = initalTime;
	}
	
	public long end() {
		endLap = System.nanoTime();
		elapsed = endLap - startLap;
		
		LapRangeException.assertValid(elapsed);
		
		return elapsed;
	}

	@Override
	public int compareTo(Lap o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getElapsed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getDeviation(double averageValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deviationCalculated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getDeviation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Lap clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSequential() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getIDString() {
		// TODO Auto-generated method stub
		return null;
	}

}
