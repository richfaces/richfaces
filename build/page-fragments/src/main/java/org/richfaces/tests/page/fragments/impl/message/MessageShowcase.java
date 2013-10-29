package org.richfaces.tests.page.fragments.impl.message;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.message.Message.MessageType;

public class MessageShowcase {

    @FindBy
    private RichFacesMessage message;

    public void showcase_message() {
        message.getDetail();
        message.getSummary();

        MessageType type = message.getType();
    }
}
