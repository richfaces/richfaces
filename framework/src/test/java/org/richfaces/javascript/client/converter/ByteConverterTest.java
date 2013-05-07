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

package org.richfaces.javascript.client.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.faces.convert.ByteConverter;
import javax.faces.convert.Converter;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

public class ByteConverterTest extends ConverterTestBase {
    public ByteConverterTest(RunParameters criteria) {
        super(criteria);
    }

    @Override
    protected Converter createConverter() {
        ByteConverter byteConverter = new ByteConverter();
        return byteConverter;
    }

    @Override
    protected String getJavaScriptFunctionName() {
        return "convertByte";
    }

    @Override
    protected void compareResult(Object convertedValue, Object jsConvertedValue) {
        assertTrue(jsConvertedValue instanceof Double);
        assertTrue(convertedValue instanceof Byte);
        Double jsDouble = (Double) jsConvertedValue;
        Double jsfDouble = new Double((Byte) convertedValue);
        assertEquals(jsfDouble, jsDouble, 0.0000001);
    }

    @Parameters
    public static List<RunParameters[]> getRunParameterss() {
        return options(
                pass("true"),
                pass("ok"),
                pass("123"),
                pass("0"),
                pass("1"),
                pass("255"),
                pass("-128"),
                pass("-129"),
                pass("256"),
                pass("-0"),
                pass("0.05"));
    }
}
