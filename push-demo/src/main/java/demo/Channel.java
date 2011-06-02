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
package demo;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 * @author Nick Belaevski
 *
 */
public class Channel implements Serializable {
    private static final long serialVersionUID = 6798558262812940593L;
    private String name;
    private boolean rendered = true;

    public Channel(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public void processChannelStateChange(ValueChangeEvent event) {
        setRendered(Boolean.TRUE.equals(event.getNewValue()));

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ChatBean chatBean = (ChatBean) externalContext.getSessionMap().get("chatBean");

        chatBean.handleStateChange(this);
    }
}
