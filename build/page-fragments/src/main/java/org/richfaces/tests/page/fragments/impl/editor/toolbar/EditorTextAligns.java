/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.page.fragments.impl.editor.toolbar;

public enum EditorTextAligns implements EditorButton {
    LEFT("cke_button_justifyleft"),
    CENTER("cke_button_justifycenter"),
    RIGHT("cke_button_justifyright"),
    BLOCK("cke_button_justifyblock");

    private final String className;

    private EditorTextAligns(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return this.className;
    }

    public String getCSSClassName() {
        return className;
    }
}