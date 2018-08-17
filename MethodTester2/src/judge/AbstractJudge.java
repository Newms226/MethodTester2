package judge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import contestant.Contender;
import lap.JudgedLapList;
import lap.Lap;
import lap.LapList;
import lap.LapPredicate;

abstract class AbstractJudge implements Judge {
	
	/**
	 * Maps the {@link Contender} objects to the {@link LapList} which were registered with this
	 * {@code Judge} object
	 */
	protected Map<Contender, JudgedLapList> contenderMap;
	
	protected int count;
	
	public final String name;
	
	/**
	 * No-Arg constructor for serialization of subclasses.
	 */
	public AbstractJudge(String name) {
		contenderMap = new HashMap<Contender, JudgedLapList>();
		this.name = name;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFullResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void analyizeFrom(Collection<Contender> contenders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Judge filter(LapPredicate predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	@Override
	public Map<Contender, LapList> extractResults() {
		return Collections.unmodifiableMap(contenderMap);
	}

	@Override
	public Collection<Contender> getContenders() {
		return Collections.unmodifiableCollection(contenderMap.keySet());
	}

	@Override
	public int count() {
		return count;
	}

	@Override
	public String getName() {
		return name;
	}

}
