package com.github.tiviz.ui.model;

import com.github.gwtd3.api.scales.Scale;
import com.github.tiviz.ui.chart.LineChart;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Model for a {@link LineChart}.
 * <p>
 * A model is constituted of a list of {@link Serie} that represents the same kind of data in one single universe.
 * <p>
 * D
 * 
 * 
 * @author <a href="mailto:schiochetanthoni@gmail.com">Anthony Schiochet</a>
 * 
 * @param <T>
 */
public class BaseChartModel<S extends Scale<S>> implements HasHandlers {

    private final AxisModel<S> xModel;
    private final AxisModel<S> yModel;

    protected final HandlerManager eventManager = new HandlerManager(this);

    public BaseChartModel(final AxisModel<S> xModel, final AxisModel<S> yModel) {
        super();
        this.xModel = xModel;
        this.yModel = yModel;
    }

    // =========== delegate models ================

    /**
     * Return the model of the x-axis.
     * 
     * @return the model
     */
    public AxisModel<S> xModel() {
        return xModel;
    }

    /**
     * Return the model of the y-axis.
     * 
     * @return the model
     */
    public AxisModel<S> yModel() {
        return yModel;
    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
        eventManager.fireEvent(event);
    }

}