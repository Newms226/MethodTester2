package lap;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Abstract object which identifies information about the lap
 * @author Michael
 *
 */
public interface LapContext extends Serializable {
	
	Lap getLap();

	String getIDString();
}
