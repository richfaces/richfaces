/**
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
 **/
package org.richfaces.component.ajax;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@SessionScoped
@Named
public class AjaxBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String value;

    public AjaxBean() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void clearValue(javax.faces.event.AjaxBehaviorEvent event) {
        this.value = null;
    }

    public void listener(String newValue) {
        this.value = newValue;
    }

    public void methodA(ActionEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("AjaxBean.methodA()"));
    }

    public void methodB(ActionEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("AjaxBean.methodB()"));
    }
}
