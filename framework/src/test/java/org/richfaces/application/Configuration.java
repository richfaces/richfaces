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
package org.richfaces.application;

import org.richfaces.application.configuration.ConfigurationItem;

/**
 * @author Nick Belaevski
 *
 */
public enum Configuration {

    @ConfigurationItem(names = "org.richfaces.LongValue")
    longValue,
    @ConfigurationItem(names = "org.richfaces.LongValueWithDefault", defaultValue = "-100")
    longValueWithDefault,
    @ConfigurationItem(names = "org.richfaces.IntValue")
    intValue,
    @ConfigurationItem(names = "org.richfaces.IntValueWithDefault", defaultValue = "-1")
    intValueWithDefault,
    @ConfigurationItem(names = "org.richfaces.StringValue")
    stringValue,
    @ConfigurationItem(names = "org.richfaces.StringValueWithDefault", defaultValue = "default name")
    stringValueWithDefault,
    @ConfigurationItem(names = "org.richfaces.EnumValue")
    enumValue,
    @ConfigurationItem(names = "org.richfaces.EnumValue", defaultValue = "foo")
    enumValueWithDefault,
    @ConfigurationItem(names = "org.richfaces.BooleanValue")
    booleanValue,
    @ConfigurationItem(names = "org.richfaces.BooleanValue", defaultValue = "false")
    booleanValueWithDefault,
    @ConfigurationItem(names = { "org.richfaces.MultiValue1", "org.richfaces.MultiValue2" })
    multiValue,
    @ConfigurationItem(names = "org.richfaces.FacesContextReference")
    facesContext,
    @ConfigurationItem(names = "org.richfaces.DynamicValueWithDefault", defaultValue = "<something>")
    dynamicValueWithDefault,
    @ConfigurationItem(names = "org.richfaces.LiteralOnlyValue", literal = true)
    literalOnly,
    @ConfigurationItem(names = "org.richfaces.LiteralOnlyWithEl", literal = true)
    literalOnlyWithEl
}
