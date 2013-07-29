package com.github.tiviz.ui.chart;

import java.util.Arrays;

import com.github.gwtd3.api.core.Selection;
import com.github.gwtd3.api.scales.ContinuousQuantitativeScale;
import com.github.gwtd3.api.scales.Scale;
import com.github.gwtd3.api.svg.Axis.Orientation;
import com.github.tiviz.ui.data.DefaultSelectionUpdater;
import com.github.tiviz.ui.data.SelectionDataJoiner;
import com.github.tiviz.ui.event.RangeChangeEvent;
import com.github.tiviz.ui.event.RangeChangeEvent.RangeChangeHandler;
import com.github.tiviz.ui.model.AxisModel;
import com.github.tiviz.ui.model.BaseChartModel;
import com.github.tiviz.ui.svg.GContainer;
import com.github.tiviz.ui.svg.SVGDocumentContainer;
import com.github.tiviz.ui.svg.SVGResources;
import com.github.tiviz.ui.svg.SVGStyles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;

/**
 * A base class for a chart with a horizontal axis and a vertical axis,
 * and a data region.
 * <p>
 * 
 * @author SCHIOCA
 */
public class BaseChart extends SVGDocumentContainer implements ChartContext {

    protected static final int DEFAULT_TOP_POSITION = 20;
    protected static final int DEFAULT_BOTTOM_POSITION = 30;
    protected static final int DEFAULT_LEFT_POSITION = 35;
    protected static final int DEFAULT_RIGHT_POSITION = 15;

    protected final AxisModel<? extends Scale<?>> xModel;

    protected final AxisModel<? extends Scale<?>> yModel;

    // ==== children =========
    protected GContainer g;

    private ChartAxis<? extends Scale<?>> xAxis;

    private ChartAxis<? extends Scale<?>> yAxis;

    private final Styles styles;

    /**
     * Support for x or y sliding
     */
    private final DragSupport dragSupport;
    /**
     *  
     */
    private final Options options = new Options(this);

    private final ClipPath dataRegionClipPath;

    /**
     * Configuration options the chart.
     * 
     * @author SCHIOCA
     * 
     */
    public class Options {
        private final BaseChart chart;

        public Options(final BaseChart chart) {
            super();
            this.chart = chart;
        }

        public Options enableXNavigation(final boolean enable) {
            if (dragSupport == null) {
                return this;
            }
            if (enable) {
                chart.dragSupport.enable();
            }
            else {
                chart.dragSupport.disable();
            }
            return this;
        }

    }

    public interface Resources extends SVGResources {
        @Source("BaseChart.css")
        BaseChart.Styles chartStyles();
    }

    public interface Styles extends SVGStyles {
        /**
         * @return the classname applied to any axis
         */
        String axis();

        /**
         * class applied to all labels
         * 
         * @return
         */
        String label();

        /**
         * class applied to all element on the y axis
         * 
         * @return
         */
        String y();

        /**
         * class applied to all element on the x axis
         * 
         * @return
         */
        String x();
    }

    public BaseChart(final BaseChartModel<?, ?> model) {
        this(model, (Resources) GWT.create(Resources.class));
    }

    @SuppressWarnings("unchecked")
    public BaseChart(final BaseChartModel<?, ?> model, final Resources resources) {
        super(resources);

        xModel = model.xModel();
        yModel = model.yModel();

        // getElement().setAttribute("viewBox", "0 0 500 400");
        styles = resources.chartStyles();
        styles.ensureInjected();

        dataRegionClipPath = new ClipPath("clip" + Random.nextInt(100000));

        // drag support is available only for 'reversible' scales
        if (xModel.scale() instanceof ContinuousQuantitativeScale<?>) {
            dragSupport = new DragSupport((AxisModel<? extends ContinuousQuantitativeScale<?>>) this.xModel);
        }
        else {
            dragSupport = null;
        }

        createChildren();
    }

    @Override
    protected void onSelectionAttached() {
        super.onSelectionAttached();

        initModel();

        // register x drag interaction
        if (dragSupport != null) {
            dragSupport.registerListeners(select()).enable();
        }
    }

    protected void initModel() {
        RangeChangeHandler handler = new RangeChangeHandler() {
            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                redrawSeries();
            }
        };
        xModel.addRangeChangeHandler(handler);
        yModel.addRangeChangeHandler(handler);
    }

    /**
     * Create the g container, and the axis components.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void createChildren() {
        // create G container
        g = new GContainer();
        add(g);
        g.transform().translate(DEFAULT_LEFT_POSITION, DEFAULT_TOP_POSITION);

        // X AXIS
        xAxis = new ChartAxis(xModel, Orientation.BOTTOM);
        // FIXME
        // xAxis.setPixelSize(0, chartWidth());
        xAxis.addStyleName(styles.x());
        // FIXME let the user configure position at center ?
        // FIXME: or automate the process by a Y domain neg and pos
        // should be yRange.apply(0) instead of chart height ?
        g.add(xAxis);

        // Y AXIS
        // FIXME
        // yAxis.scale().range(chartHeight(), 0);
        // tickSize(6, 4, 2).
        yAxis = new ChartAxis(yModel, Orientation.LEFT);

        yAxis.generator().ticks(4);// .tickSubdivide(1).tickSize(12, 6, 3);
        // append the axis to the svg
        // change styling, position, (left, right)
        // text label position / orientation
        yAxis.addStyleName(styles.y());
        g.add(yAxis);

        // SERIES RENDERER
    }

    // ============= drawing =============
    @Override
    public void redraw() {
        redrawClippath();
        redrawAxis();
        redrawSeries();
    }

    private void redrawClippath() {
        SelectionDataJoiner.update(g.select(), Arrays.asList(dataRegionClipPath),
                new DefaultSelectionUpdater<ClipPath>("#" + dataRegionClipPath.getId()) {
                    @Override
                    public String getElementName() {
                        return "clipPath";
                    }

                    @Override
                    public void afterEnter(final Selection selection) {
                        super.afterEnter(selection);
                        selection.attr("id", dataRegionClipPath.getId())
                                .append("rect");
                    }

                    @Override
                    public void onJoinEnd(final Selection selection) {
                        super.onJoinEnd(selection);
                        // set the width of the clippath to the width of the chart
                        selection.select("rect")
                                .attr("width", chartWidth())
                                .attr("height", chartHeight());

                    }

                    @Override
                    public String getKey(final ClipPath datum, final int index) {
                        return datum.getId();
                    }

                });

    }

    protected void redrawSeries() {

    }

    private void redrawAxis() {
        // TODO let the user customize the X axis position
        xAxis.transform().removeAll().translate(0, chartHeight());
        xAxis.setLength(chartWidth());
        yAxis.setLength(chartHeight());

    }

    // ============= getters =============
    /**
     * Width of the area displaying series data, bounded by axis. (excluding
     * space for Y axis labels or legend)
     * 
     * @return
     */
    public int chartWidth() {
        return getWidth() - DEFAULT_LEFT_POSITION - DEFAULT_RIGHT_POSITION;
    }

    /**
     * Width of the area displaying series data, bounded by axis.
     * 
     * @return
     */
    public int chartHeight() {
        // we add 1 pixel so we can see the line when its value is zero.
        return (getHeight() - DEFAULT_TOP_POSITION - DEFAULT_BOTTOM_POSITION) + 1;
    }

    public ChartAxis<? extends Scale<?>> xAxis() {
        return xAxis;
    }

    public ChartAxis<?> yAxis() {
        return yAxis;
    }

    public Options options() {
        return options;
    }

    @Override
    public ClipPath getSerieClipPath() {
        return dataRegionClipPath;
    }

    /**
     * Set a translation by x and y to the main container. <br>
     * The main container contains the x and y axis,
     * the data region and the series.
     * 
     * @param x position on the x axis
     * @param y position on the y axis
     */
    public void translateMainContainer(final int x, final int y) {
        g.transform().removeAll().translate(x, y);
    }

}
