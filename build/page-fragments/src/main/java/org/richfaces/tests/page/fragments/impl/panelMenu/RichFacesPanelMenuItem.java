package org.richfaces.tests.page.fragments.impl.panelMenu;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RichFacesPanelMenuItem implements PanelMenuItem {

    @FindBy(css = "td[class*=rf-][class*=-itm-lbl]")
    private WebElement label;
    @FindBy(css = "td[class*=rf-][class*=-itm-ico]")
    private WebElement leftIcon;
    @FindBy(css = "td[class*=rf-][class*=-itm-exp-ico]")
    private WebElement rightIcon;
    @FindBy(css = "td[class*=rf-][class*=-itm-ico] img")
    private WebElement leftIconImg;
    @FindBy(css = "td[class*=rf-][class*=-itm-exp-ico] img")
    private WebElement rightIconImg;

    @Root
    private WebElement root;

    private AdvancedPanelMenuItemInteractions advancedInteractions = new AdvancedPanelMenuItemInteractions();

    @Override
    public void select() {
        root.click();
    }

    public AdvancedPanelMenuItemInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedPanelMenuItemInteractions {

        public WebElement getLeftIconElement() {
            return leftIcon;
        }

        public WebElement getRightIconElement() {
            return rightIcon;
        }

        public WebElement getRightIconImgElement() {
            return rightIconImg;
        }

        public WebElement getLeftIconImgElement() {
            return leftIconImg;
        }

        public boolean isSelected() {
            return root.getAttribute("class").contains("-sel");
        }

        public WebElement getRootElement() {
            return root;
        }

        public boolean isDisabled() {
            return root.getAttribute("class").contains("-dis");
        }

        public boolean isTransparent(WebElement icon) {
            return icon.getAttribute("class").contains("-transparent");
        }
    }
}
