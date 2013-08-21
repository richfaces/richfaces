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
package org.richfaces.renderkit;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.richfaces.renderkit.TreeRendererBase.getToggleTypeOrDefault;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.SwitchType;
import org.richfaces.javascript.JSFunction;
import org.richfaces.javascript.JSReference;
import org.richfaces.javascript.ScriptStringBase;
import org.richfaces.javascript.ScriptUtils;
import org.richfaces.ui.common.ComponentAttribute;

/**
 * @author Nick Belaevski
 *
 */
public final class TreeRenderingContext {
    private static final String ATTRIBUTE_NAME = TreeRenderingContext.class.getName() + ":ATTRIBUTE_NAME";
    private static final ComponentAttribute ONTOGGLE_ATTRIBUTE = new ComponentAttribute("ontoggle").setEventNames("toggle");
    private static final ComponentAttribute ONNODETOGGLE_ATTRIBUTE = new ComponentAttribute("onnodetoggle")
        .setEventNames("nodetoggle");
    private static final ComponentAttribute ONBEFORETOGGLE_ATTRIBUTE = new ComponentAttribute("onbeforetoggle")
        .setEventNames("beforetoggle");
    private static final ComponentAttribute ONBEFORENODETOGGLE_ATTRIBUTE = new ComponentAttribute("onbeforenodetoggle")
        .setEventNames("beforenodetoggle");

    public static final class Handlers extends ScriptStringBase {
        private String toggleHandler;
        private String nodeToggleHandler;
        private String beforeToggleHandler;
        private String beforeNodeToggleHandler;

        protected Object chain(String firstHandler, String secondHandler) {
            if (isNullOrEmpty(firstHandler) && isNullOrEmpty(secondHandler)) {
                return null;
            }

            if (isNullOrEmpty(firstHandler)) {
                return secondHandler;
            }

            if (isNullOrEmpty(secondHandler)) {
                return firstHandler;
            }

            return new JSFunction("return jsf.util.chain", JSReference.THIS, JSReference.EVENT, firstHandler, secondHandler)
                .toScript();
        }

        public void setToggleHandler(String toggleHandler) {
            this.toggleHandler = toggleHandler;
        }

        public String getToggleHandler() {
            return toggleHandler;
        }

        public void setNodeToggleHandler(String nodeToggleHandler) {
            this.nodeToggleHandler = nodeToggleHandler;
        }

        public String getNodeToggleHandler() {
            return nodeToggleHandler;
        }

        public void setBeforeToggleHandler(String beforeToggleHandler) {
            this.beforeToggleHandler = beforeToggleHandler;
        }

        public String getBeforeToggleHandler() {
            return beforeToggleHandler;
        }

        public void setBeforeNodeToggleHandler(String beforeNodeToggleHandler) {
            this.beforeNodeToggleHandler = beforeNodeToggleHandler;
        }

        public String getBeforeNodeToggleHandler() {
            return beforeNodeToggleHandler;
        }

        public void appendScript(Appendable target) throws IOException {
            Object chainedToggleHandler = chain(toggleHandler, nodeToggleHandler);
            Object chainedBeforeToggleHandler = chain(beforeToggleHandler, beforeNodeToggleHandler);

            if (chainedToggleHandler != null || chainedBeforeToggleHandler != null) {
                Map<String, Object> map = new HashMap<String, Object>(2);

                if (chainedToggleHandler != null) {
                    map.put("th", chainedToggleHandler);
                }

                if (chainedBeforeToggleHandler != null) {
                    map.put("bth", chainedBeforeToggleHandler);
                }

                ScriptUtils.appendScript(target, map);
            } else {
                ScriptUtils.appendScript(target, null);
            }
        }
    }

    private FacesContext context;
    private AbstractTree tree;
    private String baseClientId;
    private Map<String, Handlers> handlersMap = new HashMap<String, Handlers>();
    private Handlers handlers;

    private TreeRenderingContext(FacesContext context, AbstractTree tree) {
        super();
        this.context = context;
        this.tree = tree;
        this.baseClientId = tree.getClientId(context);
    }

    public static TreeRenderingContext create(FacesContext context, AbstractTree tree) {
        TreeRenderingContext renderingContext = new TreeRenderingContext(context, tree);
        context.getAttributes().put(ATTRIBUTE_NAME, renderingContext);
        return renderingContext;
    }

    public static TreeRenderingContext get(FacesContext context) {
        return (TreeRenderingContext) context.getAttributes().get(ATTRIBUTE_NAME);
    }

    public static void delete(FacesContext context) {
        context.getAttributes().remove(ATTRIBUTE_NAME);
    }

    private Handlers getOrCreateHandlers(String relativeClientId) {
        if (handlers == null) {
            handlers = new Handlers();
            handlersMap.put(relativeClientId, handlers);
        }
        return handlers;
    }

    public void addHandlers(AbstractTreeNode treeNode) {
        handlers = null;

        String clientId = treeNode.getClientId(context);

        String relativeClientId = clientId.substring(baseClientId.length());

        if (getToggleTypeOrDefault(tree) != SwitchType.server) {
            String toggleHandler = (String) RenderKitUtils.getAttributeAndBehaviorsValue(context, treeNode, ONTOGGLE_ATTRIBUTE);
            if (!isNullOrEmpty(toggleHandler)) {
                getOrCreateHandlers(relativeClientId).setToggleHandler(toggleHandler);
            }

            String beforeToggleHandler = (String) RenderKitUtils.getAttributeAndBehaviorsValue(context, treeNode,
                ONBEFORETOGGLE_ATTRIBUTE);
            if (!isNullOrEmpty(beforeToggleHandler)) {
                getOrCreateHandlers(relativeClientId).setBeforeToggleHandler(beforeToggleHandler);
            }

            String nodeToggleHandler = (String) RenderKitUtils.getAttributeAndBehaviorsValue(context, tree,
                ONNODETOGGLE_ATTRIBUTE);
            if (!isNullOrEmpty(nodeToggleHandler)) {
                getOrCreateHandlers(relativeClientId).setNodeToggleHandler(nodeToggleHandler);
            }

            String beforeNodeToggleHandler = (String) RenderKitUtils.getAttributeAndBehaviorsValue(context, tree,
                ONBEFORENODETOGGLE_ATTRIBUTE);
            if (!isNullOrEmpty(beforeNodeToggleHandler)) {
                getOrCreateHandlers(relativeClientId).setBeforeNodeToggleHandler(beforeNodeToggleHandler);
            }
        }
    }

    public Object getHandlers() {
        return handlersMap;
    }
}
