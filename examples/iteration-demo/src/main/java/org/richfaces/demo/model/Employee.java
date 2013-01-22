package org.richfaces.demo.model;

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