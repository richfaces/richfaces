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
package org.richfaces.demo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.component.LogMode;

/**
 * @author Nick Belaevski
 * 
 */
@ManagedBean
@SessionScoped
public class LogBean {

    private static final LogMode[] LOG_MODES = new LogMode[] {
        LogMode.inline, LogMode.popup
    };
    
    private LogMode mode = LogMode.inline;

    private Character hotkey = 'l';
    
    public LogMode getMode() {
        return mode;
    }
    
    public void setMode(LogMode logMode) {
        this.mode = logMode;
    }

    public Character getHotkey() {
        return hotkey;
    }

    public void setHotkey(Character hotkey) {
        this.hotkey = hotkey;
    }
    
    public LogMode[] getLogModes() {
        return LOG_MODES;
    }
}
