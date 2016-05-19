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

import java.io.IOException;

import javax.faces.model.SelectItem;

import org.ajax4jsf.javascript.ScriptString;
import org.ajax4jsf.javascript.ScriptUtils;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public final class ClientSelectItem implements ScriptString {
    private String clientId;
    private final String label;
    private final String convertedValue;
    private final SelectItem selectItem;

    /**
     * The initial selection status of the SelectItem
     */
    private final boolean selected;
    /**
     * The initial sort order of the SelectItem
     */
    private final Integer sortOrder;

    public ClientSelectItem(SelectItem selectItem, String convertedValue, String label, String clientId) {
        this.selectItem = selectItem;
        this.convertedValue = convertedValue;
        this.label = label;
        this.clientId = clientId;
        this.selected = false;
        this.sortOrder = 0;
    }

    public ClientSelectItem(SelectItem selectItem, String convertedValue, String label, Integer sortOrder, boolean selected) {
        this.selectItem = selectItem;
        this.label = label;
        this.convertedValue = convertedValue;
        this.selected = selected;
        this.sortOrder = sortOrder;
    }

    public ClientSelectItem(SelectItem selectItem, String convertedValue, String label, Integer sortOrder, boolean selected, String clientId) {
        this.selectItem = selectItem;
        this.clientId = clientId;
        this.label = label;
        this.convertedValue = convertedValue;
        this.selected = selected;
        this.sortOrder = sortOrder;
    }

    public SelectItem getSelectItem() {
        return selectItem;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLabel() {
        return label;
    }

    public String getConvertedValue() {
        return convertedValue;
    }

    public void appendScript(Appendable target) throws IOException {
        target.append(this.toScript());
    }

    public void appendScriptToStringBuilder(StringBuilder stringBuilder) {
        try {
            appendScript(stringBuilder);
        } catch (IOException e) {
            // ignore
        }
    }

    public String toScript() {
        return "{ \"id\" : " + ScriptUtils.toScript(clientId)
            + " , \"label\" : " + ScriptUtils.toScript(label)
            + ", \"value\" : " + ScriptUtils.toScript(convertedValue)
            + ", \"disabled\" : " + ScriptUtils.toScript(selectItem.isDisabled())
            + "}";
    }

    public boolean isSelected() {
        return selected;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

}
