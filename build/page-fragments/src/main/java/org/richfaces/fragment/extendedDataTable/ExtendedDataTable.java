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
package org.richfaces.fragment.extendedDataTable;

import org.openqa.selenium.Keys;

/**
 * Interface representing an Extended Data Table.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public interface ExtendedDataTable {

    /**
    * Selects a particular row.
    *
    * All indexes are relative to one page. It does not take into account
    * table pagination.
    *
    * @param rowIndex the index of the row to be selected
    * @param keys the keys to be pressed prior the selecting of the row,
    * and released afterwards. No more than 2 keys can be passed. Usually,
    * used ones are SHIFT and CONTROL.
    */
    void selectRow(int rowIndex, Keys... keys);

    /**
    * Deselects a particular row.
    *
    * All indexes are relative to one page. It does not take into account
    * table pagination.
    *
    * @param rowIndex the index of the row to be deselected
    * @param keys the keys to be pressed prior the deselecting of the row,
    * and released afterwards. No more than 2 keys can be passed. Usually,
    * used ones are SHIFT and CONTROL.
    */
    void deselectRow(int rowIndex, Keys... keys);

    /**
     * Selects all rows using CTRL + A.
     */
    void selectAllRowsWithKeyShortcut();
}