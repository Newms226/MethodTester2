package lap;

import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public interface LapListAbstraction {
	
	String getName();
	
	SummaryStatistics getSummaryStatistics();
	
	/**
	 * Should return an IMMUTABLE copy of the list!
	 * @return
	 */
	List<LapAbstraction> getLaps();
}
