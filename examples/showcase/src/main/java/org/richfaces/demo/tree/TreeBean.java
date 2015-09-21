package org.richfaces.demo.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.swing.tree.TreeNode;

import org.richfaces.component.AbstractTree;
import org.richfaces.demo.tree.model.CD;
import org.richfaces.demo.tree.model.Company;
import org.richfaces.demo.tree.model.Country;
import org.richfaces.event.TreeSelectionChangeEvent;

/**
 * @author Ilya Shaikovsky
 */
@ManagedBean
@ViewScoped
public class TreeBean implements Serializable {
    private static final long serialVersionUID = 1L;
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
        AbstractTree tree = (AbstractTree) selectionChangeEvent.getSource();

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
