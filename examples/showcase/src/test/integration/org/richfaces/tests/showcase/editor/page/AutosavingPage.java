package org.richfaces.tests.showcase.editor.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.editor.RichFacesEditor;

public class AutosavingPage {

    @FindBy(css = ".example-cnt")
    public RichFacesEditor editor;

    @FindBy(className = "rf-p-b")
    public WebElement outputFromEditor;
}
