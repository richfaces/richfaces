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

import javax.faces.FacesException;

/**
 * Exception for throw in case of illegal creation of resource ( not existed file etc. )
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:08 $
 *
 */
public class ResourceNotFoundException extends FacesException {

    /**
     *
     */
    private static final long serialVersionUID = -3263301605684963184L;

    /**
     *
     */
    public ResourceNotFoundException() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public ResourceNotFoundException(String arg0) {
        super(arg0);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public ResourceNotFoundException(Throwable arg0) {
        super(arg0);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ResourceNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);

        // TODO Auto-generated constructor stub
    }
}
