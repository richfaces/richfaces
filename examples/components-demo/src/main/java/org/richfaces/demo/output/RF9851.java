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
package org.richfaces.demo.output;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author akolonitsky
 * @since Dec 6, 2010
 */
@ManagedBean(name = "RF9851")
@SessionScoped
public class RF9851 {
    private String name;
    private String email;

    public RF9851() {
        System.out.println("RF9851.RF9851");
    }

    public String getName() {
        System.out.println("RF9851.getName");
        return name;
    }

    public void setName(String name) {
        System.out.println("RF9851.setName name = " + name);
        this.name = name;
    }

    public String getEmail() {
        System.out.println("RF9851.getEmail");
        return email;
    }

    public void setEmail(String email) {
        System.out.println("RF9851.setEmail email = " + email);
        this.email = email;
    }
}
