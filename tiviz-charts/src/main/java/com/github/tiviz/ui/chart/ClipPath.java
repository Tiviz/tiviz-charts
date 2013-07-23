package com.github.tiviz.ui.chart;

import com.github.gwtd3.api.core.Selection;
import com.google.gwt.dom.client.Element;

/**
 * Represent a clip path in a SVG document.
 * <p>
 * 
 * 
 * @author SCHIOCA
 * 
 */
public class ClipPath {

    private final String id;

    public ClipPath(final String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Apply the clip path on the given node.
     * 
     * @param e
     */
    public void apply(final Element e) {
        e.setAttribute("clip-path", "url(#" + getId() + ")");
    }

    /**
     * Apply the clip path on the elements of the selection.
     * 
     * @param e
     */
    public void apply(final Selection s) {
        s.attr("clip-path", "url(#" + getId() + ")");
    }

}
