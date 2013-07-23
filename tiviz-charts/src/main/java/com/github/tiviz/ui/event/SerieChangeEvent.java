package com.github.tiviz.ui.event;

import com.github.tiviz.ui.event.SerieChangeEvent.SerieChangeHandler;
import com.github.tiviz.ui.model.Serie;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SerieChangeEvent extends GwtEvent<SerieChangeHandler> {

    public static final Type<SerieChangeHandler> TYPE = new Type<SerieChangeHandler>();
    private final Serie<?> serie;

    public interface SerieChangeHandler extends EventHandler {
        void onSerieChange(SerieChangeEvent event);
    }

    public interface SerieChangeHasHandlers extends HasHandlers {
        HandlerRegistration addSerieChangeHandler(SerieChangeHandler handler);
    }

    public SerieChangeEvent(final Serie<?> serie) {
        super();
        this.serie = serie;
    }

    @SuppressWarnings("unchecked")
    public <T> Serie<T> getSerie() {
        return (Serie<T>) serie;
    }

    @Override
    protected void dispatch(final SerieChangeHandler handler) {
        handler.onSerieChange(this);
    }

    @Override
    public final Type<SerieChangeHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<SerieChangeHandler> getType() {
        return TYPE;
    }

    public static void fire(final HasHandlers source, final Serie<?> serie) {
        source.fireEvent(new SerieChangeEvent(serie));
    }
}
