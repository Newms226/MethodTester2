package test;

/**
 * Analysis scheme for a test pair.
 * 
 * @author Michael
 *
 * @param <T>
 */
@FunctionalInterface
public interface TestPairPredicate<T> {
	
	/**
	 * Acceptor object for a TestPair. Typical implementation is an analysis 
	 * scheme similar to equals or comparable.
	 * 
	 * @param expected
	 * @param result
	 * @return
	 */
	boolean pass(T expected, T result);
}
