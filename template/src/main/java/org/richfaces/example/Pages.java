/**
 *
 */
package org.richfaces.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * @author leo
 *
 */
public class Pages {
    public static final String DEFAULT_TITLE_PATTERN = "<ui\\:param\\s+name=\"title\"\\s+value=\"([^\"]*)\"";
    private static final Pattern JSP_PATTERN = Pattern.compile(".*\\.jspx?");
    private static final Pattern XHTML_PATTERN = Pattern.compile(".*\\.xhtml");
    private Pattern titlePattern = compilePattern(DEFAULT_TITLE_PATTERN);
    private volatile List<PageDescriptionBean> jspPages;
    private Object jspMutex = new Object();
    private String path = "/examples";
    private volatile List<PageDescriptionBean> xhtmlPages;
    private Object xhtmlMutex = new Object();

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    public Pattern compilePattern(String titlePattern) {
        return Pattern.compile(titlePattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    public List<PageDescriptionBean> getJspPages() {
        if (jspPages == null && null != getExternalContext()) {
            synchronized (jspMutex) {
                if (jspPages == null) {
                    jspPages = getPagesByPattern(JSP_PATTERN);
                }
            }
        }

        return jspPages;
    }

    private ExternalContext getExternalContext() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        ExternalContext externalContext = null;
        if (null != facesContext) {
            externalContext = facesContext.getExternalContext();
        }
        return externalContext;
    }

    public List<PageDescriptionBean> getXhtmlPages() {
        if (xhtmlPages == null && null != getExternalContext()) {
            synchronized (xhtmlMutex) {
                if (xhtmlPages == null) {
                    xhtmlPages = getPagesByPattern(XHTML_PATTERN);
                }
            }
        }

        return xhtmlPages;
    }

    /**
     *
     */
    private List<PageDescriptionBean> getPagesByPattern(Pattern pattern) {
        List<PageDescriptionBean> jspPages = new ArrayList<PageDescriptionBean>();
        Set<String> resourcePaths = getExternalContext().getResourcePaths(getPath());
        for (Iterator<String> iterator = resourcePaths.iterator(); iterator.hasNext();) {
            String page = iterator.next();
            if (pattern.matcher(page).matches()) {
                PageDescriptionBean pageBean = new PageDescriptionBean();
                pageBean.setPath(page);
                InputStream pageInputStream = getExternalContext().getResourceAsStream(page);
                if (null != pageInputStream) {
                    byte[] head = new byte[1024];
                    try {
                        int readed = pageInputStream.read(head);
                        String headString = new String(head, 0, readed);
                        Matcher titleMatcher = titlePattern.matcher(headString);
                        if (titleMatcher.find() && titleMatcher.group(1).length() > 0) {
                            pageBean.setTitle(titleMatcher.group(1));
                        } else {
                            pageBean.setTitle(page);
                        }
                    } catch (IOException e) {
                        throw new FacesException("can't read directory content", e);
                    } finally {
                        try {
                            pageInputStream.close();
                        } catch (IOException e) {
                            // ignore it.
                        }
                    }
                }
                jspPages.add(pageBean);
            }
        }
        Collections.sort(jspPages);
        return jspPages;
    }

    /**
     * @param titlePattern the titlePattern to set
     */
    public void setTitlePattern(String titlePattern) {
        this.titlePattern = compilePattern(titlePattern);
    }

    /**
     * @return the titlePattern
     */
    public String getTitlePattern() {
        return titlePattern.toString();
    }
}
