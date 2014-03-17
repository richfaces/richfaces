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
package org.richfaces.renderkit.util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;

import org.jboss.test.faces.AbstractFacesTest;
import org.richfaces.component.SwitchType;

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
