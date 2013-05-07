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

package org.richfaces.validator;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.convert.BooleanConverter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.IntegerConverter;
import javax.faces.render.ClientBehaviorRenderer;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Environment.Feature;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockController;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockTestRunner.class)
public class FacesConverterServiceTest {
    @Mock()
    @Environment({ Feature.APPLICATION })
    protected MockFacesEnvironment environment;
    protected FacesConverterService serviceImpl;
    @Mock
    protected UIViewRoot viewRoot;
    @Mock
    protected UIInput input;
    @Mock
    protected ClientBehaviorRenderer behaviorRenderer;
    protected MockController controller;
    protected Converter converter;

    @Before
    public void setUp() {
        // create service impl.
        serviceImpl = new ConverterServiceImpl();
        expect(environment.getFacesContext().getViewRoot()).andStubReturn(viewRoot);
        expect(viewRoot.getLocale()).andStubReturn(Locale.ENGLISH);
        expect(environment.getApplication().getMessageBundle()).andStubReturn("javax.faces.Messages");
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("label", "foo");
        expect(input.getAttributes()).andStubReturn(attributes);
    }

    @After
    public void tearDown() {
        controller.verify();
        serviceImpl = null;
        controller.release();
    }

    @Test
    public void getConverterClass() throws Exception {
        converter = new BooleanConverter();
        controller.replay();
        ConverterDescriptor converterDescription = serviceImpl.getConverterDescription(environment.getFacesContext(), input,
            converter, null);
        assertEquals(converter.getClass(), converterDescription.getImplementationClass());
    }

    @Test
    public void getConverterMessage() throws Exception {
        converter = new IntegerConverter();
        FacesMessage facesMessage = null;
        controller.replay();
        try {
            converter.getAsObject(environment.getFacesContext(), input, "abc");
        } catch (ConverterException e) {
            facesMessage = e.getFacesMessage();
        }
        assertNotNull(facesMessage);
        ConverterDescriptor converterDescription = serviceImpl.getConverterDescription(environment.getFacesContext(), input,
            converter, null);
        String summary = converterDescription.getMessage().getSummary();
        summary = summary.replace("{2}", "foo");
        summary = summary.replace("'{0}'", "abc");
        assertEquals(facesMessage.getSummary(), summary);
    }

    @Test
    public void getConverterParameters() throws Exception {
        DateTimeConverter converter = new DateTimeConverter();

        converter.setDateStyle("short");
        converter.setPattern("MM/DD/YYYY");
        converter.setTimeStyle("full");
        converter.setType("both");
        converter.setTimeZone(TimeZone.getTimeZone("EST"));
        controller.replay();
        ConverterDescriptor converterDescription = serviceImpl.getConverterDescription(environment.getFacesContext(), input,
            converter, null);
        Map<String, ? extends Object> additionalParameters = converterDescription.getAdditionalParameters();
        assertEquals("short", additionalParameters.get("dateStyle"));
        assertEquals("MM/DD/YYYY", additionalParameters.get("pattern"));
    }
}
