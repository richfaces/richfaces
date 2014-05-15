package org.richfaces.fragment.inplaceSelect;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

public class InplaceSelectShowcase {

    @FindBy
    private InplaceSelect inplaceSelect;

    public void showcase_select() {
        inplaceSelect.select(2);
        inplaceSelect.select("Second option");
        inplaceSelect.select(ChoicePickerHelper.byVisibleText().contains("Thrid"));
    }
}
