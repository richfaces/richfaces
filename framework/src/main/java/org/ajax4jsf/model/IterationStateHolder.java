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
package org.ajax4jsf.model;

/**
 * In the original {@link javax.faces.component.UIData} component, only state for a
 * {@link javax.faces.component.EditableValueHolder} component saved for an iteration. In the Richfaces, we also save state for
 * a components implemented this interface.
 *
 * @author asmirnov
 *
 */
public interface IterationStateHolder {
    /**
     * Get component state for a current iteration.
     *
     * @return request-scope component state. Details are subject for a component implementation
     */
    Object getIterationState();

    /**
     * Set component state for the next iteration. State can be either previously saved iteration state or <code>null</code>
     * value. In the second case component should reset its state to the initial.
     *
     * @param state request-scope component state or <code>null</code>. Details are subject for a component implementation
     */
    void setIterationState(Object state);
}
