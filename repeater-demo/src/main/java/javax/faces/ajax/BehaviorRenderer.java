/**
 *
 */
package javax.faces.ajax;

import javax.faces.context.FacesContext;

/**
 * @author asmirnov
 *
 */
public interface BehaviorRenderer {
    /**
     * @param context
     * @param behavior
     */
    void decode(FacesContext context, ClientSideBehavior behavior);

    /**
     * @param context
     * @param behavior
     * @return
     */
    String getCode(FacesContext context, ClientSideBehavior behavior);
}
