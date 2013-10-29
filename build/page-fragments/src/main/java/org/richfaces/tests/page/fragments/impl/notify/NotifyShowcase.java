package org.richfaces.tests.page.fragments.impl.notify;

import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.message.Message.MessageType;

public class NotifyShowcase {

    @FindBy
    private RichFacesNotify notify;

    @FindBy
    private RichFacesNotifyMessage notifyMessage;

    public void showcase_notify() {
        List<NotifyMessage> errorMessages = (List<NotifyMessage>) notify.getItems(MessageType.ERROR);

        notify.getItem(4);

        notify.getItem("Error: name input!");
    }

    public void showcase_notify_message() {
        notifyMessage.getDetail();
        notifyMessage.getSummary();
        notifyMessage.getType();
    }
}
