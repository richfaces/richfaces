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

package org.richfaces.demo.common.navigation;

import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.richfaces.el.util.ELUtils;

public class BaseDescriptor implements Serializable {
    private static final long serialVersionUID = 5614594358147757458L;
    private static final String PHONE_HOME_VIEW_ID = "/phoneHome.xhtml";
    private String id;
    private String name;
    private boolean newItem;
    private boolean currentItem;
    private String enabled;
    private Boolean mobileExclude;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "new")
    public boolean isNewItem() {
        return newItem;
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }

    @XmlAttribute(name = "enabled")
    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public boolean isNewEnabled() {
        return isNewItem() && isCurrentlyEnabled();
    }

    @XmlAttribute(name = "mobileExclude")
    public boolean isMobileExclude() {
        return mobileExclude;
    }

    public void setMobileExclude(boolean mobileExclude) {
        this.mobileExclude = mobileExclude;
    }

    /**
     * Evaluates that this sample/demo/group is enabled in current context
     *
     * @return
     */
    public boolean isCurrentlyEnabled() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (mobileExclude != null && mobileExclude && isMobileRequest(facesContext)) {
            return false;
        }
        if (enabled != null && !evaluateBooleanRequestScopedExpression(facesContext, enabled)) {
            return false;
        }
        return true;
    }

    private boolean isMobileRequest(FacesContext facesContext) {
        return isPhoneHomeView(facesContext) || evaluateBooleanRequestScopedExpression(facesContext, "#{userAgent.mobile}");
    }

    private boolean isPhoneHomeView(FacesContext facesContext) {
        return PHONE_HOME_VIEW_ID.equals(facesContext.getViewRoot().getViewId());
    }

    /**
     * Caches results of {@link #evaluateBooleanExpression(String, FacesContext)} so one expression is evaluated at most once
     * per request
     */
    private boolean evaluateBooleanRequestScopedExpression(FacesContext facesContext, String expression) {
        String key = this.getClass().getName() + expression;
        Boolean result = (Boolean) facesContext.getAttributes().get(key);
        if (result == null) {
            result = evaluateBooleanExpression(expression, facesContext);
            facesContext.getAttributes().put(key, result);
        }
        return result;
    }

    /**
     * Evaluates given expression in provided context
     */
    private boolean evaluateBooleanExpression(String expression, FacesContext facesContext) {
        ELContext elContext = facesContext.getELContext();
        ValueExpression enabledVE = ELUtils.createValueExpression(expression);
        try {
            Boolean evaluatedResult = (Boolean) ELUtils.evaluateValueExpression(enabledVE, elContext);
            if (evaluatedResult == null) {
                throw new IllegalArgumentException("Expression '" + expression + "' evaluated to null");
            }
            return evaluatedResult;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(boolean currentItem) {
        this.currentItem = currentItem;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseDescriptor[" + name + "]";
    }
}
