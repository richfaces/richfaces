/**
 *
 */
package org.richfaces.el;

/**
 * This class describes bean property.
 *
 * @author asmirnov
 *
 */
public class ValueDescriptor {
    private final String name;
    private final Class<?> beanType;

    /**
     * @param beanType
     * @param name
     * @param propertyType
     * @param readOnly
     */
    public ValueDescriptor(Class<?> beanType, String name) {
        this.beanType = beanType;
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the beanType
     */
    public Class<?> getBeanType() {
        return beanType;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((beanType == null) ? 0 : beanType.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        ValueDescriptor other = (ValueDescriptor) obj;
        if (beanType == null) {
            if (other.beanType != null) {
                return false;
            }
        } else if (!beanType.equals(other.beanType)) {
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
