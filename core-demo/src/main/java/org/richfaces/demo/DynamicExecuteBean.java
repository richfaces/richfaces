/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.demo;

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

    private static final SelectItem[] POSSIBLE_EXECUTE_OPTIONS = new SelectItem[] {
        new SelectItem(null, "default"), new SelectItem("@none"), new SelectItem("@this"), new SelectItem("@form"),
        new SelectItem("formId"), new SelectItem("anotherFormId"), new SelectItem("@all")
    };
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
