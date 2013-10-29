package org.richfaces.tests.page.fragments.impl.list;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

public class ListShowcase {

    @FindBy
    private RichFacesList list;

    public void showcase_list() {
        RichFacesListItem item1 = list.getItem("An Item");
        item1.getText();
        item1.getRootElement();

        list.getItems();

        list.getItems(ChoicePickerHelper.byWebElement()
            .attribute("class").contains("even")
            .and()
            .attribute("class").contains("odd"));

        int size = list.size();
        list.isEmpty();

    }
}
