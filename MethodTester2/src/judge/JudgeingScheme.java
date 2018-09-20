package judge;

import java.util.Comparator;

@FunctionalInterface
public interface JudgeingScheme<T> {
	
	/**
	 * Judges a Collection &lt; according to some pre-determined scheme. Note that this class is 
	 * modeled around {@link Comparator} but places a greater restircition on what methods calls 
	 * are permitted.<p>
	 * 
	 * Method should always sort the value with a lower score, or the value equivilent to when
	 * {@code a.compareTo(b) == -1}.
	 * 
	 * @param a the first object to test
	 * @param b the second object to test
	 * 
	 * @return Equivlent to {@code return (a < b ) ? a : b}
	 */
	T judge(T a, T b);

}
