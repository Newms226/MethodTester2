package lap;

import java.time.Duration;
import java.time.Instant;

public class ClientLap extends AbstractLap {
	private static final long serialVersionUID = 8469176290953690522L;
	
	private RaceLapContext context;
	
	public ClientLap(long elapsed, int roundNumber) {
		super(elapsed);
		context = new RaceLapContext(this, roundNumber);
	}

	@Override
	public RaceLapContext getContext() {
		return context;
	}
	
	@Override
	public ClientLap clone() {
		return new ClientLap(getElapsed(), context.ROUND_NUMBER);
	}
}
