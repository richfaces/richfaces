package org.richfaces.javascript.client;

import java.util.Map;

import com.google.common.collect.Maps;

public class TestCriteria {

    private final Object value;
    
    private final Map<String, Object> options = Maps.newHashMap();

    public TestCriteria(Object value) {
        this.value = value;       
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the string
     */
    public Object getValue() {
        return value;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the options
     */
    public Map<String, Object> getOptions() {
        return options;
    }

}
