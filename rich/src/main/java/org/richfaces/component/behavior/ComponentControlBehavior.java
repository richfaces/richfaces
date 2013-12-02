/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.behavior;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfBehavior;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.behavior.ClientBehavior;

/**
 * <p>
 *     The &lt;rich:componentControl&gt; behavior allows JavaScript API functions to be called on target components.
 *     The functions are called after defined events are triggered on the component to with the
 *     &lt;rich:componentControl&gt; behavior is attached. Initialization variants and activation events can be
 *     customized, and parameters can be passed to the target component.
 * </p>
 * @author Anton Belevich
 */
@JsfBehavior(id = "org.richfaces.behavior.ComponentControlBehavior", tag = @Tag(name = "componentControl", handler = "org.richfaces.taglib.ComponentControlHandler", type = TagType.Facelets))
public class ComponentControlBehavior extends ClientBehavior {
    public static final String BEHAVIOR_ID = "org.richfaces.behavior.ComponentControlBehavior";
    private List<UIComponent> children;

    enum PropertyKeys {
        target, selector, operation, onbeforeoperation, event
    }

    public List<UIComponent> getChildren() {
        if (children == null) {
            children = new ArrayList<UIComponent>();
        }
        return children;
    }

    /**
     * Name of JavaScript event property (click, change, etc.) of parent component that triggers the behavior. If the event
     * attribute is not defined, the behavior is triggered on the event that normally provides interaction behavior for the
     * parent component
     */
    @Attribute
    public String getEvent() {
        return (String) getStateHelper().eval(PropertyKeys.event);
    }

    public void setEvent(String event) {
        getStateHelper().put(PropertyKeys.event, event);
    }

    /**
     * Comma-separated list of component IDs of components which will be the operation fired on. (Alternatively you can use
     * selector attribute).
     */
    @Attribute
    public String getTarget() {
        return (String) getStateHelper().eval(PropertyKeys.target);
    }

    public void setTarget(String target) {
        getStateHelper().put(PropertyKeys.target, target);
    }

    /**
     * jQuery selector (CSS selector with jQuery extensions) which finds target component which will be the operation fired on.
     */
    @Attribute
    public String getSelector() {
        return (String) getStateHelper().eval(PropertyKeys.selector);
    }

    public void setSelector(String selector) {
        getStateHelper().put(PropertyKeys.selector, selector);
    }

    /**
     * The function of JavaScript API that will be invoked on the target components (selected with target or selector
     * attributes).
     */
    @Attribute
    public String getOperation() {
        return (String) getStateHelper().eval(PropertyKeys.operation);
    }

    public void setOperation(String operation) {
        getStateHelper().put(PropertyKeys.operation, operation);
    }

    /**
     * Javascript invoked just before the operation will be invoked on target component.
     */
    @Attribute
    public String getOnbeforeoperation() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeoperation);
    }

    public void setOnbeforeoperation(String onbeforeoperation) {
        getStateHelper().put(PropertyKeys.onbeforeoperation, onbeforeoperation);
    }

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

    @Override
    public void setLiteralAttribute(String name, Object value) {
        if (compare(PropertyKeys.operation, name)) {
            setOperation((String) value);
        } else if (compare(PropertyKeys.target, name)) {
            setTarget((String) value);
        } else if (compare(PropertyKeys.selector, name)) {
            setSelector((String) value);
        } else if (compare(PropertyKeys.onbeforeoperation, name)) {
            setOnbeforeoperation((String) value);
        }
    }
}
