package org.richfaces.fragment.inputNumberSpinner;

import org.openqa.selenium.support.FindBy;

public class SpinnerShowcase {

    @FindBy
    private RichFacesInputNumberSpinner spinner;

    public void showcase_spinner() {
        // will decrase by clicking on the decrease button
        spinner.decrease();
        // will decrase by clicking on the decrease button 10 times
        spinner.decrease(10);

        spinner.increase();
        spinner.increase(10);

        spinner.setValue(50);
        double value = spinner.getValue();
    }

}
