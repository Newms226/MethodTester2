package lap;

import java.util.Comparator;

public class GenericContext extends AbstractLapContext {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -930068586609080393L;

	public GenericContext(Lap lap) {
		super(lap);
	}

	@Override
	public String getIDString() {
		return "N/A";
	}
}
