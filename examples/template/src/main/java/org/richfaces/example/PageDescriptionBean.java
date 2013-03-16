/**
 *
 */
package org.richfaces.example;

import javax.faces.context.FacesContext;

/**
 * @author leo
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class PageDescriptionBean implements Comparable<PageDescriptionBean> {
    private final String _path;
    private final String _title;

    public PageDescriptionBean(String _path, String _title) {
        this._path = _path;
        this._title = _title;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return _path;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return _title;
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
