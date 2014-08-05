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

import javax.faces.context.FacesContext;

/**
 * Interface for classes, able to receive visitor processing calls from ExtendedDataModel
 *
 * @author shura
 *
 */
public interface DataVisitor {
    /**
     * This method called back ( as visitor ) from {@link ExtendedDataModel#walk(FacesContext, DataVisitor, Range, Object)} for each
     * row.
     *
     * @param context current JSF context.
     * @param rowKey current row key of DataModel
     * @param argument Implementation-specific argument
     */
    DataVisitResult process(FacesContext context, Object rowKey, Object argument);
}
