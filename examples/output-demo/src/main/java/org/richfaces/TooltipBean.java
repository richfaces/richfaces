/**
 * @author Pavel Yaschenko
 *
 */
package org.richfaces;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.component.Positioning;

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
