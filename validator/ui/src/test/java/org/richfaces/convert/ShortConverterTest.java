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
package org.richfaces.convert;

import javax.faces.convert.ShortConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.appplication.FacesMessages;

/**
 * @author Pavel Yaschenko
 * 
 */
@RunWith(ConverterTestRunner.class)
public class ShortConverterTest extends BaseTest {

    public ShortConverterTest() {
        super("META-INF/resources/org.richfaces/short-converter.js");
    }

    @Test
    @TestDataHolder(successes = { @TestData(submittedValue = "-32768"), @TestData(submittedValue = "0"),
        @TestData(submittedValue = "32767") }, failures = { @TestData(submittedValue = "-32769"),
        @TestData(submittedValue = "32768"), @TestData(submittedValue = "1.0"), @TestData(submittedValue = "1.2"),
        @TestData(submittedValue = "1a"), @TestData(submittedValue = "aaa"), @TestData(submittedValue = "- 10") })
    public void testSuccess() throws Exception {
        setClientFunction("RichFaces.csv.getConverter('short')");
        setObjectId(ShortConverter.CONVERTER_ID);
        setErrorMessageEnums(FacesMessages.SHORT_CONVERTER_SHORT);
    }
}
