package it.unibo.risikoop.model.interfaces;

/**
 * Specification interface for defining criteria that an object must satisfy.
 * This interface allows for the creation of complex logical conditions by
 * combining multiple specifications using logical AND, OR, and NOT operations.
 *
 * @param <T> the type of object that this specification applies to
 */
@FunctionalInterface
public interface Specification<T> {
    /**
     * Checks if the given object satisfies the specification.
     *
     * @param cantidate the object to check
     * @return true if the object satisfies the specification, false otherwise
     */
    boolean isSatisfiedBy(T candidate);

    /**
     * Combines this specification with another using logical AND.
     *
     * @param other the other specification
     * @return a new specification that is satisfied if both specifications are
     *         satisfied
     */
    default Specification<T> and(Specification<T> other) {
        return c -> this.isSatisfiedBy(c) && other.isSatisfiedBy(c);
    }

    /**
     * Combines this specification with another using logical OR.
     *
     * @param other the other specification
     * @return a new specification that is satisfied if either specification is
     *         satisfied
     */
    default Specification<T> or(Specification<T> other) {
        return c -> this.isSatisfiedBy(c) || other.isSatisfiedBy(c);
    }

    /**
     * Negates this specification.
     *
     * @return a new specification that is satisfied if this specification is not
     *         satisfied
     */
    default Specification<T> not() {
        return c -> !this.isSatisfiedBy(c);
    }

}
