/**
 *
 */
package javax.faces.ajax;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author asmirnov
 *
 */
public interface ClientSideBehavior extends Serializable {
    /**
     * This method called during the Ajaxifiable#addAjaxBehavior(String eventName, AjaxBehavior behavior) to inform behavior
     * implementation for that component it belongs to.
     *
     * @param component
     */
    void setComponent(UIComponent component);

    /**
     * @return component for which behavior belongs.
     */
    UIComponent getComponent();

    /**
     * Create client-side listener call body. In the common case, behavior should delegate this call to its renderer.
     *
     * @param context current JSF context
     * @return client-side function call body.
     */
    String getCode(FacesContext context);

    /**
     * Return renderer for the this behavior. implementation should get appropriate renderer from the current RenderKit by
     * randerer type.
     *
     * @param context
     * @return corresponding renderer instance from the current RenderKit.
     */
    BehaviorRenderer getRenderer(FacesContext context);
}
