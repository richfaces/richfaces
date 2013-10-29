package org.richfaces.tests.page.fragments.impl.orderingList;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

public class OrderingListShowcase {

    @FindBy
    private RichFacesOrderingList orderingList;

    public void showcase_ordering_list() {
        orderingList.select(3).putItAfter(0);
        orderingList.select("Third").putItBefore(ChoicePickerHelper.byIndex().last());
    }
}
