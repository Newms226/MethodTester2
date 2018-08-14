package lap;

import tools.NumberTools;

public class LapRangeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8531268391108362246L;
	
	public static void assertValid(long startLap, long endLap) {
		if (testValiditity(startLap, endLap)) {
			return;
		}
		
		// else
		throw new LapRangeException(startLap, endLap);
	}
	
	public static boolean testValiditity(long startLap, long endLap) {
		return startLap <= endLap;
	}
	
	public static void assertValid(long elapsed) {
		if (testValiditity(elapsed)) {
			return;
		}
		
		// else
		throw new LapRangeException(elapsed);
	}
	
	public static boolean testValiditity(long elapsed) {
		return elapsed >= 0;
	}

	public LapRangeException(long startLap, long endLap) {
		super(NumberTools.format(startLap) + " > " + NumberTools.format(endLap));
	}
	
	public LapRangeException(long elapsed) {
		super(NumberTools.format(elapsed) + " is NEGATIVE");
	}

}
