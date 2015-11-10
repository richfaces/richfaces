/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * <p>The &lt;rich:jQuery&gt; component applies styles and custom behavior to both JSF (JavaServer Faces) objects and
 * regular DOM (Document Object Model) objects. It uses the jQuery JavaScript framework to add functionality to web
 * applications.</p>
 *
 * <p>This component is for use as a facelet tag.  See the &lt;rich:jQuery&gt; EL function for access to the jQuery library
 * via EL.</p>
 *
 * @author nick
 */
@JsfComponent(type = AbstractJQuery.COMPONENT_TYPE, family = AbstractJQuery.COMPONENT_FAMILY, renderer = @JsfRenderer(type = "org.richfaces.JQueryRenderer"), tag = @Tag(type = TagType.Facelets))
public abstract class AbstractJQuery extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.JQuery";
    public static final String COMPONENT_FAMILY = "org.richfaces.JQuery";

    // TODO nick - CDK should be doing this
    public AbstractJQuery() {
        setRendererType("org.richfaces.JQueryRenderer");
    }

    /**
     * The name of a function that will be generated to execute a query.
     */
    @Attribute
    public abstract String getName();

    /**
     * The jQuery selector (subset of CSS selectors defined by W3C) of the element to which the jQuery function should be applied.
     *
     * ID selectors starting with hash sign (#) will be expanded from componentId to clientId form. (e.g. #component is expanded to #form:component in case that component is nested in form)
     */
    @Attribute
    public abstract String getSelector();

    /**
     * The DOM event which should be the query bound to.
     */
    @Attribute
    public abstract String getEvent();

    /**
     * The query string that is executed for a given selector.
     */
    @Attribute
    public abstract String getQuery();

    /**
     * The type of the attachment - decides about how is operation attached to the selected elements: "bind" (default - binds to
     * the selected elements immediately, elements which could be matched by selector in the future won't be considered - offers
     * a best performance), "live" (attach an event handler for all elements which match the current selector, now and in the
     * future - may cause a slow performance), "one" (attach a handler to an event for the elements, the handler is executed at
     * most once per element)
     */
    @Attribute
    public abstract JQueryAttachType getAttachType();

    /**
     * The timing of the jQuery attachment: "domready" (when DOM is ready), "immediate" (immediately after component client-side
     * code is processed). This attribute is ignored when attribute "name" is provided. Default value - "domready"
     */
    @Attribute
    public abstract JQueryTiming getTiming();
}
