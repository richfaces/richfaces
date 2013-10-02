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

package org.richfaces.ui.select;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;

import javax.faces.FacesException;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
public abstract class AbstractOrderingComponent extends AbstractSelectManyComponent {
    @Attribute(defaultValue = "true", hidden = true) // TODO: unhide once javascript API's are available RF-11209
    public abstract boolean isShowButton();

    /**
     * The text to display in the move-to-top button
     */
    @Attribute
    public abstract String getUpTopText();

    /**
     * The text to display in the move-up button
     */
    @Attribute
    public abstract String getUpText();

    /**
     * The text to display in the move-down button
     */
    @Attribute
    public abstract String getDownText();

    /**
     * The text to display in the move-to-bottom button
     */
    @Attribute
    public abstract String getDownBottomText();

    /**
     * Format the button text attributes as a JSON object
     */
    public String getButtonsText() {
        JSONObject json = new JSONObject();
        try {
            json.put("first", getUpTopText()).put("up", getUpText()).put("down", getDownText()).put("last", getDownBottomText());
        } catch (JSONException e) {
            throw new FacesException("Error converting Button text values to JSON", e);
        }
        return json.length() == 0 ? null : json.toString();
    }

    protected boolean compareValues(Object previous, Object value) {
        if (previous == null && value != null) {
            return true;
        } else if (previous != null && value == null) {
            return true;
        } else if (previous == null) {
            return false;
        }

        List oldList;
        List newList;

        if (previous instanceof List) {
            oldList = (List) previous;
        } else {
            if (previous instanceof Object[]) {
                oldList = Arrays.asList(previous);
            } else {
                throw new IllegalArgumentException("Ordered List Components must be backed by a List or Array");
            }
        }

        if (value instanceof List) {
            newList = (List) value;
        } else {
            if (value instanceof Object[]) {
                newList = Arrays.asList(value);
            } else {
                throw new IllegalArgumentException("Ordered List Components must be backed by a List or Array");
            }
        }

        return !oldList.equals(newList);
    }
}