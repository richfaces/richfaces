/**
 * 
 */
package org.richfaces.javascript;

import java.util.List;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.component.util.Strings;
import org.richfaces.resource.ResourceKey;

import com.google.common.collect.Lists;

/**
 * @author asmirnov
 * 
 */
public class ClientScriptServiceImpl implements ClientScriptService {

    private static final String TEXT_JAVASCRIPT = "text/javascript";

    private static final String ORG_RICHFACES_CSV = "org.richfaces.csv";

    private final Map<Class<?>, LibraryFunction> defaultMapping;

    public ClientScriptServiceImpl(Map<Class<?>, LibraryFunction> defaultMapping) {
        this.defaultMapping = defaultMapping;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.validator.ClientScriptService#getScript(java.lang.Class)
     */
    public LibraryFunction getScript(FacesContext facesContext, Class<?> javaClass) throws ScriptNotFoundException {
        if (null == facesContext || null == javaClass) {
            throw new NullPointerException();
        }
        LibraryFunction function;
        try {
            function = getScriptResource(facesContext, javaClass);
        } catch (ScriptNotFoundException e) {
            if (defaultMapping.containsKey(javaClass)) {
                function = defaultMapping.get(javaClass);
            } else {
                function = getScriptFromAnnotation(javaClass);
            }
        }
        return function;
    }

    private LibraryFunction getScriptFromAnnotation(Class<?> javaClass) throws ScriptNotFoundException {
        if (javaClass.isAnnotationPresent(ClientSideScript.class)) {
            ClientSideScript clientSideScript = javaClass.getAnnotation(ClientSideScript.class);
            List<ResourceKey> resources = Lists.newArrayList();
            for(ResourceDependency dependency: clientSideScript.resources()){
                resources.add(ResourceKey.create(dependency.name(),dependency.library()));
            }
            return new LibraryFunctionImplementation(clientSideScript.function(), resources);
        } else {
            throw new ScriptNotFoundException();
        }
    }

    private LibraryFunction getScriptResource(FacesContext facesContext, Class<?> javaClass)
        throws ScriptNotFoundException {
        ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();
        String resourceName = javaClass.getSimpleName() + ".js";
        Resource facesResource = resourceHandler.createResource(resourceName, ORG_RICHFACES_CSV, TEXT_JAVASCRIPT);
        if (null != facesResource) {
            final String functionName = Strings.firstToLowerCase(javaClass.getSimpleName());
            return new LibraryFunctionImplementation(functionName,resourceName,  ORG_RICHFACES_CSV);
        } else {
            throw new ScriptNotFoundException();
        }
    }

}
