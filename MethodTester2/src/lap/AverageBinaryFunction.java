package lap;

@FunctionalInterface
public interface AverageBinaryFunction {
	
	/**
	 * Queries an object based on an average in the form of a double.
	 * @param average the average of the total set
	 * @return 
	 */
	boolean query(double average);
}
