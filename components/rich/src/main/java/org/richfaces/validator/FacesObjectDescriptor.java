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

package org.richfaces.validator;

import java.util.Map;

import org.richfaces.javascript.Message;

/**
 * <p class="changed_added_4_0">
 * Inmlementations of this interface describe JSF or JSR-303 validators. Because JSF supports 2 types of validators, this
 * interface unifies access to them.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface FacesObjectDescriptor {
    /**
     * <p class="changed_added_4_0">
     * Returns JSF {@link javax.faces.validator.Validator} implementation class or JSR-303 annotation class.
     * </p>
     */
    Class<?> getImplementationClass();

    /**
     * <p class="changed_added_4_0">
     * Concrete validator parameters
     * </p>
     *
     * @return non null map with validator instance parameters.
     */
    Map<String, ? extends Object> getAdditionalParameters();

    /**
     * <p class="changed_added_4_0">
     * Localized validator message
     * </p>
     */
    Message getMessage();
}
