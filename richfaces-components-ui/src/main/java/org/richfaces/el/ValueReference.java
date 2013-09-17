/**
 *
 */
package org.richfaces.el;

/**
 * @author asmirnov
 *
 */
public class ValueReference {
    private final Object base;
    private final Object property;
    private final ValueReference parent;

    public ValueReference(Object base, Object property, ValueReference parent) {
        this.base = base;
        this.property = property;
        this.parent = parent;
    }

    public Object getBase() {
        return base;
    }

    public Object getProperty() {
        return property;
    }

    public boolean hasNext() {
        return null != parent;
    }

    public ValueReference next() {
        return parent;
    }
}
