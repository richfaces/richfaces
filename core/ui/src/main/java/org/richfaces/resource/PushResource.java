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
package org.richfaces.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.TopicKey;

/**
 * @author Nick Belaevski
 * 
 */
@DynamicResource
public class PushResource implements UserResource {

    private static final String PUSH_TOPIC_PARAM = "pushTopic";

    private static final String FORGET_PUSH_SESSION_ID_PARAM = "forgetPushSessionId";

    private static final InputStream EMPTY_INPUT_STREAM = new ByteArrayInputStream(new byte[0]);
    
    public Map<String, String> getResponseHeaders() {
        return null;
    }

    public Date getLastModified() {
        return null;
    }

    private InputStream mapToScript(Map<String, Object> map) {
        try {
            byte[] bs = ScriptUtils.toScript(map).getBytes("UTF-8");
            return new ByteArrayInputStream(bs);
        } catch (UnsupportedEncodingException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }
    
    private Map<String, String> getFailuresMap(Map<TopicKey, String> failedSubscriptions) {
        Map<String,String> result = new HashMap<String, String>();
        
        for (Entry<TopicKey, String> entry: failedSubscriptions.entrySet()) {
            result.put(entry.getKey().getTopicAddress(), entry.getValue());
        }
        
        return result;
    }
    
    public InputStream getInputStream() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        PushContextFactory pushContextFactory = ServiceTracker.getService(PushContextFactory.class);
        
        //resource plugin stub
        if (pushContextFactory == null) {
            return EMPTY_INPUT_STREAM;
        }
        
        PushContext pushContext = pushContextFactory.getPushContext();
        
        String forgetPushSessionId = externalContext.getRequestParameterMap().get(FORGET_PUSH_SESSION_ID_PARAM);
        if (forgetPushSessionId != null) {
            Session oldSession = pushContext.getSessionManager().getPushSession(forgetPushSessionId);
            if (oldSession != null) {
                oldSession.destroy();
            }
        }
        
        Session session = pushContext.getSessionFactory().createSession(UUID.randomUUID().toString());
        
        String[] topicNames = externalContext.getRequestParameterValuesMap().get(PUSH_TOPIC_PARAM);
        
        if (topicNames == null) {
            throw new IllegalArgumentException();
        }
        
        session.subscribe(topicNames);
        
        Map<String, Object> subscriptionData = new HashMap<String, Object>(4);
        subscriptionData.put("sessionId", session.getId());

        Map<TopicKey, String> failedSubscriptions = session.getFailedSubscriptions();
        subscriptionData.put("failures", getFailuresMap(failedSubscriptions));
        
        return mapToScript(subscriptionData);
    }

    public String getContentType() {
        return "application/javascript; charset=utf-8";
    }

    public int getContentLength() {
        return -1;
    }

}
