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

/**
 * Interface defining the methods for iteration-props.xml
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public interface IterationProps {
    /**
     * A request-scope attribute via which the data object for the current row will be used when iterating
     */
    @Attribute(description = @Description("A request-scope attribute via which the data object for the current row will be used when iterating"))
    String getVar();

    /**
     * Provides access to the row key in a Request scope
     */
    @Attribute(description = @Description("Provides access to the row key in a Request scope"))
    String getRowKeyVar();

    /**
     * Provides access to the state in a Request scope
     */
    @Attribute(description = @Description("Provides access to the state in a Request scope"))
    String getStateVar();

    /**
     * Provides access to the iteration status in a Request scope
     */
    @Attribute(description = @Description("Provides access to the iteration status in a Request scope"))
    String getIterationStatusVar();

    /**
     * Boolean attribute that defines whether this iteration component will reset saved children's state before
     * rendering. By default state is reset if there are no faces messages with severity error or higher
     */
    @Attribute(description = @Description("Boolean attribute that defines whether this iteration component will reset saved children's state before rendering. By default state is reset if there are no faces messages with severity error or higher"))
    boolean isKeepSaved();

    /**
     * A zero-relative row number of the first row to display
     */
    @Attribute(description = @Description("A zero-relative row number of the first row to display"))
    int getFirst();

    /**
     * Points to the data model
     */
    @Attribute(description = @Description("Points to the data model"))
    Object getValue();
}