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
package org.ajax4jsf.component;

import java.io.Serializable;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.IntegerConverter;
import javax.faces.event.ActionEvent;
import javax.faces.validator.Validator;

import org.ajax4jsf.component.ActionListenerTest.Bean;
import org.ajax4jsf.event.AjaxEvent;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.richfaces.event.DataScrollerEvent;
import org.richfaces.event.DataScrollerListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class UIAjaxFormTest extends AbstractAjax4JsfTestCase {
    private UIAjaxForm ajaxForm;
    private UIInput child;
    private int childInvoked;
    private boolean result;

    public UIAjaxFormTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        ajaxForm = (UIAjaxForm) application.createComponent(UIAjaxForm.COMPONENT_TYPE);
        ajaxForm.setId("form");
        child = new UIInput() {
            public void processDecodes(FacesContext context) {
                childInvoked++;
                super.processDecodes(context);
            }
        };
        child.setId("input");
        child.addValidator(new TestAjaxFormValidator());
        childInvoked = 0;
        child.setId("child");
        ajaxForm.getChildren().add(child);
        facesContext.getViewRoot().getChildren().add(ajaxForm);
    }

    public void testTest() throws Exception {
        HtmlPage page = renderView();

        // System.out.println(page.asXml());
    }

    public void testProcessDecodes() throws Exception {
        facesContext.getExternalContext().getRequestParameterMap().put("form", "form");
        ajaxForm.processDecodes(facesContext);
        assertEquals(1, childInvoked);

        // test
    }

    public void testProcessValidators() throws Exception {
        result = false;
        ajaxForm.setSubmitted(true);
        child.setSubmittedValue(new String("test0"));
        ajaxForm.processValidators(facesContext);
        assertTrue(result);
    }

    // TODO processUpdates test
    public void testProcessUpdates() throws Exception {
        facesContext.getExternalContext().getRequestParameterMap().put("form", "form");
        child.setSubmittedValue(new String("test1"));
        ajaxForm.processDecodes(facesContext);
        ajaxForm.processValidators(facesContext);
        ajaxForm.processUpdates(facesContext);
    }

    public void testBroadcast() throws Exception {
        AjaxEvent event = new AjaxEvent(ajaxForm);

        try {
            ajaxForm.broadcast(event);
        } catch (Exception e) {
            fail();
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
        ajaxForm = null;
        child = null;
    }

    class TestAjaxFormValidator implements Validator {
        public void validate(javax.faces.context.FacesContext context, UIComponent component, Object value) {
            result = true;
        }
    }
}
