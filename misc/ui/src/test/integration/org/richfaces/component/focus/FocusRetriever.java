package org.richfaces.component.focus;

import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;

@JavaScript("document")
public abstract class FocusRetriever {
    public abstract WebElement getActiveElement();
    
    public static WebElement retrieveActiveElement() {
        return JSInterfaceFactory.create(FocusRetriever.class).getActiveElement();
    }
}
