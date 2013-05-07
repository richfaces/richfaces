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
package org.richfaces.demo.core;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean(name = "dynamicExecuteBean")
@SessionScoped
public class DynamicExecuteBean implements Serializable {
    private static final long serialVersionUID = -486936947341873167L;
    private static final SelectItem[] POSSIBLE_EXECUTE_OPTIONS = new SelectItem[] { new SelectItem(null, "default"),
            new SelectItem("@none"), new SelectItem("@this"), new SelectItem("@form"), new SelectItem("formId"),
            new SelectItem("anotherFormId"), new SelectItem("@all") };
    private int actionsCounter = 0;
    private Object execute = null;
    private String inputValue;
    private String value;

    public SelectItem[] getExecuteOptions() {
        return POSSIBLE_EXECUTE_OPTIONS;
    }

    public Object getExecute() {
        return execute;
    }

    public void setExecute(Object execute) {
        this.execute = execute;
    }

    public String getValue() {
        return value;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public int getActionsCounter() {
        return actionsCounter;
    }

    public void applyValue() {
        this.actionsCounter++;
        this.value = this.inputValue;
    }
}
