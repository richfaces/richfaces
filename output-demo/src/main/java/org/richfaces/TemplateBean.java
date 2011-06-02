/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author akolonitsky
 * @since Nov 2, 2010
 */
@ManagedBean
@SessionScoped
public class TemplateBean implements Serializable {
    private static final long serialVersionUID = 5078700314562231363L;
    private List<String> dataTableModel = new ArrayList<String>();
    private boolean renderForm;

    @PostConstruct
    public void init() {
        dataTableModel.add("row 1");
        dataTableModel.add("row 2");
        dataTableModel.add("row 3");
        dataTableModel.add("row 4");
    }

    public List<String> getDataTableModel() {
        return dataTableModel;
    }

    public boolean isRenderForm() {
        return renderForm;
    }

    public void setRenderForm(boolean renderForm) {
        this.renderForm = renderForm;
    }
}
