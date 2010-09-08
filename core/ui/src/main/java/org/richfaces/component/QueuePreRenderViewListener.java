/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import static org.richfaces.application.configuration.ConfigurationServiceHelper.getBooleanConfigurationValue;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.richfaces.aplication.CoreComponentsConfiguration;

/**
 * @author Nick Belaevski
 * 
 */
public class QueuePreRenderViewListener implements SystemEventListener {

    private static final String QUEUE_RESOURCE_COMPONENT_RENDERER_TYPE = "org.richfaces.QueueResourceComponentRenderer";
    
    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        
        boolean queueEnabled = getBooleanConfigurationValue(context, CoreComponentsConfiguration.Items.queueEnabled);
        if (queueEnabled) {
            Application application = context.getApplication();
            UIComponent queueResourceComponent = application.createComponent(context, 
                UIOutput.COMPONENT_TYPE, QUEUE_RESOURCE_COMPONENT_RENDERER_TYPE);
            
            //fix for JSF duplicate ID exception
            queueResourceComponent.setId(QueueRegistry.QUEUE_SCRIPT_ID);
            
            viewRoot.addComponentResource(context, queueResourceComponent);
        } else {
            //queue can be switched off at application level, not a page level, so no need to remove it if it is switched off
        }
    }

}
