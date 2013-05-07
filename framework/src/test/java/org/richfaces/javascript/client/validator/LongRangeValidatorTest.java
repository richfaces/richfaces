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

import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import java.util.List;
import java.util.Map;

/**
 * @author asmirnov
 *
 */
public class LongRangeValidatorTest extends ValidatorTestBase {
    private static final String MINIMUM = "min";
    private static final String MAXIMUM = "max";

    /**
     * @param criteria
     */
    public LongRangeValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.validator.ValidatorTestBase#createValidator()
     */
    @Override
    protected Validator createValidator() {
        LongRangeValidator validator = new LongRangeValidator();
        Map<String, Object> options = getOptions();
        if (options.containsKey(MINIMUM)) {
            validator.setMinimum((Long) options.get(MINIMUM));
        }
        if (options.containsKey(MAXIMUM)) {
            validator.setMaximum((Long) options.get(MAXIMUM));
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
        return "validateLongRange";
    }

    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(0L), pass(3L), pass(Long.MAX_VALUE), pass(0L, MINIMUM, 2L), pass(2L, MINIMUM, 2L),
            pass(3L, MINIMUM, 2L), pass(-3L, MINIMUM, 2L), pass(0L, MAXIMUM, 2L), pass(2L, MAXIMUM, 2L), pass(3L, MAXIMUM, 2L),
            pass(-3L, MAXIMUM, 2L), pass(0L, MINIMUM, 3L, MAXIMUM, 5L), pass(3L, MINIMUM, 3L, MAXIMUM, 5L),
            pass(4L, MINIMUM, 3L, MAXIMUM, 5L), pass(7L, MINIMUM, 3L, MAXIMUM, 5L));
    }
}
