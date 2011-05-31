/**
 *
 */
package org.richfaces.example;

import javax.faces.context.FacesContext;

/**
 * @author leo
 *
 */
public class PageDescriptionBean implements Comparable<PageDescriptionBean> {
    private String _path;
    private String _title;

    /**
     * @return the path
     */
    public String getPath() {
        return _path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        _path = path;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return _title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        _title = title;
    }

    public String navigate() {
        return getPath();
    }

    public String getUrl() {
        FacesContext context = FacesContext.getCurrentInstance();
        String actionURL = context.getApplication().getViewHandler().getActionURL(context, getPath());
        return context.getExternalContext().encodeActionURL(actionURL);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(PageDescriptionBean o) {
        // compare paths
        return getPath().compareToIgnoreCase(o.getPath());
    }
}
