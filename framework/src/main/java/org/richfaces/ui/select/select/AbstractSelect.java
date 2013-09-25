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

package org.richfaces.ui.select.select;

import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.validator.Validator;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.SelectProps;
import org.richfaces.ui.select.AbstractSelectComponent;

/**
 * <p>
 * The &lt;r:select&gt; component provides a drop-down list box for selecting a single value from multiple options. The
 * &lt;r:select&gt; component can be configured as a combo-box, where it will accept typed input. The component also supports
 * keyboard navigation. The &lt;r:select&gt; component functions similarly to the JSF UISelectOne component.
 * </p>
 *
 * @author abelevich
 */
@JsfComponent(type = AbstractSelect.COMPONENT_TYPE, family = AbstractSelect.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.ui.SelectRenderer"), tag = @Tag(name = "select"))
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractSelect extends AbstractSelectComponent implements CoreProps, EventsKeyProps, EventsMouseProps, SelectProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.Select";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.Select";

    /**
     * If "true", this component is disabled
     */
    @Attribute
    public abstract boolean isDisabled();

    /**
     * <p>
     * If "true" Allows the user to type into a text field to scroll through or filter the list
     * </p>
     * <p>
     * Default is "false"
     * </p>
     */
    @Attribute()
    public abstract boolean isEnableManualInput();

    /**
     * <p>
     * If "true" as the user types to narrow the list, automatically select the first element in the list. Applicable only when
     * enableManualInput is "true".
     * </p>
     * <p>
     * Default is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSelectFirst();

    /**
     * <p>
     * When "true" display a button to expand the popup list
     * </p>
     * <p>
     * Default is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowButton();

    /**
     * The minimum height ot the list
     */
    @Attribute()
    public abstract String getMinListHeight();

    /**
     * The maximum height of the list
     */
    @Attribute()
    public abstract String getMaxListHeight();

    /**
     * A javascript function used to filter the list of items in the select popup
     */
    @Attribute
    public abstract String getClientFilterFunction();

    @Attribute(hidden = true)
    public abstract String getActiveClass();

    @Attribute(hidden = true)
    public abstract String getChangedClass();

    @Attribute(hidden = true)
    public abstract String getDisabledClass();

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);

        if (event instanceof PostAddToViewEvent) {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            EditableValueHolder component = (EditableValueHolder) event.getComponent();

            Validator validator = facesContext.getApplication().createValidator(SelectLabelValueValidator.ID);
            component.addValidator(validator);
        }
    }
}
