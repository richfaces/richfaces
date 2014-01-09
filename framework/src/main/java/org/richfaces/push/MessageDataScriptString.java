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
package org.richfaces.push;

import java.io.IOException;
import java.util.Iterator;

import org.richfaces.javascript.ScriptStringBase;

/**
 * @author Nick Belaevski
 *
 */
public class MessageDataScriptString extends ScriptStringBase {
    private final Iterable<MessageData> messages;
    private long lastMessageNumber;

    public MessageDataScriptString(Iterable<MessageData> messages) {
        super();

        this.messages = messages;
    }

    private void appendMessageToScript(MessageData message, Appendable target) throws IOException {
        target.append("<msg topic=\"");
        target.append(message.getTopicKey().getTopicAddress());

        target.append("\" number=\"");
        target.append(Long.toString(message.getSequenceNumber()));
        target.append("\">");

        // append data as is - no escaping
        target.append(message.getSerializedMessage());

        target.append("</msg>");
    }

    public void appendScript(Appendable target) throws IOException {
        Iterator<MessageData> iterator = messages.iterator();

        while (iterator.hasNext()) {
            MessageData message = iterator.next();

            appendMessageToScript(message, target);

            // TODO - synchronization aids?
            lastMessageNumber = message.getSequenceNumber();
        }
    }

    public long getLastSequenceNumber() {
        return lastMessageNumber;
    }
}
