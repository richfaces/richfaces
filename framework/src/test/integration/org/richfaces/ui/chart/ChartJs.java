package org.richfaces.ui.chart;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;


@JavaScript("charttestutil")
@Dependency(sources = "org/richfaces/ui/chart/chart-testutil.js")
public interface ChartJs {


    /**
     *
     * @param id
     * @return the number of series in the chart identified by id
     */
    int seriesLength(String id);
    /**
     *
     * @param id
     * @param seriesIndex
     * @return the number of points in seriesIndex-th series in the chart
     */
    int dataLength(String id, int seriesIndex);
    /**
     *
     * @param id
     * @param seriesIndex
     * @param pointIndex
     * @return
     */
    double pointX(String id, int seriesIndex, int pointIndex);
    /**
     *
     * @param id
     * @param seriesIndex
     * @param pointIndex
     * @return
     */
    double pointY(String id, int seriesIndex, int pointIndex);
    /**
     * ATM the method is working only for line charts.
     * @param id
     * @param seriesIndex
     * @param pointIndex
     * @return Position of the point in chart canvas, its x coordinate
     */
    int pointXPos(String id, int seriesIndex, int pointIndex);
    /**
     * ATM the method is working only for line charts.
     * @param id
     * @param seriesIndex
     * @param pointIndex
     * @return Position of the point in chart canvas, its y coordinate
     */
    int pointYPos(String id, int seriesIndex, int pointIndex);

}
