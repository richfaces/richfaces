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
package org.richfaces.ui.util.renderkit;

import org.jboss.test.faces.AbstractFacesTest;
import org.richfaces.ui.common.SwitchType;
import org.richfaces.util.EnclosingFormRequiredException;
import org.richfaces.util.FormUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com created 11.04.2007
 *
 */
public class FormUtilTest extends AbstractFacesTest {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testThrowEnclFormReqExceptionIfNeed() throws Exception {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        UIComponent form = application.createComponent(UIForm.COMPONENT_TYPE);

        viewRoot.getChildren().add(form);

        UIComponent testComponent = application.createComponent(UIOutput.COMPONENT_TYPE);

        form.getChildren().add(testComponent);
        FormUtil.throwEnclFormReqExceptionIfNeed(facesContext, testComponent);
    }

    public void testThrowEnclFormReqExceptionIfNeedNoForm() throws Exception {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        UIComponent testComponent = application.createComponent(UIOutput.COMPONENT_TYPE);

        viewRoot.getChildren().add(testComponent);

        try {
            FormUtil.throwEnclFormReqExceptionIfNeed(facesContext, testComponent);
            fail();
        } catch (EnclosingFormRequiredException e) {
        }
    }

    public void testThrowEnclFormReqExceptionIfNeedClient() throws Exception {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        UIComponent testComponent = application.createComponent(UIOutput.COMPONENT_TYPE);

        testComponent.getAttributes().put("switchType", SwitchType.client);
        viewRoot.getChildren().add(testComponent);
        FormUtil.throwEnclFormReqExceptionIfNeed(facesContext, testComponent);
    }
}
