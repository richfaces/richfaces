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
package org.richfaces.fragment.common;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Icon {

    @Root
    private GrapheneElement containerElement;
    @FindByJQuery(">*:visible")
    private GrapheneElement iconElement;
    @FindByJQuery(">img:visible")
    private GrapheneElement iconImageElement;
    @FindByJQuery(">div:visible")
    private GrapheneElement iconDivElement;

    /**
     * @return root of the component
     */
    public GrapheneElement getRootElement() {
        return containerElement;
    }

    /**
     * @return actual visible 'div' icon element.
     */
    public GrapheneElement getIconDivElement() {
        return iconDivElement;
    }

    /**
     * @return actual visible icon element, could be 'img' or 'div'.
     */
    public GrapheneElement getIconElement() {
        return iconElement;
    }

    /**
     * @return actual visible 'img' icon element.
     */
    public GrapheneElement getIconImageElement() {
        return iconImageElement;
    }
}
