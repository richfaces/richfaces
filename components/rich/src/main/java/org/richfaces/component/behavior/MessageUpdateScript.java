package org.richfaces.component.behavior;

import java.io.IOException;
import java.util.List;

import org.richfaces.javascript.JSFunction;
import org.richfaces.javascript.ScriptString;
import org.richfaces.javascript.ScriptStringBase;
import org.richfaces.validator.Message;

import com.google.common.collect.Lists;

public class MessageUpdateScript extends ScriptStringBase implements ScriptString {
    private final List<Message> messages;
    private final String clientId;

    public MessageUpdateScript(String clientId, List<Message> messages) {
        this.clientId = clientId;
        this.messages = Lists.newArrayList(messages);
    }

    public void appendScript(Appendable target) throws IOException {
        JSFunction resetMessages = new JSFunction("RichFaces.csv.clearMessage", clientId);
        resetMessages.appendScript(target);
        target.append(';');
        for (Message message : messages) {
            JSFunction sendMessage = new JSFunction("RichFaces.csv.sendMessage", clientId, message);
            sendMessage.appendScript(target);
            target.append(';');
        }
    }
}
