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
package org.richfaces.ui.common;

import java.io.IOException;

import org.ajax4jsf.javascript.JSChainJSFFunction;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.javascript.ScriptStringBase;
import org.ajax4jsf.javascript.ScriptUtils;

/**
 * @author Nick Belaevski
 *
 */
public class AjaxFunction extends ScriptStringBase {
    public static final String FUNCTION_NAME = "RichFaces.ajax";
    private Object source;
    private Object event = JSReference.EVENT;
    private AjaxOptions options;

    public AjaxFunction(Object source, AjaxOptions options) {
        super();
        this.source = source;
        this.options = options;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    public AjaxOptions getOptions() {
        return options;
    }

    public void setOptions(AjaxOptions eventOptions) {
        this.options = eventOptions;
    }

    private void appendAjaxFunctionCall(Appendable target) throws IOException {
        target.append(FUNCTION_NAME);
        target.append('(');

        ScriptUtils.appendScript(target, source);
        target.append(',');
        ScriptUtils.appendScript(target, event);

        if (!options.isEmpty()) {
            target.append(',');
            ScriptUtils.appendScript(target, options);
        }

        target.append(")");
    }

    public void appendScript(Appendable target) throws IOException {
        if (options.getBeforesubmitHandler() == null) {
            appendAjaxFunctionCall(target);
        } else {
            StringBuilder ajaxCall = new StringBuilder();
            appendAjaxFunctionCall(ajaxCall);

            ScriptUtils.appendScript(target, new JSChainJSFFunction(options.getBeforesubmitHandler(), ajaxCall.toString()));
        }
    }
}
