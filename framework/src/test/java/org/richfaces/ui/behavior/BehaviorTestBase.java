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

package org.richfaces.ui.behavior;

import static org.easymock.EasyMock.expect;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.render.ClientBehaviorRenderer;

import org.jboss.test.faces.mock.Mock;
import org.junit.Before;
import org.richfaces.ValidatorTestBase;
import org.richfaces.ui.validation.validator.ClientValidatorBehavior;
import org.richfaces.ui.validation.validator.ClientValidatorImpl;

public class BehaviorTestBase extends ValidatorTestBase {
    @Mock
    protected UIInput input;
    @Mock
    protected ClientBehaviorContext behaviorContext;
    @Mock
    protected ClientBehaviorRenderer behaviorRenderer;
    protected ClientValidatorBehavior behavior;

    public BehaviorTestBase() {
        super();
    }

    @Before
    public void setUp() {
        behavior = createBehavior();
    }

    protected ClientBehaviorContext setupBehaviorContext(UIComponent component) {
        expect(behaviorContext.getComponent()).andStubReturn(component);
        expect(behaviorContext.getFacesContext()).andStubReturn(environment.getFacesContext());
        return behaviorContext;
    }

    protected ClientValidatorBehavior createBehavior() {
        return new ClientValidatorImpl();
    }
}