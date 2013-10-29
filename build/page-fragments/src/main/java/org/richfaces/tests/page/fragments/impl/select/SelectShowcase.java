package org.richfaces.tests.page.fragments.impl.select;

import org.openqa.selenium.support.FindBy;

public class SelectShowcase {

    @FindBy
    private RichFacesSelect select;

    public void showcase_select() {
        select.openSelect();
        select.type("Ala").select("Alabama");
    }
}
