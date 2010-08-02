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
package org.richfaces.demo;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean
@SessionScoped
public class RegionBean implements Serializable {

    private static final long serialVersionUID = -6371272297263012554L;

    private static final SelectItem[] AVAILABLE_EXECUTE_OPTIONS = new SelectItem[] {
        new SelectItem(null, "default"), new SelectItem("@region"), new SelectItem("@all"), new SelectItem("@this")
    };

    private String execute = null;

    private String nestedExecute = null;

    private String outerExecute = null;

    private String outerValue;

    private String regionValue;

    private String nestedRegionValue;

    private String lastExecutedLinkValue;
    
    public SelectItem[] getExecuteOptions() {
        return AVAILABLE_EXECUTE_OPTIONS;
    }

    public String getOuterValue() {
        return outerValue;
    }

    public void setOuterValue(String outerValue) {
        this.outerValue = outerValue;
    }

    public String getRegionValue() {
        return regionValue;
    }

    public void setRegionValue(String value) {
        this.regionValue = value;
    }

    public String getNestedRegionValue() {
        return nestedRegionValue;
    }

    public void setNestedRegionValue(String nestedValue) {
        this.nestedRegionValue = nestedValue;
    }

    public String getExecute() {
        return execute;
    }

    public void setExecute(String execute) {
        this.execute = execute;
    }

    public String getNestedExecute() {
        return nestedExecute;
    }

    public void setNestedExecute(String nestedExecute) {
        this.nestedExecute = nestedExecute;
    }

    public String getOuterExecute() {
        return outerExecute;
    }

    public void setOuterExecute(String outerExecute) {
        this.outerExecute = outerExecute;
    }

    public void handleBehavior(AjaxBehaviorEvent event) {
        lastExecutedLinkValue = (String) event.getComponent().getAttributes().get("value");
    }
    
    public String getLastExecutedLinkValue() {
        return lastExecutedLinkValue;
    }
    
    public void handleDefaultsValueChange(ValueChangeEvent event) {
        String newValue = (String) event.getNewValue();
        
        setOuterExecute(newValue);
        setExecute(newValue);
        setNestedExecute(newValue);
    }
}
