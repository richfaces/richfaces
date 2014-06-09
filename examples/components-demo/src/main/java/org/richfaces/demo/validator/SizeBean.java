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
package org.richfaces.demo.validator;

import javax.validation.constraints.Size;

/**
 * @author asmirnov
 *
 */
public class SizeBean extends Validable<String> {
    @Size(max = 10, min = 2, message = "incorrect field length")
    private String value;

    /**
     * @return the text
     */
    public String getValue() {
        return value;
    }

    /**
     * @param text the text to set
     */
    public void setValue(String text) {
        this.value = text;
    }

    public String getDescription() {
        return "Validate String Length, for a range 2-10 chars";
    }

    public String getLabel() {
        return "size";
    }
}
