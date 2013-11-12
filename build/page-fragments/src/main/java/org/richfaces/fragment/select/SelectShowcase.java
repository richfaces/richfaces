package org.richfaces.fragment.select;

import org.openqa.selenium.support.FindBy;

public class SelectShowcase {

    @FindBy
    private RichFacesSelect select;

    public void showcase_select() {
        select.openSelect();
        select.type("Ala").select("Alabama");
    }
}
