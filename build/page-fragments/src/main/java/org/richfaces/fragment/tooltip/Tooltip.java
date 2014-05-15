/**
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
package org.richfaces.fragment.tooltip;

import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <CONTENT>
 */
public interface Tooltip<CONTENT> {

    /**
     * Invokes the tooltip over an element, which is found by injected root element to his component.
     *
     * Note that, invoking of the tooltip should be rather by implicit action (for example hover of the mouse over some
     * element), than this explicit call.
     *
     * @return tooltip itself
     */
    Tooltip<CONTENT> show();

    /**
     * Invokes the tooltip over the given <code>target</code>.
     *
     * Note that, invoking of the tooltip should be rather by implicit action (for example hover of the mouse over some
     * element), than this explicit call.
     *
     * @param target the element on which the tooltip will be invoked
     * @return tooltip itself
     */
    Tooltip<CONTENT> show(WebElement target);

    /**
     * Dismiss the tooltip, which is found by injected root element to his component.
     *
     * Note that, hiding of the tooltip should be rather by implicit action (for example hover of the mouse out from some
     * element), than this explicit call.
     *
     * @return tooltip itself
     */
    Tooltip<CONTENT> hide();

    /**
     * Dismiss the tooltip over the given <code>target</code>.
     *
     * Note that, hiding of the tooltip should be rather by implicit action (for example hover of the mouse out from some
     * element), than this explicit call.
     *
     * @return tooltip itself
     */
    Tooltip<CONTENT> hide(WebElement target);

    /**
     * Gets the object representing the content of the tooltip.
     *
     * @return initialized page object with the content of this tooltip
     * @see TextualRichFacesTooltip
     */
    CONTENT getContent();
}
