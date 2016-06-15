package org.richfaces.component;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.validator.Validator;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AutocompleteProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.SelectItemsProps;
import org.richfaces.component.attribute.SelectProps;
import org.richfaces.component.util.SelectItemsInterface;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.renderkit.MetaComponentRenderer;
import org.richfaces.renderkit.SelectManyHelper;
import org.richfaces.validator.SelectLabelValueValidator;
import org.richfaces.view.facelets.AutocompleteHandler;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * The &lt;rich:select&gt; component provides a drop-down list box for selecting a single value from multiple options. The
 * &lt;rich:select&gt; component can be configured as a combo-box, where it will accept typed input. The component also supports
 * keyboard navigation. The &lt;rich:select&gt; component functions similarly to the JSF UISelectOne component.
 * </p>
 * <p>
 * The &lt;rich:select&gt; can optionally be used in an auto-completing mode, where the values in the drop-down list are provided
 * dynamically using either the autocompleteMethod or autocompleteList attributes.  If these attributes are omitted, the component
 * operates in the traditional non-auto-completing mode.  Refer to the individual attribute documentation to see which attributes are
 * applicable only with an auto-completing select list.
 * </p>
 *
 * @author abelevich
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        tag = @Tag(name = "select", type = TagType.Facelets, handlerClass = AutocompleteHandler.class),
        type = AbstractSelect.COMPONENT_TYPE, family = AbstractSelect.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.SelectRenderer"))
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractSelect extends AbstractSelectComponent implements SelectItemsInterface, MetaComponentResolver, MetaComponentEncoder, CoreProps, DisabledProps, EventsKeyProps, EventsMouseProps, SelectProps, AutocompleteProps, SelectItemsProps {
    public static final String ITEMS_META_COMPONENT_ID = "items";
    public static final String COMPONENT_TYPE = "org.richfaces.Select";
    public static final String COMPONENT_FAMILY = "org.richfaces.Select";

    public Object getItemValues() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> requestParameters = facesContext.getExternalContext().getRequestParameterMap();
        String value = AutocompleteMode.lazyClient == getMode() ? "" : requestParameters.get(this.getClientId(facesContext) + "Input");
        return AbstractAutocomplete.getItems(facesContext, this, value);
    }

    /**
     * <p>
     * If "true" Allows the user to type into a text field to scroll through or filter the list.  Implicitly true when using an auto-completing select list.
     * </p>
     * <p>
     * Default is "false"
     * </p>
     */
    @Attribute()
    public abstract boolean isEnableManualInput();

    /**
     * <p>
     * If "true" as the user types to narrow the list, automatically select the first element in the list. Applicable only when
     * enableManualInput is "true".
     * </p>
     * <p>
     * Default is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isSelectFirst();

    /**
     * <p>
     * When "true" display a button to expand the popup list
     * </p>
     * <p>
     * Default is "true"
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isShowButton();

    /**
     * The minimum height ot the list
     */
    @Attribute()
    public abstract String getMinListHeight();

    /**
     * The maximum height of the list
     */
    @Attribute()
    public abstract String getMaxListHeight();

    /**
     * A javascript function used to filter the list of items in the select popup
     */
    @Attribute
    public abstract String getClientFilterFunction();

    @Attribute(hidden = true)
    public abstract String getActiveClass();

    @Attribute(hidden = true)
    public abstract String getChangedClass();

    @Attribute(hidden = true)
    public abstract String getDisabledClass();

    /**
     * Value to be returned to the server if the corresponding option is selected by the user.  Used only with an auto-completing select, where the list of items comes from either the
     * autocompleteList or autocompleteMethod attributes.
     */
    @Override
    @Attribute
    public abstract Object getItemValue();

    /**
     * Label to be displayed to the user for the corresponding option.  Used only with an auto-completing select, where the list of items comes from either the
     * autocompleteList or autocompleteMethod attributes.
     */
    @Override
    @Attribute
    public abstract Object getItemLabel();

    /**
     * Expose the values from either the autocompleteList or autocompleteMethod attributes under a request scoped key so that the values may be referred to in an EL expression while rendering this component.
     * When using auto-completing select component this attribute is required.
     */
    @Override
    @Attribute
    public abstract String getVar();

    /**
     * The client-side script method to be called before an ajax request. (Only valid in autocomplete mode).
     */
    @Attribute(events = @EventName("begin"))
    public abstract String getOnbegin();

    /**
     * The client-side script method to be called after the DOM is updated. (Only valid in autocomplete mode).
     */
    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();

    /**
     * The client-side script method to be called after the ajax response comes back, but before the DOM is updated. (Only valid in autocomplete mode).
     */
    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();

    /**
     * Name of the request status component that will indicate the status of the Ajax request. (Only valid in autocomplete mode).
     */
    @Attribute
    public abstract String getStatus();

    /**
     * Override the validateValue method in cases where the component implements SelectItemsInterface
     *
     * @param facesContext
     * @param value
     */
    @Override
    protected void validateValue(FacesContext facesContext, Object value) {
        if (this instanceof SelectItemsInterface) {
            UISelectItems pseudoSelectItems = SelectManyHelper.getPseudoSelectItems((SelectItemsInterface) this);
            if (pseudoSelectItems != null) {
                this.getChildren().add(pseudoSelectItems);
                super.validateValue(facesContext, value);
                this.getChildren().remove(pseudoSelectItems);
                return;
            }
        }
        super.validateValue(facesContext, value);
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);

        if (event instanceof PostAddToViewEvent) {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            EditableValueHolder component = (EditableValueHolder) event.getComponent();

            Validator validator = facesContext.getApplication().createValidator(SelectLabelValueValidator.ID);
            component.addValidator(validator);
        }
    }

    @Override
    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (ITEMS_META_COMPONENT_ID.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }

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

    @Override
    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }

    @Override
    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        return null;
    }
}
