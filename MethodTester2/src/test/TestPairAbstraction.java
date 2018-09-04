package test;

import java.io.Serializable;

/**
 * This class does not accept null values and throws a NullPointer exception
 * if client code attempts to access a null value.
 * 
 * @author Michael Newman
 *
 * @param <T>
 */
public interface TestPairAbstraction<T> extends Cloneable, Serializable {
	void setExpected(T expected) throws NullPointerException;
	
	T getExpected() throws NullPointerException;
	
	void setResult(T result) throws NullPointerException;

	T getResult() throws NullPointerException;
	
	boolean pass() throws NullPointerException;
	
	default boolean pass(TestPairPredicate<T> acceptor) 
			throws NullPointerException
	{
		return acceptor.pass(getExpected(), getResult());
	}
}
