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

package org.richfaces.ui.toggle;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.ui.common.AbstractDivPanel;
import org.richfaces.ui.common.SwitchType;
import org.richfaces.ui.common.VisitChildrenRejectable;
import org.richfaces.ui.toggle.togglePanel.AbstractTogglePanel;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface AbstractTogglePanelItemInterface extends AbstractDivPanel, VisitChildrenRejectable {
    AbstractTogglePanel getParentPanel();

    boolean isDynamicPanelItem();

    boolean isActive();

    // ------------------------------------------------ Component Attributes

    String getName();

    boolean isRendered();

    SwitchType getSwitchType();

    // ------------------------------------------------ Html Attributes

    /**
     * The function to perform when the mouse enters the panel
     */
    @Attribute(events = @EventName("enter"))
    String getOnenter();

    /**
     * The function to perform when the mouse leaves the panel.
     */
    @Attribute(events = @EventName("leave"))
    String getOnleave();

    String getClientId();
}
