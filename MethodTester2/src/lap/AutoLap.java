package lap;

import judge.RaceProcedureException;

public class AutoLap extends AbstractLap implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6004656215657708343L;
	
	private final Runnable method;
	
	

	public AutoLap(Runnable runnable) {
		method = runnable;
	}

	@Override
	public LapContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		timestamp = System.currentTimeMillis();
	}
}
