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

import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.resource.AbstractJSONResource;

/**
 * @author Nick Belaevski
 *
 */
public class ProgressBarResource extends AbstractJSONResource {
    private static final String ATTRIBUTE_NAME = ProgressBarResource.class.getName();

    @Override
    protected Object getData(FacesContext context) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

        Integer value = (Integer) sessionMap.get(ATTRIBUTE_NAME);
        if (value == null) {
            value = Integer.valueOf(0);
        } else {
            value = Integer.valueOf(value.intValue() + 5);
        }

        sessionMap.put(ATTRIBUTE_NAME, value);

        return value;
    }
}
