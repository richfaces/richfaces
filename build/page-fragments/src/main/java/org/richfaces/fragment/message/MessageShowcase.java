package org.richfaces.fragment.message;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.Message.MessageType;

public class MessageShowcase {

    @FindBy
    private RichFacesMessage message;

    public void showcase_message() {
        message.getDetail();
        message.getSummary();

        MessageType type = message.getType();
    }
}
