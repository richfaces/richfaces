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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.ScriptStringBase;
import org.richfaces.component.AbstractTree;

/**
 * @author Nick Belaevski
 * 
 */
public final class TreeRenderingContext {

    private static final String ATTRIBUTE_NAME = TreeRenderingContext.class.getName() + ":ATTRIBUTE_NAME";
    
    public static final class Handlers extends ScriptStringBase {
        
        private String toggleHandler;
        
        private String treeToggleHandler;
        
        private String selectHandler;

        private String treeSelectHandler;

        public void setSelectHandler(String selectHandler) {
            this.selectHandler = selectHandler;
        }
        
        public String getSelectHandler() {
            return selectHandler;
        }

        public void setToggleHandler(String toggleHandler) {
            this.toggleHandler = toggleHandler;
        }
        
        public String getToggleHandler() {
            return toggleHandler;
        }

        public String getTreeToggleHandler() {
            return treeToggleHandler;
        }

        public void setTreeToggleHandler(String treeToggleHandler) {
            this.treeToggleHandler = treeToggleHandler;
        }

        public String getTreeSelectHandler() {
            return treeSelectHandler;
        }

        public void setTreeSelectHandler(String treeSelectHandler) {
            this.treeSelectHandler = treeSelectHandler;
        }
        
        public void appendScript(Appendable target) throws IOException {
            // TODO Auto-generated method stub
            
        }
    }
    
    private FacesContext context;
    
    private AbstractTree tree;
    
    private String baseClientId;
    
    private Map<String, Handlers> handlersMap = new HashMap<String, Handlers>();

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

    public void addHandlers(String toggleHandler, String treeToggleHandler, String selectHandler, String treeSelectHandler) {
        String clientId = tree.findTreeNodeComponent().getClientId(context);
        
        String relativeClientId = clientId.substring(baseClientId.length()); 
        
        if (isNullOrEmpty(toggleHandler) && isNullOrEmpty(treeToggleHandler) 
            && isNullOrEmpty(selectHandler) && isNullOrEmpty(treeSelectHandler)) {
            
            return;
        }
        
        Handlers handlers = new Handlers();
        handlers.setToggleHandler(toggleHandler);
        handlers.setTreeToggleHandler(treeToggleHandler);
        handlers.setSelectHandler(selectHandler);
        handlers.setTreeSelectHandler(treeSelectHandler);
        
        handlersMap.put(relativeClientId, handlers);
    }

    public Object getHandlers() {
        return handlersMap;
    }
}
