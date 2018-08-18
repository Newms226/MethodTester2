package judge;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import contestant.Contender;
import lap.Lap;
import lap.LapList;
import lap.LapPredicate;
import result.ResultSet;
import tools.FileTools;
import tools.NumberTools;
import tools.SerialTools;

/**
 * A class to judge the results of any race.
 * 
 * <p>
 * Note that the {@link FileTools} & {@link SerialTools} APIs are used by this class and should be
 * downloaded and located on the build path of this interface to ensure proper execution of this
 * program. While included with the .jar download of this project, they can be found at 
 * <a href = "https://github.com/Newms226/Tools.git">Tools by Michael Newman</a>.
 * 
 * @author Michael Newman
 * 
 */
public interface Judge extends Serializable, Cloneable {
	
	/**
	 * The default folder location for the serializations of a {@code Judge} object. Note that this
	 * should be changed to represent a location on the client environment as it used by the default
	 * implementation of {@link Judge#save()}
	 */
	String SERIALIZATION_BASE_FOLDER_STRING = FileTools.DEFAULT_IO_DIRECTORY + "RaceSerializations/";
	
	/**
	 * A convenience {@link File} object which points to 
	 * {@link Judge#SERIALIZATION_BASE_FOLDER_STRING}
	 */
	File SERIALIZATION_BASE_FOLDER_FILE = new File(SERIALIZATION_BASE_FOLDER_STRING);
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                    PROCEDURE METHODS                                       *
	 *                                                                                            *
	 **********************************************************************************************/
	
	/**
	 * Method which tells the judge who will be competing in the race. Note that the implementations
	 * in this package will overwrite the value returned by {@link Contender#getID()} for their
	 * 
	 * @param contender to register with the Judge
	 * @param contenders to register with the judge (optional)
	 */
	void register(Contender contender, Contender...contenders);
	
	/**
	 * Method to call at the end of the race which decides the total winner based upon the amount
	 * of <strong>laps</strong> won, NOT the smallest average time.
	 * 
	 * @return {@link Contender} who won the whole race
	 */
	Contender getTotalWinner();
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                   ANALYSIS METHODS                                         *
	 *                                                                                            *
	 **********************************************************************************************/
	
	/**
	 * Method to analyze the results of the race.
	 * <p>
	 * <strong>NOTE:</strong> THIS METHOD SHOULD NEVER BE CALLED BY CLIENT CODE WHEN A JUDGE HAS 
	 * BEEN ANALYZING A RACE. The implementations contained in this project examine the 
	 * {@link Judge#isStarted()} method <strong> whenever the client calls this method directly
	 * </strong> and will throw a {@link RaceProcedureException} if this contract has been violated.
	 * 
	 * <p>
	 * After this method has been run (internally or by the client) all the analysis methods of any
	 * implementing class should perform their expected duties regardless of the actions before 
	 * this method is called.
	 * 
	 * <p>
	 * There are three valid calls to this method.
	 * 
	 * <p>
	 * <strong> A class implementing the {@code Judge} interface: </strong>
	 * <ol>
	 * 	<li> After all of the runs have completed, a class implementing the {@code Judge} interface
	 *       wishes to analyze the results. <strong> Note:</strong> in this case, an internal method
	 *       should be used which does not examine {@link Judge#isStarted()} first. A check should
	 *       only occur when the client calls this method directly. </li>
	 * </ol>
	 * 
	 * <p>
	 * <strong> After the creation of a ***NEW**** judge: </strong>
	 * <ol>
	 *  <li> When a client has controlled the execution of the laps and wishes to analyze the 
	 *       results </li>
	 *  <li> When a client has filtered down a {@code LapSet} and wishes to analyze the filtered
	 *       list </li>
	 * </ol>
	 * 
	 * @param contenders the {@link Contender} objects to analyze.
	 * 
	 * @throws RaceProcedureException if the contract defined by the {@link Judge} interface has been 
	 *         violated.
	 */
	void analyizeFrom(Collection<ResultSet> contenders);
	
	/**
	 * Method should return a simple summary of the results, noting the winner & the percent they
	 * won by, plus other summary information.
	 * 
	 * @return a summary of the results judged.
	 */
	String getSummary();
	
	/**
	 * Method should return all the information it has calculated on the race. Note that the 
	 * {@link LapSet} objects have methods for an object-oriented analysis of the results.
	 * 
	 * @return a {@code String} representing all of the data the judge has on the outcome.
	 */
	String getFullResults();
	
	/**
	 * Method to filter down the {@link LapSet} objects this judge is currently managing according 
	 * to the {@link LapPredicate} passed into it. All implementations of this method should 
	 * return a <strong>new</strong> {@code Judge} object, instead of filtering the results
	 * contained in this race.
	 * 
	 * @param predicate to filter the {@link Lap}s through.
	 * 
	 * @return a new {@code Judge} instance for examination.
	 */
	Judge filter(LapPredicate predicate);
	
	/**
	 * Method to extract the {@link LapSet}s monitored by the judge. Essentially, this method 
	 * de-couples the {@link CoupledContender} objects it monitors. 
	 * 
	 * @return
	 */
	Map<Contender, LapList> extractResults();
	
	/**********************************************************************************************
	 *                                                                                            *
	 *                                      MISC METHODS                                          *
	 *                                                                                            *
	 **********************************************************************************************/
	
	/**
	 * @return the {@link Contender}s registered with this {@code Judge} instance
	 */
	Collection<Contender> getContenders();
	
	/**
	 * @return the number of {@link Contender}s registered with this {@code Judge} instance
	 */
	int count();
	
	/**
	 * Method to serialize the results of this judge object.
	 *  
	 * @return whether the serialization was completed successfully
	 */
	default boolean save() {
		String directoryLocation = SERIALIZATION_BASE_FOLDER_FILE + getName();
		
		if (!FileTools.createDirectory(directoryLocation)) return false;
		
		return SerialTools.seralizeObject(this, FileTools.getUniqueTxtFile(directoryLocation + "/judge"));
	}
	
	/**
	 * Returns the name of this {@code Judge}
	 * 
	 * @return the name of this {@code Judge}
	 */
	String getName();
}
