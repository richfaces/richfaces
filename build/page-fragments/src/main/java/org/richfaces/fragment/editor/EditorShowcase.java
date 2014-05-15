package org.richfaces.fragment.editor;

import org.openqa.selenium.support.FindBy;

public class EditorShowcase {

    @FindBy
    private RichFacesEditor editor;

    public void showcase_editor() {
        editor.type("Some text");
        editor.clear();
    }
}
