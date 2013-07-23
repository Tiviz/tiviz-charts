package com.github.tiviz.ui.model;

import java.util.ArrayList;
import java.util.List;

import com.github.tiviz.ui.event.SerieChangeEvent;
import com.github.tiviz.ui.event.SerieChangeEvent.SerieChangeHandler;
import com.github.tiviz.ui.event.SerieChangeEvent.SerieChangeHasHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A serie is a well-identified list of values that can be rendered on a chart.
 * <p>
 * Portions of values may be filtered using inclusion and exclusion ranges:
 * <ul>
 * <li>when a inclusionRange has been specified, all but the included range is invisible
 * <li>when a exclusion range has been specified,
 * <li>
 * </ul>
 * <p>
 * 
 * @author <a href="mailto:schiochetanthoni@gmail.com">Anthony Schiochet</a>
 * 
 * @param <T> type of values
 */
public class Serie<T> implements SerieChangeHasHandlers, ValueProvider<T> {

    private final HandlerManager eventManager = new HandlerManager(this);

    private final String id;
    // private String name;

    private List<T> values = new ArrayList<T>();

    private String classNames;

    // private final Map<String, NamedRange<T>> namedRanges = new HashMap<String, NamedRange<T>>();

    public Serie(final String id) {
        super();
        this.id = id;
    }

    // =========== id ===============

    public String id() {
        return id;
    }

    // =========== name===============
    // public String name() {
    // return name;
    // }
    //
    // public Serie<T> name(final String name) {
    // this.name = name;
    // return this;
    // }

    // =========== values ===============

    @Override
    public List<T> getValues() {
        return values;
    }

    public Serie<T> values(final List<T> t) {
        this.values = t;
        fireEvent(new SerieChangeEvent(this));
        return this;
    }

    // ============

    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public String toString() {
        String s = "[" + id + "]:";
        for (T value : values) {
            s += value + ",";
        }
        return s;
    }

    // ============ inclusion exclusions ================

    /**
     * Specify a name for a specific range of values in the serie.
     * <p>
     * If a range already existed for the given name, the old range is replaced with the new one.
     * <p>
     * The given new range cannot intersect any existing range with another name.
     * 
     * @param name
     *            the name of the range
     * @param newRange
     *            the range
     * @throws IllegalArgumentException
     *             if the new range intersects any existing range with other
     *             name
     * @return this serie
     */
    // public Serie<T> putNamedRange(final String name, final Range<Double> newRange) {
    // Preconditions.checkNotNull(name, "name cannot be null");
    // Preconditions.checkNotNull(newRange, "newRange cannot be null");
    // // remove any existing range
    // namedRanges.remove(name);
    //
    // // assert the range does not intersect with a previous range
    // assertNotIntersectingExistingRanges(newRange);
    //
    // namedRanges.put(name, new NamedRange<T>(this, name, newRange));
    // fireEvent(new SerieChangeEvent<T>(this));
    // return this;
    // }

    /**
     * Return the {@link NamedRange} for the specified name.
     * 
     * @param name
     *            the name of the {@link NamedRange}
     * @return the {@link NamedRange} corresponding to the given name
     */
    // public NamedRange getRange(final String name) {
    // return namedRanges.get(name);
    // }

    /**
     * Assert the given range is not intersecting any existing range.
     * 
     * @throws IllegalArgumentException
     *             if the given range intersects
     * @param newRange
     */
    // private void assertNotIntersectingExistingRanges(final Range<Double> newRange) {
    // Set<Entry<String, NamedRange<T>>> ranges = namedRanges.entrySet();
    // for (Entry<String, NamedRange<T>> range : ranges) {
    // Preconditions.checkArgument(range.getValue().range().intersection(newRange).isEmpty(),
    // "The given newRange %s intersect with the existing range %s",
    // newRange.toString(), range.getKey(), range.getValue().toString());
    // }
    // }

    /**
     * 
     * @return an unmodifiable list of {@link NamedRange}.
     */
    // public List<NamedRange> namedRanges() {
    // return new ArrayList<>(namedRanges.values());
    // }

    // /**
    // *
    // * @return an unmodifiable list of {@link NamedRange}.
    // */
    // public List<NamedRange<T>> namedRanges() {
    // Collection<NamedRange<T>> ranges = namedRanges.values();
    // return Collections.unmodifiableList(new ArrayList<NamedRange<T>>(ranges));
    // }
    //
    // /**
    // * Return a {@link List} containing only the {@link NamedRange}s that are
    // * fully or partially enclosed by the given range.
    // *
    // * @param
    // * @return
    // */
    // public List<NamedRange<T>> getOverlappingRanges(final Range<Double> range) {
    // List<NamedRange<T>> result = new ArrayList<NamedRange<T>>();
    // Collection<NamedRange<T>> ranges = namedRanges.values();
    // for (NamedRange<T> namedRange : ranges) {
    // if (range.isConnected(namedRange.range())) {
    // result.add(namedRange);
    // }
    // }
    // return result;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Serie)) {
            return false;
        }
        Serie<?> other = (Serie<?>) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
        eventManager.fireEvent(event);
    }

    @Override
    public HandlerRegistration addSerieChangeHandler(final SerieChangeHandler handler) {
        return eventManager.addHandler(SerieChangeEvent.TYPE, handler);
    }

    // ============== styling ====================
    /**
     * @param classNames
     */
    public Serie<T> setClassNames(final String classNames) {
        this.classNames = classNames;
        fireEvent(new SerieChangeEvent(this));
        return this;
    }

    public String getClassNames() {
        return classNames;
    }
}
