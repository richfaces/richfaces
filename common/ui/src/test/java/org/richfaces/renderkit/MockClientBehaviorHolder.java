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
package org.richfaces.renderkit;

import static org.jboss.test.faces.mock.FacesMockController.invokeCurrent;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.easymock.IMocksControl;
import org.jboss.test.faces.mock.component.MockUIComponent;

public class MockClientBehaviorHolder extends MockUIComponent implements ClientBehaviorHolder {
    public MockClientBehaviorHolder(IMocksControl control, String name) {
        super(control, name);
    }

    public void addClientBehavior(String eventName, ClientBehavior behavior) {
        invokeCurrent(this, eventName, behavior);
    }

    public Map<String, List<ClientBehavior>> getClientBehaviors() {
        return invokeCurrent(this);
    }

    public String getDefaultEventName() {
        return invokeCurrent(this);
    }

    public Collection<String> getEventNames() {
        return invokeCurrent(this);
    }
}
