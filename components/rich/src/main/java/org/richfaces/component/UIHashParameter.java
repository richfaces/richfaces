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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.view.facelets.ComponentHandler;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;

/**
 * <p>The &lt;r:hashParam&gt; component allows client-side parameters to be grouped into a hash map. The hash map can
 * then be passed to the client JavaScript API functions of any RichFaces component.</p>
 *
 * @author Anton Belevich
 */
@JsfComponent(type = UIHashParameter.COMPONENT_TYPE, family = UIHashParameter.COMPONENT_FAMILY, tag = @Tag(name = "hashParam", handlerClass = ComponentHandler.class))
public class UIHashParameter extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.HashParameter";
    public static final String COMPONENT_FAMILY = "org.richfaces.HashParameter";

    enum PropertyKeys {
        name
    }

    public UIHashParameter() {
        super();
        setRendererType(null);
    }

    @Attribute(hidden = true)
    public boolean isRendered() {return true;};

    /**
     * The name of the javascript hash property which this hash should be assigned to (can be used as nested hashParameter) or
     * null if no property should be used (can be used simply as function parameter).
     */
    @Attribute
    public String getName() {
        return (String) getStateHelper().eval(PropertyKeys.name);
    }

    public void setName(String name) {
        getStateHelper().put(PropertyKeys.name, name);
    }

    /**
     * Computes value of hash parameters
     */
    @Attribute(hidden = true)
    public Map<String, Object> getValue() {
        List<UIComponent> children = getChildren();
        Map<String, Object> parameters = new HashMap<String, Object>();

        for (UIComponent child : children) {
            if (child instanceof UIParameter) {
                UIParameter parameter = (UIParameter) child;
                parameters.put(parameter.getName(), (String) parameter.getValue());
            }

            if (child instanceof UIHashParameter) {
                UIHashParameter hashParameter = (UIHashParameter) child;
                String name = hashParameter.getName();
                Map<String, Object> value = hashParameter.getValue();
                if (name == null) {
                    throw new FacesException("attribute 'name' for the nested " + UIHashParameter.class.getName()
                            + " component (id = '" + hashParameter.getClientId() + "') should not be 'null'");
                } else {
                    parameters.put(name, value);
                }
            }
        }

        return parameters;
    }

    @Override
    public String getFamily() {
        return (COMPONENT_FAMILY);
    }
}
