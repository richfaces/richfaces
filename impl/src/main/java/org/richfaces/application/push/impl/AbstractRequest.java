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
package org.richfaces.application.push.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.javascript.JSLiteral;
import org.ajax4jsf.javascript.ScriptString;
import org.ajax4jsf.javascript.ScriptUtils;
import org.atmosphere.cpr.AtmosphereEventLifecycle;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;
import org.atmosphere.websocket.WebSocketSupport;
import org.richfaces.application.push.Request;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.TopicKey;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class AbstractRequest implements Request {

    private static final String TOPIC_KEY = "topic";

    private static final String DATA_KEY = "data";

    private static final int SUSPEND_TIMEOUT = 30 * 1000;
    
    private static final class Message implements ScriptString {

        private TopicKey topicKey;
        
        private String serializedData;

        public Message(TopicKey topicKey, String serializedData) {
            super();
            this.topicKey = topicKey;
            this.serializedData = serializedData;
        }
        
        public String toScript() {
            Map<String,Object> map = new HashMap<String, Object>(2);
            
            map.put(TOPIC_KEY, topicKey.getTopicAddress());
            map.put(DATA_KEY, new JSLiteral(serializedData));
            
            return ScriptUtils.toScript(map);
        }

        public void appendScript(StringBuffer functionString) {
            functionString.append(toScript());
        }
    }
    
    private static final class FlushMessagesTask implements Runnable {
        
        private Request request;

        public FlushMessagesTask(Request request) {
            super();
            this.request = request;
        }
        
        public void run() {
            try {
                request.flushMessages();
            } catch (Throwable e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    private final AtmosphereResourceEventListener resourceEventListener = new AtmosphereResourceEventListener() {
        
        public void onSuspend(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
            AbstractRequest.this.onSuspend();
        }
        
        public void onResume(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
            AbstractRequest.this.onResume();
        }
        
        public void onDisconnect(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
            AbstractRequest.this.onDisconnect();
        }
        
        public void onBroadcast(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
            AbstractRequest.this.onBroadcast();
        }
    };
    
    private final AtmosphereResource<HttpServletRequest, HttpServletResponse> atmosphereResource;

    private final Session session;
    
    private final ExecutorService executorService;

    private final Queue<Message> messagesQueue = new ConcurrentLinkedQueue<Message>();
    
    private AtomicBoolean submitted = new AtomicBoolean(false);
    
    public AbstractRequest(AtmosphereResource<HttpServletRequest, HttpServletResponse> atmosphereResource, Session session, 
        ExecutorService executorService) {
        
        super();
        
        this.atmosphereResource = atmosphereResource;
        
        ((AtmosphereEventLifecycle) atmosphereResource).addEventListener(resourceEventListener);
        
        this.session = session;
        this.executorService = executorService;
    }

    private void submitToWorker() {
        if (submitted.compareAndSet(false, true)) {
            executorService.submit(new FlushMessagesTask(this));
        }
    }

    private String serializeMessages() {
        return ScriptUtils.toScript(new ConsumingCollection<Message>(messagesQueue));
    }
    
    public void flushMessages() throws IOException {
        String serializedMessages = serializeMessages();
        PrintWriter writer = atmosphereResource.getResponse().getWriter();
        writer.write(serializedMessages);
        writer.flush();
        
        submitted.compareAndSet(true, false);

        if (isPolling()) {
            atmosphereResource.resume();
        } else if (!messagesQueue.isEmpty()) {
            submitToWorker();
        }
    }

    public void postMessage(TopicKey topicKey, String serializedMessage) {
        messagesQueue.add(new Message(topicKey, serializedMessage));
        submitToWorker();
    }

    public void suspend() throws IOException {
        atmosphereResource.suspend(SUSPEND_TIMEOUT, isPolling());
    }

    public void resume() throws IOException {
        atmosphereResource.resume();
    }

    public boolean isSuspended() {
        return atmosphereResource.getAtmosphereResourceEvent().isSuspended();
    }

    public boolean isPolling() {
        HttpServletRequest req = atmosphereResource.getRequest();
        boolean isWebsocket = req.getAttribute(WebSocketSupport.WEBSOCKET_SUSPEND) != null || 
            req.getAttribute(WebSocketSupport.WEBSOCKET_RESUME) != null;
        
        //TODO how to detect non-polling transports?
        return !isWebsocket;
    }

    public Session getSession() {
        return session;
    }

    protected AtmosphereResource<HttpServletRequest, HttpServletResponse> getResource() {
        return atmosphereResource;
    }
    
    protected void onSuspend() {
    }
    
    protected void onResume() {
        try {
            session.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected void onDisconnect() {
        try {
            session.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected void onBroadcast() {
    }
    
}
