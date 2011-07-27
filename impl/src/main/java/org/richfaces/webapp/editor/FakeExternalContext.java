package org.richfaces.webapp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;

public class FakeExternalContext extends ExternalContext {

    @Override
    public void dispatch(String path) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public String encodeActionURL(String url) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeNamespace(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeResourceURL(String url) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getApplicationMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAuthType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getInitParameter(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map getInitParameterMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemoteUser() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getRequest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestContextPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getRequestCookieMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String> getRequestHeaderMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String[]> getRequestHeaderValuesMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Locale getRequestLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Locale> getRequestLocales() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getRequestMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String> getRequestParameterMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<String> getRequestParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String[]> getRequestParameterValuesMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestPathInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestServletPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getResponse() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getSession(boolean create) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getSessionMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Principal getUserPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void log(String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void log(String message, Throwable exception) {
        // TODO Auto-generated method stub

    }

    @Override
    public void redirect(String url) throws IOException {
        // TODO Auto-generated method stub

    }

}
