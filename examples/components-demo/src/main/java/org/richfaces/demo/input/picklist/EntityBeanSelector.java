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
package org.richfaces.demo.input.picklist;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@RequestScoped
@ManagedBean()
public class EntityBeanSelector implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<EntityBean> values;

    public EntityBeanSelector() {
        System.out.println("post construct: initialize beanselector");
        values = new ArrayList<EntityBean>();
        values.add(new EntityBean("Option 0", "0"));
        values.add(new EntityBean("Option 1", "1"));
        values.add(new EntityBean("Option 2", "2"));
        values.add(new EntityBean("Option 3", "3"));
        values.add(new EntityBean("Option 4", "4"));
    }

    public List<EntityBean> getValues() {
        return values;
    }

    public void setValues(List<EntityBean> values) {
        this.values = values;
    }
}
