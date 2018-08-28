package lap;

public abstract class AbstractLapContext implements LapContext {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4474588034088001985L;
	
	/**
	 * The correlated {@link Lap} object
	 */
	protected final Lap lap;
	
	/**
	 * No-Arg constructor for serialization of sub classes. Note that this constructor is useless
	 * as any {@link Lap} implementation should be immutable. Method call should be avoided.
	 */
	public AbstractLapContext() {
		lap = Laps.EMPTY_LAP;
	}
	
	/**
	 * Main constructor
	 * 
	 * @param lap the lap to contextulize
	 */
	public AbstractLapContext(Lap lap) {
		this.lap = lap;
	}

	/**
	 * Returns the corresponding lap which this object contextualizes
	 * 
	 * @return the corresponding {@link Lap}
	 */
	@Override
	public Lap getLap() {
		return lap;
	}
}
