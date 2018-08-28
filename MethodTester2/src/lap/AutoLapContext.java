package lap;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AutoLapContext extends AbstractLapContext {

	public AutoLapContext(AutoLap autoLap) {
		super(autoLap);
	}

	@Override
	public String getIDString() {
		return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
				.format(lap.getTimeStampInstant());
	}

}
