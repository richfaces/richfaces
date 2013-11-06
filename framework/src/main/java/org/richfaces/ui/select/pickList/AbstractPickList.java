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

package org.richfaces.ui.select.pickList;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.MultiSelectProps;
import org.richfaces.ui.select.AbstractOrderingComponent;
import org.richfaces.ui.select.SelectItemsInterface;

/**
 * <p>The &lt;r:pickList&gt; is a component for pick items in a list (client-side).</p>
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(type = AbstractPickList.COMPONENT_TYPE, family = AbstractPickList.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = PickListRendererBase.RENDERER_TYPE), tag = @Tag(name = "pickList"))
public abstract class AbstractPickList extends AbstractOrderingComponent implements EventsKeyProps, EventsMouseProps, MultiSelectProps {
    public static final String COMPONENT_TYPE = "org.richfaces.select.PickList";
    public static final String COMPONENT_FAMILY = "org.richfaces.SelectMany";

    /**
     * <p>If "true", then the target list is orderable, and the ordering controls are displayed</p>
     * <p>Default is "true"</p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isOrderable();

    /**
     * The text placed above the source list of items
     */
    @Attribute
    public abstract String getSourceCaption();

    /**
     * The text placed above the target list of items
     */
    @Attribute
    public abstract String getTargetCaption();

    /**
     * The text to display in the add-all button
     */
    @Attribute(defaultValue = "⇒ Add all")
    public abstract String getAddAllText();

    /**
     * The text to display in the add button
     */
    @Attribute(defaultValue = "→ Add")
    public abstract String getAddText();

    /**
     * The text to display in the remove button
     */
    @Attribute(defaultValue = "← Remove")
    public abstract String getRemoveText();

    /**
     * The text to display in the remove-all button
     */
    @Attribute(defaultValue = "⇐ Remove all")
    public abstract String getRemoveAllText();

    /**
     * <p>if "true", then clicking an item moves it from one list to another</p>
     * <p>Default is "false"</p>
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isSwitchByClick();

    /**
     * <p>if "true", then double-clicking an item moves it from one list to another</p>
     * <p>Default is "true"</p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSwitchByDblClick();

    /**
     * Javascript code executed when items are removed from the source list, and added to the target list
     */
    @Attribute(events = @EventName("additems"))
    public abstract String getOnadditems();

    /**
     * Javascript code executed when items are removed from the target list, and added to the source list
     */
    @Attribute(events = @EventName("removeitems"))
    public abstract String getOnremoveitems();

}
