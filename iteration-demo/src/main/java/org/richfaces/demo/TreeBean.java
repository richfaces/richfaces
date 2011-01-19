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
package org.richfaces.demo;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.swing.tree.TreeNode;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.SwitchType;
import org.richfaces.event.TreeSelectionChangeEvent;
import org.richfaces.event.TreeSelectionChangeListener;
import org.richfaces.event.TreeToggleEvent;
import org.richfaces.event.TreeToggleListener;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;

/**
 * @author Nick Belaevski
 * 
 */
@ManagedBean
@SessionScoped
public class TreeBean implements Serializable {

    public static final class SelectionChangeHandler implements TreeSelectionChangeListener {

        private boolean fromExpression = false;

        public SelectionChangeHandler() {
        }
        
        public SelectionChangeHandler(boolean fromExpression) {
            super();
            this.fromExpression = fromExpression;
        }

        public void processSelectionChange(TreeSelectionChangeEvent event) throws AbortProcessingException {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            
            facesContext.addMessage(getTree(event).getClientId(facesContext), createEventMessage(event, fromExpression));
        }
        
    }
    
    public static final class ToggleHandler implements TreeToggleListener {

        private boolean fromExpression = false;

        public ToggleHandler() {
        }
        
        public ToggleHandler(boolean fromExpression) {
            super();
            this.fromExpression = fromExpression;
        }

        public void processToggle(TreeToggleEvent event) throws AbortProcessingException {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(getTree(event).getClientId(facesContext), createEventMessage(event, fromExpression));
        }
        
    }

    private static final long serialVersionUID = 3368885134614548497L;

    private static final Logger LOGGER = LogFactory.getLogger(TreeBean.class);
    
    private List<TreeNode> rootNodes;
    
    private List<TreeNode> lazyRootNodes;
    
    private SwitchType toggleType = SwitchType.DEFAULT;
    
    private SwitchType selectionType = SwitchType.client;

    private boolean showCustomClasses = true;
    
    private Collection<Object> selection = new TracingSet<Object>();

    private String toggleNodeEvent = "";
    
    private String executeTestText;
    
    private static Object staticGetNodeData() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().evaluateExpressionGet(facesContext, "#{node}", Object.class);
    }
    
    private static FacesMessage createEventMessage(FacesEvent event, boolean fromExpression) {
        String summary = event + (fromExpression ? " called from attribute" : " called from tag") + ", data: " + staticGetNodeData();
        return new FacesMessage(summary);
    }
    
    private static AbstractTree getTree(FacesEvent event) {
        if (event.getComponent() instanceof AbstractTree) {
            return (AbstractTree) event.getComponent();
        }
        
        return ((AbstractTreeNode) event.getComponent()).findTreeComponent();
    }
    
    private List<TreeNode> createLazyNodes(List<TreeNode> nodes) {
        List<TreeNode> result = new ArrayList<TreeNode>(nodes.size());

        for (TreeNode srcNode : nodes) {
            result.add(new LazyTreeNode(srcNode));
        }
        
        return result;
    }
    
    @PostConstruct
    public void init() {
        try {
            TreeNodeParser parser = new TreeNodeParser();
            parser.parse(TreeBean.class.getResource("plants.xml"));
            rootNodes = parser.getRootNodes();
            lazyRootNodes = createLazyNodes(rootNodes);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public List<TreeNode> getRootNodes() {
        return rootNodes;
    }

    public List<TreeNode> getLazyRootNodes() {
        return lazyRootNodes;
    }
    
    public SwitchType[] getTypes() {
        return SwitchType.values();
    }
    
    public SwitchType getToggleType() {
        return toggleType;
    }
    
    public void setToggleType(SwitchType switchType) {
        this.toggleType = switchType;
    }
    
    public SwitchType getSelectionType() {
        return selectionType;
    }
    
    public void setSelectionType(SwitchType selectionMode) {
        this.selectionType = selectionMode;
    }
    
    public Object getNodeData() {
        return staticGetNodeData();
    }
    
    public Collection<Object> getSelection() {
        return selection;
    }
    
    public void setSelection(Collection<Object> selection) {
        this.selection = selection;
    }
    
    public void clickNode() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage("Clicked node: " + getNodeData()));
    }
    
    public boolean isShowCustomClasses() {
        return showCustomClasses;
    }
    
    public void setShowCustomClasses(boolean showCustomClasses) {
        this.showCustomClasses = showCustomClasses;
    }
    
    public void behaviorToggleListener(AjaxBehaviorEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage("Toggle node: " + getNodeData() + ", source is: " + event.getSource()));
    }

    public void behaviorSelectionChangeListener(AjaxBehaviorEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(event.getComponent().getClientId(facesContext), new FacesMessage("Selection changed, source is: " + event.getSource()));
    }
    
    public void processSelectionChange(TreeSelectionChangeEvent event) {
        new SelectionChangeHandler(true).processSelectionChange(event);
    }
    
    public void processToggle(TreeToggleEvent event) {
        new ToggleHandler(true).processToggle(event);
    }

    public String getToggleNodeEvent() {
        return toggleNodeEvent;
    }

    public void setToggleNodeEvent(String toggleNodeEvent) {
        this.toggleNodeEvent = toggleNodeEvent;
    }

    public String getExecuteTestText() {
        return executeTestText;
    }
    
    public void setExecuteTestText(String executeTestText) {
        this.executeTestText = executeTestText;
    }

    public String getCurrentTimeAsString() {
        return DateFormat.getTimeInstance().format(new Date());
    }
}
