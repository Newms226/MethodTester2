package judge;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager.Log4jMarker;

import tools.Log4JTools;

/**
 * 
 * @author Michael
 *
 * @param <T> A common interface/superclass to iterate over and judge
 */
public final class Judge<T> {

	/**
	 * Factory method for Judge&ltT&gt.<p>
	 * 
	 * @param judgeingScheme 
	 * 
	 * @return
	 */
	public static <T> Judge<? super T> from(final JudgeingScheme<? extends T> judgeingScheme,
			final Collection<T> collection) {
		Log4JTools.assertNonNull(judgeingScheme, log);
		Log4JTools.as
		
		// TODO
		return null;
	}
	
	/**
	 * Apache Log4J log object
	 */
	private static final Logger log = LogManager.getLogger();
	
	/**
	 * List of contestants to judge
	 */
	private final T[] contestants;
	
	private final JudgeingScheme<T> judgeingScheme;
	
	private Judge(JudgeingScheme<T> judgeingScheme, T[] array) {
		this.judgeingScheme = judgeingScheme;
		this.contestants = new ArrayList<T>(collection.size())
	}
	
	private Comparator<T> comparator(){
		return (a, b) -> j
	}
	
}
