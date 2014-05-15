package org.richfaces.fragment.inputNumberSlider;

import org.openqa.selenium.support.FindBy;

public class NumberSliderShowcase {

    @FindBy
    private RichFacesInputNumberSlider slider;

    public void showcase_slider() {
        // will decrease according to the step attribute
        slider.decrease();

        // will repeat decrase 10 times
        slider.decrease(10);

        slider.increase();
        slider.increase(10);

        slider.setValue(50);
        double value = slider.getValue();
    }
}
