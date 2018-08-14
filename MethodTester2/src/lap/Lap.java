package lap;

import java.io.Serializable;
import java.util.function.LongConsumer;
import java.util.function.Predicate;

public interface Lap extends Cloneable, Comparable<Lap>, Serializable {
	
	long getElapsed();
	
	long getDeviation(double averageValue);
	
	boolean queryAverageBased(AverageBinaryFunction averageBasedBinaryFunction);
	
	String getSummary();
}
