/**
 * 
 */
package org.richfaces.javascript;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.faces.application.Resource;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.component.util.Strings;
import org.richfaces.resource.ResourceKey;

import com.google.common.base.Function;
import com.google.common.collect.ComputationException;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;

/**
 * @author asmirnov
 * 
 */
public class ClientScriptServiceImpl implements ClientScriptService {

    private static final String TEXT_JAVASCRIPT = "text/javascript";

    private static final String ORG_RICHFACES_CSV = "org.richfaces.csv";

    private static final Function<Class<?>, ? extends LibraryFunction> RESOURCE_SCRIPT_FUNCTION = new Function<Class<?>, LibraryFunction>() {

        public LibraryFunction apply(Class<?> arg0) {
            return getScriptResource(FacesContext.getCurrentInstance(), arg0);
        }
    };
    
    private static final Function<Class<?>, ? extends LibraryFunction> ANNOTATION_SCRIPT_FUNCTION = new Function<Class<?>, LibraryFunction>() {

        public LibraryFunction apply(Class<?> arg0) {
            return getScriptFromAnnotation(arg0);
        }
    };

    private final ConcurrentMap<Class<?>, LibraryFunction> resourcesMapping;

    private final ConcurrentMap<Class<?>, LibraryFunction> annotationsMapping;

    private final Map<Class<?>, LibraryFunction> defaultMapping;

    public ClientScriptServiceImpl(Map<Class<?>, LibraryFunction> defaultMapping) {
        this.defaultMapping = defaultMapping;
        resourcesMapping = new MapMaker().initialCapacity(10).makeComputingMap(RESOURCE_SCRIPT_FUNCTION);
        annotationsMapping = new MapMaker().initialCapacity(10).makeComputingMap(ANNOTATION_SCRIPT_FUNCTION);
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
            function = getFromComputationMap(resourcesMapping, javaClass);
        } catch (ScriptNotFoundException e) {
            if (defaultMapping.containsKey(javaClass)) {
                function = defaultMapping.get(javaClass);
            } else {
                function = getFromComputationMap(annotationsMapping, javaClass);
            }
        }
        return function;
    }
    
    private LibraryFunction getFromComputationMap(ConcurrentMap<Class<?>, LibraryFunction> map, Class<?> clazz) throws ScriptNotFoundException {
        try {
            return map.get(clazz);
        } catch (ComputationException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ScriptNotFoundException) {
                ScriptNotFoundException snfe = (ScriptNotFoundException) cause;
                throw snfe;
            }
            throw e;
        }
    }

    private static LibraryFunction getScriptFromAnnotation(Class<?> javaClass) throws ScriptNotFoundException {
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

    private static LibraryFunction getScriptResource(FacesContext facesContext, Class<?> javaClass)
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
