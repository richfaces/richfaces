package org.richfaces.webapp;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

public final class ServletConfigDefaultsWrapper implements ServletConfig {

    private final ServletConfig config;
    private final Map<String, String> defaults;

    public ServletConfigDefaultsWrapper(ServletConfig config, Map<String, String> defaults) {
        super();
        this.config = config;
        this.defaults = defaults;
    }

    public String getServletName() {
        return config.getServletName();
    }

    public ServletContext getServletContext() {
        return config.getServletContext();
    }

    public String getInitParameter(String name) {
        String parameter = config.getInitParameter(name);

        if (parameter == null) {
            parameter = config.getServletContext().getInitParameter(name);
        }

        if (parameter == null) {
            parameter = defaults.get(name);
        }

        return parameter;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Enumeration getInitParameterNames() {
        Set<String> result = Sets.newHashSet();

        Iterators.addAll(result, (Iterator<? extends String>) defaults.keySet());
        Iterators.addAll(result, Iterators.forEnumeration(config.getInitParameterNames()));
        Iterators.addAll(result, Iterators.forEnumeration(config.getServletContext().getInitParameterNames()));

        return Iterators.asEnumeration(result.iterator());
    }
}