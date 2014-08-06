package org.richfaces.utils.focus;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;

/**
 * Retrieves active (focused) element
 */
@JavaScript("document")
public abstract class FocusRetriever {
    public abstract WebElement getActiveElement();

    /**
     * Returns active (focused) element - if no element is focused (it means body element is active), null is returned
     */
    public static WebElement retrieveActiveElement() {
        GrapheneContext context = GrapheneContext.getContextFor(Default.class);
        WebElement element = JSInterfaceFactory.create(context, FocusRetriever.class).getActiveElement();
        if ("body".equals(element.getTagName())) {
            return null;
        }
        return element;
    }
}
