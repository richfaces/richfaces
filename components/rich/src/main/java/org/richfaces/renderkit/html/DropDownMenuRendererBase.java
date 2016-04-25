package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractDropDownMenu;
import org.richfaces.component.AbstractMenuGroup;
import org.richfaces.component.AbstractMenuItem;
import org.richfaces.component.AbstractMenuSeparator;
import org.richfaces.component.Mode;
import org.richfaces.component.Positioning;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.renderkit.RenderKitUtils.ScriptHashVariableWrapper;
import org.richfaces.renderkit.RendererBase;

@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.position.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "menuKeyNavigation.js"),
        @ResourceDependency(library = "org.richfaces", name = "menu-base.js"),
        @ResourceDependency(library = "org.richfaces", name = "menu.js"),
        @ResourceDependency(library = "org.richfaces", name = "menugroup.js"),
        @ResourceDependency(library = "org.richfaces", name = "menuitem.js"),
        @ResourceDependency(library = "org.richfaces", name = "dropdownmenu.ecss", target = "head") })
public abstract class DropDownMenuRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.DropDownMenuRenderer";
    public static final int DEFAULT_MIN_POPUP_WIDTH = 250;
    public static final String DEFAULT_SHOWEVENT = "mouseover";

    @Override
    public void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractDropDownMenu dropDownMenu = (AbstractDropDownMenu) component;

        for (UIComponent child : dropDownMenu.getChildren()) {
            if (child.isRendered()
                    && (child instanceof AbstractMenuGroup || child instanceof AbstractMenuItem
                        || child instanceof AbstractMenuSeparator || UIComponent.isCompositeComponent(child))) {

                child.encodeAll(facesContext);
            }
        }
    }

    protected boolean isDisabled(FacesContext facesContext, UIComponent component) {
        if (component instanceof AbstractDropDownMenu) {
            return ((AbstractDropDownMenu) component).isDisabled();
        }
        return false;
    }

    protected UIComponent getLabelFacet(FacesContext facesContext, UIComponent component) {
        UIComponent facet = null;
        AbstractDropDownMenu ddmenu = (AbstractDropDownMenu) component;
        if (ddmenu != null) {
            facet = ddmenu.getFacet(AbstractDropDownMenu.Facets.labelDisabled.toString());
            if (!ddmenu.isDisabled() || facet == null) {
                facet = ddmenu.getFacet(AbstractDropDownMenu.Facets.label.toString());
            }
        }
        return facet;
    }

    public List<Map<String, Object>> getMenuGroups(FacesContext facesContext, UIComponent component) {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        List<AbstractMenuGroup> groups = new ArrayList<AbstractMenuGroup>();
        if (component instanceof AbstractDropDownMenu) {
            if (component.isRendered() && !((AbstractDropDownMenu) component).isDisabled()) {
                getMenuGroups(component, groups);
            }
        }
        for (AbstractMenuGroup group : groups) {
            if (group.isRendered() && !group.isDisabled()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", group.getClientId(facesContext));
                RenderKitUtils.addToScriptHash(map, "onhide", group.getOnhide(), null, ScriptHashVariableWrapper.eventHandler);
                RenderKitUtils.addToScriptHash(map, "onshow", group.getOnshow(), null, ScriptHashVariableWrapper.eventHandler);
                RenderKitUtils.addToScriptHash(map, "verticalOffset", group.getVerticalOffset(), "0");
                RenderKitUtils.addToScriptHash(map, "horizontalOffset", group.getHorizontalOffset(), "0");

                Positioning jointPoint = group.getJointPoint();
                if (jointPoint == null) {
                    jointPoint = org.richfaces.component.Positioning.DEFAULT;
                }
                RenderKitUtils.addToScriptHash(map, "jointPoint", jointPoint.getValue(),
                        org.richfaces.component.Positioning.DEFAULT.getValue());

                Positioning direction = group.getDirection();
                if (direction == null) {
                    direction = org.richfaces.component.Positioning.DEFAULT;
                }
                RenderKitUtils.addToScriptHash(map, "direction", direction.getValue(),
                        org.richfaces.component.Positioning.DEFAULT.getValue());

                RenderKitUtils.addToScriptHash(map, "cssRoot", component.getAttributes().get("cssRoot"), "ddm");

                results.add(map);
            }
        }
        return results;
    }

    protected int getPopupWidth(UIComponent component) {
        int width = ((AbstractDropDownMenu) component).getPopupWidth();
        if (width <= 0) {
            width = DEFAULT_MIN_POPUP_WIDTH;
        }
        return width;
    }

    protected Mode getMode(UIComponent component) {
        Mode mode = ((AbstractDropDownMenu) component).getMode();
        if (mode == null) {
            mode = Mode.server;
        }
        return mode;
    }

    protected Positioning getJointPoint(UIComponent component) {
        Positioning jointPoint = ((AbstractDropDownMenu) component).getJointPoint();
        if (jointPoint == null) {
            jointPoint = org.richfaces.component.Positioning.DEFAULT;
        }
        return jointPoint;
    }

    protected Positioning getDirection(UIComponent component) {
        Positioning direction = ((AbstractDropDownMenu) component).getDirection();
        if (direction == null) {
            direction = org.richfaces.component.Positioning.DEFAULT;
        }
        return direction;
    }

    private void getMenuGroups(UIComponent component, List<AbstractMenuGroup> list) {
        if (component != null && list != null) {
            for (UIComponent c : component.getChildren()) {
                if (c instanceof AbstractMenuGroup) {
                    list.add((AbstractMenuGroup) c);
                }
                getMenuGroups(c, list);
            }
        }
    }

    protected String getShowEvent(UIComponent component) {
        String value = ((AbstractDropDownMenu) component).getShowEvent();
        if (value == null || "".equals(value)) {
            value = DEFAULT_SHOWEVENT;
        }
        return value;
    }
}
