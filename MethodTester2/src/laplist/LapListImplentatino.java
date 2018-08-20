package laplist;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lap.Lap;

public class LapListImplentatino extends ArrayList<Lap> implements LapList {
	private static Logger log = LogManager.getLogger(LapListImplentatino.class);

	@Override
	public void accept(Lap t) {
		// TODO Auto-generated method stub
		
	}
	

	public static void main(String[] args) {
		log.traceEntry();
		System.out.println("METHOD");
		log.error("ERROR");
		log.traceExit();
		
	}
}
