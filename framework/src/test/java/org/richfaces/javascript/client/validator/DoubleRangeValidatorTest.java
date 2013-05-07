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

import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.Validator;
import java.util.List;
import java.util.Map;

/**
 * @author asmirnov
 *
 */
public class DoubleRangeValidatorTest extends ValidatorTestBase {
    private static final String MINIMUM = "min";
    private static final String MAXIMUM = "max";

    /**
     * @param criteria
     */
    public DoubleRangeValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.validator.ValidatorTestBase#createValidator()
     */
    @Override
    protected Validator createValidator() {
        DoubleRangeValidator validator = new DoubleRangeValidator();
        Map<String, Object> options = getOptions();
        if (options.containsKey(MINIMUM)) {
            validator.setMinimum((Double) options.get(MINIMUM));
        }
        if (options.containsKey(MAXIMUM)) {
            validator.setMaximum((Double) options.get(MAXIMUM));
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
        return "validateDoubleRange";
    }

    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(0L), pass(3L), pass(Double.MAX_VALUE), pass(0.0D, MINIMUM, 2.0D, IGNORE_MESSAGE, true),
            pass(2.0D, MINIMUM, 2.0D), pass(3.0D, MINIMUM, 2.0D), pass(-3.0D, MINIMUM, 2.0D, IGNORE_MESSAGE, true),
            pass(0.0D, MAXIMUM, 2.0D), pass(2.0D, MAXIMUM, 2.0D), pass(3.0D, MAXIMUM, 2.0D, IGNORE_MESSAGE, true),
            pass(-3.0D, MAXIMUM, 2.0D), pass(0.0D, MINIMUM, 3.0D, MAXIMUM, 5.0D, IGNORE_MESSAGE, true),
            pass(3.0D, MINIMUM, 3.0D, MAXIMUM, 5.0D), pass(4.0D, MINIMUM, 3.0D, MAXIMUM, 5.0D),
            pass(7.0D, MINIMUM, 3.0D, MAXIMUM, 5.0D, IGNORE_MESSAGE, true));
    }
}
