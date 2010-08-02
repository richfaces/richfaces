/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.demo;

import java.io.Serializable;
import java.util.EventListener;
import java.util.EventObject;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ajax4jsf.event.PushEventListener;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean(name = "pushBean")
@SessionScoped
public class PushBean implements Serializable {

    private static final long serialVersionUID = 4810889475400649809L;
    
    private int counter = 0;

    private transient volatile PushEventListener listener;

    public void setListener(EventListener listener) {
        this.listener = (PushEventListener) listener;
    }

    public void generateEvent() {
        listener.onEvent(new EventObject(this));
    }

    public int getCounter() {
        return counter;
    }

    public void increaseCounter() {
        counter++;
    }
}
