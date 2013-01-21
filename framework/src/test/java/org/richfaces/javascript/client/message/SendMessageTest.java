/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.javascript.client.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class SendMessageTest extends MessageTestBase {
    @Test
    public void testSendDefault() throws Exception {
        setUpMessage();
        sendMessage();
        String messageAsText = getMessageAsText();
        assertTrue(messageAsText.contains(getErrorMessage().getSummary()));
        assertFalse(messageAsText.contains(getErrorMessage().getDetail()));
        checkTitle(DomElement.ATTRIBUTE_NOT_DEFINED);
    }

    @Test
    public void testSendWithDetail() throws Exception {
        setUpMessage(",showDetail:true");
        sendMessage();
        String messageAsText = getMessageAsText();
        assertTrue(messageAsText.contains(getErrorMessage().getSummary()));
        assertTrue(messageAsText.contains(getErrorMessage().getDetail()));
        checkTitle(DomElement.ATTRIBUTE_NOT_DEFINED);
    }

    @Test
    public void testSendWithLevel() throws Exception {
        setUpMessage(",level:3");
        sendMessage();
        String messageAsText = getMessageAsText();
        assertTrue(messageAsText.isEmpty());
        assertTrue(getMessageContentElement().getChildNodes().isEmpty());
    }

    @Test
    public void testSendWithTooltip() throws Exception {
        setUpMessage(",tooltip:true");
        sendMessage();
        String messageAsText = getMessageAsText();
        assertFalse(messageAsText.contains(getErrorMessage().getSummary()));
        assertFalse(messageAsText.contains(getErrorMessage().getDetail()));
        checkTitle(getErrorMessage().getSummary());
    }

    private void checkTitle(String title) {
        String attribute = ((DomElement) getMessageContentElement().getFirstChild()).getAttribute("title");
        assertEquals(title, attribute);
    }
}
