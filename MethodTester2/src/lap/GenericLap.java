package lap;

public class GenericLap extends AbstractLap {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7447893394847766151L;
	
	private final GenericContext context;

	public GenericLap(long startLap, long endLap) throws LapRangeException {
		super(startLap, endLap);
		context = new GenericContext(this);
	}

	public GenericLap(long elapsed) throws LapRangeException {
		super(elapsed);
		context = new GenericContext(this);
	}

	@Override
	public LapContext getContext() {
		return context;
	}
	
	@Override
	public GenericLap clone() {
		return new GenericLap(getElapsed());
	}

}
