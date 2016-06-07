/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.servlet.jsp.jstl.sql.Result;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AutocompleteProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.ErrorProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.FocusProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.MetaComponentRenderer;
import org.richfaces.view.facelets.AutocompleteHandler;

/**
 * <p>The &lt;rich:autocomplete&gt; component is an auto-completing input-box with built-in Ajax capabilities. It
 * supports client-side suggestions, browser-like selection, and customization of the look and feel.</p>
 *
 * @author Nick Belaevski
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handlerClass = AutocompleteHandler.class),
        renderer = @JsfRenderer(type = "org.richfaces.AutocompleteRenderer"))
public abstract class AbstractAutocomplete extends UIInput implements MetaComponentResolver, MetaComponentEncoder, DisabledProps, FocusProps, EventsKeyProps, EventsMouseProps, StyleClassProps, StyleProps, AutocompleteProps, CoreProps {
    public static final String ITEMS_META_COMPONENT_ID = "items";
    public static final String COMPONENT_TYPE = "org.richfaces.Autocomplete";
    public static final String COMPONENT_FAMILY = UIInput.COMPONENT_FAMILY;
    private static final Logger LOGGER = RichfacesLogger.COMPONENTS.getLogger();

    /**
     * A value to set in the target input element on a choice suggestion that isn't shown in the suggestion table.
     * It can be used for descriptive output comments or suggestions. If not set, all text in the suggestion row is set as a value
     */
    @Attribute(literal = false)
    public abstract Object getFetchValue();

    /**
     * Assigns one or more space-separated CSS class names to the selected suggestion entry
     */
    @Attribute(defaultValue = "rf-au-itm-sel")
    public abstract String getSelectedItemClass();

    /**
     * Assigns one or more space-separated CSS class names to the content of the popup suggestion element
     */
    @Attribute()
    public abstract String getPopupClass();

    /**
     * Assigns one or more space-separated CSS class names to the input element
     */
    @Attribute()
    public abstract String getInputClass();

    /**
     * <p>
     * Type of the layout encoded using nested components should be defined using layout attribute.
     * Possible values are:
     * </p>
     * <dl>
     *      <dt>list</dt>
     *      <dd>suggestions wrapped to HTML unordered list</dd>
     *      <dt>div</dt>
     *      <dd>suggestions wrapped with just div element</dd>
     *      <dt>table</dt>
     *      <dd>suggestions are encoded as table rows, column definitions are required in this case</dd>
     * </dl>
     * <p>Default: div</p>
     */
    @Attribute
    public abstract String getLayout();

    /**
     * <p>Allow a user to enter multiple values separated by specific characters. As the user types, a suggestion will
     * present as normal. When they enter the specified token character, this begins a new suggestion process,
     * and the component will then only use text entered after the token character for suggestions.</p>
     *
     * <p>Make sure that no character defined in tokens is part of any suggestion value. E.g. do not use space as a token
     * if you expect to allow spaces in suggestion values.</p>
     *
     * <p>When tokens defined, they can be naturally separated by space character - input separated by tokens ', ' or ' ,'
     * will be considered as it would be ',' token without any space.</p>
     */
    @Attribute
    public abstract String getTokens();

    /**
     * Causes the combo-box to fill the text field box with a matching suggestion as the user types
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isAutofill();

    /**
     * <p>Boolean value indicating whether to display a button to expand the popup suggestion element</p>
     * <p>Default: false</p>
     */
    @Attribute
    public abstract boolean isShowButton();

    /**
     * Boolean value indicating whether the first suggestion item is selected as the user types
     * <p>Default: true</p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSelectFirst();

    /**
     * <p>
     * A javascript function used to filter the result list returned from the ajax call to the server.
     * This function should have two parameters; subString(current input value considering tokens)
     * and value (currently iterated item value) and return boolean flag which means if the value satisfies the substring passed.
     * The function will be called for every available suggestion in order to construct a new list of suggestions.
     * </p>
     * <p>Default: A javascript method called <i>startsWith</i></p>
     */
    @Attribute
    public abstract String getClientFilterFunction();

    // ----------- Event Attributes

    /**
     * Javascript code executed when an item is selected
     */
    @Attribute(events = @EventName("selectitem"))
    public abstract String getOnselectitem();

    /**
     * Javascript code executed when this element loses focus and its value has been modified since gaining focus.
     */
    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    // ----------- List events

    /**
     * Javascript code executed when a pointer button is clicked over the popup list element.
     */
    @Attribute(events = @EventName("listclick"))
    public abstract String getOnlistclick();

    /**
     * Javascript code executed when a pointer button is double clicked over this element.
     */
    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOnlistdblclick();

    /**
     * Javascript code executed when a pointer button is pressed down over this element.
     */
    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOnlistmousedown();

    /**
     * Javascript code executed when a pointer button is released over this element.
     */
    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOnlistmouseup();

    /**
     * Javascript code executed when a pointer button is moved onto this element.
     */
    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOnlistmouseover();

    /**
     * Javascript code executed when a pointer button is moved within this element.
     */
    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOnlistmousemove();

    /**
     * Javascript code executed when a pointer button is moved away from this element.
     */
    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOnlistmouseout();

    /**
     * Javascript code executed when a key is pressed and released over this element.
     */
    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOnlistkeypress();

    /**
     * Javascript code executed when a key is pressed down over this element.
     */
    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOnlistkeydown();

    /**
     * Javascript code executed when a key is released over this element.
     */
    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOnlistkeyup();

    public DataModel<Object> getItems(FacesContext facesContext, String value) {
        return getItems(facesContext, this, value);
    }

    public static DataModel<Object> getItems(FacesContext facesContext, UIComponent component, String value) {
        if (!(component instanceof AutocompleteProps)) {
            return null;
        }
        AutocompleteProps autocomplete = (AutocompleteProps) component;
        Object itemsObject = null;

        MethodExpression autocompleteMethod = autocomplete.getAutocompleteMethod();
        if (autocompleteMethod != null) {
            try {
                try {
                    itemsObject = autocompleteMethod.invoke(facesContext.getELContext(), new Object[] { facesContext,
                            component, value });
                } catch (MethodNotFoundException e1) {
                    try {
                        // fall back to evaluating an expression assuming there is just one parameter (RF-11469)
                        itemsObject = autocomplete.getAutocompleteMethodWithOneParameter().invoke(facesContext.getELContext(), new Object[] { value });
                    } catch (MethodNotFoundException e2) {
                        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
                        autocompleteMethod = expressionFactory.createMethodExpression(facesContext.getELContext(),
                                autocompleteMethod.getExpressionString(), Object.class, new Class[] { String.class });
                        itemsObject = autocompleteMethod.invoke(facesContext.getELContext(), new Object[] { value });
                    }
                }
            } catch (ELException ee) {
                LOGGER.error(ee.getMessage(), ee);
            }
        } else {
            itemsObject = autocomplete.getAutocompleteList();
        }

        DataModel result;

        if (itemsObject instanceof Object[]) {
            result = new ArrayDataModel((Object[]) itemsObject);
        } else if (itemsObject instanceof List) {
            result = new ListDataModel((List<Object>) itemsObject);
        } else if (itemsObject instanceof Result) {
            result = new ResultDataModel((Result) itemsObject);
        } else if (itemsObject instanceof ResultSet) {
            result = new ResultSetDataModel((ResultSet) itemsObject);
        } else if (itemsObject != null) {
            List<Object> temp = new ArrayList<Object>();
            Iterator<Object> iterator = ((Iterable<Object>) itemsObject).iterator();
            while (iterator.hasNext()) {
                temp.add(iterator.next());
            }
            result = new ListDataModel(temp);
        } else {
            result = new ListDataModel();
        }

        return result;
    }



    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (ITEMS_META_COMPONENT_ID.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }

        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        return null;
    }

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (context instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback,
                    ITEMS_META_COMPONENT_ID);
                if (result == VisitResult.COMPLETE) {
                    return true;
                } else if (result == VisitResult.REJECT) {
                    return false;
                }
            }
        }

        return super.visitTree(context, callback);
    }

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }
}
