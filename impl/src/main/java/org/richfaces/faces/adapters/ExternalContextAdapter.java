/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.faces.adapters;

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

/**
 * Provides adapter for partial implementations of {@link ExternalContext}.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class ExternalContextAdapter extends ExternalContext {

    @Override
    public void dispatch(String path) throws IOException {
    }

    @Override
    public String encodeActionURL(String url) {
        return null;
    }

    @Override
    public String encodeNamespace(String name) {
        return null;
    }

    @Override
    public String encodeResourceURL(String url) {
        return null;
    }

    @Override
    public Map<String, Object> getApplicationMap() {
        return null;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Object getContext() {
        return null;
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map getInitParameterMap() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public Object getRequest() {
        return null;
    }

    @Override
    public String getRequestContextPath() {
        return null;
    }

    @Override
    public Map<String, Object> getRequestCookieMap() {
        return null;
    }

    @Override
    public Map<String, String> getRequestHeaderMap() {
        return null;
    }

    @Override
    public Map<String, String[]> getRequestHeaderValuesMap() {
        return null;
    }

    @Override
    public Locale getRequestLocale() {
        return null;
    }

    @Override
    public Iterator<Locale> getRequestLocales() {
        return null;
    }

    @Override
    public Map<String, Object> getRequestMap() {
        return null;
    }

    @Override
    public Map<String, String> getRequestParameterMap() {
        return null;
    }

    @Override
    public Iterator<String> getRequestParameterNames() {
        return null;
    }

    @Override
    public Map<String, String[]> getRequestParameterValuesMap() {
        return null;
    }

    @Override
    public String getRequestPathInfo() {
        return null;
    }

    @Override
    public String getRequestServletPath() {
        return null;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return null;
    }

    @Override
    public Object getResponse() {
        return null;
    }

    @Override
    public Object getSession(boolean create) {
        return null;
    }

    @Override
    public Map<String, Object> getSessionMap() {
        return null;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public void log(String message) {
    }

    @Override
    public void log(String message, Throwable exception) {
    }

    @Override
    public void redirect(String url) throws IOException {
    }
}
