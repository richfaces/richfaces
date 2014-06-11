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

package org.richfaces.demo.iteration;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.demo.iteration.model.Employee;
import org.richfaces.demo.iteration.utils.EmployeeUtils;
import org.ajax4jsf.model.DataComponentState;
import org.richfaces.model.SelectionMode;
import org.richfaces.event.SortingEvent;

@ManagedBean
@SessionScoped
public class IterationBean {
    DataComponentState dataTableState;
    Map<Object, Integer> stateMap = new HashMap<Object, Integer>();
    int page = 1;
    private String test1 = "test1";
    private String test2 = "test2";
    private String target = "targetId";
    private String operation = "operation";
    private boolean rendered = true;
    private boolean renderIfSinglePage = true;
    private Collection<Employee> employeeList;
    private boolean state = true;
    private SelectionMode selectionMode = SelectionMode.multiple;
    private Collection<Object> selectedRowKeys;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Map<Object, Integer> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<Object, Integer> stateMap) {
        this.stateMap = stateMap;
    }

    public DataComponentState getDataTableState() {
        return dataTableState;
    }

    public void setDataTableState(DataComponentState dataTableState) {
        this.dataTableState = dataTableState;
    }

    public void setEmployeeList(Collection<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Collection<Employee> getEmployeeList() {
        if (employeeList == null) {
            employeeList = EmployeeUtils.obtainDefaultEmployeeList();
        }
        return employeeList;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void sortingListener(SortingEvent event) {
        System.out.println(event.getSortOrder());
    }

    public Date getDate() {
        return new Date();
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public SelectionMode[] getSelectionModes() {
        return SelectionMode.values();
    }

    public void setSelectedRowKeys(Collection<Object> selectedRowKeys) {
        this.selectedRowKeys = selectedRowKeys;
    }

    public Collection<Object> getSelectedRowKeys() {
        return selectedRowKeys;
    }

    public boolean isRenderIfSinglePage() {
        return renderIfSinglePage;
    }

    public void setRenderIfSinglePage(boolean renderIfSinglePage) {
        this.renderIfSinglePage = renderIfSinglePage;
    }
}
