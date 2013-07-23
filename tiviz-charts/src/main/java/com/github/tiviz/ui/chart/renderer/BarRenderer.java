package com.github.tiviz.ui.chart.renderer;

import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.core.Selection;
import com.github.gwtd3.api.core.UpdateSelection;
import com.github.gwtd3.api.core.Value;
import com.github.gwtd3.api.functions.DatumFunction;
import com.github.tiviz.ui.chart.ClipPath;
import com.github.tiviz.ui.model.AxisModel;
import com.github.tiviz.ui.model.BarBuilder;
import com.github.tiviz.ui.model.BarRectBuilder;
import com.github.tiviz.ui.model.RectBuilder;
import com.github.tiviz.ui.model.Serie;
import com.google.gwt.dom.client.Element;

public class BarRenderer<T, L> implements Renderer {

    private final Element container;

    private final RectBuilder<T> rectBuilder;

    private final ClipPath clipPath;

    private String additionalStyleNames = "";

    private final Serie<T> serie;

    public BarRenderer(final Serie<T> serie, final BarBuilder<T, Double> domainBuilder,
            final AxisModel<?> xModel, final AxisModel<?> yModel,
            final ClipPath clipPath, final Element container) {
        super();
        this.serie = serie;
        this.rectBuilder = new BarRectBuilder<T, Double>(xModel, yModel, domainBuilder);
        this.clipPath = clipPath;
        this.container = container;
    }

    @Override
    public void render() {
        // create or get a path element
        Selection rectangles = selectOrCreateRectangleSelection(serie);
        updateRectangle(rectangles);
    }

    /**
     * Select existing rectangles, create "missing" ones, delete "obsolete" rectangles.
     * <p>
     * 
     * @param serie
     * @return
     */
    private UpdateSelection selectOrCreateRectangleSelection(final Serie<T> serie) {
        // return D3.select(container).select("[name=serie_" + serie.id() + "]").node();
        // select all Rect with name=serie.id
        final UpdateSelection updatingRectangles =
                D3.select(container)
                        .selectAll("rect[name=\"serie_" + serie.id() + "\"]").data(serie.getValues());

        updatingRectangles.enter().append("rect")
                .attr("name", "serie_" + serie.id());
        // FIXME: default class name
        // .classed(additionalClassNames, true);

        // removing
        updatingRectangles.exit().remove();
        return updatingRectangles;
    }

    /**
     * Given a selection containing rect elements,
     * update their x, y, width and height attributes according to their data
     * transformed by the rectBuilder.
     * <p>
     * @param rectangles
     */
    private void updateRectangle(final Selection rectangles) {
        rectangles
                .attr("x", new DatumFunction<Double>() {
                    @Override
                    public Double apply(final Element context, final Value d, final int index) {
                        return rectBuilder.x(d.<T> as());
                    }
                })
                .attr("y", new DatumFunction<Double>() {
                    @Override
                    public Double apply(final Element context, final Value d, final int index) {
                        return rectBuilder.y(d.<T> as());
                    }
                })
                .attr("width", new DatumFunction<Double>() {
                    @Override
                    public Double apply(final Element context, final Value d, final int index) {
                        return rectBuilder.width(d.<T> as());
                    }
                })
                .attr("height", new DatumFunction<Double>() {
                    @Override
                    public Double apply(final Element context, final Value d, final int index) {
                        return rectBuilder.height(d.<T> as());
                    }
                })
                .attr("class", new DatumFunction<String>() {
                    @Override
                    public String apply(final Element context, final Value d, final int index) {
                        String styleNames = rectBuilder.styleNames(d.<T> as());
                        return additionalStyleNames + " " + styleNames;
                    }
                });
        clipPath.apply(rectangles);
    }

    public BarRenderer<T, L> addStyleNames(final String styleNames) {
        this.additionalStyleNames += " " + styleNames;
        return this;
    }

}
