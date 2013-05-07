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

package org.richfaces.demo.region;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.richfaces.demo.common.UserBean;

@ManagedBean
@RequestScoped
public class RegionBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3844974441732156513L;
    private UserBean user1 = new UserBean();
    private UserBean user2 = new UserBean();

    public UserBean getUser1() {
        return user1;
    }

    public UserBean getUser2() {
        return user2;
    }

    public void setUser1(UserBean user1) {
        this.user1 = user1;
    }

    public void setUser2(UserBean user2) {
        this.user2 = user2;
    }
}
