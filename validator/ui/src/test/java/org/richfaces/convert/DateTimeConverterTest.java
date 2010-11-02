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

import javax.faces.convert.DateTimeConverter;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nick Belaevski
 * 
 */
@RunWith(ConverterTestRunner.class)
public class DateTimeConverterTest extends BaseTest {

    public DateTimeConverterTest() {
        super("org/richfaces/convert/testConverter.js");
    }

    @Test
    @TestDataHolder(successes = { @TestData(submittedValue = "18-10-2010")
    // ,@TestData(submittedValue = "17-10-2010")
    }, failures = {
    // @TestData(submittedValue = "10/17/2010")
    })
    public void testPattern() throws Exception {
        setClientFunction("org.rf.DateTimeConverter");
        setObjectId(DateTimeConverter.CONVERTER_ID);
        setAttribute("pattern", "dd-MM-yyyy");
    }

    @Test
    public void testNoPattern() throws Exception {

    }
}
