package lap;

import java.time.Instant;

import javax.management.InstanceAlreadyExistsException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Lap implements LapAbstraction {
	private static final Logger log = LogManager.getLogger();
	
	public static Lap from(LapAbstraction lap) {
		Lap toReturn = new Lap(lap.getElapsed());
		toReturn.setTimeStampMills(lap.getTimeStampMills());
		return log.traceExit(toReturn);
	}
	
	public static Lap from(long elapsed) {
		return log.traceExit(new Lap(elapsed));
	}
	
	private final long elapsed;
	
	private long timeStamp;
	
	private final LapStats stats;
	
	private Lap(long elapsed) {
		timeStamp = System.currentTimeMillis();
		
		this.elapsed = elapsed;
		stats = new LapStats(this);
	}

	@Override
	public Instant getTimeStapInstant() {
		return Instant.ofEpochMilli(timeStamp);
	}

	@Override
	public long getTimeStampMills() {
		return timeStamp;
	}
	
	private void setTimeStampMills(long mills) {
		this.timeStamp = mills;
	}

	@Override
	public long getElapsed() {
		return elapsed;
	}

	@Override
	public LapStats getLapStatistics() {
		return stats;
	}

}
