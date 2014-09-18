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
package org.richfaces.fragment.common;

import org.richfaces.fragment.collapsibleSubTable.RichFacesCollapsibleSubTable;

/**
 * This class is used when you don't want to insert particular fragment to some components.
 * EX: There is header, row and footer in {@link RichFacesCollapsibleSubTable}
 * if there should be displayed only rows then {@link NullFragment} is used this way:
 * RichFacesCollapsibleSubTable<NullFragment, EmployeeRecord, NullFragment>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class NullFragment {

}
