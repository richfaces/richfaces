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

package org.richfaces.demo.common;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * @author Ilya Shaikovsky
 */
@ManagedBean(name = "selectsBean")
@RequestScoped
public class SelectsBean {
    private static final String[] FRUITS = { "", "Banana", "Cranberry", "Blueberry", "Orange" };
    private static final String[] VEGETABLES = { "", "Potatoes", "Broccoli", "Garlic", "Carrot" };
    private String currentItem = "";
    private String currentType = "";
    private List<SelectItem> firstList = new ArrayList<SelectItem>();
    private List<SelectItem> secondList = new ArrayList<SelectItem>();

    public SelectsBean() {
        SelectItem item = new SelectItem("", "");

        firstList.add(item);
        item = new SelectItem("fruits", "Fruits");
        firstList.add(item);
        item = new SelectItem("vegetables", "Vegetables");
        firstList.add(item);

        for (int i = 0; i < FRUITS.length; i++) {
            item = new SelectItem(FRUITS[i]);
        }
    }

    public List<SelectItem> getFirstList() {
        return firstList;
    }

    public List<SelectItem> getSecondList() {
        return secondList;
    }

    public static String[] getFRUITS() {
        return FRUITS;
    }

    public static String[] getVEGETABLES() {
        return VEGETABLES;
    }

    public void valueChanged(ValueChangeEvent event) {
        secondList.clear();
        if (null != event.getNewValue()) {
            String[] currentItems;

            if (((String) event.getNewValue()).equals("fruits")) {
                currentItems = FRUITS;
            } else {
                currentItems = VEGETABLES;
            }

            for (int i = 0; i < currentItems.length; i++) {
                SelectItem item = new SelectItem(currentItems[i]);

                secondList.add(item);
            }
        }
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public String getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(String currentItem) {
        this.currentItem = currentItem;
    }
}
