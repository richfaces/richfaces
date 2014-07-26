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
package org.richfaces.application;

import java.io.ObjectStreamException;
import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.el.BaseReadOnlyValueExpression;

/**
 * @author Nick Belaevski
 *
 */
public class GlobalResourcesViewHandler extends ViewHandlerWrapper {
    private static final String SKINNING_RESOURCE_ID = "__rf_skinning_resource";
    private static final String CLASSES_ECSS = "_classes.ecss";
    private static final String ECSS = ".ecss";
    private static final String BOTH_ECSS = "_both.ecss";
    private static final String CONTROLS_SKINNING;
    private static final String BOTH_SKINNING;
    private static final String CLASSES_SKINNING;
    private static final String HEAD = "head";
    private static final String LIBRARY = "org.richfaces";

    static {
        String skinningName = "skinning";

        CONTROLS_SKINNING = skinningName + ECSS;
        BOTH_SKINNING = skinningName + BOTH_ECSS;
        CLASSES_SKINNING = skinningName + CLASSES_ECSS;
    }

    private ViewHandler viewHandler;

    public GlobalResourcesViewHandler(ViewHandler viewHandler) {
        super();
        this.viewHandler = viewHandler;
    }

    @Override
    public ViewHandler getWrapped() {
        return viewHandler;
    }

    private static final class SkinningResourceNameExpression extends BaseReadOnlyValueExpression {
        public static final ValueExpression INSTANCE = new SkinningResourceNameExpression();
        private static final long serialVersionUID = 7520575496522682120L;

        private SkinningResourceNameExpression() {
            super(String.class);
        }

        @Override
        public Object getValue(ELContext context) {
            FacesContext facesContext = getFacesContext(context);

            ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);

            boolean controls = configurationService.getBooleanValue(facesContext,
                CoreConfiguration.Items.standardControlsSkinning);
            boolean classes = configurationService.getBooleanValue(facesContext,
                CoreConfiguration.Items.standardControlsSkinningClasses);

            if (controls && classes) {
                return BOTH_SKINNING;
            }

            if (classes) {
                return CLASSES_SKINNING;
            }

            return CONTROLS_SKINNING;
        }

        private Object readResolve() throws ObjectStreamException {
            return INSTANCE;
        }
    }

    private static final class SkinningResourceRenderedExpression extends BaseReadOnlyValueExpression {
        public static final ValueExpression INSTANCE = new SkinningResourceRenderedExpression();
        private static final long serialVersionUID = -1579256471133808739L;

        private SkinningResourceRenderedExpression() {
            super(Boolean.TYPE);
        }

        @Override
        public Object getValue(ELContext context) {
            FacesContext facesContext = getFacesContext(context);

            ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);

            return configurationService.getBooleanValue(facesContext, CoreConfiguration.Items.standardControlsSkinning)
                || configurationService.getBooleanValue(facesContext, CoreConfiguration.Items.standardControlsSkinningClasses);
        }

        private Object readResolve() throws ObjectStreamException {
            return INSTANCE;
        }
    }

    private UIComponent createComponentResource(FacesContext context) {
        Application application = context.getApplication();

        // renderkit id is not set on FacesContext at this point, so calling
        // application.createComponent(context, componentType, rendererType) causes NPE
        UIComponent resourceComponent = application.createComponent(UIOutput.COMPONENT_TYPE);

        String rendererType = application.getResourceHandler().getRendererTypeForResourceName(BOTH_SKINNING);
        resourceComponent.setRendererType(rendererType);

        return resourceComponent;
    }

    private UIViewRoot addSkinningResourcesToViewRoot(FacesContext context, UIViewRoot viewRoot) {
        if (viewRoot != null) {
            boolean skinningResourceFound = false;
            List<UIComponent> resources = viewRoot.getComponentResources(context, HEAD);
            for (UIComponent resource : resources) {
                if (SKINNING_RESOURCE_ID.equals(resource.getId())) {
                    skinningResourceFound = true;
                    break;
                }
            }

            if (!skinningResourceFound) {
                // it's important for skinning resources to come *before* any users/components stylesheet,
                // that's why they are *always* added here
                UIComponent basic = createComponentResource(context);
                basic.getAttributes().put("library", LIBRARY);
                basic.setValueExpression("name", SkinningResourceNameExpression.INSTANCE);
                basic.setValueExpression("rendered", SkinningResourceRenderedExpression.INSTANCE);
                basic.setId(SKINNING_RESOURCE_ID);

                // workaround for Mojarra: RF-8937
                boolean initialProcessingEvents = context.isProcessingEvents();
                context.setProcessingEvents(false);
                viewRoot.addComponentResource(context, basic);
                context.setProcessingEvents(initialProcessingEvents);
            }
        }

        return viewRoot;
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        UIViewRoot viewRoot = super.restoreView(context, viewId);

        return addSkinningResourcesToViewRoot(context, viewRoot);
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot viewRoot = super.createView(context, viewId);

        return addSkinningResourcesToViewRoot(context, viewRoot);
    }
}
