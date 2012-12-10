/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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
package org.richfaces.photoalbum.ui;

import javax.enterprise.event.Observes;
import javax.inject.Named;

import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.SimpleEvent;

/**
 * Convenience UI class for rich:editor component
 *
 * @author Andrey Markhel
 */

@Named
public class EditorBean {

    private String currentConfiguration = "/org/richfaces/photoalbum/editor/advanced";

    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public void setMessage(@Observes @EventType(Events.CLEAR_EDITOR_EVENT) SimpleEvent se) {
//        this.message = se.getMessage();
//    }

    public void clearMessage(@Observes @EventType(Events.CLEAR_EDITOR_EVENT) SimpleEvent se) {
        this.message = "";
    }

    public String getCurrentConfiguration() {
        return currentConfiguration;
    }
}