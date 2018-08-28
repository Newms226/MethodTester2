package round;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.math3.util.Pair;

import contestant.Contender;
import context.Context;
public class RoundContext {
	
	private Lock contextLock; // TODO
	
	private int contenderCount;
	
	private int raceSize;
	
	private Map<Integer, Contender> IDMap;
	
	private Map<Contender, Integer> contenderMap;
	
	private int nextID;

	public RoundContext(int raceSize) {
		contenderMap = new HashMap<>();
		IDMap = new HashMap<>();
		
		this.contenderCount = raceSize;
	}
	
	public int contenderCount() {
		return contenderCount;
	}
	
	public int raceSize() {
		return raceSize;
	}

	public Map<Integer, Contender> getIDMap() {
		return Collections.unmodifiableMap(IDMap);
	}
	
	protected int register(Contender contender) throws IllegalArgumentException {
		if (contenderMap.containsKey(contender)) {
			return contenderMap.get(contender);
		}
		
		// else
		Integer mappingID = getNextIDAvailable();
		contenderMap.put(contender, mappingID);
		IDMap.put(mappingID, contender);
		return mappingID;
	}
	
	private synchronized int getNextIDAvailable() throws IllegalArgumentException {
		if (nextID >= contenderCount) {
			throw new IllegalArgumentException("EXCEEDED RACE SIZE > Count: " + nextID);
		}

		return nextID++;
	}
	
	public int getMapping(Contender contender) {
		return contenderMap.get(contender);
	}
	
	public Contender getMapping(int ID) {
		return IDMap.get(ID);
	}


}
