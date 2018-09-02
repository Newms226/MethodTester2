package round;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lap.Lap;
import lap.LapAbstraction;
import lap.LapListAbstraction;
import tools.Log4JTools;
import tools.NumberTools;

public final class RoundList implements RoundListAbstraction {
	private static final Logger log = LogManager.getLogger();
	
	private final int runFor;
	
	private final RoundContext context;
	
	private final boolean nanoSecondPrecise;
	
	private final SummaryStatistics[] summaryStats;
	
	private final int count;
	
	private LapAbstraction[][] laps;
	
	public RoundList(RoundContext context, int runFor, 
			         boolean nanoSecondPrecise) 
	{
		Log4JTools.assertNonNull(context, log);
		if (runFor <= 0) {
			throw log.throwing(new IllegalArgumentException("runFor must be at "
					+ "least 1: " + runFor));
		}
		
		this.context = context;
		this.runFor = runFor;
		this.nanoSecondPrecise = nanoSecondPrecise;
		this.count = context.count();
		summaryStats = new SummaryStatistics[count];
		for (int i = 0; i < count; i++) {
			summaryStats[i] = new SummaryStatistics();
		}
		laps = new Lap[runFor][count];
		
		log.traceExit("Created new " + getClass() + " object of size " + count);
	}

	@Override
	public Iterator<LapAbstraction> iterator(String str) {
		return new LapBasedRoundIterator(str);
	}

	@Override
	public Stream<LapAbstraction> stream(String str) {
		return StreamSupport.stream(
					Spliterators.spliterator(new LapBasedRoundIterator(str),
							                 runFor,
							                 Spliterator.IMMUTABLE 
							                 | Spliterator.ORDERED), // TODO: is this right?
					true);
	}

	@Override
	public RoundContext getContext() {
		return context;
	}

	// TODO: This is very labour intensive for something much easier accomplished with a simplier data sctructure
	@Override
	public void lap(String str, LapAbstraction lap) {
		log.traceEntry("lap({}, {})", Objects.requireNonNull(str), 
				Objects.requireNonNull(lap));
		int index = context.getMapping(str + currentBaseIndex);
		indexRangeCheck(index);
		
		// look out for setting previously non-null objects
		if (laps[index] != null) {
			log.warn("Lap at round " + NumberTools.format(index / runFor) 
				+ " is currently " + laps[index] + " and is being set to "
				+ lap);
		}
		
		// set value;
		laps[index] = lap;
		
		// reset additions count
		if (additions == count) {
			additions = 0;
			currentBaseIndex += count;
		}
		
		log.traceExit("Set " + str + "'s lap as " 
				+ NumberTools.format(index / runFor));
	}

	@Override
	public void lap(String str, long elapsed) {
		lap(str, Lap.from(elapsed));
	}

	@Override
	public LapAbstraction getLap(String str, int round) {
		roundRangeCheck(round);
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoundAbstraction getRound(int round) {
		roundRangeCheck(round);
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex(String str, int round) {
		roundRangeCheck(round);
		
		return context.getMapping(str) + (round * count);
	}

	@Override
	public LapListAbstraction getLapList(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<LapListAbstraction> getLapLists() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void roundRangeCheck(int round) throws IndexOutOfBoundsException {
		if (round >= runFor)
			throw log.throwing(new IndexOutOfBoundsException(
					"Invalid round number. Max: = " 
					+ NumberTools.format(runFor) + " Entered: " 
					+ NumberTools.format(round)));
	}
	
	private class LapBasedRoundIterator implements Iterator<LapAbstraction> {
		private int offSet;
		
		private int currentIndex;
		
		private LapBasedRoundIterator(String str) {
			offSet = context.getMapping(str);
		}

		@Override
		public boolean hasNext() {
			return currentIndex >= runFor;
		}

		@Override
		public LapAbstraction next() throws NoSuchElementException {
			try {
				return laps[currentIndex++][offSet];
			} catch (ArrayIndexOutOfBoundsException e) {
				throw log.throwing(Level.ERROR, new NoSuchElementException());
			}
		}
		
	}
	
	public static void main(String[] args) {
		int x = 0;
		System.out.println((x++) + "");
		System.out.println(x);
	}
}
