package org.richfaces.validator;


public final class GraphValidatorState {
    public static final String STATE_ATTRIBUTE_PREFIX = "org.richfaces.GraphValidator:";
    boolean active = false;
    Object cloned;
    Object base;
    Object property;
    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active
     *            the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the cloned
     */
    public Object getCloned() {
        return cloned;
    }

    /**
     * @param cloned
     *            the cloned to set
     */
    public void setCloned(Object cloned) {
        this.cloned = cloned;
    }

    /**
     * @return the base
     */
    public Object getBase() {
        return base;
    }

    /**
     * @param base
     *            the base to set
     */
    public void setBase(Object base) {
        this.base = base;
    }

    /**
     * @return the property
     */
    public Object getProperty() {
        return property;
    }

    /**
     * @param property
     *            the property to set
     */
    public void setProperty(Object property) {
        this.property = property;
    }

    public boolean isSameBase(Object base) {
        return (null == base && null == this.base) || (base == this.base);
    }

    public boolean isSameProperty(Object property) {
        if (null == this.property) {
            return null == property;
        } else {
            return this.property.equals(property);
        }
    }

    public boolean isSame(Object base, Object property) {
        return isSameBase(base) && isSameProperty(property) && active;
    }
}