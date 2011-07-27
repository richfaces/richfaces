package org.richfaces.webapp.editor;

import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

public class FakeFacesContext extends FacesContext {
    
    @Override
    public Application getApplication() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<String> getClientIdsWithMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExternalContext getExternalContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Severity getMaximumSeverity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<FacesMessage> getMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<FacesMessage> getMessages(String clientId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RenderKit getRenderKit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getRenderResponse() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getResponseComplete() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ResponseStream getResponseStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setResponseStream(ResponseStream responseStream) {
        // TODO Auto-generated method stub

    }

    @Override
    public ResponseWriter getResponseWriter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setResponseWriter(ResponseWriter responseWriter) {
        // TODO Auto-generated method stub

    }

    @Override
    public UIViewRoot getViewRoot() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setViewRoot(UIViewRoot root) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addMessage(String clientId, FacesMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void release() {
        // TODO Auto-generated method stub

    }

    @Override
    public void renderResponse() {
        // TODO Auto-generated method stub

    }

    @Override
    public void responseComplete() {
        // TODO Auto-generated method stub

    }

}
