package org.richfaces.tests.page.fragments.impl.inplaceInput;

import org.openqa.selenium.support.FindBy;

public class InplaceInputShowcase {

    @FindBy
    private RichFacesInplaceInput inplaceInput;

    public void showcase_inplace_input() {
        // will fill in the input and confirm it by the OK button controll next to the input
        inplaceInput.type("Foo Bar").confirmByControlls();

        inplaceInput.type("Other val").cancelByControlls();

        // will firstly type Blah string, but then it cancels it via pressing CTRL + ESC combination
        inplaceInput.type("Blah").cancel();

        // confirms via CTRL + Enter
        inplaceInput.type("Blah").confirm();
    }
}
