package round;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lap.LapAbstraction;
import lap.LapListAbstraction;
import tools.NumberTools;

public final class RoundList implements RoundListAbstraction {
	private static final Logger log = LogManager.getLogger();
	
	private final int runFor;
	
	private final RoundContext context;
	
	private final boolean nanoSecondPrecise;
	
	private final SummaryStatistics[] summaryStats;
	
	private final int count;
	
	private final int maxIndex;
	
	private int currentBaseIndex;
	
	private int additions;
	
	private LapAbstraction[] laps;
	
	public RoundList(RoundContext context, int runFor, 
			         boolean nanoSecondPrecise) 
	{
		this.context = context;
		this.runFor = runFor;
		this.nanoSecondPrecise = nanoSecondPrecise;
		this.count = context.count();
		summaryStats = new SummaryStatistics[count];
		for (int i = 0; i < count; i++) {
			summaryStats[i] = new SummaryStatistics();
		}
		maxIndex = count * runFor;
		laps = new LapAbstraction[maxIndex];
		
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
							                 maxIndex / count,
							                 Spliterator.IMMUTABLE 
							                 | Spliterator.ORDERED), // TODO: is this right?
					true);
	}

	@Override
	public RoundContext getContext() {
		return context;
	}

	@Override
	public void lap(String str, LapAbstraction lap) {
		log.traceEntry("lap({}, {})", str, lap);
		int index = context.getMapping(str + currentBaseIndex);
		
		// look out for setting previously non-null objects
		if (laps[index] != null) {
			log.warn("Lap at round " + NumberTools.format(index / runFor) 
				+ " is currently " + laps[index] + " and is being set to "
				+ lap);
		}
		
		// set value;
		laps[index] = lap;
		log.traceExit("Set " + str + "'s lap as");
	}

	@Override
	public void lap(String str, long elapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNanoSecondPrecise() {
		return nanoSecondPrecise;
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
			currentIndex = 0 - offSet;
		}

		@Override
		public boolean hasNext() {
			return currentIndex + count < maxIndex;
		}

		@Override
		public LapAbstraction next() throws NoSuchElementException {
			try {
				return laps[currentIndex += offSet];
			} catch (ArrayIndexOutOfBoundsException e) {
				throw log.throwing(Level.ERROR, new NoSuchElementException());
			}
		}
		
	}
	
	public static void main(String[] args) {
		int x = -1;
		System.out.println((x += 1) + "");
		System.out.println(x);
	}
}
