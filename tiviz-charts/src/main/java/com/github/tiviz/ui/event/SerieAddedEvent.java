package com.github.tiviz.ui.event;

import com.github.tiviz.ui.event.SerieAddedEvent.SerieAddedHandler;
import com.github.tiviz.ui.model.Serie;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SerieAddedEvent extends GwtEvent<SerieAddedHandler> {

    public static final Type<SerieAddedHandler> TYPE = new Type<SerieAddedHandler>();
    private final Serie<?> serie;

    public interface SerieAddedHandler extends EventHandler {
        void onSerieAdded(SerieAddedEvent event);
    }

    public interface SerieAddedHasHandlers extends HasHandlers {
        HandlerRegistration addSerieAddedHandler(SerieAddedHandler handler);
    }

    public SerieAddedEvent(final Serie<?> serie) {
        super();
        this.serie = serie;
    }

    @SuppressWarnings("unchecked")
    public <T> Serie<T> getSerie() {
        return (Serie<T>) serie;
    }

    @Override
    protected void dispatch(final SerieAddedHandler handler) {
        handler.onSerieAdded(this);
    }

    @Override
    public final Type<SerieAddedHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<SerieAddedHandler> getType() {
        return TYPE;
    }

    public static void fire(final HasHandlers source, final Serie<?> serie) {
        source.fireEvent(new SerieAddedEvent(serie));
    }
}
