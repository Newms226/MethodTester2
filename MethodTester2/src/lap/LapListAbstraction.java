package lap;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import round.CSV;

public interface LapListAbstraction extends CSV, Serializable {
	
	String getName();
	
	SummaryStatistics getSummaryStatistics();
	
	/**
	 * Should return an IMMUTABLE copy of the list!
	 * @return
	 */
	List<LapAbstraction> getLaps();
}
