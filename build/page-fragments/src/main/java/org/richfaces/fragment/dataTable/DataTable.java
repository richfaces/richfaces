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
package org.richfaces.fragment.dataTable;

import java.util.List;

/**
 * Interface representing a data table.
 *
 * <p>Each table consists of its header, row and footer. Those should be page
 * fragments, which will be then returned as a properly initialized objects.</p>
 *
 * <p>It is solely on the end user how the implementations of the HEADER, ROW
 * or FOOTER would look like.</p> For example HEADER page fragment can provide
 * encapsulated filtering or sorting table services.
 *
 * <p>Note 1: Table does not take into account pagination. Thus one has to
 *  switch to a different page manually in order to access all rows. All indexes
 *  used by methods are relative to one page.</p>
 *
 * <p>Note 2: that one can use <tt>org.richfaces.fragment.common.NullFragment</tt>
 * as a Null Object pattern for any of the generic types.</p>
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @param <HEADER>
 * @param <ROW>
 * @param <FOOTER>
 */
public interface DataTable<HEADER, ROW, FOOTER> {

    /**
    * Returns a <tt>ROW</tt> page fragment with index <tt>n</tt>.
    *
    * All indexes are relative to one page. It does not take into account
    * table pagination.
    *
    * @param n zero based index of the row to be returned
    * @return
    */
    ROW getRow(int n);

    /**
    * Returns the first <tt>ROW</tt> page fragment.
    *
    * All indexes are relative to one page. It does not take into account
    * table pagination.
    *
    * @return
    */
    ROW getFirstRow();

    /**
    * Returns the last <tt>ROW</tt> page fragment.
    *
    * All indexes are relative to one page. It does not take into account
    * table pagination.
    *
    * @return
    */
    ROW getLastRow();

    /**
    * Returns a list of <tt>ROW</tt> page fragments.
    *
    * All indexes are relative to one page. It does not take into account
    * table pagination.
    *
    * @return
    */
    List<ROW> getAllRows();

    /**
    * Returns a <tt>HEADER</tt> page fragment of this table.
    *
    * @return
    */
    HEADER getHeader();

    /**
    * Returns a <tt>FOOTER</tt> page fragment of this table.
    *
    * @return
    */
    FOOTER getFooter();
}