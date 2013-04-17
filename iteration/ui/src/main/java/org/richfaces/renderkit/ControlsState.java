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
package org.richfaces.renderkit;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 *
 */
public class ControlsState {
    private boolean firstRendered = true;
    private boolean firstEnabled = true;
    private boolean lastRendered = true;
    private boolean lastEnabled = true;
    private boolean nextRendered = true;
    private boolean nextEnabled = true;
    private boolean previousRendered = true;
    private boolean previousEnabled = true;
    private boolean fastRewindRendered = true;
    private boolean fastRewindEnabled = true;
    private boolean fastForwardRendered = true;
    private boolean fastForwardEnabled = true;
    private boolean controlsSeparatorRendered = false;

    public boolean getFirstRendered() {
        return firstRendered;
    }

    public void setFirstRendered(boolean firstRendered) {
        this.firstRendered = firstRendered;
    }

    public boolean getFirstEnabled() {
        return firstEnabled;
    }

    public void setFirstEnabled(boolean firstEnabled) {
        this.firstEnabled = firstEnabled;
    }

    public boolean getLastRendered() {
        return lastRendered;
    }

    public void setLastRendered(boolean lastRendered) {
        this.lastRendered = lastRendered;
    }

    public boolean getLastEnabled() {
        return lastEnabled;
    }

    public void setLastEnabled(boolean lastEnabled) {
        this.lastEnabled = lastEnabled;
    }

    public boolean getFastRewindRendered() {
        return fastRewindRendered;
    }

    public void setFastRewindRendered(boolean fastRewindRendered) {
        this.fastRewindRendered = fastRewindRendered;
    }

    public boolean getFastRewindEnabled() {
        return fastRewindEnabled;
    }

    public void setFastRewindEnabled(boolean fastRewindEnabled) {
        this.fastRewindEnabled = fastRewindEnabled;
    }

    public boolean getFastForwardRendered() {
        return fastForwardRendered;
    }

    public void setFastForwardRendered(boolean fastForwardRendered) {
        this.fastForwardRendered = fastForwardRendered;
    }

    public boolean getFastForwardEnabled() {
        return fastForwardEnabled;
    }

    public void setFastForwardEnabled(boolean fastForwardEnabled) {
        this.fastForwardEnabled = fastForwardEnabled;
    }

    public boolean getNextRendered() {
        return nextRendered;
    }

    public void setNextRendered(boolean nextRendered) {
        this.nextRendered = nextRendered;
    }

    public boolean getNextEnabled() {
        return nextEnabled;
    }

    public void setNextEnabled(boolean nextEnabled) {
        this.nextEnabled = nextEnabled;
    }

    public boolean getPreviousRendered() {
        return previousRendered;
    }

    public void setPreviousRendered(boolean previousRendered) {
        this.previousRendered = previousRendered;
    }

    public boolean getPreviousEnabled() {
        return previousEnabled;
    }

    public void setPreviousEnabled(boolean previousEnabled) {
        this.previousEnabled = previousEnabled;
    }

    public boolean getControlsSeparatorRendered() {
        return controlsSeparatorRendered;
    }

    public void setControlsSeparatorRendered(boolean controlsSeparatorRendered) {
        this.controlsSeparatorRendered = controlsSeparatorRendered;
    }
}
