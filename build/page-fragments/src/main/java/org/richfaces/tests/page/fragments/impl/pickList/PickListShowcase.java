package org.richfaces.tests.page.fragments.impl.pickList;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

public class PickListShowcase {

    @FindBy
    private RichFacesPickList pickList;

    public void pick_lisT_showcase() {
        pickList.addMultiple(ChoicePickerHelper.byIndex().everyNth(3));

        pickList.remove(4);
    }
}
