/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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


package org.richfaces.component;

import org.richfaces.TooltipLayout;
import org.richfaces.TooltipMode;

/**
 * @author amarkhel
 * @since 2010-10-24
 */
public class UITooltip extends AbstractTooltip {

    public enum PropertyKeys {
        target,
        value,
        layout,
        attached,
        direction,
        disabled,
        followMouse,
        hideDelay,
        hideEvent,
        horizontalOffset,
        mode,
        showDelay,
        showEvent,
        verticalOffset,
        bypassUpdates,
        limitRender,
        data,
        status,
        execute,
        render
    }

    public String getTarget() {
        return (String) getStateHelper().eval(PropertyKeys.target, getParent().getClientId());
    }

    public void setTarget(String target) {
        getStateHelper().put(PropertyKeys.target, target);
    }

    public String getValue() {
        return (String) getStateHelper().eval(PropertyKeys.value);
    }

    public void setValue(String value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public TooltipLayout getLayout() {
        return (TooltipLayout) getStateHelper().eval(PropertyKeys.layout, TooltipLayout.DEFAULT);
    }

    public void setLayout(TooltipLayout layout) {
        getStateHelper().put(PropertyKeys.layout, layout);
    }

    public boolean isAttached() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.attached, true)));
    }

    public void setAttached(boolean attached) {
        getStateHelper().put(PropertyKeys.attached, attached);
    }

    public TooltipDirection getDirection() {
        return (TooltipDirection) getStateHelper().eval(PropertyKeys.direction, TooltipDirection.DEFAULT);
    }

    public void setDirection(TooltipDirection direction) {
        getStateHelper().put(PropertyKeys.direction, direction);
    }

    public boolean isDisabled() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.disabled)));
    }

    public void setDisabled(boolean disabled) {
        getStateHelper().put(PropertyKeys.disabled, disabled);
    }

    public boolean isFollowMouse() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.followMouse, true)));
    }

    public void setFollowMouse(boolean followMouse) {
        getStateHelper().put(PropertyKeys.followMouse, followMouse);
    }

    public int getHideDelay() {
        return (Integer) getStateHelper().eval(PropertyKeys.hideDelay, 0);
    }

    public void setHideDelay(int hideDelay) {
        getStateHelper().put(PropertyKeys.hideDelay, hideDelay);
    }

    public String getHideEvent() {
        return (String) getStateHelper().eval(PropertyKeys.hideEvent, "mouseleave");
    }

    public void setHideEvent(String hideEvent) {
        getStateHelper().put(PropertyKeys.hideEvent, hideEvent);
    }

    public int getHorizontalOffset() {
        return (Integer) getStateHelper().eval(PropertyKeys.horizontalOffset, 10);
    }

    public void setHorizontalOffset(int horizontalOffset) {
        getStateHelper().put(PropertyKeys.horizontalOffset, horizontalOffset);
    }

    public TooltipMode getMode() {
        return (TooltipMode) getStateHelper().eval(PropertyKeys.mode, TooltipMode.DEFAULT);
    }

    public void setMode(TooltipMode mode) {
        getStateHelper().put(PropertyKeys.mode, mode);
    }

    public int getShowDelay() {
        return (Integer) getStateHelper().eval(PropertyKeys.showDelay, 0);
    }

    public void setShowDelay(int showDelay) {
        getStateHelper().put(PropertyKeys.showDelay, showDelay);
    }

    public String getShowEvent() {
        return (String) getStateHelper().eval(PropertyKeys.showEvent, "mouseenter");
    }

    public void setShowEvent(String showEvent) {
        getStateHelper().put(PropertyKeys.showEvent, showEvent);
    }

    public int getVerticalOffset() {
        return (Integer) getStateHelper().eval(PropertyKeys.verticalOffset, 10);
    }

    public void setVerticalOffset(int verticalOffset) {
        getStateHelper().put(PropertyKeys.verticalOffset, verticalOffset);
    }

    public boolean isBypassUpdates() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.bypassUpdates)));
    }

    public void setBypassUpdates(boolean bypassUpdates) {
        getStateHelper().put(PropertyKeys.bypassUpdates, bypassUpdates);
    }

    public boolean isLimitRender() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.limitRender)));
    }

    public void setLimitRender(boolean limitRender) {
        getStateHelper().put(PropertyKeys.limitRender, limitRender);
    }

    public Object getData() {
        return getStateHelper().eval(PropertyKeys.data);
    }

    public void setData(Object data) {
        getStateHelper().put(PropertyKeys.data, data);
    }

    public String getStatus() {
        return (String) getStateHelper().eval(PropertyKeys.status);
    }

    public void setStatus(String status) {
        getStateHelper().put(PropertyKeys.status, status);
    }

    public Object getExecute() {
        return getStateHelper().eval(PropertyKeys.execute);
    }

    public void setExecute(Object execute) {
        getStateHelper().put(PropertyKeys.execute, execute);
    }

    public Object getRender() {
        return getStateHelper().eval(PropertyKeys.render);
    }

    public void setRender(Object render) {
        getStateHelper().put(PropertyKeys.render, render);
    }


}
