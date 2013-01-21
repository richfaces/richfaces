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
package org.richfaces;

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
