package org.richfaces.component;

import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.ajax4jsf.component.AjaxOutput;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.FocusRendererBase;
import org.richfaces.renderkit.focus.FocusRendererInterface;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * <p>
 * Focus component allows to set focus based on validation of components or alternatively it can preserve focus on currently
 * focused form input.
 * </p>
 *
 * <p>
 * Focus can be bound to form (in case of placement to h:form) or to whole view (when placed outside of forms) - in latter case,
 * all forms will be managed by one Focus. There can be at most one Focus per form. When there is one view-scoped Focus and form
 * defines own Focus, form-scoped Focus settings will be used.
 * </p>
 *
 * <p>
 * Focus is applied each time the component is rendered - for each full page submit and for each partial page request (in case
 * of ajaxRendered=true). Alternatively, you can use JavaScript API: <tt>applyFocus()</tt> function will immediately cause.
 * </p>
 */
@JsfComponent(type = AbstractFocus.COMPONENT_TYPE, family = AbstractFocus.COMPONENT_FAMILY, renderer = @JsfRenderer(type = FocusRendererBase.RENDERER_TYPE), tag = @Tag(type = TagType.Facelets))
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractFocus extends UIOutput implements AjaxOutput {

    public static final String COMPONENT_TYPE = "org.richfaces.Focus";
    public static final String COMPONENT_FAMILY = "org.richfaces.Focus";

    /**
     * Defines whether focus state should be updated during each AJAX request automatically. (default: true)
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isAjaxRendered();

    /**
     * <p>
     * Defines if focus should respect validation of inputs.
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
     * Defines whether focus should not be applied after initial page load, but will need to be triggered by JavaScript function
     * from Focus API: <tt>applyFocus()</tt> or via re-rendering (postback).
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isDelayed();

    /**
     * Hide keepTransient attribute from AjaxOutput
     */
    @Attribute(hidden = true)
    public abstract boolean isKeepTransient();

    @Attribute(hidden = true)
    public abstract Converter getConverter();

    @Attribute(hidden = true)
    public abstract Object getValue();

    /**
     * Returns a mode of Focus component
     */
    public Mode getMode() {
        UIForm form = (UIForm) RendererUtils.getInstance().getNestingForm(this);
        if (form == null) {
            return Mode.VIEW;
        }

        return Mode.FORM;
    }

    public static enum Mode {
        /**
         * In this mode, focus is processed every time form is submitted.
         *
         * There can be only one focus of this type per form.
         */
        FORM,
        /**
         * This mode brings focus functionality to all forms in a view.
         *
         * The view focus settings can be overridden for each specific form by using {{@link #FORM} mode.
         */
        VIEW
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {

        if (event.getSource() == this) {
            if (event instanceof PostAddToViewEvent) {

                FacesContext context = FacesContext.getCurrentInstance();

                FocusRendererInterface renderer = (FocusRendererInterface) context.getRenderKit().getRenderer(COMPONENT_FAMILY,
                        FocusRendererBase.RENDERER_TYPE);
                renderer.postAddToView(context, this);
            }
        }

        super.processEvent(event);
    }
}
