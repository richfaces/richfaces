package org.richfaces.ui.message;

import javax.faces.context.FacesContext;

public interface ClientSideMessage {
    void updateMessages(FacesContext context, String clientId);

    String getFor();
}