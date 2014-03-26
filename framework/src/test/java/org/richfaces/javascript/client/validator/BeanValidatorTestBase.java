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

package org.richfaces.javascript.client.validator;

import com.gargoylesoftware.htmlunit.ScriptException;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;

import org.ajax4jsf.javascript.JSFunction;
import org.junit.Test;
import org.richfaces.javascript.client.MockTestBase;
import org.richfaces.javascript.client.RunParameters;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public abstract class BeanValidatorTestBase extends MockTestBase {
    protected static final String PROP = "property";

    public BeanValidatorTestBase(RunParameters criteria) {
        super(criteria);
    }

    @Test
    public void testValidator() throws Exception {
        Validator validator = createValidator();
        Set<?> constrains = validator.validateValue(getBeanType(), (String) getOptions().get(PROP), criteria.getValue());
        try {
            validateOnClient(validator);
            assertTrue("Bean validator found error for value: " + criteria.getValue() + ", validator options: " + getOptions(),
                constrains.isEmpty());
        } catch (ScriptException e2) {
            // both methods throws exceptions - it's ok.
            Throwable cause = e2.getCause();
            assertTrue(cause instanceof JavaScriptException);
        }
    }

    protected abstract Class<?> getBeanType();

    protected Object validateOnClient(Validator validator) throws ValidationException {
        JSFunction clientSideFunction = new JSFunction("RichFaces.csv." + getJavaScriptFunctionName(), criteria.getValue(),
            TEST_COMPONENT_ID, getJavaScriptOptions(), getErrorMessage());
        return qunit.runScript(clientSideFunction.toScript());
    }

    protected Validator createValidator() {
        return Validation.buildDefaultValidatorFactory().usingContext().getValidator();
    }
}
