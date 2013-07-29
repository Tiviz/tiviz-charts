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
public class BaseChartModel<X extends Scale<X>, Y extends Scale<Y>> implements HasHandlers {

    private final AxisModel<X> xModel;
    private final AxisModel<Y> yModel;

    protected final HandlerManager eventManager = new HandlerManager(this);

    public BaseChartModel(final AxisModel<X> xModel, final AxisModel<Y> yModel) {
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
    public AxisModel<X> xModel() {
        return xModel;
    }

    /**
     * Return the model of the y-axis.
     * 
     * @return the model
     */
    public AxisModel<Y> yModel() {
        return yModel;
    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
        eventManager.fireEvent(event);
    }

}