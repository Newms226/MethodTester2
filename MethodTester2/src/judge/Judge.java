package judge;

import contestant.Contender;
import tools.NumberTools;

public interface Judge {
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                      LAP METHODS                                           *
	 *                                                                                            *
	 **********************************************************************************************/
	
	/**
	 * Default functional method of this interface. 
	 * <p>
	 * Logs an elapsed time of this contender. 
	 * 
	 * @param contestant the object competing in a race
	 * @param elapsed the time elapsed over the 
	 * @return the {@code Contender} passed as a parameter
	 * @throws IllegalArgumentException if elapsed &lt 0
	 */
	Contender lap(Contender contestant, long elapsed) throws IllegalArgumentException;
	
	/**
	 * Overloaded method which automatically calculates the elapsed time of a given lap. Note that
	 * startLap MUST BE &lt= endLap
	 * @param contestant the object competing in this race
	 * @param startLap the beginning of the lap
	 * @param endLap the end of the lap
	 * 
	 * @return the {@code Contender} passed as a parameter
	 * 
	 * @throws IllegalArgumentException if start &gt end
	 */
	default Contender lap(Contender contestant, long startLap, long endLap)
				throws IllegalArgumentException {
		if (startLap <= endLap) {
			return lap(contestant, endLap - startLap);
		}
		
		// else
		throw new IllegalArgumentException("INVALID RANGE > start: " + NumberTools.format(startLap)
				+ " end: " + NumberTools.format(endLap));
	}
	
	/**
	 * 
	 * @param contender
	 * @return the {@code Contender} passed as a parameter
	 */
	Contender register(Contender contender);
}
