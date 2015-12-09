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

import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.model.PlotClickEvent;
import org.richfaces.model.PlotClickListener;
import org.richfaces.renderkit.ChartRendererBase;
import org.richfaces.view.facelets.html.ChartTagHandler;


/**
 * A component used to generate visual charts.
 *
 * @author Lukas Macko
 */
@JsfComponent(
        type= AbstractChart.COMPONENT_TYPE,
        family = AbstractChart.COMPONENT_FAMILY,
        tag = @Tag(name="chart",handlerClass=ChartTagHandler.class,type = TagType.Facelets),
        facets = @Facet(name = "hooks", description = @Description("A set of JavaScript functions to modify the plotting process.")),
        renderer = @JsfRenderer(type = ChartRendererBase.RENDERER_TYPE),
        fires = { @Event(value = PlotClickEvent.class, listener = PlotClickListener.class) })
public abstract class AbstractChart extends UIComponentBase implements CoreProps {

    public static final String COMPONENT_TYPE = "org.richfaces.Chart";
    public static final String COMPONENT_FAMILY = "org.richfaces.Chart";

    /**
     * Attribute define whether zoom is enabled. To reset zoom you can use JS
     * API $('#id').chart('resetZoom') Attribute is currently supported by line
     * chart.
     */
    @Attribute
    public abstract boolean isZoom();

    /**
     * A set of JavaScript functions to modify the plotting process.
     */
    @Attribute
    public abstract String getHooks();

    /**
     * Javascript handler function for plotclick event called for each series.
     * You can setup handler for particular series only. See series tag
     * attribute onplotclick.
     */
    @Attribute(events = @EventName("plotclick"))
    public abstract String getOnplotclick();

    /**
     * Javascript handler function for plothover event for each series. You can
     * setup handler for particular series only. See series tag attribute
     * onplothover.
     */
    @Attribute(events = @EventName("plothover"))
    public abstract String getOnplothover();

    /**
     * Complementary event for plothover fired when mouse leaves the chart grid.
     */
    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    /**
     * Server-side listener for plotclick event.
     */
    @Attribute(signature = @Signature(parameters = PlotClickEvent.class))
    public abstract MethodExpression getPlotClickListener();

    public abstract void setPlotClickListener(MethodExpression plotClickListener);

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {

        if (event instanceof PlotClickEvent) {
            FacesContext context = getFacesContext();
            MethodExpression expression = getPlotClickListener();

            if (expression != null) {
                expression.invoke(context.getELContext(),
                        new Object[] { event });
            }

            //particular series listener
            int seriesId = ((PlotClickEvent) event).getSeriesIndex();
            List<MethodExpression> particularSeriesListeners = (List<MethodExpression>) getAttributes().get("particularSeriesListeners");

            if(particularSeriesListeners!=null){
                if(seriesId >= 0 && seriesId < particularSeriesListeners.size()){
                    MethodExpression expr = particularSeriesListeners.get(seriesId);
                    if(expr!=null){
                        expr.invoke(context.getELContext(), new Object[]{event});
                    }
                }
            }
        }
        super.broadcast(event);
    }

}
