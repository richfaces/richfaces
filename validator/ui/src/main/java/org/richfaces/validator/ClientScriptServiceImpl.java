/**
 * 
 */
package org.richfaces.validator;

import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.component.util.Strings;

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
            return new LibraryFunctionImplementation(clientSideScript.library(), clientSideScript.resource(), clientSideScript.function());
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
            return new LibraryFunctionImplementation(ORG_RICHFACES_CSV,resourceName,  functionName);
        } else {
            throw new ScriptNotFoundException();
        }
    }

}
