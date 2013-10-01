/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

package org.richfaces.ui.menu.menu;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.richfaces.ui.ajax.command.AjaxCommandRendererBase;
import org.richfaces.ui.behavior.HandlersChain;
import org.richfaces.ui.common.Mode;

@ResourceDependencies(@ResourceDependency(library = "org.richfaces", name = "menu/dropDownMenu/dropdownmenu.ecss", target = "head"))
public class MenuItemRendererBase extends AjaxCommandRendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.ui.MenuItemRenderer";

    protected boolean isDisabled(FacesContext facesContext, UIComponent component) {
        if (component instanceof AbstractMenuItem) {
            return ((AbstractMenuItem) component).isDisabled();
        }
        return false;
    }

    protected UIComponent getIconFacet(FacesContext facesContext, UIComponent component) {
        UIComponent facet = null;
        AbstractMenuItem menuItem = (AbstractMenuItem) component;
        if (menuItem != null) {

            if (menuItem.isDisabled()) {
                facet = menuItem.getFacet(AbstractMenuItem.Facets.iconDisabled.toString());
            } else {
                facet = menuItem.getFacet(AbstractMenuItem.Facets.icon.toString());
            }
        }
        return facet;
    }

    protected String getIconAttribute(FacesContext facesContext, UIComponent component) {
        String icon = null;
        AbstractMenuItem menuItem = (AbstractMenuItem) component;
        if (menuItem != null) {

            if (menuItem.isDisabled()) {
                icon = menuItem.getIconDisabled();
            } else {
                icon = menuItem.getIcon();
            }
        }
        return icon;
    }

    @Override
    public void doDecode(FacesContext context, UIComponent component) {
        AbstractMenuItem menuItem = (AbstractMenuItem) component;
        if (menuItem != null) {
            Mode mode = resolveSubmitMode(menuItem);
            if (!Mode.client.equals(mode)) {
                super.doDecode(context, component);
            }
        }
    }

    private UIComponent getUIForm(UIComponent component) {
        if (component != null) {
            UIComponent parent = component.getParent();
            while (parent != null) {
                if (parent instanceof UIForm) {
                    return parent;
                }
                parent = parent.getParent();
            }
        }
        return null;
    }

    /**
     * overridden due to {@link https://issues.jboss.org/browse/RF-10695}
     *
     * @param context
     * @param component
     */
    @Override
    public String getOnClick(FacesContext context, UIComponent component) {
        AbstractMenuItem menuItem = (AbstractMenuItem) component;
        Mode submitMode = resolveSubmitMode(menuItem);
        StringBuffer onClick = new StringBuffer();

        if (!getUtils().isBooleanAttribute(component, "disabled")) {
            HandlersChain handlersChain = new HandlersChain(context, component);

            handlersChain.addInlineHandlerFromAttribute("onclick");
            handlersChain.addBehaviors("click", "action");

            switch (submitMode) {
                case ajax:
                    handlersChain.addAjaxSubmitFunction();
                    break;
                case server:
                    handlersChain.addInlineHandlerAsValue("RichFaces.submitForm(event.form, event.itemId)");
            }

            String handlerScript = handlersChain.toScript();

            if (handlerScript != null) {
                onClick.append(handlerScript);
            }

            if (!"reset".equals(component.getAttributes().get("type"))) {
                onClick.append(";return false;");
            }
        } else {
            onClick.append("return false;");
        }

        return onClick.toString();
    }

    protected Mode resolveSubmitMode(AbstractMenuItem menuItem) {
        if (menuItem.getMode() != null) {
            return menuItem.getMode();
        }
        AbstractMenuContainer parent = getMenuParent(menuItem);
        if (parent != null && parent.getMode() != null) {
            return parent.getMode();
        }
        return Mode.server;
    }

    protected String getStyleClass(FacesContext facesContext, UIComponent component, String menuParentStyle,
            String menuGroupStyle, String menuItemStyle) {
        UIComponent parent = getMenuParent(component);
        UIComponent menuGroup = getMenuGroup(component);
        Object styleClass = null;
        if (parent != null && menuParentStyle != null && menuParentStyle.length() > 0) {
            styleClass = parent.getAttributes().get(menuParentStyle);
        }
        if (menuGroup != null && menuGroupStyle != null && menuGroupStyle.length() > 0) {
            styleClass = concatClasses(styleClass, menuGroup.getAttributes().get(menuGroupStyle));
        }

        return concatClasses(styleClass, component.getAttributes().get(menuItemStyle));
    }

    /**
     * Finds a parent of given UI <code>component</code>.
     *
     * @param component <code>UIComponent</code>
     * @param parentClass <code>Class</code> of desired parent
     * @return <code>UIComponent</code>
     */
    private UIComponent getParent(UIComponent component, Class<?> parentClass) {
        if (component != null && parentClass != null) {
            UIComponent parent = component.getParent();
            while (parent != null) {
                if (parentClass.isInstance(parent)) {
                    return parent;
                }
                parent = parent.getParent();
            }
        }
        return null;
    }

    /**
     * Returns a parent <code>AbstractDropDownMenu</code> object of the given component.
     *
     * @param component
     * @return <code>AbstractDropDownMenu</code>
     */
    protected AbstractMenuContainer getMenuParent(UIComponent component) {
        return (AbstractMenuContainer) getParent(component, AbstractMenuContainer.class);
    }

    /**
     * Returns a parent <code>AbstractMenuGroup</code> object of the given component.
     *
     * @param component
     * @return <code>AbstractMenuGroup</code>
     */
    protected AbstractMenuGroup getMenuGroup(UIComponent component) {
        return (AbstractMenuGroup) getParent(component, AbstractMenuGroup.class);
    }

    /**
     * It is introduced due to RF-10004 CDK: isEmpty method is generated incorrectly
     *
     * @param str
     */
    protected boolean isStringEmpty(String str) {
        if (str != null && str.trim().length() > 0) {
            return false;
        }
        return true;
    }

    public String getSubmitMode(UIComponent component) {
        return this.resolveSubmitMode((AbstractMenuItem) component).name();
    }
}
