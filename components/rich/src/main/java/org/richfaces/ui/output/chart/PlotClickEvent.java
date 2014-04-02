/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.ui.output.chart;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * The class represents plotclick event fired by the chart component, when user
 * clicks a point in a chart.
 * @author Lukas Macko
 */
public class PlotClickEvent extends FacesEvent {
    /**
     * Index into chart series. The first series has index 0.
     */
    private int seriesIndex;

    /**
     * Index into list of points inside series. The first point has index 0.
     */
    private int pointIndex;

    /**
     * The value independent variable of the clicked point. x-coordinate
     */
    private String x;

    /**
     * Dependent variable. y-coordinate
     */
    private Number y;

    public PlotClickEvent(UIComponent component, int seriesIndex,
            int pointIndex, String x, Number y) {
        super(component);
        this.seriesIndex = seriesIndex;
        this.pointIndex = pointIndex;
        this.x = x;
        this.y = y;
    }

    public int getSeriesIndex() {
        return seriesIndex;
    }

    public int getPointIndex() {
        return pointIndex;
    }

    public String getX() {
        return x;
    }

    public Number getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point with index " + getPointIndex() + " within series "
                + getSeriesIndex() + " was clicked." + System.getProperty("line.separator") + "Point coordinates: ["
                + getX() + "," + getY() + "].";
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof PlotClickListener;
    }

    @Override
    public void processListener(FacesListener listener) {
        ((PlotClickListener) listener).processDataClick(this);
    }

}
