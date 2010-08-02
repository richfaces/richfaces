
/**
 *
 */
package org.ajax4jsf.context;

import java.io.IOException;

import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author asmirnov
 *
 */
public class MockAjaxContext extends AjaxContext {

    /**
     *
     */
    public MockAjaxContext() {

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#addComponentToAjaxRender(javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public void addComponentToAjaxRender(UIComponent base, String id) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#addComponentToAjaxRender(javax.faces.component.UIComponent)
     */
    @Override
    public void addComponentToAjaxRender(UIComponent component) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#addRegionsFromComponent(javax.faces.component.UIComponent)
     */
    @Override
    public void addRegionsFromComponent(UIComponent component) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#addRenderedArea(java.lang.String)
     */
    @Override
    public void addRenderedArea(String id) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#decode(javax.faces.context.FacesContext)
     */
    @Override
    public void decode(FacesContext context) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#encodeAjaxBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public void encodeAjaxBegin(FacesContext context) throws IOException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#encodeAjaxEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public void encodeAjaxEnd(FacesContext context) throws IOException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getAjaxActionURL(javax.faces.context.FacesContext)
     */
    @Override
    public String getAjaxActionURL(FacesContext context) {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getAjaxActionURL()
     */

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getAjaxAreasToRender()
     */
    @Override
    public Set<String> getAjaxAreasToRender() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getAjaxRenderedAreas()
     */
    @Override
    public Set<String> getAjaxRenderedAreas() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getAjaxSingleClientId()
     */
    @Override
    public String getAjaxSingleClientId() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getCommonAjaxParameters()
     */
    @Override
    public Map<String, Object> getCommonAjaxParameters() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getOncomplete()
     */
    @Override
    public Object getOncomplete() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getResponseData()
     */
    @Override
    public Object getResponseData() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getResponseDataMap()
     */
    @Override
    public Map<String, Object> getResponseDataMap() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getSubmittedRegionClientId(javax.faces.context.FacesContext)
     */
    @Override
    public String getSubmittedRegionClientId() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#getViewIdHolder()
     */
    @Override
    public ViewIdHolder getViewIdHolder() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#isAjaxRequest(javax.faces.context.FacesContext)
     */

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#isAjaxRequest()
     */
    @Override
    public boolean isAjaxRequest() {

        // TODO Auto-generated method stub
        return false;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#isSelfRender()
     */
    @Override
    public boolean isSelfRender() {

        // TODO Auto-generated method stub
        return false;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#release()
     */
    @Override
    public void release() {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#removeRenderedArea(java.lang.String)
     */
    @Override
    public boolean removeRenderedArea(String id) {

        // TODO Auto-generated method stub
        return false;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#renderSubmittedAjaxRegion(javax.faces.context.FacesContext)
     */
    @Override
    public void renderAjax(FacesContext context) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#saveViewState(javax.faces.context.FacesContext)
     */
    @Override
    public void saveViewState(FacesContext context) throws IOException {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#setAjaxRequest(boolean)
     */
    @Override
    public void setAjaxRequest(boolean b) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#setAjaxSingleClientId(java.lang.String)
     */
    @Override
    public void setAjaxSingleClientId(String ajaxSingleClientId) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#setOncomplete(java.lang.Object)
     */
    @Override
    public void setOncomplete(Object oncompleteFunction) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#setResponseData(java.lang.Object)
     */
    @Override
    public void setResponseData(Object responseData) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#setSelfRender(boolean)
     */
    @Override
    public void setSelfRender(boolean b) {

        // TODO Auto-generated method stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.context.AjaxContext#setViewIdHolder(org.ajax4jsf.context.ViewIdHolder)
     */
    @Override
    public void setViewIdHolder(ViewIdHolder viewIdHolder) {

        // TODO Auto-generated method stub
    }

    public void setAjaxAreasToProcess(Set<String> ajaxAreasToProcess) {}

    @Override
    public Set<String> getAjaxAreasToProcess() {

        // TODO Auto-generated method stub
        return null;
    }

    public void setSubmittedRegionClientId(String submittedClientId) {}
}
