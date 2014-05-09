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
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.component.SwitchType;

import javax.el.MethodExpression;
import java.util.Collection;

/**
 * Interface defining the methods for tree-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface TreeProps {
    /**
     * Space-separated list of CSS style class(es) to be applied to the tree nodes.
     */
    @Attribute
    String getNodeClass();

    /**
     * Determines which treeNode should be used for rendering. Should contain EL expressions which uses request-scoped variable with name defied in attribute 'var'.
     */
    @Attribute
    String getNodeType();

    /**
     * The client-side script method to be called after the node is toggled.
     */
    @Attribute(events = @EventName("nodetoggle"))
    String getOnnodetoggle();

    /**
     * The client-side script method to be called after the selection is changed.
     */
    @Attribute(events = @EventName("selectionchange"))
    String getOnselectionchange();

    /**
     *  The client-side script method to be called before the node is toggled.
     */
    @Attribute(events = @EventName("beforenodetoggle"))
    String getOnbeforenodetoggle();

    /**
     * The client-side script method to be called before the selection is changed.
     */
    @Attribute(events = @EventName("beforeselectionchange"))
    String getOnbeforeselectionchange();

    /**
     * Client-side event used for toggling tree nodes (click, dblclick, etc.)
     */
    @Attribute
    String getToggleNodeEvent();

    /**
     * The collections of selected nodes.
     */
    @Attribute
    Collection getSelection();

    /**
     *The type of type of the selection - ajax (default), client, server.
     */
    @Attribute
    SwitchType getSelectionType();

    /**
     * The type of type of the node toggling - ajax (default), client, server.
     */
    @Attribute
    SwitchType getToggleType();

    /**
     * MethodExpression representing an tree node toggle listener method that will be notified when the tree node
     * is toggled. The expression must evaluate to a public method that takes an TreeToggleEvent parameter, with a
     * return type of void, or to a public method that takes no arguments with a return type of void.
     * In the latter case, the method has no way of easily knowing where the event came from, but this can be
     * useful in cases where a notification is needed that "some action happened".
     */
    @Attribute
    MethodExpression getSelectionChangeListener();

    /**
     * Method expression referencing a method that will be called when an TreeSelectionChangeEvent has been broadcast for the listener.
     */
    @Attribute
    MethodExpression getToggleListener();

    // TODO: There is some overlap between the attributes below and with IterationProps.

    /**
     * A request-scope attribute via which the data object for the current row will be used when iterating
     */
    @Attribute
    String getVar();

    /**
     * Provides access to the row key in a Request scope
     */
    @Attribute
    String getRowKeyVar();

    /**
     * Provides access to the state in a Request scope
     */
    @Attribute
    String getStateVar();

    /**
     * Boolean attribute that defines whether this iteration component will reset saved children's state before
     * rendering. By default state is reset if there are no faces messages with severity error or higher
     */
    @Attribute
    boolean isKeepSaved();
}