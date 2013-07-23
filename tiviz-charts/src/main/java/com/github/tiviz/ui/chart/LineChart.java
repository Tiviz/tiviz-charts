package com.github.tiviz.ui.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.gwtd3.api.scales.LinearScale;
import com.github.tiviz.ui.chart.renderer.BarRenderer;
import com.github.tiviz.ui.chart.renderer.LineRenderer;
import com.github.tiviz.ui.chart.renderer.Renderer;
import com.github.tiviz.ui.event.SerieAddedEvent;
import com.github.tiviz.ui.event.SerieAddedEvent.SerieAddedHandler;
import com.github.tiviz.ui.event.SerieChangeEvent;
import com.github.tiviz.ui.event.SerieChangeEvent.SerieChangeHandler;
import com.github.tiviz.ui.event.SerieRemovedEvent;
import com.github.tiviz.ui.event.SerieRemovedEvent.SerieRemovedHandler;
import com.github.tiviz.ui.model.BarBuilder;
import com.github.tiviz.ui.model.LineChartModel;
import com.github.tiviz.ui.model.PointBuilder;
import com.github.tiviz.ui.model.Serie;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A line chart displaying several series on the same amount.
 * <p>
 * You can customize the styling by providing your own {@link Styles} instance during construction of the chart.
 * <p>
 * You can configure the chart behaviour using {@link #options()}.
 * <p>
 * User is able by default to navigate accross the X dimension domain. Call {@link Options#enableXNavigation(boolean)}
 * with false to disable it.
 * <p>
 * FIXME: allow X axis TimeScale
 * 
 * FIXME: styling lines (colors, etc...) FIXME: styling serie label (position, font, etc...)
 * 
 * FIXME: customize scaling functions (linear, log, etc...)
 * 
 * FIXME: configuring ticks
 * 
 * FIXME: slide in X => events to grab new data
 * 
 * FIXME: caching hidden data if slide is possible by presending events
 * 
 * @author <a href="mailto:schiochetanthoni@gmail.com">Anthony Schiochet</a>
 * 
 * @param
 */
public class LineChart extends BaseChart implements SerieAddedHandler, SerieRemovedHandler,
        SerieChangeHandler {

    /**
     * The model defining this chart
     */
    private final LineChartModel<LinearScale> model;

    private final LineChart.Styles styles;

    private final Map<Serie<?>, HandlerRegistration> serieChangeRegistrations =
            new HashMap<Serie<?>, HandlerRegistration>();

    private final SerieRendererMap serieRendererMap = new SerieRendererMap();

    private class SerieRendererMap {
        private final Map<Serie<?>, Renderer> renderers = new HashMap<Serie<?>, Renderer>();

        public <T> Renderer get(final Serie<T> serie) {
            return renderers.get(serie);
        }

        public <T> Renderer put(final Serie<T> serie, final Renderer renderer) {
            return renderers.put(serie, renderer);
        }
    }

    // ========== Resources and Styles classes =====================

    public interface Resources extends BaseChart.Resources {
        @Override
        @Source("LineChart.css")
        LineChart.Styles chartStyles();
    }

    // public static interface XAxisResources extends ChartAxis.Resources {
    // @Source("ChartAxis.css")
    // ChartAxis.Styles xStyles();
    // }
    //
    // public static interface YAxisResources extends ChartAxis.Resources {
    // @Source("ChartAxis.css")
    // ChartAxis.Styles yStyles();
    // }

    @ImportedWithPrefix("d3-line-chart")
    public interface Styles extends BaseChart.Styles {
        /**
         * a classna;e applied to series lines.
         * 
         * @return
         */
        String serie();
    }

    public LineChart(final LineChartModel<LinearScale> model) {
        this(model, createDefaultResources());
    }

    public LineChart(final LineChartModel<LinearScale> model, final Resources resources) {
        super(model, resources);
        // getElement().setAttribute("viewBox", "0 0 500 400");
        styles = resources.chartStyles();
        styles.ensureInjected();

        this.model = model;
        // new LineChartModel<LinearScale>(xModel, yModel, domainBuilder);
    }

    // ============== initialization ========================
    @Override
    protected void initModel() {
        super.initModel();

        this.model.addSerieAddedHandler(this);
        this.model.addSerieRemovedHandler(this);

    }

    private static Resources createDefaultResources() {
        return GWT.create(Resources.class);
    }

    // ==================== redraw methods ================

    @Override
    protected void redrawSeries() {
        super.redrawSeries();

        List<Serie<?>> series = model().series();
        for (Serie<?> serie : series) {
            redrawSerie(serie);
        }
    }

    private <T> void redrawSerie(final Serie<?> serie) {
        Renderer renderer = getRenderer(serie);
        if (renderer == null) {
            GWT.log("no renderer defined for the serie " + serie.id());
        }
        else {
            renderer.render();
        }
    }

    // ================== renderers =======================

    /**
     * Register a Line Renderer for the given serie. The LineRenderer will be using
     * the given domainbuilder to create the points.
     * <p>
     * @param serie the serie
     * @param domainBuilder the domainbuilder
     * @return renderer the renderer for further configuration
     */
    public <T> LineRenderer<T> renderLines(final Serie<T> serie, final PointBuilder<T> domainBuilder) {
        @SuppressWarnings("unchecked")
        LineRenderer<T> renderer = (LineRenderer<T>) getRenderer(serie);
        if (renderer == null) {
            renderer = new LineRenderer<T>(
                    serie,
                    domainBuilder, xModel, yModel,
                    getSerieClipPath(), g.getElement()).addStyleNames(styles.serie());
            // store internally
            serieRendererMap.put(serie, renderer);
        }
        scheduleRedraw();
        return renderer;
    }

    /**
     * Register a BarRenderer for the given serie. The BarRenderer will be using
     * the given domainbuilder to create the points.
     * <p>
     * @param serie the serie
     * @param domainBuilder the domainbuilder
     */
    public <T> BarRenderer<T, Double> renderBars(final Serie<T> serie,
            final BarBuilder<T, Double> domainBuilder) {
        @SuppressWarnings("unchecked")
        BarRenderer<T, Double> renderer = (BarRenderer<T, Double>) serieRendererMap.get(serie);
        if (renderer == null) {
            renderer = new BarRenderer<T, Double>(
                    serie,
                    domainBuilder, xModel, yModel,
                    getSerieClipPath(), g.getElement()).addStyleNames(styles.serie());
            // store internally
            serieRendererMap.put(serie, renderer);
        }
        scheduleRedraw();
        return renderer;
    }

    private <T> Renderer getRenderer(final Serie<T> serie) {
        return serieRendererMap.get(serie);
    }

    // ============= getters =============

    protected LineChart.Styles styles() {
        return styles;
    }

    /**
     * Return the model driving this chart.
     * 
     * @return the model
     */
    public LineChartModel<LinearScale> model() {
        return model;
    }

    // =========== listens to model events ==============

    @Override
    public void onSerieRemoved(final SerieRemovedEvent event) {
        HandlerRegistration registration = serieChangeRegistrations.remove(event.getSerie());
        if (registration != null) {
            registration.removeHandler();
        }
        redrawSeries();
    }

    @Override
    public void onSerieAdded(final SerieAddedEvent event) {
        // attach listener on the serie
        serieChangeRegistrations.put(event.getSerie(), event.getSerie().addSerieChangeHandler(this));
        redrawSeries();
    }

    @Override
    public void onSerieChange(final SerieChangeEvent event) {
        redrawSerie(event.getSerie());
    }

}
