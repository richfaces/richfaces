/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.component;

import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.ajax4jsf.component.IterationStateHolder;
import org.ajax4jsf.renderkit.RendererUtils;
import org.richfaces.DataScrollerUtils;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.event.DataScrollerEvent;
import org.richfaces.event.DataScrollerListener;
import org.richfaces.event.DataScrollerSource;

public class UIDataScroller extends UIComponentBase implements DataScrollerSource, IterationStateHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.DataScroller";

    public static final String COMPONENT_FAMILY = "org.richfaces.DataScroller";

    public static final String SCROLLER_STATE_ATTRIBUTE = COMPONENT_TYPE + ":page";

    public static final String FIRST_FACET_NAME = "first";

    public static final String LAST_FACET_NAME = "last";

    public static final String FAST_FORWARD_FACET_NAME = "fastforward";

    public static final String FAST_REWIND_FACET_NAME = "fastrewind";

    public static final String FIRST_DISABLED_FACET_NAME = "first_disabled";

    public static final String LAST_DISABLED_FACET_NAME = "last_disabled";

    public static final String PAGEMODE_FULL = "full";

    public static final String PAGEMODE_SHORT = "short";
    
    private static final int MAX_PAGES_DEFAULT = 10;

    private Integer page;

    protected enum PropertyKeys {
        boundaryControls, fastControls, fastStep, forComponent, inactiveStyle, selectStyle, inactiveStyleClass, selectStyleClass, scrollerListener, lastPageMode, maxPages, pageIndexVar, pagesVar, renderIfSinglePage, style, styleClass, stepControls
    }

    public String getLastPageMode() {
        return (String) getStateHelper().eval(PropertyKeys.lastPageMode);
    }

    public void setLastPageMode(String lastPageMode) {
        getStateHelper().put(PropertyKeys.lastPageMode, lastPageMode);
    }

    public int getFastStep() {
        return (Integer) getStateHelper().eval(PropertyKeys.fastStep, 0);
    }

    public void setFastStep(int fastStep) {
        getStateHelper().put(PropertyKeys.fastStep, fastStep);
    }

    public String getForComponent() {
        return (String) getStateHelper().eval(PropertyKeys.forComponent);
    }

    public void setForComponent(String forComponent) {
        getStateHelper().put(PropertyKeys.forComponent, forComponent);
    }

    public int getMaxPages() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxPages, MAX_PAGES_DEFAULT);
    }

    public void setMaxPages(int maxPages) {
        getStateHelper().put(PropertyKeys.maxPages, maxPages);
    }

    public String getBoundaryControls() {
        return (String) getStateHelper().eval(PropertyKeys.boundaryControls, "show");
    }

    public void setBoundaryControls(String boundaryControls) {
        getStateHelper().put(PropertyKeys.boundaryControls, boundaryControls);
    }

    public String getFastControls() {
        return (String) getStateHelper().eval(PropertyKeys.fastControls, "show");
    }

    public void setFastControls(String fastControls) {
        getStateHelper().put(PropertyKeys.fastControls, fastControls);
    }

    public void addScrollerListener(DataScrollerListener listener) {
        addFacesListener(listener);
    }

    public DataScrollerListener[] getScrollerListeners() {
        return (DataScrollerListener[]) getFacesListeners(DataScrollerListener.class);
    }

    public void removeScrollerListener(DataScrollerListener listener) {
        removeFacesListener(listener);
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        

        if (event instanceof DataScrollerEvent) {
            DataScrollerEvent dataScrollerEvent = (DataScrollerEvent) event;

            updateModel(dataScrollerEvent.getPage());

            FacesContext facesContext = getFacesContext();

            UIComponent dataTable = getDataTable();

            List<UIDataScroller> dataScrollers = DataScrollerUtils.findDataScrollers(dataTable);
            for (UIDataScroller dataScroller : dataScrollers) {
                facesContext.getPartialViewContext().getRenderIds().add(dataScroller.getClientId(facesContext));
            }

            String dataTableId = null;
            if (dataTable instanceof MetaComponentResolver) {
                dataTableId = ((MetaComponentResolver) dataTable).resolveClientId(facesContext, dataTable, "body");
            }

            if (dataTableId == null) {
                dataTableId = dataTable.getClientId(facesContext);
            }

            facesContext.getPartialViewContext().getRenderIds().add(dataTableId);
            
            //add datascroller to render 
            String dataScrollerId = getClientId(facesContext);
            if(!facesContext.getPartialViewContext().getRenderIds().contains(dataScrollerId)) {
                facesContext.getPartialViewContext().getRenderIds().add(dataScrollerId);
            }
            
        }
        
        super.broadcast(event);
    }

    /**
     * Finds the dataTable which id is mapped to the "for" property
     * 
     * @return the dataTable component
     */

    public UIComponent getDataTable() {
        String forAttribute = (String) getAttributes().get(PropertyKeys.forComponent.toString());
        UIComponent forComp;

        if (forAttribute == null) {
            forComp = this;

            while ((forComp = forComp.getParent()) != null) {
                if (forComp instanceof UIData || forComp instanceof UIDataAdaptor) {
                    getStateHelper().put(PropertyKeys.forComponent, forComp.getId());
                    return forComp;
                }
            }
            throw new FacesException("could not find dataTable for  datascroller " + this.getId());

        } else {
            forComp = RendererUtils.getInstance().findComponentFor(this, forAttribute);
        }

        if (forComp == null) {
            throw new IllegalArgumentException("could not find dataTable with id '" + forAttribute + "'");
        } else if (!((forComp instanceof UIData) || (forComp instanceof UIDataAdaptor))) {

            throw new IllegalArgumentException("component with id '" + forAttribute + "' must be of type "
                + UIData.class.getName() + " or " + UIDataAdaptor.class + ", not type " + forComp.getClass().getName());
        }

        return forComp;
    }

    private int getFastStepOrDefault() {
        return (Integer) getStateHelper().eval(PropertyKeys.fastStep, 1);
    }

    public int getPageForFacet(String facetName) {
        if (facetName == null) {
            throw new NullPointerException();
        }

        int newPage = 1;
        int pageCount = getPageCount();

        if (FIRST_FACET_NAME.equals(facetName)) {
            newPage = 1;
        } else if (LAST_FACET_NAME.equals(facetName)) {
            newPage = pageCount > 0 ? pageCount : 1;
        } else if (FAST_FORWARD_FACET_NAME.equals(facetName)) {
            newPage = getPage() + getFastStepOrDefault();
        } else if (FAST_REWIND_FACET_NAME.equals(facetName)) {
            newPage = getPage() - getFastStepOrDefault();
        } else {
            try {
                newPage = Integer.parseInt(facetName.toString());
            } catch (NumberFormatException e) {
                throw new FacesException(e.getLocalizedMessage(), e);
            }
        }

        if (newPage >= 1 && newPage <= pageCount) {
            return newPage;
        } else {
            return 0;
        }
    }

    public int getPageCount(UIComponent data) {
        int rowCount = getRowCount(data);
        int rows = getRows(data);
        return getPageCount(data, rowCount, rows);
    }

    public int getPageCount(UIComponent data, int rowCount, int rows) {
        return DataScrollerUtils.getPageCount(data, rowCount, rows);
    }

    /** @return the page count of the uidata */
    public int getPageCount() {
        return getPageCount(getDataTable());
    }

    public int getRowCount(UIComponent data) {
        return (Integer) data.getAttributes().get("rowCount");
    }

    /** @return int */
    public int getRowCount() {
        return getRowCount(getDataTable());
    }

    public int getRows(UIComponent data) {
        return DataScrollerUtils.getRows(data);
    }

    // facet getter methods
    public UIComponent getFirst() {
        return getFacet(FIRST_FACET_NAME);
    }

    public UIComponent getLast() {
        return getFacetByKey(LAST_FACET_NAME);
    }

    public UIComponent getFastForward() {
        return getFacetByKey(FAST_FORWARD_FACET_NAME);
    }

    public UIComponent getFastRewind() {
        return getFacetByKey(FAST_REWIND_FACET_NAME);
    }

    private UIComponent getFacetByKey(String key) {
        return getFacet(key.toString());
    }

    private static boolean isRendered(UIComponent component) {
        UIComponent c = component;
        while (c != null) {
            if (!c.isRendered()) {
                return false;
            }
            c = c.getParent();
        }

        return true;
    }

    public void setPage(int newPage) {
        this.page = newPage;
    }

    public int getPage() {

        UIComponent dataTable = getDataTable();
        Map<String, Object> attributes = dataTable.getAttributes();

        FacesContext facesContext = getFacesContext();
        Integer state = (Integer) attributes.get(dataTable.getClientId(facesContext) + SCROLLER_STATE_ATTRIBUTE);

        if (state != null) {
            return state;
        }

        if (this.page != null) {
            return page;
        }

        ValueExpression ve = getValueExpression("page");
        if (ve != null) {
            try {
                Integer pageObject = (Integer) ve.getValue(getFacesContext().getELContext());

                if (pageObject != null) {
                    return pageObject;
                }
            } catch (ELException e) {
                throw new FacesException(e);
            }
        }

        return 1;
    }

    private void updateModel(int newPage) {

        FacesContext facesContext = getFacesContext();
        UIComponent dataTable = getDataTable();

        if (isRendered(dataTable)) {
            dataTable.getAttributes().put("first", (newPage - 1) * getRows(dataTable));
        }

        Map<String, Object> attributes = dataTable.getAttributes();
        attributes.put(dataTable.getClientId(facesContext) + SCROLLER_STATE_ATTRIBUTE, newPage);

        ValueExpression ve = getValueExpression("page");
        if (ve != null) {
            try {
                ve.setValue(facesContext.getELContext(), newPage);
                attributes.remove(dataTable.getClientId(facesContext) + SCROLLER_STATE_ATTRIBUTE);
            } catch (ELException e) {
                String messageStr = e.getMessage();
                Throwable result = e.getCause();
                while (null != result && result.getClass().isAssignableFrom(ELException.class)) {
                    messageStr = result.getMessage();
                    result = result.getCause();
                }
                FacesMessage message;
                if (null == messageStr) {
                    message = MessageUtil.getMessage(facesContext, UIInput.UPDATE_MESSAGE_ID,
                        new Object[] {MessageUtil.getLabel(facesContext, this) });
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageStr, messageStr);
                }
                facesContext.getExternalContext().log(message.getSummary(), result);
                facesContext.addMessage(getClientId(facesContext), message);
                facesContext.renderResponse();
            } catch (IllegalArgumentException e) {
                FacesMessage message = MessageUtil.getMessage(facesContext, UIInput.UPDATE_MESSAGE_ID,
                    new Object[] {MessageUtil.getLabel(facesContext, this) });
                facesContext.getExternalContext().log(message.getSummary(), e);
                facesContext.addMessage(getClientId(facesContext), message);
                facesContext.renderResponse();
            } catch (Exception e) {
                FacesMessage message = MessageUtil.getMessage(facesContext, UIInput.UPDATE_MESSAGE_ID,
                    new Object[] {MessageUtil.getLabel(facesContext, this) });
                facesContext.getExternalContext().log(message.getSummary(), e);
                facesContext.addMessage(getClientId(facesContext), message);
                facesContext.renderResponse();
            }
        }
    }

    public boolean isLocalPageSet() {
        return page != null;
    }

    public void resetLocalPage() {
        page = null;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getIterationState() {
        return this.page;
    }


    public void setIterationState(Object state) {
        this.page = (Integer) state;
    }
}
