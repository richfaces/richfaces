/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.ajax4jsf.resource;

import java.util.Date;

import javax.el.MethodExpression;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:13 $
 *
 */
public interface ResourceComponent {

    /**
     * Get Mime-type for target .
     * @return
     */
    public abstract String getMimeType();

    /**
     * Set Mime-type for target .
     * @param newvalue
     */
    public abstract void setMimeType(String newvalue);

    /**
     * Get Last modified date  for target .
     * @return
     */
    @Deprecated
    public abstract Date getLastModified();

    /**
     * Set Last modified for target .
     * @param newvalue
     */
    @Deprecated
    public abstract void setLastModified(Date newvalue);

    /**
     * Get Expiration time for target .
     * @return
     */
    @Deprecated
    public abstract Date getExpires();

    /**
     * Set Expiration time for target .
     * @param newvalue
     */
    @Deprecated
    public abstract void setExpires(Date newvalue);

    /**
     * Get caching flag for resource.
     * @return
     */
    public abstract boolean isCacheable();

    /**
     * Set caching flag for resource.
     * @param newvalue
     */
    public abstract void setCacheable(boolean newvalue);

    /**
     * Get session-avare flag for resource.
     * @return true if resource depend of client session. If false, no JSESSIONID encoded in URI
     */
    @Deprecated
    public abstract boolean isSession();

    /**
     * Set session-avare flag for resource.
     * @param newvalue
     */
    @Deprecated
    public abstract void setSession(boolean newvalue);

    /**
     * Get Data object, encoded in uri and passed to "send" method for generate resource content.
     * @return
     */
    public abstract Object getValue();

    /**
     * Set Data object, encoded in uri and passed to "send" method for generate resource content.
     * @param newvalue
     */
    public abstract void setValue(Object newvalue);

    /**
     * Get MethodExpression to method in user bean to send resource. Method will
     * called with two parameters - restored data object and servlet output
     * stream.
     *
     * @return MethodExpression
     */
    public abstract MethodExpression getCreateContent();

    /**
     * Set MethodExpression to method in user bean to send resource. Method will
     * called with two parameters - restored data object and servlet output
     * stream.
     *
     * @param newvalue - new MethodExpression value
     */
    public abstract void setCreateContent(MethodExpression newvalue);
}
