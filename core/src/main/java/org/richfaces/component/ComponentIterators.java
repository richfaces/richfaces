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
package org.richfaces.component;

import java.util.Iterator;

import javax.faces.component.UIComponent;

import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 *
 */
public final class ComponentIterators {
    private ComponentIterators() {
    }

    // TODO nick - convert to filter/find functions
    public static Iterator<UIComponent> parents(final UIComponent component) {
        if (component == null) {
            return ImmutableSet.<UIComponent>of().iterator();
        }

        return new AbstractIterator<UIComponent>() {
            private UIComponent currentComponent = component;

            @Override
            protected UIComponent computeNext() {
                currentComponent = currentComponent.getParent();

                if (currentComponent == null) {
                    endOfData();
                }

                return currentComponent;
            }
        };
    }

    public static Iterator<UIComponent> parentsAndSelf(final UIComponent component) {
        if (component == null) {
            return ImmutableSet.<UIComponent>of().iterator();
        }

        return Iterators.concat(Iterators.singletonIterator(component), parents(component));
    }

    public static UIComponent getParent(UIComponent component, Predicate<UIComponent> predicat) {
        if (component == null || predicat == null) {
            return null;
        }

        UIComponent parent = component.getParent();
        while (parent != null) {
            if (predicat.apply(parent)) {
                return parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * Finds a parent of given UI <code>component</code>.
     *
     * @param component <code>UIComponent</code>
     * @param parentClass <code>Class</code> of desired parent
     * @return <code>UIComponent</code>
     */
    public static <T extends UIComponent> T getParent(UIComponent component, Class<T> parentClass) {
        if (component == null || parentClass == null) {
            return null;
        }

        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parentClass.isInstance(parent)) {
                return (T) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
