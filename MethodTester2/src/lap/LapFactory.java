package lap;

public class LapFactory {

	private LapFactory() {}
	
	public static Lap from(long elapsed) {
		return new GenericLap(elapsed);
	}

}
