/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractDragSource;
import org.richfaces.component.AbstractDropTarget;
import org.richfaces.event.DropEvent;
import org.richfaces.javascript.DnDScript;
import org.richfaces.javascript.DropScript;
import org.richfaces.renderkit.util.AjaxRendererUtils;
import org.richfaces.util.Sets;

import com.google.common.base.Strings;

/**
 * @author abelevich
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "com.jqueryui", name = "core.js"),
        @ResourceDependency(library = "com.jqueryui", name = "widget.js"),
        @ResourceDependency(library = "com.jqueryui", name = "mouse.js"),
        @ResourceDependency(library = "com.jqueryui", name = "draggable.js"),
        @ResourceDependency(library = "com.jqueryui", name = "droppable.js"),
        @ResourceDependency(library = "org.richfaces", name = "dnd-droppable.js") })
@JsfRenderer(type = "org.richfaces.DropTargetRenderer", family = AbstractDropTarget.COMPONENT_FAMILY)
public class DropTargetRenderer extends DnDRenderBase {
    /**
     *
     */
    private static final Set<String> ALL_SET = Collections.singleton("@all");
    /**
     *
     */
    private static final Set<String> NONE_SET = Collections.singleton("@none");

    @Override
    protected void doDecode(FacesContext facesContext, UIComponent component) {
        Map<String, String> requestParamMap = facesContext.getExternalContext().getRequestParameterMap();

        if (requestParamMap.get(component.getClientId(facesContext)) == null) {
            return;
        }

        String dragSourceId = (String) requestParamMap.get("dragSource");
        if (Strings.isNullOrEmpty(dragSourceId)) {
            return;
        }

        DragSourceContextCallBack dragSourceContextCallBack = new DragSourceContextCallBack();
        boolean invocationResult = facesContext.getViewRoot().invokeOnComponent(facesContext, dragSourceId,
            dragSourceContextCallBack);

        if (!invocationResult) {
            // TODO - log
            return;
        }

        AbstractDropTarget dropTarget = (AbstractDropTarget) component;
        new DropEvent(dropTarget, dropTarget.getDropValue(), dragSourceContextCallBack.getDragSource(),
            dragSourceContextCallBack.getDragValue()).queue();
    }

    private final class DragSourceContextCallBack implements ContextCallback {
        private AbstractDragSource dragSource;
        private Object dragValue;

        public void invokeContextCallback(FacesContext context, UIComponent target) {
            if (target instanceof AbstractDragSource) {
                this.dragSource = (AbstractDragSource) target;
                this.dragValue = this.dragSource.getDragValue();
            }
        }

        public AbstractDragSource getDragSource() {
            return dragSource;
        }

        public Object getDragValue() {
            return dragValue;
        }
    }

    @Override
    public DnDScript createScript(String name) {
        return new DropScript(name);
    }

    @Override
    public Map<String, Object> getOptions(FacesContext facesContext, UIComponent component) {
        Map<String, Object> options = new HashMap<String, Object>();

        if (component instanceof AbstractDropTarget) {
            JSReference dragSourceId = new JSReference("dragSourceId");
            JSFunctionDefinition function = new JSFunctionDefinition(JSReference.EVENT, dragSourceId);

            AjaxFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(facesContext, component);
            ajaxFunction.getOptions().setParameter("dragSource", dragSourceId);
            ajaxFunction.getOptions().setParameter(component.getClientId(facesContext), component.getClientId(facesContext));
            ajaxFunction.setSource(new JSReference("event", "target"));
            ajaxFunction.getOptions().setAjaxComponent(component.getClientId(facesContext));
            function.addToBody(ajaxFunction);

            AbstractDropTarget dropTarget = (AbstractDropTarget) component;
            Set<String> acceptedTypes = Sets.asSet(dropTarget.getAcceptedTypes());

            if (acceptedTypes != null) {
                if (acceptedTypes.contains("@none")) {
                    acceptedTypes = NONE_SET;
                } else if (acceptedTypes.contains("@all")) {
                    acceptedTypes = ALL_SET;
                }
            }

            options.put("acceptedTypes", acceptedTypes);
            options.put("ajaxFunction", function);
            options.put("parentId", getParentClientId(facesContext, component));
        }
        return options;
    }

    @Override
    public String getScriptName() {
        return "new RichFaces.ui.Droppable";
    }
}
