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
package org.richfaces.javascript;

import javax.faces.application.FacesMessage;

/**
 * @author asmirnov
 *
 */
public final class Message {
    private final int severity;
    private final String detail;
    private final String summary;

    public Message(int severity, String detail, String summary) {
        this.severity = severity;
        this.detail = detail;
        this.summary = summary;
    }

    public Message(FacesMessage msg) {
        this.severity = msg.getSeverity().getOrdinal();
        this.summary = msg.getSummary();
        this.detail = msg.getDetail();
    }

    /**
     * @return the severity
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.detail == null) ? 0 : this.detail.hashCode());
        result = prime * result + this.severity;
        result = prime * result + ((this.summary == null) ? 0 : this.summary.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        Message other = (Message) obj;
        if (this.detail == null) {
            if (other.detail != null) {
                return false;
            }
        } else if (!this.detail.equals(other.detail)) {
            return false;
        }
        if (this.severity != other.severity) {
            return false;
        }
        if (this.summary == null) {
            if (other.summary != null) {
                return false;
            }
        } else if (!this.summary.equals(other.summary)) {
            return false;
        }
        return true;
    }
}
