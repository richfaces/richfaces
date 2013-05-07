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

package org.richfaces.demo.notify;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class NotifyBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int stayTime = 1500;
    private boolean sticky = false;
    private boolean nonblocking = false;
    private double nonblockingOpacity = 0.5;
    private boolean showShadow = false;
    private boolean showCloseButton = true;

    private int topLeftCounter = 0;
    private int bottomRightCounter = 0;

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public boolean isNonblocking() {
        return nonblocking;
    }

    public void setNonblocking(boolean nonblocking) {
        this.nonblocking = nonblocking;
    }

    public double getNonblockingOpacity() {
        return nonblockingOpacity;
    }

    public void setNonblockingOpacity(double nonblockingOpacity) {
        this.nonblockingOpacity = nonblockingOpacity;
    }

    public boolean isShowShadow() {
        return showShadow;
    }

    public void setShowShadow(boolean showShadow) {
        this.showShadow = showShadow;
    }

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public void setShowCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
    }

    public String getDetail() {
        StringBuffer detail = new StringBuffer("");
        if (sticky) {
            detail.append("<li>This message is <b>sticky</b>, it will stay here until you close it.</li>");
        } else {
            detail.append("<li>This message will <b>stay " + stayTime + "</b>.</li>");
            if (nonblocking) {
                detail.append("<li>You can click through notification since it is <b>non-blocking</b> (it's opacity is "
                        + nonblockingOpacity + ").</li>");
            }
            if (showShadow) {
                detail.append("<li>Under the notification, you can see <b>shadow</b>.</li>");
            }
            if (!showCloseButton) {
                detail.append("<li><b>Close-button</b> is not shown, you need to wait for message to disappear.</li>");
            }
        }
        return "<ul>" + detail.toString() + "</ul>";
    }

    public int getTopLeftCounter() {
        return ++topLeftCounter;
    }

    public int getBottomRightCounter() {
        return ++bottomRightCounter;
    }
}
