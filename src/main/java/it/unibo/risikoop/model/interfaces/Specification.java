package it.unibo.risikoop.model.interfaces;

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
