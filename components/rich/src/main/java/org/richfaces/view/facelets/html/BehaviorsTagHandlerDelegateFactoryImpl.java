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
package org.richfaces.view.facelets.html;

import javax.faces.FacesWrapper;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.ConverterHandler;
import javax.faces.view.facelets.TagHandlerDelegate;
import javax.faces.view.facelets.TagHandlerDelegateFactory;
import javax.faces.view.facelets.ValidatorHandler;

/**
 * @author Nick Belaevski
 */
public class BehaviorsTagHandlerDelegateFactoryImpl extends TagHandlerDelegateFactory implements
    FacesWrapper<TagHandlerDelegateFactory> {
    private TagHandlerDelegateFactory factory;

    public BehaviorsTagHandlerDelegateFactoryImpl(TagHandlerDelegateFactory factory) {
        this.factory = factory;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.view.facelets.TagHandlerDelegateFactory#createBehaviorHandlerDelegate(javax.faces.view.facelets.BehaviorHandler
     * )
     */
    @Override
    public TagHandlerDelegate createBehaviorHandlerDelegate(BehaviorHandler owner) {
        return factory.createBehaviorHandlerDelegate(owner);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.view.facelets.TagHandlerDelegateFactory#createComponentHandlerDelegate(javax.faces.view.facelets.ComponentHandler
     * )
     */
    @Override
    public TagHandlerDelegate createComponentHandlerDelegate(ComponentHandler owner) {

        // TagHandlers structure is created when view is compiled
        // so there's no need to check for BehaviorsStack

        if (owner instanceof BehaviorsAddingComponentHandlerWrapper) {
            // this is to avoid StackOverflowError because of ComponentHandler constructor call
            return factory.createComponentHandlerDelegate(owner);
        }

        ComponentHandler wrappedHandler = new BehaviorsAddingComponentHandlerWrapper(owner);

        return factory.createComponentHandlerDelegate(wrappedHandler);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.view.facelets.TagHandlerDelegateFactory#createConverterHandlerDelegate(javax.faces.view.facelets.ConverterHandler
     * )
     */
    @Override
    public TagHandlerDelegate createConverterHandlerDelegate(ConverterHandler owner) {
        return factory.createConverterHandlerDelegate(owner);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.view.facelets.TagHandlerDelegateFactory#createValidatorHandlerDelegate(javax.faces.view.facelets.ValidatorHandler
     * )
     */
    @Override
    public TagHandlerDelegate createValidatorHandlerDelegate(ValidatorHandler owner) {
        return factory.createValidatorHandlerDelegate(owner);
    }

    public TagHandlerDelegateFactory getWrapped() {
        return factory;
    }
}
