package org.richfaces.ui.output.chart;

import java.io.IOException;
import org.richfaces.json.JSONObject;

/**
 * @author Lukas Macko
 */
public interface ChartStrategy {

    Object export(ChartDataModel model) throws IOException;
}
