package org.richfaces.demo.dragdrop;

import java.io.Serializable;

public class Framework implements Serializable {

    private static final long serialVersionUID = -2316100725668694225L;

    public enum Family {
        php, cf, dotNet
    }

    private String name;

    private Family family;

    public Framework(String name, Family family) {
        this.name = name;
        this.family = family;
    }

    public Family getFamily() {
        return family;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((family == null) ? 0 : family.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Framework other = (Framework) obj;
        if (family != other.family) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
