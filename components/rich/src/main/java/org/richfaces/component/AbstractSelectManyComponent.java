/**
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

import com.google.common.collect.Iterators;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.MultiSelectProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.component.util.SelectItemsInterface;
import org.richfaces.renderkit.SelectManyHelper;

import javax.faces.component.UIColumn;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
public abstract class AbstractSelectManyComponent extends UISelectMany implements EventsKeyProps, EventsMouseProps, DisabledProps, MultiSelectProps, StyleProps, StyleClassProps {

    public Iterator<UIColumn> columns() {
        return Iterators.filter(getChildren().iterator(), UIColumn.class);
    }

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
