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

/**
 *
 */
package org.richfaces.javascript.client.validator;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import java.util.List;
import java.util.Map;

/**
 * @author asmirnov
 *
 */
public class LengthValidatorTest extends ValidatorTestBase {
    private static final String MINIMUM = "min";
    private static final String MAXIMUM = "max";

    /**
     * @param criteria
     */
    public LengthValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.validator.ValidatorTestBase#createValidator()
     */
    @Override
    protected Validator createValidator() {
        LengthValidator validator = new LengthValidator();
        Map<String, Object> options = getOptions();
        if (options.containsKey(MINIMUM)) {
            validator.setMinimum((Integer) options.get(MINIMUM));
        }
        if (options.containsKey(MAXIMUM)) {
            validator.setMaximum((Integer) options.get(MAXIMUM));
        }
        return validator;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.MockTestBase#getJavaScriptFunctionName()
     */
    @Override
    protected String getJavaScriptFunctionName() {
        return "validateLength";
    }

    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(""), pass("aaa"), pass("123"), pass("", MINIMUM, 2), pass("vv", MINIMUM, 2),
            pass("vvv", MINIMUM, 2), pass("", MAXIMUM, 2), pass("vv", MAXIMUM, 2), pass("123", MAXIMUM, 2),
            pass("", MINIMUM, 3, MAXIMUM, 5, IGNORE_MESSAGE, true), pass("ddd", MINIMUM, 3, MAXIMUM, 5),
            pass("dddd", MINIMUM, 3, MAXIMUM, 5), pass("abcdefg", MINIMUM, 3, MAXIMUM, 5, IGNORE_MESSAGE, true));
    }
}
