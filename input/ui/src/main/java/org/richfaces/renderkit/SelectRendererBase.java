package org.richfaces.renderkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.AbstractSelect;

@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.js"), @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces.js"), @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces-event.js"), @ResourceDependency(name = "richfaces-base-component.js"),
        @ResourceDependency(name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupList.js"),
        @ResourceDependency(library = "org.richfaces", name = "selectList.js"),
        @ResourceDependency(library = "org.richfaces", name = "select.js"),
        @ResourceDependency(library = "org.richfaces", name = "select.ecss") })
public class SelectRendererBase extends InputRendererBase {
    
    //TODO: move to SelectHelper <!--
    public static final String OPTION_SHOWCONTROL = "showControl";
    
    public static final String OPTIONS_SELECT_ITEM_VALUE_INPUT = "selValueInput";

    public static final String OPTION_LIST_ITEMS = "items";
    // -->
    
    public void renderListHandlers(FacesContext facesContext, UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext, component, SelectHelper.SELECT_LIST_HANDLER_ATTRIBUTES);
    }

    public List<ClientSelectItem> getConvertedSelectItems(FacesContext facesContext, UIComponent component) {
        return SelectHelper.getConvertedSelectItems(facesContext, component);
    }

    public String getSelectInputLabel(FacesContext facesContext, UIComponent component) {
        return SelectHelper.getSelectInputLabel(facesContext, component);
    }

    public void encodeItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems)
        throws IOException {
        SelectHelper.encodeItems(facesContext, component, clientSelectItems, HtmlConstants.DIV_ELEM);
    }

    public void buildScript(ResponseWriter writer, FacesContext facesContext, UIComponent component, List<ClientSelectItem> selectItems) throws IOException {
        if (!(component instanceof AbstractSelect)) {
            return;
        }

        AbstractSelect abstractSelect = (AbstractSelect)component;
        String scriptName = getScriptName();
        JSFunction function = new JSFunction(scriptName);

        String clientId = abstractSelect.getClientId(facesContext);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(OPTION_SHOWCONTROL, abstractSelect.isShowButton());
        options.put(OPTION_LIST_ITEMS, selectItems);
        options.put(PopupConstants.OPTIONS_ITEM_CLASS, abstractSelect.getItemCss());
        options.put(PopupConstants.OPTIONS_SELECT_ITEM_CLASS, abstractSelect.getSelectItemCss());
        options.put(PopupConstants.OPTIONS_LIST_CLASS, abstractSelect.getListCss());
        options.put(PopupConstants.OPTIONS_LIST_CORD, clientId + "List");

        function.addParameter(clientId);
        function.addParameter(options);

        writer.write(function.toString());
    }
    
    public String getValue(FacesContext facesContext, UIComponent component) throws IOException {
        String value = getInputValue(facesContext, component);
        if (value == null || "".equals(value)) {
            value = ((AbstractSelect) component).getDefaultLabel();
        }
        return value;
    }
    
    protected String getScriptName() {
        return "new RichFaces.ui.Select";
    }

    public String getListStyles(FacesContext facesContext, UIComponent component) {
        return "";
    }
}
