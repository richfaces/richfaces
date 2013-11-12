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
package org.richfaces.fragment.inplaceInput;

/**
 * Class for confirming the already typed text in various input components,
 * or for canceling it, and returning to the previous state.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public interface ConfirmOrCancel {

    /**
     * Confirms the text which is in the input during its editing state.
     *
     * In other words, an user firstly type text, and then confirms it
     * in order to really change it.
     */
    void confirm();

    /**
     * By using the built in controls, it confirms the text, which is in
     * the inplaceInput during its editing state.
     *
     * In other words, a user firstly type text into the inplaceInput and then
     * confirms it in order to really change the input's value.
     *
     * @throws IllegalStateException when the inplaceInput on which it was
     *                               called does not contain controls
     */
    void confirmByControlls();

    /**
     * Cancels the text which is in the input during its editing state,
     * thus it returns back the previous confirmed text.
     *
     * In other words, an user firstly type text, then change his/her
     * mind and can return back to the previous state by issuing this method.
     */
    void cancel();

    /**
     * By using the built in controls, it cancels the text which is in the
     * input during its editing state, thus it returns back the previous
     * confirmed text.
     *
     * In other words, an user firstly type text, then change his/her
     * mind and can return back to the previous state by issuing this method.
     *
     * @throws IllegalStateException when the inplaceInput on which it was
     *                               called does not contain controls
     */
    void cancelByControlls();
}
