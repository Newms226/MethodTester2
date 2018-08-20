package lap;

public interface SequentialLap extends Lap {
	
	int getLapNumber();
	
	@Override
	default boolean isSequential() {
		return true;
	}
}
