package com.github.tiviz.ui.chart.renderer;

import java.util.Arrays;

import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.core.Selection;
import com.github.tiviz.ui.chart.ClipPath;
import com.github.tiviz.ui.chart.LineGenerator;
import com.github.tiviz.ui.model.AxisCoordsBuilder;
import com.github.tiviz.ui.model.AxisModel;
import com.github.tiviz.ui.model.PointBuilder;
import com.github.tiviz.ui.model.RangeDomainFilter;
import com.github.tiviz.ui.model.Serie;
import com.github.tiviz.ui.svg.DOM;
import com.google.common.collect.Range;
import com.google.gwt.dom.client.Element;

public class LineRenderer<T> implements Renderer {

    private final Element container;
    private final LineGenerator<T> generator;
    private final AxisModel<?> xModel;
    private final AxisModel<?> yModel;

    private String additionalClassNames = "";
    private final ClipPath globalClipPath;
    private ClipPath localClipPath;

    private final Serie<T> serie;

    public LineRenderer(final Serie<T> serie, final PointBuilder<T> domainBuilder,
            final AxisModel<?> xModel, final AxisModel<?> yModel,
            final ClipPath clipPath, final Element container) {
        super();
        this.serie = serie;
        this.xModel = xModel;
        this.yModel = yModel;
        this.globalClipPath = clipPath;
        this.container = container;
        AxisCoordsBuilder<T> pointBuilder = new AxisCoordsBuilder<T>(xModel, yModel, domainBuilder);
        this.generator =
                new LineGenerator<T>(
                        pointBuilder,
                        new RangeDomainFilter<T>(domainBuilder,
                                xModel));
    }

    @Override
    public void render() {
        // create or get a path element
        Element path = getOrCreatePathElement();
        configurePathElement(path);
        path.setAttribute("d", generator.generate(serie.getValues()));

    }

    private Element getOrCreatePathElement() {
        Element e = findPath();
        if (e == null) {
            e = createPath();
        }
        return e;
    }

    private Element createPath() {
        Element pathElement = DOM.createSVGElement("path");
        pathElement.setAttribute("name", "serie_" + serie.id());
        container.appendChild(pathElement);
        return pathElement;
    }

    private Element findPath() {
        return D3.select(container).select("[name=\"serie_" + serie.id() + "\"]").node();
    }

    private void configurePathElement(final Element e) {
        // apply the cli path and the class names
        Selection select = D3.select(e);
        if (additionalClassNames != null) {
            select.classed(additionalClassNames, true);
        }
        applyClipPath(select);
    }

    private void applyClipPath(final Selection select) {
        if (localClipPath != null) {
            localClipPath.apply(select);
        }
        else {
            globalClipPath.apply(select);
        }
    }

    /**
     * Create a rectangular clip path that draw only the part of the lines
     * included in the given range.
     * <p>
     * The given range is expressed in terms of x domain space.
     * <p>
     * 
     * 
     * @param includedRange
     */
    public LineRenderer<T> include(final Range<Double> includedRange) {
        String id = "clip" + serie.id();

        // ENTER : create a clip path in the container (if needed)
        Selection containerSelection =
                D3.select(container)
                        .selectAll("#" + id);

        containerSelection
                .data(Arrays.asList(id))
                .enter()
                .append("clipPath")
                .attr("id", id)
                .attr("clip-path", "url(#" + globalClipPath.getId() + ")")
                .append("rect");

        // UPDATE the clip path
        double extent = Math.abs(includedRange.lowerEndpoint() - includedRange.upperEndpoint());
        containerSelection
                .selectAll("rect")
                .attr("x", xModel.toPixel(includedRange.lowerEndpoint()))
                .attr("y", 0)
                .attr("width", xModel.toPixelSize(extent))
                .attr("height", yModel.toPixelSize(yModel.visibleDomainLength()));
        // store the id to be used when path will be drawed
        if (localClipPath == null) {
            localClipPath = new ClipPath(id);
        }
        return this;
    }

    /**
     * Specify styleNames to be applied to the generated path lines
     * @param names
     * @return
     */
    public LineRenderer<T> addStyleNames(final String names) {
        this.additionalClassNames += " " + names;
        return this;
    }
}
