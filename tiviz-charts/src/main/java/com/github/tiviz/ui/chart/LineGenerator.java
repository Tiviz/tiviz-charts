package com.github.tiviz.ui.chart;

import java.util.List;

import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.JsArrays;
import com.github.gwtd3.api.core.Value;
import com.github.gwtd3.api.functions.DatumFunction;
import com.github.gwtd3.api.svg.Line;
import com.github.gwtd3.api.svg.Line.InterpolationMode;
import com.github.tiviz.ui.model.DomainFilter;
import com.github.tiviz.ui.model.PointBuilder;
import com.google.gwt.dom.client.Element;

/**
 * A LineGenerator defines a set of points forming a path, based on:
 * <ul>
 * <li>the values provided with {@link #generate(List)} method, in which you pass your model data values.
 * <li>a {@link PointBuilder} instance used to convert your values to x and y domain coordinates
 * <li>an optional {@link DomainFilter} used to ignore some values based on your custom conditions
 * </ul>
 * <p>
 * With this information, it generates the "d" attribute of a path element.
 * <p>
 * 
 * 
 * @author Leonard De Vathaire
 * 
 * @param <T>
 */
public class LineGenerator<T> {

    private Line generator;
    protected PointBuilder<T> builder;
    protected DomainFilter<T> filter;

    /**
     * Create a generator that will construct
     * @param builder
     * @param filter
     */
    public LineGenerator(final PointBuilder<T> builder, final DomainFilter<T> filter) {
        super();
        this.builder = builder;
        this.filter = filter;
        setup();
    }

    protected void setup() {
        generator = D3.svg().line().interpolate(InterpolationMode.STEP_AFTER)
                // convert the domain object to a pixel distance
                .x(new DatumFunction<Double>() {
                    @Override
                    public Double apply(final Element context, final Value d, final int index) {
                        int x = (int) builder.x(d.<T> as());
                        return (double) x;
                    }
                })
                // convert the domain to a pixel distance
                .y(new DatumFunction<Double>() {
                    @Override
                    public Double apply(final Element context, final Value d, final int index) {
                        int y = (int) builder.y(d.<T> as());
                        return (double) y;
                    }
                })
                .defined(new DatumFunction<Boolean>() {
                    @Override
                    public Boolean apply(final Element context, final Value d, final int index) {
                        T value = d.<T> as();
                        return filter == null ? true : filter.accept(value);
                    }
                }
                );
    }

    /**
     * Set the filter used to filter the T objects translated to x domain values
     * that should not participate in building the lines.
     * 
     * @param filter
     */
    public void setFilter(final DomainFilter<T> filter) {
        this.filter = filter;
    }

    public String generate(final List<T> values) {
        return generator.generate(JsArrays.asJsArray(values));
    }
}
