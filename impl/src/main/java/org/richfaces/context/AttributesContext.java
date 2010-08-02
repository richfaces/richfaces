package org.richfaces.context;

import java.util.HashMap;
import java.util.Map;

public class AttributesContext {
    private Map<String, Object> attributesMap = new HashMap<String, Object>();

    public Object getAttribute(String name) {
        return attributesMap.get(name);
    }

    public Object setAttribute(String name, Object value) {
        if (value != null) {
            return attributesMap.put(name, value);
        } else {
            return attributesMap.remove(name);
        }
    }
}
