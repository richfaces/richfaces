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
package org.richfaces.util;

import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import com.google.common.base.Splitter;

/**
 * Caches JSF separator char for a context of current classloader and exposes useful utilities for joining and splitting by
 * separator char
 */
public final class SeparatorChar {

    private SeparatorChar() {
    }

    public static final char SEPARATOR_CHAR = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
    public static final FastJoiner JOINER = FastJoiner.on(SEPARATOR_CHAR);
    public static final Splitter SPLITTER = Splitter.on(SEPARATOR_CHAR);
}
