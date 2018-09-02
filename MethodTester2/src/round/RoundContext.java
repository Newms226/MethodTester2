package round;

import java.awt.image.IndexColorModel;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The organizational anchor of this package. Functions as a mapping of a 
 * {@code String} object to an {@code int} for the purposes of identification
 * and and array access within a class implementing 
 * {@link RoundListAbstraction}. The {@code String} object should be a final
 * name which easily identifies the object or method being timed with this
 * package. This package refers the {@code String} objects passed into this
 * object as 'Contenders'. <p>
 * 
 * Class is <strong>immutable</strong> and {@code final} to prevent the 
 * alteration of its' mappings once created. As such, a factory method is
 * provided for the creation of this class. Note that it requires at least
 * two contenders to be registered, as any fewer does not fall within the 
 * context of a race.<p>
 * 
 * Persistent uses of this package, such as in {@link} TODO, should not use this
 * class and instead should use {@link} TODO
 * 
 * @author Michael Newman
 *
 */
public final class RoundContext {
	private static final Logger log = LogManager.getLogger();
	
	/**
	 * Factory method to create {@code RoundContext} objects.
	 * 
	 * This class does not accept any {@code null} objects and will throw a 
	 * {@link NullPointerException} if this contract is violated.
	 * 
	 * @param contender1 The first Contender to map.
	 * @param contender2 The second Contender to map.
	 * @param otherContenders any other Contenders to map.
	 * 
	 * @return a {@code RoundContext} object created. 
	 * 
	 * @throws RoundContextException if any parameters are {@code null}.
	 */
	public static RoundContext from(String contender1, String contender2, 
	                                String...otherContenders)
			throws RoundContextException 
	{
		String entryMessage = "Called static Factory method with " + contender1
				 + ", " + contender2 + (otherContenders.length > 0 ? " " 
						 + Arrays.toString(otherContenders) : "");
		log.traceEntry(entryMessage);
		
		String[] contenders = new String[2 + otherContenders.length];
		
		try {
			// Add a minimum of 2 contenders
			contenders[0] = Objects.requireNonNull(contender1);
			contenders[1] = Objects.requireNonNull(contender2);
			
			// Add contenders from the optional array
			// TODO: Will this throw a NullPointer if the array is null?
			for (int i = 0, j = 2; i < otherContenders.length; i++, j++) {
				contenders[j] = Objects.requireNonNull(otherContenders[i]);
			}
		} catch (NullPointerException e) {
			throw log.throwing(Level.ERROR, new RoundContextException(
					"Null strings cannot be registered with a "
					+ "RoundContextObject"));
		}
		
		Arrays.sort(contenders, String.CASE_INSENSITIVE_ORDER);
		return log.traceExit("Created round context object from: {}", 
				             new RoundContext(contenders));
	}
	
	/**
	 * Count of the current contenders. Must be checked whenever adding a 
	 * contender to ensure validity.
	 */
	private int count;
	
	/**
	 * Mapping of {@code String} objects, representative of a contender's name
	 * to its identification (as an {@code int}).
	 */
	private final String[] contenders;
	
	/**
	 * Private constructor for use by 
	 * {@link RoundContext#from(String, String, String...)}.
	 * 
	 * @param contenders the array of {@code String} objects to map.
	 */
	private RoundContext(String...contenders) {
		count = contenders.length;
		this.contenders = contenders;
	}
	
	/**
	 * Returns the name of the contender mapped to the {@code int} passed as 
	 * a parameter or throws a {@link RoundContextException} if no such 
	 * mapping exists.
	 * 
	 * @param index the index of inquiry.
	 * 
	 * @return the name of the Contender mapped to the {@code int} passed as a 
	 *         parameter.
	 *         
	 * @throws RoundContextException if no such mapping exists. 
	 */
	public String getMapping(int index) throws RoundContextException {
		if (index >= count) {
			throw log.throwing(new RoundContextException("Index out of bounds"
					+ " exception. No Mapping for: " + index));
		}
		
		return contenders[index];
	}
	
	/**
	 * Returns the {@code int} mapping of the {@code String} object passed to
	 * it as a reference or throws a {@link RoundContextException} if no such 
	 * mapping exists. 
	 * 
	 * @param str the Contender mapping to search for.
	 * 
	 * @return the int mapping of the parameter.
	 * 
	 * @throws RoundContextException if no such mapping exists.
	 */
	// TODO: This method is called the most, its inefficent to do a binary 
    //       search each time!
	public int getMapping(String str) throws RoundContextException {
		int toReturn = Arrays.binarySearch(contenders, str);
		if (toReturn == -1) {
			throw log.throwing(new RoundContextException("No mapping present"
					+ " for: " + str));
		}
		
		// else
		return toReturn;
	}
	
	/**
	 * Returns the count of mappings contained in this object.
	 * 
	 * @return the count of mappings contained in this object.
	 */
	public int count() {
		return count;
	}
}
