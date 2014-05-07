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
package org.richfaces.application;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;

import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.l10n.BundleLoader;
import org.richfaces.l10n.MessageBundle;

/**
 * @author Nick Belaevski
 *
 */
public class MessageFactoryImplementationTest {
    @MessageBundle(baseName = "org.richfaces.application.Messages")
    public enum Messages {

        UIINPUT_CONVERSION("javax.faces.component.UIInput.CONVERSION"),
        ENUM_CONVERTER_ENUM("javax.faces.converter.EnumConverter.ENUM"),
        CONVERTER_STRING("javax.faces.converter.STRING");
        private String key;

        private Messages(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }

    private MockFacesEnvironment facesEnvironment;
    private MessageFactory messageFactory;

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.US);

        facesEnvironment = MockFacesEnvironment.createEnvironment().withApplication();

        messageFactory = new MessageFactoryImpl(new BundleLoader());
    }

    @After
    public void tearDown() throws Exception {
        facesEnvironment.verify();
        facesEnvironment.release();
        facesEnvironment = null;

        messageFactory = null;
    }

    @Test
    public void testCreateMessageFromFacesBundle() throws Exception {
        expect(facesEnvironment.getFacesContext().getViewRoot()).andStubReturn(null);
        expect(facesEnvironment.getApplication().getMessageBundle()).andStubReturn(null);
        facesEnvironment.replay();

        // {1}: Could not convert ''{0}'' to a string.
        FacesMessage stringConverterMessage = messageFactory.createMessage(facesEnvironment.getFacesContext(),
            Messages.CONVERTER_STRING, "something", "Message");

        assertNotNull(stringConverterMessage);
        assertEquals(FacesMessage.SEVERITY_INFO, stringConverterMessage.getSeverity());
        assertEquals("Message: Could not convert 'something' to a string.", stringConverterMessage.getSummary());
        assertEquals(stringConverterMessage.getSummary(), stringConverterMessage.getDetail());

        // javax.faces.converter.EnumConverter.ENUM={2}: ''{0}'' must be convertible to an enum.
        // javax.faces.converter.EnumConverter.ENUM_detail={2}: ''{0}'' must be convertible to an enum from the enum that
        // contains the constant ''{1}''.
        FacesMessage longConverterMessage = messageFactory.createMessage(facesEnvironment.getFacesContext(),
            FacesMessage.SEVERITY_ERROR, Messages.ENUM_CONVERTER_ENUM, "field", "anotherField", "Failed");
        assertNotNull(longConverterMessage);
        assertEquals(FacesMessage.SEVERITY_ERROR, longConverterMessage.getSeverity());
        assertEquals("Failed: 'field' must be convertible to an enum.", longConverterMessage.getSummary());
        assertEquals("Failed: 'field' must be convertible to an enum from the enum that contains the constant 'anotherField'.",
            longConverterMessage.getDetail());
    }

    @Test
    public void testCreateMessageFromApplicationBundle() throws Exception {
        UIViewRoot mockViewRoot = facesEnvironment.createMock(UIViewRoot.class);
        expect(mockViewRoot.getLocale()).andStubReturn(new Locale("ru", "RU"));
        expect(facesEnvironment.getFacesContext().getViewRoot()).andStubReturn(mockViewRoot);
        expect(facesEnvironment.getApplication().getMessageBundle()).andStubReturn(
            "org.richfaces.application.MessageFactoryImplTest");
        facesEnvironment.replay();

        // {1}: ''{0}'' ne konvertiruyetsia v stroku.
        FacesMessage stringConverterMessage = messageFactory.createMessage(facesEnvironment.getFacesContext(),
            Messages.CONVERTER_STRING, "something", "Message");

        assertNotNull(stringConverterMessage);
        assertEquals(FacesMessage.SEVERITY_INFO, stringConverterMessage.getSeverity());
        assertEquals("Message: 'something' ne konvertiruyetsia v stroku.", stringConverterMessage.getSummary());
        assertEquals(stringConverterMessage.getSummary(), stringConverterMessage.getDetail());

        // javax.faces.converter.EnumConverter.ENUM={2}: ''{0}'' dolzhno konvertirovat''sia v enum.
        // javax.faces.converter.EnumConverter.ENUM_detail={2}: ''{0}'' dolzhno konvertirovat''sia v enum iz enum s konstantoj
        // ''{1}''.
        FacesMessage longConverterMessage = messageFactory.createMessage(facesEnvironment.getFacesContext(),
            FacesMessage.SEVERITY_ERROR, Messages.ENUM_CONVERTER_ENUM, "field", "anotherField", "Failed");
        assertNotNull(longConverterMessage);
        assertEquals(FacesMessage.SEVERITY_ERROR, longConverterMessage.getSeverity());
        assertEquals("Failed: 'field' dolzhno konvertirovat'sia v enum.", longConverterMessage.getSummary());
        assertEquals("Failed: 'field' dolzhno konvertirovat'sia v enum iz enum s konstantoj 'anotherField'.",
            longConverterMessage.getDetail());

        // javax.faces.component.UIInput.CONVERSION={0}: Conversion error occurred.
        FacesMessage inputConversionMessage = messageFactory.createMessage(facesEnvironment.getFacesContext(),
            Messages.UIINPUT_CONVERSION, "Failure message");
        assertNotNull(inputConversionMessage);
        assertEquals(FacesMessage.SEVERITY_INFO, inputConversionMessage.getSeverity());
        assertEquals("Failure message: Conversion error occurred.", inputConversionMessage.getSummary());
    }
}
