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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.component.attribute.AjaxCommandProps;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.ErrorProps;
import org.richfaces.util.Sets;

public class BasicActionComponent extends AbstractActionComponent implements AjaxCommandProps {

    enum PropertyKeys {
        render,
        execute
    }

    private Set<String> copyToSet(Object collection) {
        return Collections.unmodifiableSet(Sets.asSet(collection));
    }

    private Set<String> toSet(Serializable propertyName, Object value) {

        Set<String> result = null;

        result = Sets.asSet(value);

        if (result == null) {
            throw new FacesException(
                propertyName.toString()
                    + "' attribute value must be Collection, List, array, String, comma-separated String, whitespace-separate String'");
        }

        return result;
    }

    private Collection<String> getCollectionValue(Serializable propertyName) {
        Object value = getStateHelper().get(propertyName);
        if (value != null) {
            return (Collection<String>) value;
        }

        Collection<String> result = null;

        ValueExpression expression = getValueExpression(propertyName.toString());
        if (expression != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            value = expression.getValue(facesContext.getELContext());

            if (value != null) {

                if (value instanceof Collection) {
                    return (Collection<String>) value;
                }

                result = toSet(propertyName, value);
            }
        }
        return result == null ? Collections.<String>emptyList() : result;
    }

    /**
     * Ids of components that will participate in the "execute" portion of the Request Processing Lifecycle. Can be a single id,
     * a space or comma separated list of Id's, or an EL Expression evaluating to an array or Collection. Any of the keywords
     * "@this", "@form", "@all", "@none", "@region" may be specified in the identifier list. Some components make use of
     * additional keywords.<br/>
     * Default value is "@region" which resolves to this component if no region is present.
     */
    @Attribute
    public Object getExecute() {
        return getCollectionValue(PropertyKeys.execute);
    }

    public void setExecute(Object execute) {
        getStateHelper().put(PropertyKeys.execute, copyToSet(execute));
        clearInitialState();
    }

    /**
     * Ids of components that will participate in the "render" portion of the Request Processing Lifecycle. Can be a single id,
     * a space or comma separated list of Id's, or an EL Expression evaluating to an array or Collection. Any of the keywords
     * "@this", "@form", "@all", "@none", "@region" may be specified in the identifier list. Some components make use of
     * additional keywords
     */
    @Attribute
    public Object getRender() {
        return getCollectionValue(PropertyKeys.render);
    }

    public void setRender(Object render) {
        getStateHelper().put(PropertyKeys.render, copyToSet(render));
        clearInitialState();
    }

    @Override
    public boolean isBypassUpdates() {
        return false;
    }

    @Override
    public boolean isLimitRender() {
        return false;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public String getOnbeforedomupdate() {
        return null;
    }

    @Override
    public String getOncomplete() {
        return null;
    }

    @Override
    public String getOnerror() {
        return null;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public String getOnbegin() {
        return null;
    }

    @Override
    public boolean isResetValues() {
        return false;
    }

}
