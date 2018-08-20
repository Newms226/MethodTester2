package lap;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import tools.NumberTools;

/**
 * Class which represents a non-consecutive {@link Lap} object. Primary use is outside of a race
 * setting, such as the runtime of a method in its normal execution environment.
 * 
 * @author Michael Newman
 *
 */
public interface TimeStampedLap extends Lap {

//	/**
//	 * Serial Version UID
//	 */
//	private static final long serialVersionUID = 1772162752192086245L;
//	
//	/**
//	 * Mills-precise time instance was created, generated inside the constructor
//	 */
//	private final long timeCreated;
//	
//	/**
//	 * Mills-precise time instance was created, generated inside the constructor, as 
//	 * an {@link Instant} object.
//	 */
//	private Instant timeCreatedAsInstant;
//	
//	public TimeStampedLap(long elapsed) {
//		super(-1, elapsed);
//		
//		timeCreated = System.currentTimeMillis();
//	}
//	
//	public TimeStampedLap(long start, long end) {
//		super(-1, end - start);
//		
//		timeCreated = System.currentTimeMillis();
//	}
//	
//	public long getCreationTimeMills() {
//		return timeCreated;
//	}
//	
//	public Instant getCreationTimeInstant() {
//		ensureInstantObjPresnet();
//		return timeCreatedAsInstant;
//	}
//	
//	private void ensureInstantObjPresnet() {
//		if (timeCreatedAsInstant == null) {
//			timeCreatedAsInstant = Instant.ofEpochMilli(timeCreated);
//		}
//	}
//	
//	@Override
//	public String toString() {
//		return toString(DateTimeFormatter.RFC_1123_DATE_TIME);
//	}
//	
//	public String toString(DateTimeFormatter formatter) {
//		ensureInstantObjPresnet();
//		return formatter.format(timeCreatedAsInstant) + ": " + Lap.nanosecondsToString(elapased) 
//				+ (deviationCalculated ? " σ²: " + NumberTools.format(deviation) 
//				                       : "");
//	}

}
