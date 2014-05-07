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

import com.google.common.base.Predicate;

import javax.faces.component.UIComponent;

/**
 * @author Nick Belaevski
 *
 */
public final class ComponentPredicates {
    private static final class WithIdPredicate implements Predicate<UIComponent> {
        private final String id;

        public WithIdPredicate(String id) {
            super();
            this.id = id;
        }

        public boolean apply(UIComponent input) {
            return id.equals(input.getId());
        }
    }

    private static final Predicate<UIComponent> IS_RENDERED = new Predicate<UIComponent>() {
        public boolean apply(UIComponent input) {
            return input.isRendered();
        }

        ;
    };

    private ComponentPredicates() {
    }

    public static Predicate<UIComponent> isRendered() {
        return IS_RENDERED;
    }

    public static Predicate<UIComponent> withId(String id) {
        if (id == null) {
            throw new NullPointerException("id");
        }

        return new WithIdPredicate(id);
    }
}
