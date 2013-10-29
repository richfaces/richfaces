package org.richfaces.tests.page.fragments.impl.accordion;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

public class AccordionShowcase {

    @FindBy
    private RichFacesAccordion accordion;

    // main functionality of accordion is switching and then getting the content from the panel it is switched in
    public void showcase_the_accorditon_api() {
        TheFirstAccordion firstAccordion = accordion.switchTo(0).getContent(TheFirstAccordion.class);
        firstAccordion.getTable();

        firstAccordion = accordion.switchTo("First accordion").getContent(TheFirstAccordion.class);

        firstAccordion = accordion.switchTo(ChoicePickerHelper.byIndex().beforeLast(3))
                                  .getContent(TheFirstAccordion.class);
    }

    public class TheFirstAccordion {

        @FindBy
        private WebElement table;

        public WebElement getTable() {
            return table;
        }
    }
}
