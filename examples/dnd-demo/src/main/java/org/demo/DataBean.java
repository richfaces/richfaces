package org.demo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class DataBean {
    private List<String> dropValues = new ArrayList<String>();
    private String dragValue1 = "dragValue 1";
    private String dragValue2 = "dragValue 2";
    private String dragValue3 = "dragValue 3";
    private String acceptedTypes = "drg1, drg2";
    private String phaseId = "none";
    private boolean immediate = false;
    private boolean bypassUpdates = false;
    private Object execute;
    private String executeTest = "none";

    public String getExecuteTest() {
        return executeTest;
    }

    public void setExecuteTest(String executeTest) {
        this.executeTest = executeTest;
    }

    public Object getExecute() {
        return execute;
    }

    public void setExecute(Object execute) {
        this.execute = execute;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public boolean isBypassUpdates() {
        return bypassUpdates;
    }

    public void setBypassUpdates(boolean bypassUpdates) {
        this.bypassUpdates = bypassUpdates;
    }

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public List<String> getDropValues() {
        return dropValues;
    }

    public String getDragValue1() {
        return dragValue1;
    }

    public void setDragValue1(String dragValue1) {
        this.dragValue1 = dragValue1;
    }

    public String getDragValue2() {
        return dragValue2;
    }

    public void setDragValue2(String dragValue2) {
        this.dragValue2 = dragValue2;
    }

    public String getDragValue3() {
        return dragValue3;
    }

    public void setDragValue3(String dragValue3) {
        this.dragValue3 = dragValue3;
    }

    public void setDropValues(List<String> dropValues) {
        this.dropValues = dropValues;
    }

    public String getAcceptedTypes() {
        return acceptedTypes;
    }

    public void setAcceptedTypes(String acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }

    public void addDropValues(String value) {
        dropValues.add(value);
    }

    public void makeAcceptedTypesNullable() {
        this.setAcceptedTypes(null);
        this.setDropValues(new ArrayList<String>());
    }

    public void resetAcceptedTypes() {
        this.setAcceptedTypes("drg1, drg2");
        this.setDropValues(new ArrayList<String>());
    }
}
