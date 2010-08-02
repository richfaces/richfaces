/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.component;

import java.io.IOException;
import java.util.EventListener;

import javax.el.MethodExpression;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * Component for periodically call AJAX events on server ( poll actions )
 * @author shura
 *
 */
@JsfComponent(
    tag = @Tag(generate = false, handler = "org.richfaces.view.facelets.html.AjaxPushHandler", type = TagType.Facelets),
    renderer = @JsfRenderer(type = "org.richfaces.PushRenderer")
)
public abstract class AbstractPush extends AbstractActionComponent {

    public static final String COMPONENT_TYPE = "org.richfaces.Push";

    public static final String COMPONENT_FAMILY = "org.richfaces.Push";
    
    public static final String DATA_AVAILABLE = "dataAvailable";

    public static final String ON_DATA_AVAILABLE = "ondataavailable";

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        MethodExpression producer = getEventProducer();

        // Subscribe events producer to push status listener.
        if (null != producer) {
            producer.invoke(context.getELContext(), new Object[] {getListener(context)});
        }

        super.encodeBegin(context);
    }

    private PushEventTracker getListener(FacesContext context) {
        PushListenersManager pushListenersManager = PushListenersManager.getInstance(context);

        return pushListenersManager.getListener(getListenerId(context));
    }

    public String getListenerId(FacesContext context) {
        Object session = context.getExternalContext().getSession(false);
        StringBuffer id = new StringBuffer();

        if (null != session && session instanceof HttpSession) {
            HttpSession httpSession = (HttpSession) session;

            id.append(httpSession.getId());
        }

        id.append(context.getViewRoot().getViewId());
        id.append(NamingContainer.SEPARATOR_CHAR);
        id.append(getClientId(context));

        return id.toString();
    }

    // ---------------------------------------
    @Attribute(signature = @Signature(parameters = EventListener.class))
    public abstract MethodExpression getEventProducer();

    public abstract void setEventProducer(MethodExpression producer);

    /**
     * @return time in mc for polling interval.
     */
    @Attribute(defaultValue = "1000")
    public abstract int getInterval();

    @Attribute(defaultValue = "true")
    public abstract boolean isEnabled();

    // TODO what wrong with that name?
    @Attribute(events = @EventName(value = DATA_AVAILABLE, defaultEvent = true))
    public abstract String getOndataavailable();

    @Attribute(events = @EventName("begin"))
    public abstract String getOnbegin();

    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();

    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();
}
