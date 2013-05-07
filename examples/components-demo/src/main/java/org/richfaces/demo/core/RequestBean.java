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
package org.richfaces.demo.core;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean(name = "requestBean")
@RequestScoped
public class RequestBean {
    private String idsToRender;

    public String getIdsToRender() {
        return idsToRender;
    }

    public void setIdsToRender(String idsToRender) {
        this.idsToRender = idsToRender;
    }

    public void setupIdsToRender() {
        this.idsToRender = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idsToRender");
    }

    public Object getItems() {
        SelectItem[] item = new SelectItem[1000];

        for (int i = 0; i < item.length; i++) {
            item[i] = new SelectItem(i);
        }

        return item;
    }
}
