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

package org.richfaces.component.attribute;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.component.SwitchType;

import javax.el.MethodExpression;

import java.util.Collection;

/**
 * Interface defining the methods for tree-common-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface TreeCommonProps {
    /**
     * The icon for component leaves
     */
    @Attribute(description = @Description(value = "The icon for component leaves."))
    String getIconLeaf();

    /**
     * The icon for expanded node
     */
    @Attribute(description = @Description(value = "The icon for expanded node."))
    String getIconExpanded();

    /**
     * The icon for collapsed node
     */
    @Attribute(description = @Description(value = "The icon for collapsed node."))
    String getIconCollapsed();

    /**
     * Space-separated list of CSS style class(es) to be applied to the tree node handles.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the tree node handles."))
    String getHandleClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the tree node icons.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the tree node icons."))
    String getIconClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the tree node labels.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the tree node labels."))
    String getLabelClass();
}