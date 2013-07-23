package com.github.tiviz.ui.event;

import com.github.tiviz.ui.event.SerieRemovedEvent.SerieRemovedHandler;
import com.github.tiviz.ui.model.Serie;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SerieRemovedEvent extends GwtEvent<SerieRemovedHandler> {

    public static final Type<SerieRemovedHandler> TYPE = new Type<SerieRemovedHandler>();

    private final Serie<?> serie;

    public interface SerieRemovedHandler extends EventHandler {
        void onSerieRemoved(SerieRemovedEvent event);
    }

    public interface SerieRemovedHasHandlers extends HasHandlers {
        HandlerRegistration addSerieRemovedHandler(SerieRemovedHandler handler);
    }

    public SerieRemovedEvent(final Serie<?> serie) {
        super();
        this.serie = serie;
    }

    @SuppressWarnings("unchecked")
    public <T> Serie<T> getSerie() {
        return (Serie<T>) serie;
    }

    @Override
    protected void dispatch(final SerieRemovedHandler handler) {
        handler.onSerieRemoved(this);
    }

    @Override
    public final Type<SerieRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<SerieRemovedHandler> getType() {
        return TYPE;
    }

    public static <T> void fire(final HasHandlers source, final Serie<T> serie) {
        source.fireEvent(new SerieRemovedEvent(serie));
    }
}
