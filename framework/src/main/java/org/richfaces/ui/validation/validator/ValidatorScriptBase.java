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

package org.richfaces.ui.validation.validator;

import java.io.IOException;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;

public abstract class ValidatorScriptBase extends JSFunctionDefinition implements ComponentValidatorScript {
    public static final String ELEMENT = "e";
    public static final JSReference ELEMENT_REF = new JSReference(ELEMENT);
    public static final String EVENT = "event";
    public static final JSReference EVENT_REF = new JSReference(EVENT);
    public static final String DISABLE_AJAX = "da";
    public static final JSReference DISABLE_AJAX_REF = new JSReference(DISABLE_AJAX);
    public static final String EOL = ";\n";
    public static final String CLIENT_ID = "id";
    public static final JSReference CLIENT_ID_REF = new JSReference(CLIENT_ID);
    public static final String SOURCE_ID = "sid";
    public static final JSReference SOURCE_ID_REF = new JSReference(SOURCE_ID);
    public static final String CONVERTER = "c";
    public static final JSReference CONVERTER_REF = new JSReference(CONVERTER);
    public static final String VALIDATORS = "v";
    public static final JSReference VALIDATORS_REF = new JSReference(VALIDATORS);
    public static final String AJAX = "a";
    public static final JSReference AJAX_REF = new JSReference(AJAX);
    public static final String PARAMS = "p";
    public static final JSReference PARAMS_REF = new JSReference(PARAMS);
    public static final String MESSAGE = "m";
    public static final JSReference MESSAGE_REF = new JSReference(MESSAGE);
    public static final NullConverterScript NULL_CONVERTER_SCRIPT = new NullConverterScript();
    public static final String CSV_NAMESPACE = "RichFaces.csv.";
    public static final String VALUE_FUNCTION_NAME = CSV_NAMESPACE + "getValue";
    public static final JSFunction GET_VALUE_FUNCTION = new JSFunction(VALUE_FUNCTION_NAME, CLIENT_ID_REF, ELEMENT_REF);
    public static final String VALIDATE_FUNCTION_NAME = CSV_NAMESPACE + "validate";
    public static final JSFunction VALIDATE_FUNCTION = new JSFunction(VALIDATE_FUNCTION_NAME, CLIENT_ID_REF, ELEMENT_REF);

    public ValidatorScriptBase() {
        super(EVENT, CLIENT_ID, ELEMENT, DISABLE_AJAX);
    }

    public String createCallScript(String clientId, String sourceId) {
        JSFunction callFunction = new JSFunction(getName(), EVENT_REF, clientId, null != sourceId ? sourceId : JSReference.THIS);
        return callFunction.toScript();
    }

    protected void appendParameters(Appendable target) throws IOException {
    }

    @Override
    protected void appendBody(Appendable target) throws IOException {
        appendParametersDefinition(target);
        appendValidatorCall(target);
    }

    private void appendParametersDefinition(Appendable target) throws IOException {
        target.append("var ").append(PARAMS).append("={");
        target.append(DISABLE_AJAX).append(':').append(DISABLE_AJAX).append(',');
        appendParameters(target);
        target.append("}").append(EOL);
    }

    protected void appendValidatorCall(Appendable target) throws IOException {
        JSFunction callValidator = new JSFunction(VALIDATE_FUNCTION_NAME, EVENT_REF, CLIENT_ID_REF, ELEMENT_REF, PARAMS_REF);
        callValidator.appendScript(target);
        target.append(EOL);
    }

    protected void appendAjaxParameter(Appendable target, String ajaxScript) throws IOException {
        target.append(AJAX).append(':');
        appendAjaxFunction(target, ajaxScript);
    }

    protected void appendAjaxFunction(Appendable target, String ajaxScript) throws IOException {
        JSFunctionDefinition ajaxFunction = new JSFunctionDefinition(EVENT, CLIENT_ID);
        ajaxFunction.addToBody(ajaxScript);
        ajaxFunction.appendScript(target);
    }
}