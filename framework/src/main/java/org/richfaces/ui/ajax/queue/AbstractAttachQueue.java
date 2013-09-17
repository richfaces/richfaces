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
package org.richfaces.ui.ajax.queue;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.ajax.ajax.AjaxBehavior;
import org.richfaces.util.AjaxRendererUtils;

/**
 * <p>
 * The &lt;r:attachQueue&gt; behavior is used together with a &lt;r:queue&gt; component to further customize queuing for
 * particular components and behaviors. The &lt;r:attachQueue&gt; behavior can override the scope-wide queue settings for
 * an individual component, or attach specific requests to a queue.
 * </p>
 * @author Nick Belaevski
 */
@JsfComponent(renderer = @JsfRenderer(type = "org.richfaces.ui.AttachQueueRenderer"), tag = @Tag(name = "attachQueue", handlerClass = AttachQueueHandler.class,

generate = false, type = TagType.Facelets))
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractAttachQueue extends UIComponentBase implements ComponentSystemEventListener {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.AttachQueue";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.AttachQueue";
    private transient List<UIComponent> componentsToAssociate;
    private transient List<AjaxBehavior> behaviorsToAssociate;

    /**
     * Specify the groupingId to process multiple requests together. Requests from multiple <r:attachQueue> behaviors
     * can use the same identifier to group requests together
     */
    @Attribute
    public abstract String getRequestGroupingId();

    /**
     * Attribute defines the time (in ms) the request will be waiting in the queue before it is ready to be sent.
     */
    @Attribute
    public abstract int getRequestDelay();

    /**
     * Attribute allows you to ignore an Ajax response produced by a request if the newest 'similar' request is in the
     * queue already. ignoreDupResponses="true" does not cancel the request while it is processed on the server, but
     * just allows avoiding unnecessary updates on the client side if the response isn't actual now
     */
    @Attribute
    public abstract boolean isIgnoreDupResponses();

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
     * Identify the name of the destination queue
     */
    @Attribute
    public abstract String getName();

    public String getQueueId() {
        return getName();
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    private static void immediateAssociateWith(UIComponent component, String queueId) {
        component.getAttributes().put(AjaxRendererUtils.QUEUE_ID_ATTRIBUTE, queueId);
    }

    private static void immediateAssociateWith(AjaxBehavior behavior, String queueId) {
        behavior.setQueueId(queueId);
    }

    /**
     * <p>
     * Establishes association between attachQueue component and component passed as method argument.
     * </p>
     * <p>
     * Association can be established either immediately just before returning from this method, or postponed until attachQueue
     * component will be added to view.
     * </p>
     *
     * @param component
     */
    public void associateWith(UIComponent component) {
        if (isInView()) {
            immediateAssociateWith(component, getClientId());
        } else {
            if (componentsToAssociate == null) {
                componentsToAssociate = new ArrayList<UIComponent>(2);
            }

            componentsToAssociate.add(component);
        }
    }

    /**
     * <p>
     * Establishes association between attachQueue component and behavior passed as method argument.
     * </p>
     * <p>
     * Association can be established either immediately just before returning from this method, or postponed until attachQueue
     * component will be added to view.
     * </p>
     *
     * @param behavior
     */
    public void associateWith(AjaxBehavior behavior) {
        if (isInView()) {
            immediateAssociateWith(behavior, getClientId());
        } else {
            if (behaviorsToAssociate == null) {
                behaviorsToAssociate = new ArrayList<AjaxBehavior>(2);
            }

            behaviorsToAssociate.add(behavior);
        }
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);

        if (event instanceof PostAddToViewEvent) {
            String queueId = getClientId();

            if (componentsToAssociate != null) {
                for (UIComponent componentToAssociate : componentsToAssociate) {
                    immediateAssociateWith(componentToAssociate, queueId);
                }

                componentsToAssociate = null;
            }

            if (behaviorsToAssociate != null) {
                for (AjaxBehavior ajaxBehavior : behaviorsToAssociate) {
                    immediateAssociateWith(ajaxBehavior, queueId);
                }

                behaviorsToAssociate = null;
            }
        }
    }
}
