/**
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
 **/
package org.richfaces.component;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class TogglePanelVisitState {
    private String name;
    private int count;
    private AbstractTogglePanelItemInterface item;
    private boolean dynamic;
    private String clientId;

    public TogglePanelVisitState() {
        count = 0;
    }

    public void setState(String name, AbstractTogglePanelItemInterface item) {
        this.name = name;
        this.item = item;
        if (item != null) {
            this.clientId = item.getClientId();
    }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int increment() {
        return ++count;
    }

    public AbstractTogglePanelItemInterface getItem() {
        return item;
    }

    public void setItem(AbstractTogglePanelItemInterface item) {
        this.item = item;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
}
