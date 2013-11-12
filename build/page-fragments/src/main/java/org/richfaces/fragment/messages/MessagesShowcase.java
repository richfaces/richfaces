package org.richfaces.fragment.messages;

import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.Message;
import org.richfaces.fragment.message.Message.MessageType;

public class MessagesShowcase {

    @FindBy
    private RichFacesMessages messages;

    @SuppressWarnings("unchecked")
    public void messages_showcase() {
        List<Message> errorMessages = (List<Message>) messages.getItems(MessageType.ERROR);

        messages.getItem(4);

        messages.getItem("Error: name input!");
    }
}
