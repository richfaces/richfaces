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
package org.richfaces.tests.page.fragments.impl.panel;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jodah.typetools.TypeResolver;
import org.openqa.selenium.WebElement;

public abstract class AbstractPanel<HEADER, BODY> implements Panel<HEADER, BODY> {

    @Root
    private WebElement root;

    private final Class<BODY> bodyClass = (Class<BODY>) TypeResolver.resolveRawArguments(Panel.class, getClass())[1];
    private final Class<HEADER> headerClass = (Class<HEADER>) TypeResolver.resolveRawArguments(Panel.class, getClass())[0];

    @Override
    @SuppressWarnings(value = "unchecked")
    public BODY getBodyContent() {
        return Graphene.createPageFragment(bodyClass, getBodyElement());
    }

    protected abstract WebElement getBodyElement();

    @Override
    @SuppressWarnings(value = "unchecked")
    public HEADER getHeaderContent() {
        if (!getHeaderElement().isPresent()) {
            throw new IllegalStateException("You are trying to get header content of the panel which does not have header!");
        }
        return Graphene.createPageFragment(headerClass, getHeaderElement());
    }

    protected abstract GrapheneElement getHeaderElement();

    protected WebElement getRootElement() {
        return root;
    }
}
