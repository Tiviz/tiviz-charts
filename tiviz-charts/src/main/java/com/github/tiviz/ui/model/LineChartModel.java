package com.github.tiviz.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.gwtd3.api.JsArrays;
import com.github.gwtd3.api.arrays.Array;
import com.github.gwtd3.api.scales.Scale;
import com.github.tiviz.ui.event.SerieAddedEvent;
import com.github.tiviz.ui.event.SerieAddedEvent.SerieAddedHandler;
import com.github.tiviz.ui.event.SerieAddedEvent.SerieAddedHasHandlers;
import com.github.tiviz.ui.event.SerieRemovedEvent;
import com.github.tiviz.ui.event.SerieRemovedEvent.SerieRemovedHandler;
import com.github.tiviz.ui.event.SerieRemovedEvent.SerieRemovedHasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class LineChartModel<S extends Scale<S>> extends BaseChartModel<S> implements SerieAddedHasHandlers,
        SerieRemovedHasHandlers {

    private final Map<String, Serie<?>> series = new HashMap<String, Serie<?>>();

    public LineChartModel(final AxisModel<S> xModel, final AxisModel<S> yModel) {
        super(xModel, yModel);
    }

    public static <Y extends Scale<Y>> LineChartModel<Y> create(final AxisModel<Y> xModel,
            final AxisModel<Y> yModel) {
        return new LineChartModel<Y>(xModel, yModel);
    }

    // =========== series methods ================

    public boolean isEmpty() {
        return series.isEmpty();
    }

    /**
     * Return an unmodifiable list of the series.
     * 
     * @return the list of series.
     */
    public List<Serie<?>> series() {
        return Collections.unmodifiableList(new ArrayList<Serie<?>>(series.values()));
    }

    public Array<Serie<?>> seriesAsArray() {
        return JsArrays.asJsArray(series());
    }

    /**
     * Returns the series identified by the given id.
     * <p>
     * If such a series does not exist, it is created.
     * <p>
     * @param series
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Serie<T> series(final String id) {
        Serie<T> serie = (Serie<T>) this.series.get(id);
        if (serie == null) {
            serie = new Serie<T>(id);
            this.series.put(id, serie);
            fireEvent(new SerieAddedEvent(serie));
        }
        return serie;
    }

    /**
     * @param id
     * @param clazz
     * @return
     */
    public <T> Serie<T> serie(final String id, final Class<T> clazz) {
        return series(id);
    }

    public BaseChartModel<S> removeSerie(final String id) {
        Serie<?> serie = this.series.remove(id);
        if (serie != null) {
            fireEvent(new SerieRemovedEvent(serie));
        }
        return this;
    }

    // =========== events methods ================

    @Override
    public HandlerRegistration addSerieAddedHandler(final SerieAddedHandler handler) {
        return eventManager.addHandler(SerieAddedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addSerieRemovedHandler(final SerieRemovedHandler handler) {
        return eventManager.addHandler(SerieRemovedEvent.TYPE, handler);
    }

}
