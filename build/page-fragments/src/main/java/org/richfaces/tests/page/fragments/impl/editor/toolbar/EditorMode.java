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

public enum EditorMode implements EditorButton {

    SOURCE("cke_button_source"),
    SPELL_CHECK_AS_YOU_TYPE("cke_button_scayt"),
    BOLD("cke_button_bold"),
    ITALIC("cke_button_italic"),
    UNDERLINE("cke_button_underline"),
    STRIKE_THROUGH("cke_button_strike"),
    SUBSCRIPT("cke_button_subscript"),
    SUPERSCRIPT("cke_button_superscript"),
    NUMBERED_LIST("cke_button_numberedlist"),
    BULLETED_LIST("cke_button_bulletedlist"),
    BLOCK_QUOTE("cke_button_blockquote"),
    MAXIMIZE("cke_button_maximize"),
    SHOW_BLOCKS("cke_button_showblocks");

    private final String className;

    private EditorMode(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return className;
    }

    public String getCSSClassName() {
        return className;
    }
}