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

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.BypassProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.context.ExtendedRenderVisitContext;
import org.richfaces.renderkit.html.DivPanelRenderer;

import com.google.common.collect.ImmutableSet;

/**
 * <p>The &lt;rich:tab&gt; component represents an individual tab inside a &lt;rich:tabPanel&gt; component, including
 * the tab's content. Clicking on the tab header will bring its corresponding content to the front of other tabs.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(
        tag = @Tag(type = TagType.Facelets), facets = { @Facet(name = "header", generate = false) },
        renderer = @JsfRenderer(type = "org.richfaces.TabRenderer"))
public abstract class AbstractTab extends AbstractActionComponent implements AbstractTogglePanelTitledItem, ClientBehaviorHolder, AjaxProps, BypassProps, CoreProps, EventsMouseProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.Tab";
    public static final String COMPONENT_FAMILY = "org.richfaces.Tab";

    public AbstractTab() {setRendererType("org.richfaces.TabRenderer");
    }

    // ------------------------------------------------ Html Attributes
    enum Properties {
        headerDisabledClass, headerInactiveClass, headerClass, contentClass, execute, headerActiveClass, header, switchType
    }

    /**
     * Suppress the inherited value attribute from the taglib.
     */
    @Attribute(hidden = true)
    public abstract Object getValue();

    /**
     * The CSS class applied to the header when this panel is active
     */
    @Attribute(generate = false)
    public String getHeaderActiveClass() {
        String value = (String) getStateHelper().eval(Properties.headerActiveClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabActiveHeaderClass();
    }

    public void setHeaderActiveClass(String headerActiveClass) {
        getStateHelper().put(Properties.headerActiveClass, headerActiveClass);
    }

    /**
     * The CSS class applied to the header when this panel is disabled
     */
    @Attribute(generate = false)
    public String getHeaderDisabledClass() {
        String value = (String) getStateHelper().eval(Properties.headerDisabledClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabDisabledHeaderClass();
    }

    public void setHeaderDisabledClass(String headerDisabledClass) {
        getStateHelper().put(Properties.headerDisabledClass, headerDisabledClass);
    }

    /**
     * The CSS class applied to the header when this panel is inactive
     */
    @Attribute(generate = false)
    public String getHeaderInactiveClass() {
        String value = (String) getStateHelper().eval(Properties.headerInactiveClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabInactiveHeaderClass();
    }

    public void setHeaderInactiveClass(String headerInactiveClass) {
        getStateHelper().put(Properties.headerInactiveClass, headerInactiveClass);
    }

    /**
     * The CSS class applied to the header
     */
    @Attribute(generate = false)
    public String getHeaderClass() {
        String value = (String) getStateHelper().eval(Properties.headerClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabHeaderClass();
    }

    public void setHeaderClass(String headerClass) {
        getStateHelper().put(Properties.headerClass, headerClass);
    }

    /**
     * The CSS class applied to the panel content
     */
    @Attribute(generate = false)
    public String getContentClass() {
        String value = (String) getStateHelper().eval(Properties.contentClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabContentClass();
    }

    public void setContentClass(String contentClass) {
        getStateHelper().put(Properties.contentClass, contentClass);
    }

    @Attribute(generate = false)
    public Object getExecute() {
        Object execute = getStateHelper().eval(Properties.execute);
        if (execute == null) {
            execute = "";
        }
        return execute + " " + getTabPanel().getId();
    }

    public void setExecute(Object execute) {
        getStateHelper().put(Properties.execute, execute);
    }

    // ///////////////////////////////////////////////////////////////////////

    public UIComponent getHeaderFacet(Enum<?> state) {
        return getHeaderFacet(this, state);
    }

    public static UIComponent getHeaderFacet(UIComponent component, Enum<?> state) {
        UIComponent headerFacet = null;
        if (state != null) {
            headerFacet = component.getFacet("header" + DivPanelRenderer.capitalize(state.toString()));
        }

        if (headerFacet == null) {
            headerFacet = component.getFacet("header");
        }
        return headerFacet;
    }

    /**
     * If the VisitContext is not the RenderExtendedVisitContext, return the usual FacetsAndChildren iterator.
     * Otherwise return only the Facet iterator when a child visit is not required.
     *
     * This is useful to not render the tab contents when an item is not visible, while still visiting the header facets.
     *
     * @param visitContext The VisitContext of the component tree visit.
     */
    public static Iterator<UIComponent> getVisitableChildren(UIComponent component, VisitContext visitContext) {
        Iterator<UIComponent> kids;
        if (ExtendedRenderVisitContext.isExtendedRenderVisitContext(visitContext)
                && component instanceof VisitChildrenRejectable
                && ! ((VisitChildrenRejectable)component).shouldVisitChildren()) {
            if (component.getFacetCount() > 0) {
                kids = component.getFacets().values().iterator();
            } else {
                kids = ImmutableSet.<UIComponent>of().iterator();
            }
        } else {
            kids =  component.getFacetsAndChildren();
        }
        return kids;
    }

    // ------------------------------------------------ Component Attributes

    /**
     * The header label of the tab
     */
    @Attribute(generate = false)
    public String getHeader() {
        return (String) getStateHelper().eval(Properties.header, getName());
    }

    public void setHeader(String header) {
        getStateHelper().put(Properties.header, header);
    }

    // ------------------------------------------------ AbstractTogglePanelItemInterface

    @Override
    public AbstractTabPanel getParentPanel() {
        return ComponentIterators.getParent(this, AbstractTabPanel.class);
    }

    @Override
    public boolean isDynamicPanelItem() {
        return AbstractTogglePanel.isPanelItemDynamic(this);
    }

    public AbstractTabPanel getTabPanel() {
        return getParentPanel();
    }

    @Override
    public boolean isActive() {
        return getTabPanel().isActiveItem(this);
    }

    @Override
    public boolean shouldVisitChildren() {
        return isActive() || getSwitchType() == SwitchType.client;
    }

    /**
     * The name of the tab, used for identifying and manipulating the active panel
     */
    @Attribute(generate = false)
    @Override
    public String getName() {
        return (String) getStateHelper().eval(AbstractTogglePanelItem.NAME, getClientId());
    }

    public void setName(String name) {
        getStateHelper().put(AbstractTogglePanelItem.NAME, name);
    }

    @Override
    public String toString() {
        return "TogglePanelItem {name: " + getName() + ", switchType: " + getSwitchType() + '}';
    }

    /**
     * The switch type for this toggle panel: client, ajax (default), server
     */
    @Attribute(generate = false)
    @Override
    public SwitchType getSwitchType() {
        SwitchType switchType = (SwitchType) getStateHelper().eval(Properties.switchType);
        if (switchType == null) {
            switchType = getParentPanel().getSwitchType();
        }
        if (switchType == null) {
            switchType = SwitchType.DEFAULT;
        }
        return switchType;
    }

    public void setSwitchType(SwitchType switchType) {
        getStateHelper().put(Properties.switchType, switchType);
    }

    @Override
    public Renderer getRenderer(FacesContext context) {
        return super.getRenderer(context);
    }

    @Override
    /**
     * UIComponent#visitTree modified to delegate to AbstractTab#getVisitableChildren() to retrieve the children iterator
     */
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (!isVisitable(context)) {
            return false;
        }

        FacesContext facesContext = context.getFacesContext();
        pushComponentToEL(facesContext, null);

        try {
            VisitResult result = context.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE) {
                return true;
            }

            if (result == VisitResult.ACCEPT) {
                // Do not render the non-active children, but always render the visible header facets.
                Iterator<UIComponent> kids = AbstractTab.getVisitableChildren(this, context);

                while(kids.hasNext()) {
                    boolean done = kids.next().visitTree(context, callback);

                    if (done) {
                        return true;
                    }
                }
            }
        }
        finally {
            popComponentFromEL(facesContext);
        }

        return false;
    }
}
