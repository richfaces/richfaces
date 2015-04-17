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
package org.richfaces.validator.csv;

import java.io.IOException;
import java.text.MessageFormat;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import org.ajax4jsf.javascript.ScriptString;
import org.ajax4jsf.javascript.ScriptStringBase;
import org.richfaces.javascript.JavaScriptService;

/**
 * Renders script for adding new CSV message type with given ID, derived from provided FacesMessage
 *
 * This script can be then included by {@link JavaScriptService} to the page.
 *
 * @author Lukas Fryc
 */
public class AddCSVMessageScript extends ScriptStringBase implements ScriptString {

    private static final String MESSAGE_OBJECT = "if (RichFaces.csv && RichFaces.csv.addMessage) '{' RichFaces.csv.addMessage('{' ''{0}'': '{'detail:''{1}'',summary:''{2}'',severity:{3}'}' '}'); '}'";

    private FacesMessage facesMessage;
    private String messageId;

    public AddCSVMessageScript(String messageId, FacesMessage facesMessage) {
        this.messageId = messageId;
        this.facesMessage = facesMessage;
    }

    @Override
    public void appendScript(Appendable target) throws IOException {

        String summary = facesMessage.getSummary().replace("'", "\\'");
        String detail = facesMessage.getDetail().replace("'", "\\'");
        int severity = facesMessage.getSeverity().getOrdinal();

        String script = MessageFormat.format(MESSAGE_OBJECT, messageId, summary, detail, severity);

        target.append(script);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((facesMessage == null) ? 0 : messageHashCode(facesMessage));
        result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AddCSVMessageScript other = (AddCSVMessageScript) obj;
        if (facesMessage == null) {
            if (other.facesMessage != null) {
                return false;
            }
        } else if (!messagesEquals(facesMessage, other.facesMessage)) {
            return false;
        }
        if (messageId == null) {
            if (other.messageId != null) {
                return false;
            }
        } else if (!messageId.equals(other.messageId)) {
            return false;
        }
        return true;
    }

    private int messageHashCode(FacesMessage message) {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message.getDetail() == null) ? 0 : message.getDetail().hashCode());
        result = prime * result + ((message.getSummary() == null) ? 0 : message.getSummary().hashCode());
        result = prime * result + ((message.getSeverity() == null) ? 0 : message.getSeverity().getOrdinal());
        return result;
    }

    private boolean messagesEquals(FacesMessage message1, FacesMessage message2) {
        if (message1 == message2) {
            return true;
        }
        if (message1 == null && message2 != null) {
            return false;
        }
        if (message1 != null && message2 == null) {
            return false;
        }

        return stringEquals(message1.getSummary(), message2.getSummary())
                && stringEquals(message1.getDetail(), message2.getDetail())
                && severityEquals(message1.getSeverity(), message2.getSeverity());
    }

    private boolean severityEquals(Severity severity1, Severity severity2) {
        if (severity1 == severity2) {
            return true;
        }
        if (severity1 == null && severity2 != null) {
            return false;
        }
        if (severity1 != null && severity2 == null) {
            return false;
        }

        return severity1.getOrdinal() == severity2.getOrdinal();
    }

    private boolean stringEquals(String string1, String string2) {
        if (string1 == string2) {
            return true;
        }
        if (string1 == null && string2 != null) {
            return false;
        }
        if (string1 != null && string2 == null) {
            return false;
        }

        return string1.equals(string2);
    }
}