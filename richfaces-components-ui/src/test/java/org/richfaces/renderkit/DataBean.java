/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBean {
    private String value = "Test String";

    private List<Data> list = new ArrayList<Data>();

    public DataBean() {
        list.add(new Data("RF-8282", "jQuery 1.4 AJAX requests broken if using Richfaces"));
        list.add(new Data("RF-6900", "scrollableDataTable: is getting broken after vertical scrolling"));
        list.add(new Data("RF-6917", "scrollableDataTable: scroll + sort + F5 actions brake down the table at richfaces-demo"));
        list.add(new Data("RF-6920", "scrollableDataTable: scroll + rerender + F5 causes to enpty selection in FF3"));
        list.add(new Data("RF-7695",
            "Error rendering extendedDataTable on tab panel when switch type is ajax. in Google Chrome"));
        list.add(new Data("RF-8072", "ExtendedDataTable : the table bugs when the navigator is resized"));
        list.add(new Data("RF-8143", "suggestionBox: submit the page when you choose a list iten in Opera"));
        list.add(new Data("RF-8490", "Editor can't be updated via ajax in Chrome"));
        list.add(new Data("RF-8521", "context menu doesn't show with rightclickselection in FF OS X"));
        list.add(new Data("RF-8536", "fileUpload does not always complete in IE8"));
        list.add(new Data("RF-2580",
            "orderingList, listShuttle, pickList: buttons sometimes don't work if mouse cursor is above some special areas of the button"));
        list.add(new Data("RF-3220",
            "InplaceInput: \"editEvent\" attribute with value \"ondblclick\" doesn't work on Opera 9.25"));
        list.add(new Data("RF-3398", "InplaceSelect: Broken under Opera."));
        list.add(new Data("RF-3504", "InputNumberSpinner: Doesn't work after reRender in Opera."));
        list.add(new Data("RF-3704", "InplaceInput component: part of defaultLabel displays in the page (only for IE6)"));
        list.add(new Data("RF-5285", "PickList: onlistchanged, onkeypress, onblur events do not work in some browsers."));
        list.add(new Data("RF-6926", "Tree: rightClickSelection attribute does not work in Safari and Opera."));
        list.add(new Data("RF-6967", "InputNumberSpinner: disableBrowserAutoComplete works wrong"));
        list.add(new Data("RF-7063", "Editor: Use Seam Text mode is work incorrectly in safari and google chrome"));
        list.add(new Data("RF-7100", "colorPicker: input and color box are missaligned"));
        list.add(new Data("RF-7162", "realworld/images/Opera: Unordered list and Ordered list icons work incorrectly"));
        list.add(new Data("RF-7169", "scrollableDataTable: first column disappears after crolling"));
        list.add(new Data("RF-7187", "InplaceInput attribute selectOnEdit not worked in Safari/Chrome2"));
        list.add(new Data("RF-7663",
            "scrollableDataTable: onRowDblClick and onRowClick events is triggered once after sorting in safari"));
        list.add(new Data("RF-7664", "colorPicker is opened up with flat=\"true\" in ie 7"));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Data> getList() {
        return list;
    }

    public List<Data> getEmptyList() {
        return Collections.emptyList();
    }
}
