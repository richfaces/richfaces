package org.richfaces.demo;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ajax4jsf.model.DataComponentState;
import org.richfaces.demo.model.Employee;
import org.richfaces.demo.utils.EmployeeUtils;
import org.richfaces.event.SortingEvent;
import org.richfaces.model.SelectionMode;

@ManagedBean(name = "dataBean")
@SessionScoped
public class DataBean {
    DataComponentState dataTableState;
    Map<Object, Integer> stateMap = new HashMap<Object, Integer>();
    int page = 1;
    private String test1 = "test1";
    private String test2 = "test2";
    private String target = "targetId";
    private String operation = "operation";
    private boolean rendered = true;
    private boolean renderIfSinglePage = true;
    private List<Employee> employeeList;
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

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<Employee> getEmployeeList() {
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
