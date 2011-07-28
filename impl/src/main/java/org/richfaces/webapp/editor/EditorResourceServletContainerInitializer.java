package org.richfaces.webapp.editor;

import java.text.MessageFormat;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

public class EditorResourceServletContainerInitializer implements ServletContainerInitializer {

    private static final String SKIP_SERVLET_REGISTRATION_PARAM = "org.richfaces.editor.skipResourceServletRegistration";
    public static final String EDITOR_RESOURCES_DEFAULT_MAPPING = "/org.richfaces.resources/";

    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
        if (Boolean.valueOf(servletContext.getInitParameter(SKIP_SERVLET_REGISTRATION_PARAM))) {
            return;
        }

        try {
            registerServlet(servletContext);
        } catch (Exception e) {
            servletContext.log(
                    MessageFormat.format("Exception registering RichFaces Editor Resources Servlet: {0}", e.getMessage()), e);
        }
    }

    private static void registerServlet(ServletContext context) {
        Dynamic dynamicRegistration = context.addServlet("AutoRegisteredEditorResourceServlet", EditorResourceServlet.class);
        dynamicRegistration.addMapping(EDITOR_RESOURCES_DEFAULT_MAPPING);
    }
}
