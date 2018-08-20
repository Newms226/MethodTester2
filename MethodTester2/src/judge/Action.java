package judge;

import java.util.Collection;

public interface Action {
	/**
	 * Method to call to begin the race. Sets the value of the {@code started} flag but does not
	 * perform any other functions.
	 */
	void start();
	
	/**
	 * Method to test whether this {@code Judge} instance has begun keeping track of the laps 
	 * occurred in this race.
	 * 
	 * <p>
	 * This is the method which any {@code Judge} instance should check before allowing a client
	 * to call the {@link Judge#analyizeFrom(Collection)} method.
	 * 
	 * @return a {@code boolean} representing if the race has begun.
	 */
	boolean isStarted();
	
	/**
	 * Method call to end the race and begin the analysis of the race and sets the value of the 
	 * {@code ended} flag.
	 */
	void end();
	
	/**
	 * Method to test whether this {@code Judge} instance has finished keeping track of the laps 
	 * occurred in this race.
	 * 
	 * @return a {@code boolean} representing if the race has finished.
	 */
	boolean isEnded();
	
	/**
	 * Method to determine if the Judge is still monitoring the race. Note that if 
	 * {@link Judge#end()} is not called, this method may not return the valid response.
	 * 
	 * @return whether the race is currently running.
	 */
	default boolean isRunning() {
		return isStarted() && (!isEnded());
	}
	
	boolean wasStarted();
}
