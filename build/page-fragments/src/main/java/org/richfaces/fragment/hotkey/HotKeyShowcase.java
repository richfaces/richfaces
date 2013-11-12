package org.richfaces.fragment.hotkey;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HotKeyShowcase {

    @FindBy
    private RichFacesHotkey ctrlAHotKey;

    @FindBy
    private WebElement someElement;

    public void showcase_hot_key() {
        //will perform CTRL + A hotkey, fragment will retrieve the hotkey combination from the <script> element
        ctrlAHotKey.invoke();

        ctrlAHotKey.invoke(someElement);
    }
}
