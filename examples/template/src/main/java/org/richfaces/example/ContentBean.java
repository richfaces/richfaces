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
package org.richfaces.example;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 * @author leo
 *
 */
public class ContentBean {
    private static final String PRELUDE = "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n"
            + " xmlns:f=\"http://java.sun.com/jsf/core\"\n" + " xmlns:h=\"http://java.sun.com/jsf/html\"\n"
            + " xmlns:rich=\"http://richfaces.org/rich\"\n" + " xmlns:c=\"http://java.sun.com/jsp/jstl/core\">\n";
    private static final String TAIL = "\n</html>";
    private String xpath = "//*[local-name()='define'][@name='content']/*";

    public String getContent() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        XMLBody xmlBody = new XMLBody();
        try {
            xmlBody.loadXML(context.getExternalContext().getResourceAsStream(viewId), false);
            return PRELUDE + xmlBody.getContent(getXpath()) + TAIL;
        } catch (ParsingException e) {
            throw new FacesException(e);
        }
    }

    /**
     * @param xpath the xpath to set
     */
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    /**
     * @return the xpath
     */
    public String getXpath() {
        return xpath;
    }
}
