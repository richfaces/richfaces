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

package org.richfaces.demo.iteration;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "rf10859")
@SessionScoped
public class RF10859 implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<DataItem> listDataItems = new LinkedList<DataItem>() {
        private static final long serialVersionUID = 1L;

        {
            add(new DataItem());
            add(new DataItem());
            add(new DataItem());
        }
    };

    public List<DataItem> getListDataItems() {
        return listDataItems;
    }

    public void setListDataItems(List<DataItem> listDataItems) {
        this.listDataItems = listDataItems;
    }

    public static class DataItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private List<Inner> list = new LinkedList<Inner>() {
            private static final long serialVersionUID = 1L;

            {
                add(new Inner());
            }
        };

        public List<Inner> getList() {
            return list;
        }

        public void setList(List<Inner> list) {
            this.list = list;
        }
    }

    public static class Inner implements Serializable {
        private static final long serialVersionUID = 1L;

        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
