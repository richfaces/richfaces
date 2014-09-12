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
package org.richfaces.component;
import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;

import org.richfaces.model.ChartDataModel;
import org.richfaces.model.SeriesHandler;
import org.richfaces.model.PlotClickEvent;
import org.richfaces.model.PlotClickListener;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * The &lt;rich:chartSeries&gt; defines the data to be plotted in a chart. It represents
 * the set of values with a common label. Data can be passed using attribute
 * data. It expects ChartDataModel object. You can also use facelet iteration.
 *
 * @author Lukas Macko
 */
@JsfComponent(
        family = "org.richfaces.ui.output.ChartFamily",
        tag = @Tag(name = "chartSeries", generate = false, handlerClass=SeriesHandler.class, type = TagType.Facelets),
        fires = {@Event(value = PlotClickEvent.class, listener = PlotClickListener.class)})
public abstract class AbstractChartSeries extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.ui.output.Series";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.output.ChartFamily";

    /**
     * The attributes define type of a chart. Allowed values:
     * <ul>
     * <li>line</li>
     * <li>bar</li>
     * <li>pie</li>
     * </ul>
     */
    @Attribute(required = true)
    public abstract ChartDataModel.ChartType getType();

    /**
     * Point symbol for line chart Allowed values:
     * <ul>
     * <li>circle</li>
     * <li>square</li>
     * <li>cross</li>
     * <li>triangle</li>
     * <li>diamond</li>
     * </ul>
     */
    @Attribute
    public abstract SymbolType getSymbol();

    /**
     * Description of data shown in a legend.
     */
    @Attribute
    public abstract String getLabel();

    /**
     * Data passed into chart. If attribute is null, nested &lt;s:point&gt; tags
     * are expected - facelet iteration.
     */
    @Attribute
    public abstract ChartDataModel getData();

    /**
     * Attribute define the color of data plotted.
     */
    @Attribute
    public abstract String getColor();

    /**
     * Javascript handler for plotclick event for this series only.
     */
    @Attribute(events = @EventName("plotclick"))
    public abstract String getOnplotclick();

    /**
     * Mouse over handler event for this series only.
     */
    @Attribute(events = @EventName("plothover"))
    public abstract String getOnplothover();

    /**
     * Server-side listener for plotclick event fired by this series only. Not
     * implemented yet.
     */
    @Attribute(signature = @Signature(parameters = PlotClickEvent.class))
    public abstract MethodExpression getPlotClickListener();

    public abstract void setPlotClickListener(MethodExpression e);
    /**
     * Point symbols for line chart
     */
    public enum SymbolType {
        circle, square, diamond, triangle, cross
    }

}
