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

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;


/**
 * @author Lukas Macko
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, generate = true), renderer = @JsfRenderer(type = ChartRendererBase.RENDERER_TYPE), fires = { @Event(value = PlotClickEvent.class, listener = PlotClickListener.class) })
public abstract class AbstractChart extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.ui.output.Chart";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.output.ChartFamily";

    /**
     * Chart title shown above the chart.
     */
    @Attribute
    public abstract String getTitle();

    /**
     * The attribute assign CSS class to component div.
     */
    @Attribute
    public abstract String getStyleClass();

    /**
     * Attribute define whether zoom is enabled. To reset zoom you can use JS
     * API $('#id').chart('resetZoom') Attribute is currently supported by line
     * chart.
     */
    @Attribute
    public abstract boolean isZoom();

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
    public abstract MethodExpression getClickListener();

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {

        if (event instanceof PlotClickEvent) {
            FacesContext context = getFacesContext();
            MethodExpression expression = getClickListener();

            if (expression != null) {
                expression.invoke(context.getELContext(),
                        new Object[] { event });
            }
        }
        super.broadcast(event);
    }

}
