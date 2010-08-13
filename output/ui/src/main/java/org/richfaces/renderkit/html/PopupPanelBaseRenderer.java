package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.renderkit.RendererBase;
import org.richfaces.component.AbstractPopupPanel;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONMap;

//TODO nick - JSF have concept of library, it should be used instead of '/' in resource names
@ResourceDependencies( { @ResourceDependency(name = "jquery.js"), @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-base-component.js"), 
    @ResourceDependency(library = "org.richfaces", name = "popupPanel.js"),
    @ResourceDependency(library = "org.richfaces", name = "popupPanelBorders.js"), 
    @ResourceDependency(library = "org.richfaces", name = "popupPanelSizer.js"),
    @ResourceDependency(library = "org.richfaces", name = "popupPanel.ecss")

})
public class PopupPanelBaseRenderer extends RendererBase {

    private static final String CONTROLS_FACET = "controls";
    private static final String HEADER_FACET = "header";
    private static final int SIZE = 10;
    private static final String STATE_OPTION_SUFFIX = "StateOption_";
    
    //TODO nick - use enums
    private static final Set<String> ALLOWED_ATTACHMENT_OPTIONS = new HashSet<String>();
    static {
        ALLOWED_ATTACHMENT_OPTIONS.add("body");
        ALLOWED_ATTACHMENT_OPTIONS.add("parent");
        ALLOWED_ATTACHMENT_OPTIONS.add("form");
    }

    public void renderHeaderFacet(FacesContext context, UIComponent component) throws IOException {
        renderFacet(context, component, HEADER_FACET);
    }

    public void renderControlsFacet(FacesContext context, UIComponent component) throws IOException {
        renderFacet(context, component, CONTROLS_FACET);
    }

    private void renderFacet(FacesContext context, UIComponent component, String facet) throws IOException {
        UIComponent headerFacet = component.getFacet(facet);
        headerFacet.encodeAll(context);
    }

    @SuppressWarnings("unchecked")
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);

        AbstractPopupPanel panel = (AbstractPopupPanel) component;
        ExternalContext exCtx = context.getExternalContext();
        Map<String, String> rqMap = exCtx.getRequestParameterMap();
        Object panelOpenState = rqMap.get(panel.getClientId(context) + "OpenedState");

        if (panel.isKeepVisualState()) {
            if (null != panelOpenState) {
                // Bug https://jira.jboss.org/jira/browse/RF-2466
                // Incorrect old:
                // panel.setShowWhenRendered(Boolean.parseBoolean((String) clnId));
                // ShowWhenRendered can be settled separately with modal panel "showWhenRendered" attribute
                // so we should combine ShowWhenRendered || KeepVisualState && (OpenedState==TRUE) against rewriting
                boolean show = panel.isShow() || Boolean.parseBoolean((String) panelOpenState);
                panel.setShow(show);

                Map<String, Object> visualOptions = (Map<String, Object>) getHandledVisualOptions(panel);
                Iterator<Entry<String, String>> it = rqMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    int suffixPos = entry.getKey().toString().indexOf(STATE_OPTION_SUFFIX);
                    if (-1 != suffixPos) {
                        String key = entry.getKey().toString().substring(suffixPos + STATE_OPTION_SUFFIX.length());
                        visualOptions.put(key, entry.getValue());
                    }
                }
            }
        }
    }

    protected Class getComponentClass() {
        return AbstractPopupPanel.class;
    }

    public void checkOptions(FacesContext context, UIComponent component) {
    	AbstractPopupPanel panel = (AbstractPopupPanel) component;
        if (panel.isAutosized() && panel.isResizeable()) {
            throw new IllegalArgumentException("Autosized modal panel can't be resizeable.");
        }

        String domElementAttachment = panel.getDomElementAttachment();
        if (domElementAttachment != null && domElementAttachment.trim().length() != 0) {
            if (!ALLOWED_ATTACHMENT_OPTIONS.contains(domElementAttachment)) {
                throw new IllegalArgumentException("Value '" + domElementAttachment
                    + "' of domElementAttachment attribute is illegal. " + "Allowed values are: "
                    + ALLOWED_ATTACHMENT_OPTIONS);
            }
        }

        if (panel.getMinHeight() != -1) {
            if (panel.getMinHeight() < SIZE) {
                throw new IllegalArgumentException();
            }

        }

        if (panel.getMinWidth() != -1) {
            if (panel.getMinWidth() < SIZE) {
                throw new IllegalArgumentException();
            }

        }
    }

    public boolean getRendersChildren() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public String buildShowScript(FacesContext context, UIComponent component) {
    	AbstractPopupPanel panel = (AbstractPopupPanel) component;
        StringBuilder result = new StringBuilder();

        // Bug https://jira.jboss.org/jira/browse/RF-2466
        // We are already processed KeepVisualState and current open state in
        // doDecode, so no need to check panel.isKeepVisualState() here.
        if (panel.isShow()) {
            result.append("RichFaces.ui.PopupPanel.showPopupPanel('" + panel.getClientId(context) + "', {");

            //TODO nick - use ScriptUtils.toScript
            Iterator<Map.Entry<String, Object>> it = ((Map<String, Object>) getHandledVisualOptions(panel)).entrySet()
                .iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();

                result.append(entry.getKey() + ": '" + entry.getValue() + "'");
                if (it.hasNext()) {
                    result.append(", ");
                }
            }

            result.append("});");
        }
        return result.toString();
    }

    public String getStyleIfTrimmed(UIComponent panel){
    	if (panel.getAttributes().get("trimOverlayedElements").equals(Boolean.TRUE)) {
    	    return "position: relative, z-index : 0";
    	}
    	return "";
    }
    
    private void writeOption(StringBuilder builder, String attribbute, Object value, UIComponent component,
        boolean isString) {

        //TODO nick - use ScriptUtils.toScript
        if (component.getAttributes().get(attribbute) != null) {
            builder.append(attribbute + ":");
            if (isString) {
                builder.append("'");
            }
            builder.append(value);
            if (isString) {
                builder.append("'");
            }
            builder.append(",");
        }
    }
    
    public String buildScript(FacesContext context, UIComponent component) throws IOException {
    	AbstractPopupPanel panel = (AbstractPopupPanel) component;
        StringBuilder result = new StringBuilder();
        result.append("new RichFaces.ui.PopupPanel('");
        result.append(panel.getClientId());
        result.append("',{");
        //TODO nick - use RendererUtils.addToScriptHash(Map<String, Object>, String, Object)
        writeOption(result, "width", panel.getWidth(), component, false);
        writeOption(result, "height", panel.getHeight(), component, false);
        writeOption(result, "minWidth", panel.getMinWidth(), component, false);
        writeOption(result, "minHeight", panel.getMinHeight(), component, false);
        writeOption(result, "maxWidth", panel.getMaxWidth(), component, false);
        writeOption(result, "maxHeight", panel.getMaxHeight(), component, false);
        writeOption(result, "resizeable", panel.isResizeable(), component, false);
        writeOption(result, "moveable", panel.isMoveable(), component, false);
        writeOption(result, "left", panel.getLeft(), component, true);
        writeOption(result, "top", panel.getTop(), component, true);
        writeOption(result, "zIndex", panel.getZIndex(), component, false);
        writeOption(result, "onresize", writeEventHandlerFunction(context, panel, "onresize"), component, false);
        writeOption(result, "onmove", writeEventHandlerFunction(context, panel, "onmove"), component, false);
        writeOption(result, "onshow", writeEventHandlerFunction(context, panel, "onshow"), component, false);
        writeOption(result, "onhide", writeEventHandlerFunction(context, panel, "onhide"), component, false);
        writeOption(result, "onbeforeshow", writeEventHandlerFunction(context, panel, "onbeforeshow"), component, false);
        writeOption(result, "onbeforehide", writeEventHandlerFunction(context, panel, "onbeforehide"), component, false);
        writeOption(result, "shadowDepth", panel.getShadowDepth(), component, true);
        writeOption(result, "shadowOpacity", panel.getShadowOpacity(), component, true);
        writeOption(result, "domElementAttachment", panel.getDomElementAttachment(), component, true);
        writeOption(result, "keepVisualState", panel.isKeepVisualState(), component, false);
        writeOption(result, "show", panel.isShow(), component, false);
        writeOption(result, "modal", panel.isModal(), component, false);
        writeOption(result, "autosized", panel.isAutosized(), component, false);
        writeOption(result, "overlapEmbedObjects", panel.isOverlapEmbedObjects(), component, false);
        //TODO nick - what is deleted here?
        result.delete(result.length() - 1, result.length());
        if (component.getAttributes().get("visualOptions") != null) {
            result.append(writeVisualOptions(context, panel));
        }
        result.append("});");
        return result.toString();
    }

    public String writeEventHandlerFunction(FacesContext context, UIComponent component, String eventName)
        throws IOException {
        String event = (String) component.getAttributes().get(eventName);

        if (event != null) {
            event = event.trim();

            if (event.length() != 0) {
                JSFunctionDefinition function = new JSFunctionDefinition();

                function.addParameter("event");
                function.addToBody(event);

                return function.toScript();
            }
        }

        return "";
    }

    public Map<String, Object> getHandledVisualOptions(AbstractPopupPanel panel) {
        String options = panel.getVisualOptions();
        Map<String, Object> result;
        result = prepareVisualOptions(options, panel);

        if (null == result) {
            result = new HashMap<String, Object>();
        }
        return result;
    }
    
    private String writeVisualOptions(FacesContext context, AbstractPopupPanel panel) throws IOException {
        StringBuffer result = new StringBuffer();

        Iterator<Map.Entry<String, Object>> it = ((Map<String, Object>) getHandledVisualOptions(panel)).entrySet()
            .iterator();
        if (it.hasNext()) {
            result.append(",\n");
        }
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();

            result.append(entry.getKey() + ": '" + entry.getValue() + "'");
            if (it.hasNext()) {
                result.append(",");
            }
        }
        return result.toString();
    }
    
    private Map<String, Object> prepareVisualOptions(Object value, AbstractPopupPanel panel) {
        if (null == value) {
            return new HashMap<String, Object>();
        } else if (value instanceof Map) {
            return (Map<String, Object>) value;
        } else if (value instanceof String) {
            String s = (String) value;
            if (!s.startsWith("{")) {
                s = "{" + s + "}";
            }
            try {
                return new HashMap<String, Object>(new JSONMap(s));
            } catch (JSONException e) {
                throw new FacesException(e);
            }
        } else {
            throw new FacesException("Attribute visualOptions of component [" + panel.getClientId(FacesContext.getCurrentInstance())
                + "] must be instance of Map or String, but its type is " + value.getClass().getSimpleName());
        }
    }
}
