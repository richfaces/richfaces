package org.richfaces;

import org.jboss.test.faces.ServletHolder;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.richfaces.servlet.ResourceServlet;

public class CustomizedHtmlUnitEnvironment extends HtmlUnitEnvironment {

    private static ThreadLocal<ResourceServletHolder> RESOURCE_SERVLET_HOLDER = new ThreadLocal<CustomizedHtmlUnitEnvironment.ResourceServletHolder>() {
        protected ResourceServletHolder initialValue() {
            return new ResourceServletHolder();
        };
    };

    public CustomizedHtmlUnitEnvironment() {
        this.getServer().addServlet(RESOURCE_SERVLET_HOLDER.get());
    }

    private static class ResourceServletHolder extends ServletHolder {

        private ResourceServletHolder() {
            super("/org.richfaces.resources/*", new ResourceServlet());
        }
    }

}
