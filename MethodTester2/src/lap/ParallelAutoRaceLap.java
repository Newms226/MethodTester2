package lap;

public interface ParallelAutoRaceLap extends AutoRaceLap {

	@Override
	default boolean isSequential() {
		return false;
	}

}
