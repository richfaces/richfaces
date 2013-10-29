/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.ui.input.autocomplete;

import java.io.IOException;

import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.ui.attribute.AjaxEventsProps;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.FocusProps;
import org.richfaces.ui.attribute.StyleClassProps;
import org.richfaces.ui.attribute.StyleProps;
import org.richfaces.ui.common.meta.MetaComponentEncoder;
import org.richfaces.ui.common.meta.MetaComponentResolver;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * <p>
 * The &lt;r:autocomplete&gt; component is an auto-completing input-box with built-in Ajax capabilities. It supports client-side
 * suggestions, browser-like selection, and customization of the look and feel.
 * </p>
 *
 * @author Lukas Fryc
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, generate = true), renderer = @JsfRenderer(type = AutocompleteRendererBase.RENDERER_TYPE))
public abstract class AbstractAutocomplete extends UIInput implements AjaxEventsProps, FocusProps, EventsKeyProps, EventsMouseProps, StyleProps,
        StyleClassProps, MetaComponentResolver, MetaComponentEncoder {

    public static final String COMPONENT_TYPE = "org.richfaces.ui.Autocomplete";
    public static final String COMPONENT_FAMILY = UIInput.COMPONENT_FAMILY;

    static final String SUGGESTIONS_META_COMPONENT_ID = "suggestions";

    /**
     * A collection of suggestions that will be resented to the user
     */
    @Attribute()
    public abstract Object getAutocompleteList();

    /**
     * A method which returns a list of suggestions according to a supplied prefix
     */
    @Attribute(signature = @Signature(returnType = Object.class, parameters = { FacesContext.class, UIComponent.class,
            String.class }))
    public abstract MethodExpression getAutocompleteMethod();

    public abstract void setAutocompleteMethod(MethodExpression expression);

    /**
     * A request-scope attribute via which the data object for the current row will be used when iterating
     */
    @Attribute(literal = true)
    public abstract String getVar();

    // TODO nick - el-only?

    /**
     * A value to set in the target input element on a choice suggestion that isn't shown in the suggestion table. It can be
     * used for descriptive output comments or suggestions. If not set, all text in the suggestion row is set as a value
     */
    @Attribute(literal = false)
    public abstract Object getFetchValue();

    /**
     * Minimal number of chars in input to activate suggestion popup
     */
    @Attribute(defaultValue = "1")
    public abstract int getMinChars();

    /**
     * <p>
     * Determine how the suggestion list is requested:
     * </p>
     * <dl>
     * <dt>client</dt>
     * <dd>pre-loads data to the client and uses the input to filter the possible suggestions</dd>
     * <dt>ajax</dt>
     * <dd>fetches suggestions with every input change using Ajax requests</dd>
     * <dt>cachedAjax</dt>
     * <dd>
     * pre-loads data via Ajax requests when the input length matches a minimum value. Set the minimum value with the minChars
     * attribute. All suggestions are handled on the client until the input prefix is changed, at which point a new request is
     * made based on the new input prefix</dd>
     * </dl>
     * <p>
     * Default: cachedAjax
     * </p>
     */
    @Attribute(defaultValue = "AutocompleteMode.cachedAjax")
    public abstract AutocompleteMode getMode();

    /**
     * <p>
     * Allow a user to enter multiple values separated by specific characters. As the user types, a suggestion will present as
     * normal. When they enter the specified token character, this begins a new suggestion process, and the component will then
     * only use text entered after the token character for suggestions.
     * </p>
     *
     * <p>
     * Make sure that no character defined in tokens is part of any suggestion value. E.g. do not use space as a token if you
     * expect to allow spaces in suggestion values.
     * </p>
     *
     * <p>
     * When tokens defined, they can be naturally separated by space character - input separated by tokens ', ' or ' ,' will be
     * considered as it would be ',' token without any space.
     * </p>
     */
    @Attribute
    public abstract String getTokens();

    /**
     * Causes the combo-box to fill the text field box with a matching suggestion as the user types
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isAutofill();

    /**
     * Boolean value indicating whether this component is disabled
     * <p>
     * Default: false
     * </p>
     */
    @Attribute
    public abstract boolean isDisabled();

    /**
     * <p>
     * Boolean value indicating whether to display a button to expand the popup suggestion element
     * </p>
     * <p>
     * Default: false
     * </p>
     */
    @Attribute
    public abstract boolean isShowButton();

    /**
     * Boolean value indicating whether the first suggestion item is selected as the user types
     * <p>
     * Default: true
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSelectFirst();

    /**
     * <p>
     * A javascript function used to filter the result list returned from the ajax call to the server. This function should have
     * two parameters; subString(current input value considering tokens) and value (currently iterated item value) and return
     * boolean flag which means if the value satisfies the substring passed. The function will be called for every available
     * suggestion in order to construct a new list of suggestions.
     * </p>
     * <p>
     * Default: A javascript method called <i>startsWith</i>
     * </p>
     */
    @Attribute
    public abstract String getClientFilterFunction();

    // ----------- Event Attributes

    /**
     * Javascript code triggered when an item is selected from the menu. The default action is to replace the text field's value with the value of the selected item.
     *
     * Canceling this event prevents the value from being updated, but does not prevent the menu from closing.
     */
    @Attribute(events = @EventName("select"))
    public abstract String getOnselect();

    /**
     * @deprecated use {@link #getOnselect()} ('select' event instead)
     */
    @Deprecated
    @Attribute(events = @EventName("selectitem"))
    public abstract String getOnselectitem();

    /**
     * Javascript code triggered when the field is blurred, if the value has changed.
     */
    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    /**
     * Javascript code triggered when the menu is hidden. Not every close event will be accompanied by a change event.
     */
    @Attribute(events = @EventName(value = "close"))
    public abstract String getOnclose();

    /**
     * Javascript code triggered when focus is moved to an item (not selecting). The default action is to replace the text
     * field's value with the value of the focused item, though only if the event was triggered by a keyboard interaction.
     *
     * Canceling this event prevents the value from being updated, but does not prevent the menu item from being focused.
     */
    @Attribute(events = @EventName(value = "focus"))
    public abstract String getOnfocus();

    /**
     * Javascript code triggered when the suggestion menu is opened or updated.
     */
    @Attribute(events = @EventName(value = "open"))
    public abstract String getOnopen();

    /**
     * Javascript code triggered after a search completes, before the menu is shown. Useful for local manipulation of suggestion
     * data, where a custom source option callback is not required. This event is always triggered when a search completes, even
     * if the menu will not be shown because there are no results or the Autocomplete is disabled.
     */
    @Attribute(events = @EventName(value = "response"))
    public abstract String getOnresponse();

    /**
     * Javascript code triggered before a search is performed, after minLength and delay are met. If canceled, then no request
     * will be started and no items suggested.
     */
    @Attribute(events = @EventName(value = "search"))
    public abstract String getOnsearch();

    // ----------- selected ajax props

    /**
     * The client-side script method to be called when an error has occurred during Ajax communications
     */
    @Attribute(events = @EventName("error"))
    public abstract String getOnerror();

    /**
     * Returns the meta component ID for the suggestions ('id&#64;suggestions')
     */
    String getSuggestionsMetaComponentId(FacesContext facesContext) {
        return resolveClientId(facesContext, this, AbstractAutocomplete.SUGGESTIONS_META_COMPONENT_ID);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.ui.common.meta.MetaComponentResolver#resolveClientId(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (SUGGESTIONS_META_COMPONENT_ID.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.ui.common.meta.MetaComponentResolver#substituteUnresolvedClientId(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        return null;
    }

    /**
     * Makes sure that the rendering of meta component is requested.
     */
    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (context instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback,
                        SUGGESTIONS_META_COMPONENT_ID);
                if (result == VisitResult.COMPLETE) {
                    return true;
                } else if (result == VisitResult.REJECT) {
                    return false;
                }
            }
        }

        return super.visitTree(context, callback);
    }

    /**
     * Delegates rendering of meta components to Autocomplete renderer
     */
    @Override
    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((AutocompleteRendererBase) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }

    /**
     * Returns 'table' if all children are columns and thus the component should be rendered as a table; it returns 'list'
     * otherwise
     */
    @Attribute(generate = false, hidden = true, defaultValue = "Layout.list")
    public String getLayout() {
        return (getChildCount() > 0 && Iterables.all(getChildren(), Predicates.instanceOf(UIColumn.class))) ? "table" : "list";
    }

    /**
     * {@link #setLayout(String)} was deprecated in 5.0
     *
     * The layout is determined by components provide as children of the autocomplete component
     */
    @Deprecated
    public void setLayout(String layout) {
        String message = "Autocomplete @layout attribute was deprecated in 5.0. The layout of the component is determined from provide children.";
        RichfacesLogger.COMPONENTS.getLogger().warn(message);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
    }
}
