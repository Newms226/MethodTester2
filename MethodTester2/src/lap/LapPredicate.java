package lap;

import java.util.function.Predicate;

/**
 * Queries a Lap through the {@link Predicate} interface.
 * 
 * @author Michael Newman
 */
@FunctionalInterface
public interface LapPredicate extends Predicate<Lap> {}
