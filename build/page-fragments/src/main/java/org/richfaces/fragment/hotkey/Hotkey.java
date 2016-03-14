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
package org.richfaces.fragment.hotkey;

import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface Hotkey {

    /**
     * Invokes the hotkey on an element which is specified by selector attribute of hotkey, when the selector is empty, then the
     * hotkey will be invoked on <code>body</code> element.
     *
     * @see #setHotkey
     * @see #setSelector
     */
    void invoke();

    /**
     * Invokes the hotkey on a specified element.
     *
     * @param element element on which the hotkey will be invoked. In case the element is null, the actually focused element is
     * used, otherwise the <code>body</code> element is used.
     * @throws IllegalArgumentException if you have not set hotkey correctly
     * @see #setHotkey
     */
    void invoke(WebElement element);

    /**
     * Sets the hotkey which will be invoked
     *
     * @param hotkey key sequence which will be invoked, keys are separated with '+' sign and are in lowercase. For example:
     * 'alt+x'.
     * @throws IllegalArgumentException if the given <code>hotkey</code> is null, or empty
     */
    void setHotkey(String hotkey);

    /**
     * Sets the JQuery selector, by which the element on which the hotkey will be invoked, will be found.
     *
     * @param selector JQuery selector to locate element on which the hotkey will be applied
     * @throws IllegalArgumentException if the given <code>selector</code> is null, or empty
     */
    void setSelector(String selector);
}
