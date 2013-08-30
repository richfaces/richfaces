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
package org.richfaces.ui.select;

import com.google.common.collect.Iterators;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;

import javax.faces.component.UIColumn;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;

import java.util.Iterator;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
public abstract class AbstractSelectManyComponent extends UISelectMany implements EventsKeyProps, EventsMouseProps {

    public Iterator<UIColumn> columns() {
        return Iterators.filter(getChildren().iterator(), UIColumn.class);
    }

    //-------- multiselect-props.xml

    @Attribute
    public abstract String getVar();

    @Attribute
    public abstract String getCollectionType();

    @Attribute
    public abstract boolean isDisabled();

    @Attribute()
    public abstract String getListWidth();

    @Attribute()
    public abstract String getListHeight();

    @Attribute()
    public abstract String getMinListHeight();

    @Attribute()
    public abstract String getMaxListHeight();

    @Attribute
    public abstract String getStyle();

    @Attribute(hidden = true)
    public abstract String getActiveClass();

    @Attribute(hidden = true)
    public abstract String getChangedClass();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getItemClass();

    @Attribute
    public abstract String getSelectItemClass();

    @Attribute
    public abstract String getDisabledClass();

    @Attribute
    public abstract String getColumnClasses();

    @Attribute
    public abstract String getHeaderClass();

    /**
     * Javascript code executed when the list element loses focus and its value has been modified since gaining focus.
     */
    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    /**
     * Javascript code executed when this element receives focus
     */
    @Attribute(events = @EventName("focus"))
    public abstract String getOnfocus();

    /**
     * Javascript code executed when this element loses focus.
     */
    @Attribute(events = @EventName("blur"))
    public abstract String getOnblur();

    /**
     * Override the validateValue of SelectMany in cases where the component implements SelectItemsInterface
     *
     * @param facesContext
     * @param value
     */
    @Override
    protected void validateValue(FacesContext facesContext, Object value) {
        if (this instanceof SelectItemsInterface) {
            UISelectItems pseudoSelectItems = SelectManyHelper.getPseudoSelectItems((SelectItemsInterface) this);
            if (pseudoSelectItems != null) {
                this.getChildren().add(pseudoSelectItems);
                super.validateValue(facesContext, value);
                this.getChildren().remove(pseudoSelectItems);
                return;
            }
        }
        super.validateValue(facesContext, value);
    }
}
