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

import java.util.Collection;

import javax.el.MethodExpression;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.component.SwitchType;

/**
 * Interface defining the methods for tree-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface TreeProps {
    /**
     * Space-separated list of CSS style class(es) to be applied to the tree nodes.
     */
    @Attribute(description = @Description(value = "Space-separated list of CSS style class(es) to be applied to the tree nodes."))
    String getNodeClass();

    /**
     * Determines which treeNode should be used for rendering. Should contain EL expressions which uses request-scoped variable with name defied in attribute 'var'. If not set a default treeNode will be created.
     */
    @Attribute(description = @Description(value = "Determines which treeNode should be used for rendering. Should contain EL expressions which uses request-scoped variable with name defied in attribute 'var'. If not set a default treeNode will be created."))
    String getNodeType();

    /**
     * The client-side script method to be called after the node is toggled.
     */
    @Attribute(events = @EventName("nodetoggle"), description = @Description(value = "The client-side script method to be called after the node is toggled."))
    String getOnnodetoggle();

    /**
     * The client-side script method to be called after the selection is changed.
     */
    @Attribute(events = @EventName("selectionchange"), description = @Description(value = "The client-side script method to be called after the selection is changed."))
    String getOnselectionchange();

    /**
     *  The client-side script method to be called before the node is toggled.
     */
    @Attribute(events = @EventName("beforenodetoggle"), description = @Description(value = "The client-side script method to be called before the node is toggled."))
    String getOnbeforenodetoggle();

    /**
     * The client-side script method to be called before the selection is changed.
     */
    @Attribute(events = @EventName("beforeselectionchange"), description = @Description(value = "The client-side script method to be called before the selection is changed."))
    String getOnbeforeselectionchange();

    /**
     * Client-side event used for toggling tree nodes (click, dblclick, etc.)
     */
    @Attribute(description = @Description(value = "Client-side event used for toggling tree nodes (click, dblclick, etc.)"))
    String getToggleNodeEvent();

    /**
     * The collections of selected nodes.
     */
    @Attribute(description = @Description(value = "The collections of selected nodes."))
    Collection getSelection();

    /**
     *The type of type of the selection - ajax (default), client, server.
     */
    @Attribute(description = @Description(value = "The type of type of the selection - ajax (default), client, server."))
    SwitchType getSelectionType();

    /**
     * The type of type of the node toggling - ajax (default), client, server.
     */
    @Attribute(description = @Description(value = "The type of type of the node toggling - ajax (default), client, server."))
    SwitchType getToggleType();

    /**
     * Method expression referencing a method that will be called when an TreeSelectionChangeEvent has been broadcast for the listener.
     */
    @Attribute(description = @Description(value = "Method expression referencing a method that will be called when an TreeSelectionChangeEvent has been broadcast for the listener."))
    MethodExpression getSelectionChangeListener();

    // TODO: There is some overlap between the attributes below and with IterationProps.

    /**
     * A request-scope attribute via which the data object for the current row will be used when iterating
     */
    @Attribute(description = @Description(value = "A request-scope attribute via which the data object for the current row will be used when iterating."))
    String getVar();

    /**
     * Provides access to the row key in a Request scope
     */
    @Attribute(description = @Description(value = "Provides access to the row key in a Request scope."))
    String getRowKeyVar();

    /**
     * Provides access to the state in a Request scope
     */
    @Attribute(description = @Description(value = "Provides access to the state in a Request scope."))
    String getStateVar();

    /**
     * Boolean attribute that defines whether this iteration component will reset saved children's state before
     * rendering. By default state is reset if there are no faces messages with severity error or higher
     */
    @Attribute(description = @Description(value = "Boolean attribute that defines whether this iteration component will reset saved children's state before rendering. By default state is reset if there are no faces messages with severity error or higher."))
    boolean isKeepSaved();

    /**
     * When "true" a default treeNode will be created for nodes in the dataModel that do not match any existing treeNode. Default value - "false".
     */
    @Attribute(defaultValue = "false", description = @Description("When \"true\" a default treeNode will be created for nodes in the dataModel that do not match any existing treeNode. Default value - \"false\"."))
    boolean isUseDefaultNode();
}
