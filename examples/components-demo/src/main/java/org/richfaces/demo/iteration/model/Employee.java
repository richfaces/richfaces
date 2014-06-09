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

package org.richfaces.demo.iteration.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.richfaces.event.CollapsibleSubTableToggleEvent;

public class Employee {
    private static String[] phoneNames = { "Cell phone", "Work phone", "Home phone" };
    private String name;
    private String title;
    private List<Company> companies;
    private String eMail;
    private boolean expand;
    private List<String[]> phones = new ArrayList<String[]>();

    public Employee(String name, String title) {
        this.name = name;
        this.title = title;
        initPhones();
    }

    private void initPhones() {
        Random random = new Random();
        int count = random.nextInt(phoneNames.length + 1);
        for (int i = 0; i < count; i++) {
            phones.add(new String[] { phoneNames[i], "+" + random.nextInt(1000) + "-" + random.nextInt(1000000000) });
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public void setPhones(List<String[]> phones) {
        this.phones = phones;
    }

    public List<String[]> getPhones() {
        return phones;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) obj;
        return name.equals(employee.getName()) && title.equals(employee.getTitle())
                && (companies == null ? employee.getCompanies() == null : companies.equals(employee.getCompanies()));
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + name.hashCode();
        hash = hash * 17 + title.hashCode();
        hash = hash * 17 + (companies == null ? 0 : companies.hashCode());
        return hash;
    }

    public void toggle(CollapsibleSubTableToggleEvent event) {
        this.expand = event.isExpanded();
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}