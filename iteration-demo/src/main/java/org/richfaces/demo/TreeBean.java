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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.swing.tree.TreeNode;

import org.richfaces.component.SwitchType;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;

/**
 * @author Nick Belaevski
 * 
 */
@ManagedBean
@SessionScoped
public class TreeBean implements Serializable {

    private static final long serialVersionUID = 3368885134614548497L;

    private static final Logger LOGGER = LogFactory.getLogger(TreeBean.class);
    
    private List<TreeNode> rootNodes;
    
    private SwitchType toggleMode = SwitchType.DEFAULT;
    
    private Object nodeData;
    
    @PostConstruct
    public void init() {
        try {
            TreeNodeParser parser = new TreeNodeParser();
            parser.parse(TreeBean.class.getResource("plants.xml"));
            rootNodes = parser.getRootNodes();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public List<TreeNode> getRootNodes() {
        return rootNodes;
    }

    public SwitchType[] getModes() {
        return SwitchType.values();
    }
    
    public SwitchType getToggleMode() {
        return toggleMode;
    }
    
    public void setToggleMode(SwitchType switchType) {
        this.toggleMode = switchType;
    }
    
    public Object getNodeData() {
        return nodeData;
    }
    
    public void setNodeData(Object nodeData) {
        this.nodeData = nodeData;
    }
    
    public void clickNode() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage("Clicked node: " + getNodeData()));
    }
}
