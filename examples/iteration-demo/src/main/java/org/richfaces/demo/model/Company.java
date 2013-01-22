package org.richfaces.demo.model;

public class Company {
    private String name;
    private String state;
    private String phone;

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Company)) {
            return false;
        }
        Company company = (Company) obj;
        return name.equals(company.getName());
    }

    @Override
    public int hashCode() {
        return 31 + 17 * name.hashCode();
    }
}
