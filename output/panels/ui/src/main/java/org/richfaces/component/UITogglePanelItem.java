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

/**
 * @author akolonitsky
 */
public class UITogglePanelItem extends AbstractTogglePanelItem {

    private enum PropertyKeys {
        name,
        switchType
    }

    @Override
    public String getName() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.name));
    }

    public void setName(String name) {
        getStateHelper().put(PropertyKeys.name, name);
    }

    @Override
    public SwitchType getSwitchType() {
        return (SwitchType) getStateHelper().eval(PropertyKeys.switchType, getParent().getSwitchType());
    }

    public void setSwitchType(SwitchType switchType) {
        getStateHelper().put(PropertyKeys.switchType, switchType);
    }
}
