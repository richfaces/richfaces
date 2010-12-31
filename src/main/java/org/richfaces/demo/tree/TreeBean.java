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
package org.richfaces.demo.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.swing.tree.TreeNode;

import org.richfaces.component.UITree;
import org.richfaces.demo.tree.model.CD;
import org.richfaces.demo.tree.model.Company;
import org.richfaces.demo.tree.model.Country;
import org.richfaces.event.TreeSelectionChangeEvent;

/**
 * @author Ilya Shaikovsky
 * 
 */
@ManagedBean
@ApplicationScoped
public class TreeBean implements Serializable {
    @ManagedProperty(value = "#{cdsParser.cdsList}")
    private List<CDXmlDescriptor> cdXmlDescriptors;
    private List<TreeNode> rootNodes = new ArrayList<TreeNode>();
    private Map<String, Country> countriesCache = new HashMap<String, Country>();
    private Map<String, Company> companiesCache = new HashMap<String, Company>();
    private TreeNode currentSelection = null;

    @PostConstruct
    public void init() {
        for (CDXmlDescriptor current : cdXmlDescriptors) {
            String countryName = current.getCountry();
            String companyName = current.getCompany();
            Country country = getCountryByName(current);
            Company company = getCompanyByName(current, country);
            CD cd = new CD(current.getTitle(), current.getArtist(), company, current.getPrice(), current.getYear());
            company.getCds().add(cd);
        }
    }

    public void selectionChanged(TreeSelectionChangeEvent selectionChangeEvent) {
        // considering only single selection
        List<Object> selection = new ArrayList<Object>(selectionChangeEvent.getNewSelection());
        Object currentSelectionKey = selection.get(0);
        UITree tree = (UITree) selectionChangeEvent.getSource();

        Object storedKey = tree.getRowKey();
        tree.setRowKey(currentSelectionKey);
        currentSelection = (TreeNode) tree.getRowData();
        tree.setRowKey(storedKey);
    }

    private Country getCountryByName(CDXmlDescriptor descriptor) {
        String countryName = descriptor.getCountry();
        Country country = countriesCache.get(countryName);
        if (country == null) {
            country = new Country();
            country.setName(countryName);
            countriesCache.put(countryName, country);
            rootNodes.add(country);
        }
        return country;
    }

    private Company getCompanyByName(CDXmlDescriptor descriptor, Country country) {
        String companyName = descriptor.getCompany();
        Company company = companiesCache.get(companyName);
        if (company == null) {
            company = new Company();
            company.setName(companyName);
            company.setParent(country);
            country.getCompanies().add(company);
            companiesCache.put(companyName, company);
        }
        return company;
    }

    public List<CDXmlDescriptor> getCdXmlDescriptors() {
        return cdXmlDescriptors;
    }

    public void setCdXmlDescriptors(List<CDXmlDescriptor> cdXmlDescriptors) {
        this.cdXmlDescriptors = cdXmlDescriptors;
    }

    public List<TreeNode> getRootNodes() {
        return rootNodes;
    }

    public void setRootNodes(List<TreeNode> rootNodes) {
        this.rootNodes = rootNodes;
    }

    public TreeNode getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(TreeNode currentSelection) {
        this.currentSelection = currentSelection;
    }

}
