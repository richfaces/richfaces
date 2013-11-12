package org.richfaces.fragment.orderingList;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

public class OrderingListShowcase {

    @FindBy
    private RichFacesOrderingList orderingList;

    public void showcase_ordering_list() {
        orderingList.select(3).putItAfter(0);
        orderingList.select("Third").putItBefore(ChoicePickerHelper.byIndex().last());
    }
}
