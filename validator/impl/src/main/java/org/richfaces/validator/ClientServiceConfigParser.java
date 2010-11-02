/**
 * 
 */
package org.richfaces.validator;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.faces.FacesException;
import javax.xml.bind.JAXB;

import org.richfaces.validator.model.ClientSideScripts;
import org.richfaces.validator.model.Component;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;

/**
 * @author asmirnov
 * 
 */
public final class ClientServiceConfigParser {

    private ClientServiceConfigParser() {
    }

    public static Map<Class<?>, LibraryFunction> parseConfig(String name) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (null == loader) {
            loader = ClientServiceConfigParser.class.getClassLoader();
        }
        Builder<Class<?>, LibraryFunction> resultBuilder = ImmutableMap.builder();
        try {
            Enumeration<URL> resources = loader.getResources(name);
            while (resources.hasMoreElements()) {
                URL url = (URL) resources.nextElement();
                resultBuilder.putAll(parse(loader, url));
            }
        } catch (IOException e) {
            return Collections.emptyMap();
        }
        return resultBuilder.build();
    }

    static Map<Class<?>, LibraryFunction> parse(ClassLoader loader, URL url) {
        Map<Class<?>, LibraryFunction> result = Maps.newHashMap();
        try {
            ClientSideScripts clientSideScripts = JAXB.unmarshal(url, ClientSideScripts.class);
            for (Component component : clientSideScripts.getComponent()) {
                Class<?> componentClass = loader.loadClass(component.getType());
                LibraryFunctionImplementation function = new LibraryFunctionImplementation(component.getLibrary(),
                    component.getResource(), component.getFunction());
                result.put(componentClass, function);
            }
        } catch (ClassNotFoundException e) {
            throw new FacesException("Class for component not found",e);
        } catch (Exception e) {
            throw new FacesException("Error parsing config file "+url,e);
        }
        return result;
    }

}
