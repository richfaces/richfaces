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
package org.richfaces.context;

import java.util.List;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.richfaces.application.CoreConfiguration;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.el.BaseReadOnlyValueExpression;

/**
 * @author Nick Belaevski
 * 
 */
public class SkinningResourcesPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 7430448731396547419L;

    private static final String SKINNING_RESOURCE_MARKER = SkinningResourcesPhaseListener.class.getName();

    private static final String CLASSES_ECSS = "_classes.ecss";

    private static final String ECSS = ".ecss";

    private static final String BOTH_ECSS = "_both.ecss";

    private static final String CONTROLS_SKINNING;

    private static final String BOTH_SKINNING;

    private static final String CLASSES_SKINNING;

    private static final String HEAD = "head";
    
    static {
        String skinningName = "skinning";
        
        CONTROLS_SKINNING = skinningName + ECSS;
        BOTH_SKINNING = skinningName + BOTH_ECSS;
        CLASSES_SKINNING = skinningName + CLASSES_ECSS;

    }

    private static final class SkinningResourceNameExpression extends BaseReadOnlyValueExpression {

        private static final long serialVersionUID = 7520575496522682120L;

        public SkinningResourceNameExpression() {
            super(String.class);
        }

        @Override
        public Object getValue(ELContext context) {
            FacesContext facesContext = getFacesContext(context);

            ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);
            
            boolean controls = configurationService.getBooleanValue(facesContext, CoreConfiguration.Items.standardControlsSkinning);
            boolean classes = configurationService.getBooleanValue(facesContext, CoreConfiguration.Items.standardControlsSkinningClasses);
            
            if (controls && classes) {
                return BOTH_SKINNING;
            }
            
            if (classes) {
                return CLASSES_SKINNING;
            }
            
            return CONTROLS_SKINNING;
        }
        
    }

    private static final class SkinningResourceRenderedExpression extends BaseReadOnlyValueExpression {

        private static final long serialVersionUID = -1579256471133808739L;

        public SkinningResourceRenderedExpression() {
            super(Boolean.TYPE);
        }

        @Override
        public Object getValue(ELContext context) {
            FacesContext facesContext = getFacesContext(context);

            ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);
            
            return configurationService.getBooleanValue(facesContext, CoreConfiguration.Items.standardControlsSkinning) ||
                configurationService.getBooleanValue(facesContext, CoreConfiguration.Items.standardControlsSkinningClasses);
        }
        
    }

    private UIComponent createComponentResource(FacesContext context) {
        Application application = context.getApplication();
        String rendererType = application.getResourceHandler().getRendererTypeForResourceName(BOTH_SKINNING);
        UIComponent resourceComponent = application.createComponent(context, UIOutput.COMPONENT_TYPE, rendererType);

        return resourceComponent;
    }
    
    public void afterPhase(PhaseEvent event) {
        //not used
    }

    public void beforePhase(PhaseEvent event) {
        //it's important for skinning resources to come *before* any users/components stylesheet, 
        //that's why they are added via phase listener
        
        FacesContext context = event.getFacesContext();
        UIViewRoot viewRoot = context.getViewRoot();
        
        assert viewRoot != null;

        boolean skinnigResourceFound = false;
        List<UIComponent> resources = viewRoot.getComponentResources(context, HEAD);
        for (UIComponent resource : resources) {
            if (resource.getAttributes().get(SKINNING_RESOURCE_MARKER) != null) {
                skinnigResourceFound = true;
                break;
            }
        }

        if (!skinnigResourceFound) {
            UIComponent basic = createComponentResource(context);
            basic.setValueExpression("name", new SkinningResourceNameExpression());
            basic.setValueExpression("rendered", new SkinningResourceRenderedExpression());
            basic.getAttributes().put(SKINNING_RESOURCE_MARKER, Boolean.TRUE);
            
            viewRoot.addComponentResource(context, basic);
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
