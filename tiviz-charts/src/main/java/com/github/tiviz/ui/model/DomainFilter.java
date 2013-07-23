package com.github.tiviz.ui.model;

/**
 * Defines the contract for an object filtering domain values.
 * <p>
 * 
 * @author VATHAIL
 * 
 * @param <T> the type of domain values
 */
public interface DomainFilter<T> {

    /**
     * Return true if the given value should be accepted.
     * 
     * @param value the domain value to filter
     * @return true to accept, false otherwise
     */
    public boolean accept(T value);

}
