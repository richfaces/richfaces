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

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

import javax.faces.convert.BooleanConverter;
import javax.faces.convert.Converter;
import java.util.List;

public class BooleanConverterTest extends ConverterTestBase {
    public BooleanConverterTest(RunParameters criteria) {
        super(criteria);
    }

    @Override
    protected Converter createConverter() {
        return new BooleanConverter();
    }

    @Override
    protected String getJavaScriptFunctionName() {
        return "convertBoolean";
    }

    @Parameters
    public static List<RunParameters[]> getRunParameterss() {
        return options(pass("true"), pass("ok"), pass("123"), pass("0"), pass("1"), pass("no"));
    }
}
