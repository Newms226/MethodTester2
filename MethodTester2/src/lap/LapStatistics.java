package lap;

public class LapStatistics {
	
	private double standard;
	
	private double deviationFromStandard;
	
	private boolean isDevationFromStandardCalculated;
	
	private final Lap lap;
	
	public LapStatistics(Lap lap) {
		this.lap = lap;
		deviationFromStandard = Double.NaN;
	}
	
	public double getStandard() {
		return isDevationFromStandardCalculated ? standard : Double.NaN;
	}
	
	public double getActualVariation(double value) {
		return ((double)lap.getElapsed()) - value;
	}
	
	/**
	 * <strong>Warning:</strong> this method simply tests whether the value of 
	 * {@linkplain LapStatistics#deviationFromStandard} has been set. It does not make any 
	 * guarantees on the validity of the value.
	 * 
	 * @return whether the deviation has ever been calculated
	 */
	public boolean isDevationFromStandardCalculated() {
		return isDevationFromStandardCalculated;
	}
	
	public double setDeviationFromStandard(double value) {
		standard = value;
		return deviationFromStandard = getActualVariation(value);
	}
	
	public double getDeviationFromStandard() {
		return deviationFromStandard;
	}
	
	public Lap getLap() {
		return lap;
	}

}
