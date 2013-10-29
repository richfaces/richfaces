package org.richfaces.tests.page.fragments.impl.autocomplete;

import org.openqa.selenium.support.FindBy;

public class AutocompleteShowcase {

    @FindBy
    private RichFacesAutocomplete autocomplete;

    public void showcase_autocomplete_fragment() {
        autocomplete.type("Ala").confirm();
        // assert that autocomplete input have only Ala in it

        autocomplete.type("A").select(2);
        // assert that autocomplete input have Arizona in it
    }
}
