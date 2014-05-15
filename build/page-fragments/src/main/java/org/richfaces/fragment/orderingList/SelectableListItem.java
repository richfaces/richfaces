/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.fragment.orderingList;

import org.richfaces.fragment.list.ListItem;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface SelectableListItem extends ListItem {

    /**
     * Finds out whether this item is selected.
     *
     * @return <code>true</code> if this item is selected, <code>false</code> otherwise
     */
    boolean isSelected();

    /**
     * Selects item without deselecting others.
     */
    void select();

    /**
     * Select item and deselect other items depending on @deselectOthers.
     *
     * @param deselectOthers if <code>true</code> other items will be unselected.
     */
    void select(boolean deselectOthers);

    /**
     * Deselects this item.
     */
    void deselect();
}
