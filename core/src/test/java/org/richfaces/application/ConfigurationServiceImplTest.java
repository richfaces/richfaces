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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.richfaces.application.configuration.ConfigurationServiceHelper.getBooleanConfigurationValue;
import static org.richfaces.application.configuration.ConfigurationServiceHelper.getConfigurationValue;
import static org.richfaces.application.configuration.ConfigurationServiceHelper.getEnumConfigurationValue;
import static org.richfaces.application.configuration.ConfigurationServiceHelper.getIntConfigurationValue;
import static org.richfaces.application.configuration.ConfigurationServiceHelper.getLongConfigurationValue;
import static org.richfaces.application.configuration.ConfigurationServiceHelper.getStringConfigurationValue;

import javax.faces.context.FacesContext;

import org.junit.Rule;
import org.junit.Test;
import org.richfaces.ContextInitParameter;
import org.richfaces.ContextInitParameters;
import org.richfaces.FacesRequestSetupRule;

/**
 * @author Nick Belaevski
 *
 */
public class ConfigurationServiceImplTest {
    @Rule
    public FacesRequestSetupRule rule = new FacesRequestSetupRule();

    @Test
    @ContextInitParameters({ @ContextInitParameter(name = "org.richfaces.LongValue", value = "223372036854775807"),
            @ContextInitParameter(name = "org.richfaces.IntValue", value = "32768"),
            @ContextInitParameter(name = "org.richfaces.StringValue", value = "some string"),
            @ContextInitParameter(name = "org.richfaces.BooleanValue", value = "true"),
            @ContextInitParameter(name = "org.richfaces.EnumValue", value = "bar") })
    public void testLiteralValues() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        assertEquals(223372036854775807L, getLongConfigurationValue(context, Configuration.longValue).longValue());
        assertEquals(32768, getIntConfigurationValue(context, Configuration.intValue).intValue());
        assertEquals("some string", getStringConfigurationValue(context, Configuration.stringValue));
        assertEquals(true, getBooleanConfigurationValue(context, Configuration.booleanValue).booleanValue());
        assertEquals(Enumeration.bar, getEnumConfigurationValue(context, Configuration.enumValue, Enumeration.class));
    }

    @Test
    public void testDefaultValues() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        assertEquals(-100L, getLongConfigurationValue(context, Configuration.longValueWithDefault).longValue());
        assertEquals(-1, getIntConfigurationValue(context, Configuration.intValueWithDefault).intValue());
        assertEquals("default name", getStringConfigurationValue(context, Configuration.stringValueWithDefault));
        assertEquals(false, getBooleanConfigurationValue(context, Configuration.booleanValueWithDefault).booleanValue());
        assertEquals(Enumeration.foo, getEnumConfigurationValue(context, Configuration.enumValueWithDefault, Enumeration.class));
    }

    @Test
    @ContextInitParameters({ @ContextInitParameter(name = "org.richfaces.MultiValue2", value = "test value") })
    public void testMultiValues() throws Exception {
        assertEquals("test value", getStringConfigurationValue(FacesContext.getCurrentInstance(), Configuration.multiValue));
    }

    @Test
    @ContextInitParameters({
            @ContextInitParameter(name = "org.richfaces.FacesContextReference", value = "#{facesContext}"),
            @ContextInitParameter(name = "org.richfaces.DynamicValueWithDefault", value = "#{facesContext.attributes['dummyValue']}") })
    public void testDynamicValues() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        assertSame(context, getConfigurationValue(context, Configuration.facesContext));
        assertEquals("<something>", getConfigurationValue(context, Configuration.dynamicValueWithDefault));
    }

    @Test
    @ContextInitParameters({ @ContextInitParameter(name = "org.richfaces.LiteralOnlyValue", value = "pure literal"),
            @ContextInitParameter(name = "org.richfaces.LiteralOnlyWithEl", value = "#{someEl}") })
    public void testLiteral() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        assertEquals("pure literal", getConfigurationValue(context, Configuration.literalOnly));
        assertEquals("#{someEl}", getConfigurationValue(context, Configuration.literalOnlyWithEl));
    }
}
