package org.richfaces.component;

import javax.faces.context.FacesContext;

public interface ClientSideMessage {
    void updateMessages(FacesContext context, String clientId);

    String getFor();
}