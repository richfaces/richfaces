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

/**
 *
 */
package org.richfaces.demo.output;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author Ilya Shaikovsky
 *
 */
@ManagedBean
@ViewScoped
public class ProgressBarBean implements Serializable {
    private static final long serialVersionUID = -446286889238296278L;
    private int minValue = 0;
    private int maxValue = 100;
    private int value = 50;
    private String label = "'label' attribute";
    private boolean childrenRendered = false;
    private boolean enabled = false;
    private boolean initialFacetRendered = true;
    private boolean finishFacetRendered = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChildrenRendered() {
        return childrenRendered;
    }

    public void setChildrenRendered(boolean childrenRendered) {
        this.childrenRendered = childrenRendered;
    }

    public boolean isInitialFacetRendered() {
        return initialFacetRendered;
    }

    public void setInitialFacetRendered(boolean renderInitialFacet) {
        this.initialFacetRendered = renderInitialFacet;
    }

    public boolean isFinishFacetRendered() {
        return finishFacetRendered;
    }

    public void setFinishFacetRendered(boolean renderFinishFacet) {
        this.finishFacetRendered = renderFinishFacet;
    }

    public void decreaseValueByFive() {
        value -= 5;
    }

    public void increaseValueByFive() {
        value += 5;
    }

    public String getCurrentTimeAsString() {
        return DateFormat.getTimeInstance().format(new Date());
    }
}