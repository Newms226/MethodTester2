package judge;

import java.util.Collection;

import contestant.Contender;
import lap.Lap;
import tools.NumberTools;

public interface RaceJudge extends Judge, Action {
	
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
	 *  
	 * @return the {@code Contender} passed as a parameter
	 * 
	 * @throws IllegalArgumentException if elapsed &lt 0
	 */
	Lap lap(Contender contestant, long elapsed) throws IllegalArgumentException;
	
	/**
	 * Overloaded method which automatically calculates the elapsed time of a given lap. Note that
	 * startLap MUST BE &lt= endLap
	 * 
	 * @param contestant the object competing in this race
	 * @param startLap the beginning of the lap
	 * @param endLap the end of the lap
	 * 
	 * @return the {@link Contender} passed as a parameter
	 * 
	 * @throws IllegalArgumentException if start &gt end
	 */
	default Lap lap(Contender contestant, long startLap, long endLap)
				throws IllegalArgumentException {
		if (startLap <= endLap) {
			return lap(contestant, endLap - startLap);
		}
		
		// else
		throw new IllegalArgumentException("INVALID RANGE > start: " + NumberTools.format(startLap)
				+ " end: " + NumberTools.format(endLap));
	}
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                    PROCEDURE METHODS                                       *
	 *                                                                                            *
	 **********************************************************************************************/
	
	
	
//	/**
//	 * Method to call after each lap which decides the winner of the lap. Note that there is no 
//	 * functional requirement to this method, an implementation may choose to decide the winner
//	 * of each lap once the race has finished. 
//	 * 
//	 * <p>
//	 * Custom implementations of this interface should use this method to examine the results of the
//	 * races and decide a winner. This projects implementations regard the winner of the total race
//	 * as the {@code Contender} who wins the most lap <strong>NOT</strong> the {@link Contender}
//	 * who has the lowest average time. While this is the contract specified by this interface,
//	 * their is no functional requirement to it.
//	 * 
//	 * @return {@code Contender} who had the slowest elapsed time over a single lap
//	 */
//	Contender getLapWinner();

}
