package judge;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;

import contestant.Contender;
import lap.Lap;
import laplist.AbstractLapList;
import laplist.LapList;

public class JudgedContender extends AbstractLapList implements JudgeableContender {

	private int ID;
	
	private int winCount;
	
	
	public JudgedContender() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void setID(int ID) {
		this.ID = ID;
	}

	@Override
	public Contender getContender() {
		return this;
	}

	@Override
	public LapList getLapSet() {
		return this;
	}

	@Override
	public int getWinCount() {
		return winCount;
	}

	@Override
	public void increaseWinCount() {
		winCount++;
	}

}
