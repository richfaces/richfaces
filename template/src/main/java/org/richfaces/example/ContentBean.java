/**
 *
 */
package org.richfaces.example;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 * @author leo
 *
 */
public class ContentBean {
    private static final String PRELUDE = "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n"
            + " xmlns:f=\"http://java.sun.com/jsf/core\"\n" + " xmlns:h=\"http://java.sun.com/jsf/html\"\n"
            + " xmlns:a4j=\"http://richfaces.org/a4j\"\n" + " xmlns:rich=\"http://richfaces.org/rich\"\n"
            + " xmlns:c=\"http://java.sun.com/jsp/jstl/core\">\n";
    private static final String TAIL = "\n</html>";
    private String xpath = "//*[local-name()='define'][@name='content']/*";

    public String getContent() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        XMLBody xmlBody = new XMLBody();
        try {
            xmlBody.loadXML(context.getExternalContext().getResourceAsStream(viewId), false);
            return PRELUDE + xmlBody.getContent(getXpath()) + TAIL;
        } catch (ParsingException e) {
            throw new FacesException(e);
        }
    }

    /**
     * @param xpath the xpath to set
     */
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    /**
     * @return the xpath
     */
    public String getXpath() {
        return xpath;
    }
}
