package org.richfaces.component;

import javax.faces.component.UIOutput;

import org.ajax4jsf.component.AjaxOutput;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.FocusRendererBase;

/**
 * <p>
 * Focus Manager component allows to set focus based on validation of components or alternatively it can preserve focus on
 * currently focused form input.
 * </p>
 *
 * <p>
 * Focus Manager can be bound to form (in case of placement to h:form) or to whole view (when placed outside of forms) - in
 * latter case, all forms will be managed by one Focus Manager. There can be at most one Focus Manager per form. When there is
 * one view-scoped Focus Manager and form defines own Focus Manager, form-scoped Focus Manager settings will be used.
 * </p>
 *
 * <p>
 * Focus Manager is applied each time the component is rendered - for each full page submit and for each partial page request
 * (in case of ajaxRendered=true). Alternatively, you can use JavaScript API: <tt>applyFocus()</tt> function will immediately
 * cause.
 * </p>
 */
@JsfComponent(type = AbstractFocus.COMPONENT_TYPE, family = AbstractFocus.COMPONENT_FAMILY, renderer = @JsfRenderer(type = FocusRendererBase.RENDERER_TYPE), tag = @Tag(type = TagType.Facelets))
public abstract class AbstractFocus extends UIOutput implements AjaxOutput {

    public static final String COMPONENT_TYPE = "org.richfaces.Focus";
    public static final String COMPONENT_FAMILY = "org.richfaces.Focus";

    /**
     * Defines whether focus manager state should be updated during each AJAX request automatically. (default: true)
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isAjaxRendered();

    /**
     * <p>
     * Defines if focus manager should respect validation of inputs.
     * </p>
     *
     * <p>
     * If true, only invalid form fields will be focused when focus applied.
     * </p>
     *
     * <p>
     * (default: true)
     * </p>
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isValidationAware();

    /**
     * Defines whether focus should be preserved on last focused input before request was done (default: false)
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isPreserve();

    /**
     * Defines whether focus should not be applied right fater rendering, but will need to be triggered by JavaScript function
     * from Focus Manager API: <tt>applyFocus()</tt>.
     */
    @Attribute(defaultValue = "false", hidden = true)
    public abstract boolean isDelayed();
    
    /**
     * Hide keepTransient attribute from AjaxOutput
     */
    @Attribute(hidden = true)
    public abstract boolean isKeepTransient();
}
