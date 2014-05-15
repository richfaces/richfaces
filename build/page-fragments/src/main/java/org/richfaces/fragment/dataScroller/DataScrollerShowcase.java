package org.richfaces.fragment.dataScroller;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton;

public class DataScrollerShowcase {

    @FindBy
    private RichFacesDataScroller dataScroller;

    public void showcase_data_scroller() {
        dataScroller.switchTo(DataScrollerSwitchButton.LAST);

        dataScroller.switchTo(8);
    }
}
