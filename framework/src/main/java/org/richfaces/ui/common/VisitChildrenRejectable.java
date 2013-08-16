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
package org.richfaces.ui.common;

/**
 * The component is able to decide whether its subtree should be visited or not.
 *
 * @author Lukas Fryc
 */
public interface VisitChildrenRejectable {

    /**
     * Indicates whether processing logic such as renderer and visitors should visit (process) the children of this component.
     *
     * Prevents visiting (processing) of subtrees which are not for any reason "active".
     *
     * @return true if the component's subtree should be visited; false otherwise
     */
    boolean shouldVisitChildren();
}