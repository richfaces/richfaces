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
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.ajax4jsf.component.IterationStateHolder;
import org.richfaces.DataScrollerUtils;
import org.richfaces.application.FacesMessages;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DataScrollListener;
import org.richfaces.event.DataScrollSource;
import org.richfaces.taglib.DataScrollerHandler;

/**
 * <p> The &lt;rich:dataScroller&gt; component is used for navigating through multiple pages of tables or grids. </p>
 */
@JsfComponent(type = AbstractDataScroller.COMPONENT_TYPE, family = AbstractDataScroller.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.DataScrollerRenderer"),
        tag = @Tag(name = "dataScroller", handlerClass = DataScrollerHandler.class, type = TagType.Facelets) )
public abstract class AbstractDataScroller extends UIComponentBase implements DataScrollSource, IterationStateHolder, AjaxProps, CoreProps {
    public static final String COMPONENT_TYPE = "org.richfaces.DataScroller";
    public static final String COMPONENT_FAMILY = "org.richfaces.DataScroller";
    public static final String SCROLLER_STATE_ATTRIBUTE = COMPONENT_TYPE + ":page";
    public static final String FIRST_FACET_NAME = "first";
    public static final String LAST_FACET_NAME = "last";
    public static final String NEXT_FACET_NAME = "next";
    public static final String PREVIOUS_FACET_NAME = "previous";
    public static final String FAST_FORWARD_FACET_NAME = "fastForward";
    public static final String FAST_REWIND_FACET_NAME = "fastRewind";
    public static final String FIRST_DISABLED_FACET_NAME = "first_disabled";
    public static final String LAST_DISABLED_FACET_NAME = "last_disabled";
    public static final String NEXT_DISABLED_FACET_NAME = "next_disabled";
    public static final String PREVIOUS_DISABLED_FACET_NAME = "previous_disabled";
    public static final String FAST_FORWARD_DISABLED_FACET_NAME = "fastForward_disabled";
    public static final String FAST_REWIND_DISABLED_FACET_NAME = "fastRewind_disabled";
    public static final String CONTROLS_SEPARATOR_FACET_NAME = "controlsSeparator";
    public static final String PAGES_FACET_NAME = "pages";
    public static final String PAGEMODE_FULL = "full";
    public static final String PAGEMODE_SHORT = "short";
    private Integer page;

    /**
     * If renderIfSinglePage is "true" then datascroller is displayed on condition that the data hold on one page. Default value
     * is "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isRenderIfSinglePage();

    /**
     * The attribute to control whether last page of datascroller shows "rows" number of items or just the rest. Possible values
     * are "full" and "short". Default value is "short".
     */
    @Attribute
    public abstract String getLastPageMode();

    /**
     * Maximum quantity of pages. Default value is "10".
     */
    @Attribute
    public abstract int getMaxPages();

    public int getMaxPagesOrDefault() {
        int maxPages = getMaxPages();
        if (maxPages <= 0) {
            maxPages = 10;
        }

        return maxPages;
    }

    /**
     * The attribute specifies the visibility of boundaryControls. Possible values are: "show" (controls are always visible ).
     * "hide" (controls are hidden. "auto" (unnecessary controls are hidden). Default value is "show".
     */
    @Attribute
    public abstract DataScrollerControlsMode getBoundaryControls();

    /**
     * The attribute specifies the visibility of fastControls. Possible values are: "show" (controls are always visible ).
     * "hide" (controls are hidden. "auto" (unnecessary controls are hidden). Default value is "show".
     */
    @Attribute
    public abstract DataScrollerControlsMode getFastControls();

    /**
     * The attribute specifies the visibility of stepControls. Possible values are: "show" (controls are always visible ).
     * "hide" (controls are hidden. "auto" (unnecessary controls are hidden). Default value is "show".
     */
    @Attribute
    public abstract DataScrollerControlsMode getStepControls();

    /**
     * The attribute indicates pages quantity to switch onto when fast scrolling is used. Default value is "1".
     *
     * @return
     */
    @Attribute
    public abstract int getFastStep();

    public int getFastStepOrDefault() {
        int fastStep = getFastStep();
        if (fastStep <= 0) {
            fastStep = 1;
        }
        return fastStep;
    }

    /**
     * Component ID of the table whose data is scrolled
     */
    @Attribute
    public String getFor() {
        return (String) getStateHelper().eval("for");
    }

    public void setFor(String forId) {
        getStateHelper().put("for", forId);
    }

    @Attribute
    public abstract MethodExpression getScrollListener();

    public void addScrollListener(DataScrollListener listener) {
        addFacesListener(listener);
    }

    public DataScrollListener[] getScrollListeners() {
        return (DataScrollListener[]) getFacesListeners(DataScrollListener.class);
    }

    public void removeScrollListener(DataScrollListener listener) {
        removeFacesListener(listener);
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof DataScrollEvent) {
            DataScrollEvent dataScrollerEvent = (DataScrollEvent) event;

            updateModel(dataScrollerEvent.getPage());

            FacesContext facesContext = getFacesContext();

            UIComponent dataTable = getDataTable();

            List<AbstractDataScroller> dataScrollers = DataScrollerUtils.findDataScrollers(dataTable);
            for (AbstractDataScroller dataScroller : dataScrollers) {
                facesContext.getPartialViewContext().getRenderIds().add(dataScroller.getClientId(facesContext));
            }

            String dataTableId = null;
            if (dataTable instanceof MetaComponentResolver && ! (dataTable instanceof UIDataTableBase && ((UIDataTableBase)dataTable).hasRowChildren())) {
                dataTableId = ((MetaComponentResolver) dataTable).resolveClientId(facesContext, dataTable, "body");
            }

            if (dataTableId == null) {
                dataTableId = dataTable.getClientId(facesContext);
            }

            facesContext.getPartialViewContext().getRenderIds().add(dataTableId);

            // add datascroller to render
            String dataScrollerId = getClientId(facesContext);
            if (!facesContext.getPartialViewContext().getRenderIds().contains(dataScrollerId)) {
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
        return DataScrollerUtils.findDataTable(this);
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
        } else if (PREVIOUS_FACET_NAME.equals(facetName)) {
            newPage = getPage() - 1;
        } else if (NEXT_FACET_NAME.equals(facetName)) {
            newPage = getPage() + 1;
        } else if (FAST_FORWARD_FACET_NAME.equals(facetName)) {
            newPage = getPage() + getFastStepOrDefault();
            newPage = newPage < pageCount ? newPage : pageCount;
        } else if (FAST_REWIND_FACET_NAME.equals(facetName)) {
            newPage = getPage() - getFastStepOrDefault();
            newPage = newPage > 0 ? newPage : 1;
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
    @Facet
    public UIComponent getFirst() {
        return getFacet(FIRST_FACET_NAME);
    }

    @Facet
    public abstract UIComponent getLast();

    @Facet
    public abstract UIComponent getNext();

    @Facet
    public abstract UIComponent getPrevious();

    @Facet
    public abstract UIComponent getFastForward();

    @Facet
    public abstract UIComponent getFastRewind();

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

    /**
     * If page >= 1 then it's a page number to show
     */
    @Attribute
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

    private MessageFactory getMessageFactory(FacesContext context) {
        return ServiceTracker.getService(MessageFactory.class);
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
                    message = ServiceTracker.getService(MessageFactory.class).createMessage(facesContext,
                            FacesMessages.UIINPUT_UPDATE, MessageUtil.getLabel(facesContext, this));
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageStr, messageStr);
                }
                facesContext.getExternalContext().log(message.getSummary(), result);
                facesContext.addMessage(getClientId(facesContext), message);
                facesContext.renderResponse();
            } catch (IllegalArgumentException e) {
                FacesMessage message = ServiceTracker.getService(MessageFactory.class).createMessage(facesContext,
                        FacesMessages.UIINPUT_UPDATE, MessageUtil.getLabel(facesContext, this));
                facesContext.getExternalContext().log(message.getSummary(), e);
                facesContext.addMessage(getClientId(facesContext), message);
                facesContext.renderResponse();
            } catch (Exception e) {
                FacesMessage message = ServiceTracker.getService(MessageFactory.class).createMessage(facesContext,
                        FacesMessages.UIINPUT_UPDATE, MessageUtil.getLabel(facesContext, this));
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

    public Object getIterationState() {
        return this.page;
    }

    public void setIterationState(Object state) {
        this.page = (Integer) state;
    }
}
