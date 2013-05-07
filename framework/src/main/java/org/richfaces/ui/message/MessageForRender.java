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

package org.richfaces.ui.message;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

public class MessageForRender {
    private final FacesMessage msg;
    private final String sourceId;

    public MessageForRender(FacesMessage msg, String sourceId) {
        this.msg = msg;
        this.sourceId = sourceId;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @see javax.faces.application.FacesMessage#getDetail()
     */
    public String getDetail() {
        return this.msg.getDetail();
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @see javax.faces.application.FacesMessage#getSeverity()
     */
    public Severity getSeverity() {
        return this.msg.getSeverity();
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @see javax.faces.application.FacesMessage#getSummary()
     */
    public String getSummary() {
        return this.msg.getSummary();
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @see javax.faces.application.FacesMessage#isRendered()
     */
    public boolean isRendered() {
        return this.msg.isRendered();
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @see javax.faces.application.FacesMessage#rendered()
     */
    public void rendered() {
        this.msg.rendered();
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the sourceId
     */
    public String getSourceId() {
        return sourceId;
    }
}
