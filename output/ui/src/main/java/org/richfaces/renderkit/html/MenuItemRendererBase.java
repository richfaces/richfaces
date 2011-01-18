package org.richfaces.renderkit.html;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.AbstractDropDownMenu;
import org.richfaces.component.AbstractMenuGroup;
import org.richfaces.component.AbstractMenuItem;
import org.richfaces.component.Mode;
import org.richfaces.renderkit.AjaxCommandRendererBase;

public class MenuItemRendererBase extends AjaxCommandRendererBase {
    
    public static final String RENDERER_TYPE = "org.richfaces.MenuItemRenderer";
    
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
    
    private String getServerSubmitFunction(UIComponent component) {
        UIComponent form = getUIForm(component);
        if (component != null && form != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(component.getClientId(), component.getClientId());
            
            JSFunction submitFunction = new JSFunction("RichFaces.submitForm");
            submitFunction.addParameter(form.getClientId());
            submitFunction.addParameter(param);
            
            return submitFunction.toScript();
        }

        return "";
    }

    protected String getOnClickFunction(FacesContext facesContext, UIComponent component) {
        AbstractMenuItem menuItem = (AbstractMenuItem) component;
        Mode subminMode = resolveSubmitMode(menuItem);
        if (Mode.ajax.equals(subminMode)) {
            return getOnClick(facesContext, menuItem);
        } else {
            return "";
        }
    }
    
    protected Mode resolveSubmitMode(AbstractMenuItem menuItem) {
        if (menuItem.getMode() != null) {
            return menuItem.getMode();
        }
        AbstractDropDownMenu parent = getDDMenu(menuItem);
        if (parent != null && parent.getMode() != null) {
            return parent.getMode();
        }
        return Mode.server;
    } 
    
    protected String getStyleClass(FacesContext facesContext, UIComponent component, String ddMenuStyle, String menuGroupStyle, String menuItemStyle) {
        UIComponent ddMenu = getDDMenu(component);
        UIComponent menuGroup = getMenuGroup(component);
        Object styleClass = null;
        if (ddMenu != null && ddMenuStyle != null && ddMenuStyle.length() > 0) {
            styleClass = ddMenu.getAttributes().get(ddMenuStyle);
        }
        if (menuGroup != null && menuGroupStyle != null && menuGroupStyle.length() > 0) {
            styleClass = concatClasses(styleClass, menuGroup.getAttributes().get(menuGroupStyle));
        }

        return concatClasses(styleClass, component.getAttributes().get(menuItemStyle));
    }

    /**
     * Finds a parent of given UI <code>component</code>.
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
     * @param component
     * @return <code>AbstractDropDownMenu</code>
     */
    protected AbstractDropDownMenu getDDMenu(UIComponent component) {
        return (AbstractDropDownMenu) getParent(component, AbstractDropDownMenu.class);
    }  
    
    /**
     * Returns a parent <code>AbstractMenuGroup</code> object of the given component.
     * @param component
     * @return <code>AbstractMenuGroup</code>
     */
    protected AbstractMenuGroup getMenuGroup(UIComponent component) {
        return (AbstractMenuGroup) getParent(component, AbstractMenuGroup.class);
    }
    
    /**
     * It is introduced due to RF-10004 CDK: isEmpty method is generated incorrectly 
     * @param str
     * @return
     */
    protected boolean isStringEmpty(String str) {
        if (str != null && str.trim().length() > 0) {
            return false;
        }
        return true;
    }

    public String getSubmitMode(UIComponent component){
        return this.resolveSubmitMode((AbstractMenuItem) component).name();
    }
}
