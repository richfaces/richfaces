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
 * @author Pavel Yaschenko
 *
 */
package org.richfaces.demo.output;

import org.richfaces.component.Positioning;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class TooltipBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2860886265782541618L;
    private int tooltipCounter = 0;
    private Positioning jointPoint = Positioning.DEFAULT;
    private Positioning direction = Positioning.DEFAULT;
    private int horizontalOffset = 0;
    private int verticalOffset = 0;
    private Positioning[] positioningValues = Positioning.values();

    public int getTooltipCounter() {
        return tooltipCounter++;
    }

    public Date getTooltipDate() {
        return new Date();
    }

    public void setJointPoint(Positioning jointPoint) {
        this.jointPoint = jointPoint;
    }

    public Positioning getJointPoint() {
        return jointPoint;
    }

    public void setDirection(Positioning direction) {
        this.direction = direction;
    }

    public Positioning getDirection() {
        return direction;
    }

    public void setHorizontalOffset(int horizontalOffset) {
        this.horizontalOffset = horizontalOffset;
    }

    public int getHorizontalOffset() {
        return horizontalOffset;
    }

    public void setVerticalOffset(int verticalOffset) {
        this.verticalOffset = verticalOffset;
    }

    public int getVerticalOffset() {
        return verticalOffset;
    }

    public Positioning[] getPositioningValues() {
        return positioningValues;
    }
}
