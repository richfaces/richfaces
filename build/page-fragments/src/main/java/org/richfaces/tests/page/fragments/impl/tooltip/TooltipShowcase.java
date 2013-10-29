package org.richfaces.tests.page.fragments.impl.tooltip;

import org.openqa.selenium.support.FindBy;

public class TooltipShowcase {

    @FindBy
    private RichFacesTooltip<String> tooltip;

    public void showcase_tooltip() {
        // show the tooltip via JS
        tooltip.show();
        String tooltipText = tooltip.getContent();

        tooltip.hide();
    }
}
