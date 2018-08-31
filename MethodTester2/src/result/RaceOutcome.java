package result;

import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import contestant.Contender;
import judge.AnalysisScheme;
import judge.Judge;
import lap.LapAbstraction;
import lap.LapAbstraction;
import laplist.LapList;

/**
 * Represents the outcome of the race, including
 *  1. All contender objects
 *    a. Their corresponding LapLists
 *      > Contains a sumamry statistics object    
 * @author Michael
 *
 */
public interface RaceOutcome {
	
	Contender getWinner();
	
	default NavigableSet<Contender> getSortedContenderSet(){
		return (NavigableSet<Contender>) getContenders()
				.stream()
				.sorted()
				.collect(Collectors.toSet());
	}
	
	Set<Contender> getContenders();
	
	AnalysisScheme getAnalysisScheme();
	
	LapPredicate getLapPredicate();
	
	/**
	 * Method should return a simple summary of the results, noting the winner & the percent they
	 * won by, plus other summary information.
	 * 
	 * @return a summary of the results judged.
	 */
	String getSummary();
	
	/**
	 * Method should return all the information it has calculated on the race. Note that the 
	 * {@link LapSet} objects have methods for an object-oriented analysis of the results.
	 * 
	 * @return a {@code String} representing all of the data the judge has on the outcome.
	 */
	String getFullResults();
	
	/**
	 * Method to extract the {@link LapSet}s monitored by the judge. Essentially, this method 
	 * de-couples the {@link CoupledContender} objects it monitors. 
	 * 
	 * @return
	 */
	Map<Contender, LapList> extractResults();
}
