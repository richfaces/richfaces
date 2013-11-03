package org.richfaces.ui.output.chart;

import javax.faces.event.FacesListener;

/**
 * Define listener for PlotClickEvent.
 *
 * @author Lukas Macko
 */
public interface PlotClickListener extends FacesListener {

    void processDataClick(PlotClickEvent event);

}
