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

import javax.validation.constraints.Max;
import java.util.List;

/**
 * @author asmirnov
 *
 */
public class MaxValidatorTest extends BeanValidatorTestBase {
    private static final String MAXIMUM = "value";

    /**
     * @param criteria
     */
    public MaxValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.MockTestBase#getJavaScriptFunctionName()
     */
    @Override
    protected String getJavaScriptFunctionName() {
        return "validateMax";
    }

    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(0, PROP, "number", MAXIMUM, 2), pass(2, PROP, "number", MAXIMUM, 2),
            pass(123, PROP, "number", MAXIMUM, 2));
    }

    public static final class Bean {
        @Max(2)
        public int getNumber() {
            return 0;
        }
    }

    @Override
    protected Class<?> getBeanType() {
        return Bean.class;
    }
}
