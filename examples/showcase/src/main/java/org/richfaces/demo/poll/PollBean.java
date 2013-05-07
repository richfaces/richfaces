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
package org.richfaces.demo.poll;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author Ilya Shaikovsky
 */
@ManagedBean
@ViewScoped
public class PollBean implements Serializable {
    private static final int POLL_DISABLEMENT_INTERVAL = 60000;
    private static final long serialVersionUID = 7871292328251171957L;
    private Date pollStartTime;
    private boolean pollEnabled;

    public PollBean() {
        pollEnabled = true;
    }

    public Date getDate() {
        Date date = new Date();
        if (null == pollStartTime) {
            pollStartTime = new Date();
            return date;
        }
        if ((date.getTime() - pollStartTime.getTime()) >= POLL_DISABLEMENT_INTERVAL) {
            setPollEnabled(false);
        }
        return date;
    }

    public boolean getPollEnabled() {
        return pollEnabled;
    }

    public void setPollEnabled(boolean pollEnabled) {
        if (pollEnabled) {
            setPollStartTime(null);
        }
        this.pollEnabled = pollEnabled;
    }

    public Date getPollStartTime() {
        return pollStartTime;
    }

    public void setPollStartTime(Date pollStartTime) {
        this.pollStartTime = pollStartTime;
    }
}
