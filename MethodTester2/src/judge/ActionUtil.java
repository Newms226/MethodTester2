package judge;

public class ActionUtil {

	public static void assertNotStarted(Action o, String cause) {
		if (o.isStarted()) {
			throw new RaceProcedureException("Race has already started. " + cause);
		}
	}
	
	public static void ensureNotEnded(Action o, String cause) {
		if (o.isEnded()) {
			throw new RaceProcedureException("Race has not ended: " + cause);
		}
	}
	
	public static void ensureCompletion(Action o, String cause) {
		if (!o.wasStarted()) {
			throw new RaceProcedureException("Race was never started: " + cause);
		}
		
		if (!o.isEnded()) {
			throw new RaceProcedureException("Race has not ended: " + cause);
		}
	}

}
