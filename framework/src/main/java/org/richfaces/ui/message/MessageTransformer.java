package org.richfaces.ui.message;

import com.google.common.base.Function;

import javax.faces.application.FacesMessage;

public class MessageTransformer implements Function<FacesMessage, MessageForRender> {
    private final String sourceId;

    public MessageTransformer(String sourceId) {
        this.sourceId = sourceId;
    }

    public MessageForRender apply(FacesMessage input) {
        return new MessageForRender(input, sourceId);
    }
}
