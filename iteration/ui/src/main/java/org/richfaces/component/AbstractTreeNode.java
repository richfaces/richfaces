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

import java.io.IOException;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.ajax4jsf.component.IterationStateHolder;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.event.TreeToggleEvent;
import org.richfaces.event.TreeToggleListener;
import org.richfaces.event.TreeToggleSource;
import org.richfaces.renderkit.MetaComponentRenderer;

/**
 * @author Nick Belaevski
 * 
 */
@JsfComponent(
    type = AbstractTreeNode.COMPONENT_TYPE,
    family = AbstractTreeNode.COMPONENT_FAMILY, 
    tag = @Tag(name = "treeNode", handler = "org.richfaces.view.facelets.TreeNodeHandler"),
    renderer = @JsfRenderer(type = "org.richfaces.TreeNodeRenderer"),
    attributes = {"events-props.xml", "core-props.xml", "i18n-props.xml"}
)
public abstract class AbstractTreeNode extends UIComponentBase implements MetaComponentResolver, MetaComponentEncoder, IterationStateHolder, TreeToggleSource {

    public static final String COMPONENT_TYPE = "org.richfaces.TreeNode";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.TreeNode";

    public static final String SUBTREE_META_COMPONENT_ID = "subtree";
    
    enum PropertyKeys {
        expanded
    }

    @Attribute(generate = false, signature = @Signature(returnType = Void.class, parameters = TreeToggleEvent.class))
    private MethodExpression toggleListener;

    public AbstractTreeNode() {
        setRendererType("org.richfaces.TreeNodeRenderer");
    }
    
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    @Attribute
    public abstract boolean isImmediate();
    
    public abstract String getType();
    
    public abstract String getIconLeaf();
    
    public abstract String getIconExpanded();
    
    public abstract String getIconCollapsed();

    public abstract String getHandleClass();
    
    public abstract String getIconClass();
    
    public abstract String getLabelClass();
    
    @Attribute(events = @EventName("toggle"))
    public abstract String getOntoggle();
    
    @Attribute(events = @EventName("beforetoggle"))
    public abstract String getOnbeforetoggle();
    
    protected Boolean getLocalExpandedValue(FacesContext facesContext) {
        return (Boolean) getStateHelper().get(PropertyKeys.expanded);
    }

    public boolean isExpanded() {
        FacesContext context = getFacesContext();
        Boolean localExpandedValue = getLocalExpandedValue(context);
        if (localExpandedValue != null) {
            return localExpandedValue.booleanValue();
        }

        ValueExpression ve = getValueExpression(PropertyKeys.expanded.toString());
        if (ve != null) {
            return Boolean.TRUE.equals(ve.getValue(context.getELContext()));
        }

        return false;
    }

    public void setExpanded(boolean newValue) {
        getStateHelper().put(PropertyKeys.expanded, newValue);
    }

    public Object getIterationState() {
        return getStateHelper().get(PropertyKeys.expanded);
    }
    
    public void setIterationState(Object state) {
        getStateHelper().put(PropertyKeys.expanded, state);
    }

    public AbstractTree findTreeComponent() {
        UIComponent c = this;
        while (c != null && !(c instanceof AbstractTree)) {
            c = c.getParent();
        }
        
        return (AbstractTree) c;
    }
    
    @Override
    public void queueEvent(FacesEvent event) {
        if (this.equals(event.getComponent())) {
            if (event instanceof TreeToggleEvent) {
                PhaseId targetPhase = (isImmediate() || findTreeComponent().isImmediate()) ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.PROCESS_VALIDATIONS;
                event.setPhaseId(targetPhase);
            }
        }
        
        super.queueEvent(event);
    }
    
    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        
        if (event instanceof TreeToggleEvent) {
            TreeToggleEvent toggleEvent = (TreeToggleEvent) event;
            new TreeToggleEvent(findTreeComponent(), toggleEvent.isExpanded()).queue();
        }
    }

    public void addTreeToggleListener(TreeToggleListener listener) {
        addFacesListener(listener);
    }

    @Attribute(hidden = true)
    public TreeToggleListener[] getTreeToggleListeners() {
        return (TreeToggleListener[]) getFacesListeners(TreeToggleListener.class);
    }
    
    public void removeTreeToggleListener(TreeToggleListener listener) {
        removeFacesListener(listener);
    }
    
    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (SUBTREE_META_COMPONENT_ID.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }
        
        return null;
    }
    
    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent,
        String metaComponentId) {

        return null;
    }
    
    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (context instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {
                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, SUBTREE_META_COMPONENT_ID);
                
                if (result != VisitResult.ACCEPT) {
                    return result == VisitResult.COMPLETE;
                }
            }
        }
        
        return super.visitTree(context, callback);
    }
    
    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }
}
