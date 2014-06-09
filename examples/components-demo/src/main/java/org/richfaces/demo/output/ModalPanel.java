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

package org.richfaces.demo.output;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.event.ItemChangeEvent;

@ManagedBean
@SessionScoped
public class ModalPanel {
    private boolean autosized;
    private boolean keepVisualState;
    private String left;
    private String top;
    private boolean rendered;
    private String shadowDepth;
    private String shadowOpacity;
    private boolean show;
    private int zindex;
    private int minHeight;
    private int minWidth;
    private int maxHeight;
    private int maxWidth;
    private int height;
    private int width;
    private boolean moveable;
    private boolean resizeable;
    private String inputTextTest;
    private String domElementAttachment;
    private Date date;

    public String getInputTextTest() {
        return inputTextTest;
    }

    public void setInputTextTest(String inputTextTest) {
        this.inputTextTest = inputTextTest;
    }

    public ModalPanel() {
        this.inputTextTest = "text";
        this.minHeight = 100;
        this.minWidth = 100;
        this.height = 300;
        this.width = 300;
        this.maxWidth = -1;
        this.maxHeight = -1;
        this.moveable = true;
        this.resizeable = true;
        this.autosized = false;
        this.keepVisualState = false;
        this.left = "auto";
        this.top = "auto";
        this.rendered = true;
        this.shadowDepth = "3";
        this.shadowOpacity = "3";
        this.show = false;
        this.domElementAttachment = "body";
        this.zindex = 123;
    }

    public int getZindex() {
        return zindex;
    }

    public void setZindex(int zindex) {
        this.zindex = zindex;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = minWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public boolean isResizeable() {
        return resizeable;
    }

    public void setResizeable(boolean resizeable) {
        this.resizeable = resizeable;
    }

    public void setAutosized(boolean autosized) {
        this.autosized = autosized;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isAutosized() {
        return autosized;
    }

    public boolean isKeepVisualState() {
        return keepVisualState;
    }

    public void setKeepVisualState(boolean keepVisualState) {
        this.keepVisualState = keepVisualState;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public boolean getRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public String getShadowDepth() {
        return shadowDepth;
    }

    public void setShadowDepth(String shadowDepth) {
        this.shadowDepth = shadowDepth;
    }

    public String getShadowOpacity() {
        return shadowOpacity;
    }

    public void setShadowOpacity(String shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getDomElementAttachment() {
        return domElementAttachment;
    }

    public void setDomElementAttachment(String domElementAttachment) {
        this.domElementAttachment = domElementAttachment;
    }

    public void itemChangeEventListener() {

    }

    public void itemChangeEventListener(ItemChangeEvent event) {
        System.out.println("item was changed from '" + event.getOldItem() + "' to '" + event.getNewItem() + "'");
    }

    public void action() {
        System.out.println("ModalPanel.action");
    }

    public Date getDate() {
        date = new Date();
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}