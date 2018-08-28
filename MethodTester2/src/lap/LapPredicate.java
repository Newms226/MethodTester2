package lap;

import java.util.function.Predicate;

import lap.Lap;

/**
 * Queries a Lap through the {@link Predicate} interface.
 * 
 * @author Michael Newman
 */
@FunctionalInterface
public interface LapPredicate extends Predicate<Lap> {}
