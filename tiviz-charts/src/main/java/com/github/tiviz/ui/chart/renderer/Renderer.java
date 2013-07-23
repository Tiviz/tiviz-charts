package com.github.tiviz.ui.chart.renderer;

import com.github.tiviz.ui.model.Serie;

/**
 * Renderer allow a {@link Serie} to be drawn in a document.
 * <p>
 * 
 * @author <a href="mailto:schiochetanthoni@gmail.com">Anthony Schiochet</a>
 * 
 */
public interface Renderer {

    /**
     * The renderer used in the frame of
     * @param serie
     */
    void render();

}
