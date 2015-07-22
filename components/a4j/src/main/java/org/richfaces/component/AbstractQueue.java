/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
import org.richfaces.component.attribute.ErrorProps;

/**
 * <p>
 * The &lt;a4j:queue&gt; component manages the JSF queue of Ajax requests. It provides additional options for a finer
 * control of request processing.
 * </p>
 * @author Nick Belaevski
 */
@JsfComponent(tag = @Tag(name = "queue", generate = false, type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.QueueRenderer"))
public abstract class AbstractQueue extends UIComponentBase implements ErrorProps {
    public static final String GLOBAL_QUEUE_NAME = "org.richfaces.queue.global";
    public static final String COMPONENT_TYPE = "org.richfaces.Queue";
    public static final String COMPONENT_FAMILY = "org.richfaces.Queue";

    /**
     * Attribute defines the time (in ms) the request will be waiting in the queue before it is ready to be sent.
     */
    @Attribute
    public abstract int getRequestDelay();

    /**
     * The client-side script method to be called before an ajax request is submitted
     */
    @Attribute
    public abstract String getOnsubmit();

    /**
     * The client-side script method to be called after the request is completed
     */
    @Attribute
    public abstract String getOncomplete();

    /**
     * The client-side script method to be called before DOM is updated
     */
    @Attribute
    public abstract String getOnbeforedomupdate();

    /**
     * The client-side script method to be called when an error has occurred during Ajax communications
     */
    @Attribute
    public abstract String getOnerror();

    /**
     * The client-side script method to be called when the request is added to the queue
     */
    @Attribute
    public abstract String getOnrequestqueue();

    /**
     * The client-side script method to be called after the request is removed from the queue
     */
    @Attribute
    public abstract String getOnrequestdequeue();

    /**
     * Attribute allows you to ignore an Ajax response produced by a request if the newest 'similar' request is in the
     * queue already. ignoreDupResponses="true" does not cancel the request while it is processed on the server, but
     * just allows avoiding unnecessary updates on the client side if the response isn't actual now
     */
    @Attribute
    public abstract boolean isIgnoreDupResponses();

    /**
     * Specifies the name for the queue, allowing it to be referenced via the name attribute of the attachQueue component
     */
    @Attribute
    public abstract String getName();

    /**
     * Name of the request status component that will indicate the status of the Ajax request going through the queue
     */
    @Attribute
    public abstract String getStatus();
}
