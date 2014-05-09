package org.richfaces.component;

import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.validator.Validator;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.SelectProps;
import org.richfaces.validator.SelectLabelValueValidator;

/**
 * <p>
 * The &lt;rich:select&gt; component provides a drop-down list box for selecting a single value from multiple options. The
 * &lt;rich:select&gt; component can be configured as a combo-box, where it will accept typed input. The component also supports
 * keyboard navigation. The &lt;rich:select&gt; component functions similarly to the JSF UISelectOne component.
 * </p>
 *
 * @author abelevich
 */
@JsfComponent(type = AbstractSelect.COMPONENT_TYPE, family = AbstractSelect.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.SelectRenderer"), tag = @Tag(name = "select"))
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractSelect extends AbstractSelectComponent implements CoreProps, EventsKeyProps, EventsMouseProps, SelectProps {
    public static final String COMPONENT_TYPE = "org.richfaces.Select";
    public static final String COMPONENT_FAMILY = "org.richfaces.Select";

    /**
     * If "true", this component is disabled
     */
    @Attribute
    public abstract boolean isDisabled();

    /**
     * <p>
     * If "true" Allows the user to type into a text field to scroll through or filter the list
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
}
