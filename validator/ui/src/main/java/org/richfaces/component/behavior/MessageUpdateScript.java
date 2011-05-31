package org.richfaces.component.behavior;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.FacesMessage;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.ScriptString;
import org.ajax4jsf.javascript.ScriptStringBase;
import org.richfaces.javascript.Message;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

public class MessageUpdateScript extends ScriptStringBase implements ScriptString {
    private static final Function<? super FacesMessage, Message> MESSAGES_TRANSFORMER = new Function<FacesMessage, Message>() {
        public Message apply(FacesMessage msg) {
            return new Message(msg);
        }
    };
    private final ImmutableList<Message> messages;
    private final String clientId;

    public MessageUpdateScript(String clientId, Iterator<FacesMessage> messages) {
        this.clientId = clientId;
        this.messages = ImmutableList.copyOf(Iterators.transform(messages, MESSAGES_TRANSFORMER));
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
