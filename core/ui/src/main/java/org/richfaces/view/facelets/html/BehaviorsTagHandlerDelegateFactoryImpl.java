/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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

import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.ConverterHandler;
import javax.faces.view.facelets.TagHandlerDelegate;
import javax.faces.view.facelets.TagHandlerDelegateFactory;
import javax.faces.view.facelets.ValidatorHandler;

import org.richfaces.log.RichfacesLogger;
import org.richfaces.log.Logger;

/**
 * @author Nick Belaevski
 */
public class BehaviorsTagHandlerDelegateFactoryImpl extends TagHandlerDelegateFactory {

    private static final Logger LOGGER = RichfacesLogger.WEBAPP.getLogger();
    
    private TagHandlerDelegateFactory factory;

    private boolean isMyFaces = false;
    
    public BehaviorsTagHandlerDelegateFactoryImpl(TagHandlerDelegateFactory factory) {
        this.factory = factory;
        detectMyFaces();
    }

    private void detectMyFaces() {
        String implementationTitle = Application.class.getPackage().getImplementationTitle();
        if (implementationTitle != null) {
            isMyFaces = implementationTitle.toLowerCase(Locale.US).contains("myfaces");
            
            if (isMyFaces) {
                //TODO - RF M3 workaround for https://jira.jboss.org/browse/RF-9025 / https://issues.apache.org/jira/browse/MYFACES-2888
                LOGGER.warn("MyFaces implementation of JavaServer Faces detected. " +
                		"Wrapping of components using RichFaces behaviors (a4j:ajax etc.) won't work!");
            }
        } else {
            LOGGER.warn("Cannot detect Mojarra vs MyFaces implementation of JavaServer Faces");
        }
    }
    
    /*
     *  (non-Javadoc)
     * @see javax.faces.view.facelets.TagHandlerDelegateFactory#createBehaviorHandlerDelegate(javax.faces.view.facelets.BehaviorHandler)
     */
    @Override
    public TagHandlerDelegate createBehaviorHandlerDelegate(BehaviorHandler owner) {
        return factory.createBehaviorHandlerDelegate(owner);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.view.facelets.TagHandlerDelegateFactory#createComponentHandlerDelegate(javax.faces.view.facelets.ComponentHandler)
     */
    @Override
    public TagHandlerDelegate createComponentHandlerDelegate(ComponentHandler owner) {

        // TagHandlers structure is created when view is compiled
        // so there's no need to check for BehaviorsStack
        
        ComponentHandler handler = owner;

        if (!isMyFaces) {
            handler = new BehaviorsAddingComponentHandlerWrapper(owner);
        }
        
        return factory.createComponentHandlerDelegate(handler);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.view.facelets.TagHandlerDelegateFactory#createConverterHandlerDelegate(javax.faces.view.facelets.ConverterHandler)
     */
    @Override
    public TagHandlerDelegate createConverterHandlerDelegate(ConverterHandler owner) {
        return factory.createConverterHandlerDelegate(owner);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.view.facelets.TagHandlerDelegateFactory#createValidatorHandlerDelegate(javax.faces.view.facelets.ValidatorHandler)
     */
    @Override
    public TagHandlerDelegate createValidatorHandlerDelegate(ValidatorHandler owner) {
        return factory.createValidatorHandlerDelegate(owner);
    }
}
